package com.camellias.resizer.handlers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.Nullable;

import com.camellias.resizer.Main;
import com.camellias.resizer.network.ResizePacketHandler;
import com.camellias.resizer.network.packets.PacketNormalSize;
import com.camellias.resizer.network.packets.PacketOnResize;
import com.camellias.resizer.network.packets.PacketSpawnParticles;
import com.camellias.resizer.network.packets.PacketAlteredSize;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionAddedEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionExpiryEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionRemoveEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
public class PotionHandler
{
	public static Method setSize = ObfuscationReflectionHelper.findMethod(Entity.class, "func_70105_a", void.class, float.class, float.class);
	
	@SubscribeEvent
	public static void onPotionAdded(PotionAddedEvent event)
	{
		if(event.getEntityLiving() instanceof EntityPlayerMP)
		{
			EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();
			PacketOnResize packet = getResizePacketAdded(event, player, Main.SHRINKING);
			if (packet == null)
			{
				packet = getResizePacketAdded(event, player, Main.GROWTH);
			}
			if (packet != null)
			{
				sendResizePacket(player, packet);
			}
		}
	}
	
	/**
	 * Gets packet to resize a player when a shrinking or growth effect is added to them
	 * 
	 * @param event potion addition event
	 * @param player resized player
	 * @param potionTarget shrinking or growth resize potion
	 * @return resize packet, if the new effect's potion matches the target potion, or null if it does not
	 */
	@Nullable
	private static PacketOnResize getResizePacketAdded(PotionAddedEvent event, EntityPlayerMP player, Potion potionTarget)
	{
		PotionEffect effectNew = event.getPotionEffect();
		if (effectNew.getPotion() == potionTarget)
		{
			boolean shouldSpawnParticles = false;
			// Prevent particle spawning if the effect was added by a beacon
			if (!effectNew.getIsAmbient())
			{
				// Only spawn particles if the added effect is not the same type and level
				PotionEffect effectOld = event.getOldPotionEffect();
				if (effectOld == null || effectOld.getPotion() != potionTarget || effectOld.getAmplifier() != effectNew.getAmplifier())
				{
					shouldSpawnParticles = true;
				}
			}
			return new PacketAlteredSize(player, potionTarget == Main.GROWTH, effectNew.getDuration(), effectNew.getAmplifier(), shouldSpawnParticles);
		}
		return null;
	}
	
	@SubscribeEvent
	public static void onPotionRemoved(PotionRemoveEvent event)
	{
		sendResizePacketRemoved(event.getEntityLiving(), event.getPotion());
	}
	
	@SubscribeEvent
	public static void onPotionEnd(PotionExpiryEvent event)
	{
		PotionEffect effect = event.getPotionEffect();
		if (effect != null)
		{
			sendResizePacketRemoved(event.getEntityLiving(), effect.getPotion());
		}
	}
	
	/**
	 * Sends packet to resize a player when a shrinking or growth effect is removed (either by force or as a result of expiration) from them
	 * 
	 * @param entity potentially resized player
	 * @param potionTarget shrinking or growth resize potion
	 */
	private static void sendResizePacketRemoved(EntityLivingBase entity, Potion potionTarget)
	{
		if (entity instanceof EntityPlayerMP && (potionTarget == Main.GROWTH || potionTarget == Main.SHRINKING))
		{
			EntityPlayerMP player = (EntityPlayerMP) entity;
			sendResizePacket(player, new PacketNormalSize(player));
		}
	}
	
	/**
	 * Sends resize/particle-spawning packet to all players tracking the resized player, and sends particle-spawning packet to the resized player if allowed
	 * 
	 * @param player resized player
	 * @param packet {@link PacketAlteredSize shrinking/growth} or {@link PacketNormalSize size-restoring} packet for resized player
	 */
	private static void sendResizePacket(EntityPlayerMP player, PacketOnResize packet)
	{
		ResizePacketHandler.INSTANCE.sendToAllTracking(packet, player);
		if (packet.shouldSpawnParticles())
		{
			ResizePacketHandler.INSTANCE.sendTo(new PacketSpawnParticles(player), player);
		}
	}
	
	@SubscribeEvent
	public static void isPotionApplicable(PotionApplicableEvent event)
	{
		if(event.getEntityLiving() instanceof EntityPlayer)
		{
			setPotionApplicability(event, Main.SHRINKING);
			setPotionApplicability(event, Main.GROWTH);
		}
	}
	
	/**
	 * Allows or denies the addition of a shrinking or growth resize potion
	 * 
	 * @param event potion applicability event
	 * @param potionTarget shrinking or growth resize potion
	 */
	private static void setPotionApplicability(PotionApplicableEvent event, Potion potionTarget)
	{
		if (event.getPotionEffect().getPotion() == potionTarget)
		{
			Potion potionOld = potionTarget == Main.GROWTH ? Main.SHRINKING : Main.GROWTH;
			event.setResult(((EntityPlayer) event.getEntityLiving()).isPotionActive(potionOld) ? Event.Result.DENY : Event.Result.ALLOW);
		}
	}
	
	
	
	
	
//-------------------------------------------------------------------------------------------------------------------------//
	
	
	
	
	
	@SubscribeEvent
	public static void onPlayerUpdate(TickEvent.PlayerTickEvent event)
	{
		//----Thank you to XzeroAir from the MMD Discord for helping out with the hitbox changes. Life saver, that guy.----//
		
		EntityPlayer player = event.player;
		PotionEffect growth = player.getActivePotionEffect(Main.GROWTH);
		PotionEffect shrinking = player.getActivePotionEffect(Main.SHRINKING);
		
		if(player.isPotionActive(Main.GROWTH))
		{
			player.height = 1.8F + (growth.getAmplifier() + 1F);
			player.width = player.height * (1F / 3F);
			AxisAlignedBB aabb = player.getEntityBoundingBox();
			double d0 = (double)player.width / 2.0D;
			
			player.eyeHeight = player.height * 0.9F;
			player.stepHeight = player.height / 3F;
			
			try
			{
				setSize.invoke(player, player.width, player.height);
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
			catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			}
			catch (InvocationTargetException e)
			{
				e.printStackTrace();
			}
            
			player.setEntityBoundingBox(new AxisAlignedBB(player.posX - d0, aabb.minY, player.posZ - d0, 
            		player.posX + d0, aabb.minY + (double)player.height, player.posZ + d0));
		}
		
		if(player.isPotionActive(Main.SHRINKING))
		{
			player.height = 0.9F / (shrinking.getAmplifier() + 1);
			player.width = 0.35F;
			AxisAlignedBB aabb = player.getEntityBoundingBox();
			double d0 = (double)player.width / 2.0D;
			
			player.eyeHeight = player.height * 0.85F;
			player.stepHeight = player.height / 3F;
			player.jumpMovementFactor *= 1.75F;
			player.fallDistance = 0.0F;
			
			try
			{
				setSize.invoke(player, player.width, player.height);
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
			catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			}
			catch (InvocationTargetException e)
			{
				e.printStackTrace();
			}
			
			if(shrinking.getAmplifier() >= 1)
			{
				if((ClimbingHandler.canClimb(player) != false))
				{
					if(player.collidedHorizontally)
                    {
						if(!player.isSneaking())
                        {
                            player.motionY = 0.1F;
                        }
                        
                        if(player.isSneaking())
                        {
                            player.motionY = 0.0F;
                        }
                    }
				}
			}
			
			player.setEntityBoundingBox(new AxisAlignedBB(player.posX - d0, aabb.minY, player.posZ - d0, 
            		player.posX + d0, aabb.minY + (double)player.height, player.posZ + d0));
		}
		
		if(player.isPotionActive(Main.GROWTH) == false && player.isPotionActive(Main.SHRINKING) == false)
		{
			player.eyeHeight = player.getDefaultEyeHeight();
			player.stepHeight = 0.6F;
		}
	}
	
	@SubscribeEvent
	public static void onPlayerJump(LivingJumpEvent event)
	{
		EntityLivingBase entity = event.getEntityLiving();
		PotionEffect potion = entity.getActivePotionEffect(Main.SHRINKING);
		
		if(entity.isPotionActive(Main.SHRINKING))
		{
			if(potion.getAmplifier() == 0)
			{
				entity.motionY += potion.getAmplifier() + 0.25D;
			}
			else
			{
				entity.motionY += potion.getAmplifier() / 2.0D;
			}
		}
	}
	
	
	
	
	
//-------------------------------------------------------------------------------------------------------------------------//
	
	
	
	
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onPlayerRenderPre(RenderPlayerEvent.Pre event)
	{
		//----Thank you Melonslise from the MMD Discord for helping getting the rendering working properly.----//
		
		EntityPlayer player = event.getEntityPlayer();
		
		if(player.isPotionActive(Main.GROWTH) || player.isPotionActive(Main.SHRINKING))
		{
			float scale = player.height / 1.8F;
			
			GlStateManager.pushMatrix();
			GlStateManager.scale(scale, scale, scale);
			GlStateManager.translate((event.getX() / scale) - event.getX(), 
					(event.getY() / scale) - event.getY(), (event.getZ() / scale) - event.getZ());
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onPlayerRenderPost(RenderPlayerEvent.Post event)
	{
		EntityPlayer player = event.getEntityPlayer();
		
		if(player.isPotionActive(Main.GROWTH) || player.isPotionActive(Main.SHRINKING))
		{
			GlStateManager.popMatrix();
		}
	}
}
