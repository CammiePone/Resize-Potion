package com.camellias.resizer.handlers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.Nullable;

import com.camellias.resizer.Main;
import com.camellias.resizer.network.ResizePacketHandler;
import com.camellias.resizer.network.packets.PacketAlteredSize;
import com.camellias.resizer.network.packets.PacketNormalSize;
import com.camellias.resizer.network.packets.PacketOnResize;
import com.camellias.resizer.network.packets.PacketSpawnParticles;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionAddedEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionExpiryEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionRemoveEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
public class PotionHandler
{
	public static Method setSize = ObfuscationReflectionHelper.findMethod(Entity.class, "func_70105_a", void.class, float.class, float.class);
	
	@SubscribeEvent
	public static void onPotionAdded(PotionAddedEvent event)
	{
		if(!event.getEntityLiving().world.isRemote)
		{
			EntityLivingBase entity = event.getEntityLiving();
			PacketOnResize packet = getResizePacketAdded(event, entity, Main.SHRINKING);
			if (packet == null)
			{
				packet = getResizePacketAdded(event, entity, Main.GROWTH);
			}
			if (packet != null)
			{
				sendResizePacket(entity, packet);
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
	private static PacketOnResize getResizePacketAdded(PotionAddedEvent event, EntityLivingBase entity, Potion potionTarget)
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
			return new PacketAlteredSize(entity, potionTarget == Main.GROWTH, effectNew.getDuration(), effectNew.getAmplifier(), shouldSpawnParticles);
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
		if (!entity.world.isRemote && (potionTarget == Main.GROWTH || potionTarget == Main.SHRINKING))
		{
			sendResizePacket(entity, new PacketNormalSize(entity));
		}
	}
	
	/**
	 * Sends resize/particle-spawning packet to all players tracking the resized player, and sends particle-spawning packet to the resized player if allowed
	 * 
	 * @param entity resized player
	 * @param packet {@link PacketAlteredSize shrinking/growth} or {@link PacketNormalSize size-restoring} packet for resized player
	 */
	private static void sendResizePacket(EntityLivingBase entity, PacketOnResize packet)
	{
		ResizePacketHandler.INSTANCE.sendToAllTracking(packet, entity);
		if (packet.shouldSpawnParticles() && entity instanceof EntityPlayerMP)
		{
			ResizePacketHandler.INSTANCE.sendTo(new PacketSpawnParticles(entity), (EntityPlayerMP) entity);
		}
	}
	
	@SubscribeEvent
	public static void isPotionApplicable(PotionApplicableEvent event)
	{
		setPotionApplicability(event, Main.SHRINKING);
		setPotionApplicability(event, Main.GROWTH);
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
			event.setResult(event.getEntityLiving().isPotionActive(potionOld) ? Event.Result.DENY : Event.Result.ALLOW);
		}
	}
	
	
	
	
	
//-------------------------------------------------------------------------------------------------------------------------//
	
	
	
	
	
	@SubscribeEvent
	public static void onLivingUpdate(LivingUpdateEvent event)
	{
		//----Thank you to XzeroAir from the MMD Discord for helping out with the hitbox changes. Life saver, that guy.----//
		
		EntityLivingBase entity = event.getEntityLiving();
		PotionEffect growth = entity.getActivePotionEffect(Main.GROWTH);
		PotionEffect shrinking = entity.getActivePotionEffect(Main.SHRINKING);
		
		if(entity.isPotionActive(Main.GROWTH))
		{
			entity.height = 1.8F + (growth.getAmplifier() + 1F);
			entity.width = entity.height * (1F / 3F);
			AxisAlignedBB aabb = entity.getEntityBoundingBox();
			double d0 = entity.width / 2.0D;
			
			if(entity instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) entity;
				player.eyeHeight = entity.height * 0.9F;
			}
			
			entity.stepHeight = entity.height / 3F;
			
			try
			{
				setSize.invoke(entity, entity.width, entity.height);
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
            
			entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - d0, aabb.minY, entity.posZ - d0, 
            		entity.posX + d0, aabb.minY + entity.height, entity.posZ + d0));
		}
		
		if(entity.isPotionActive(Main.SHRINKING))
		{
			entity.height = 0.9F / (shrinking.getAmplifier() + 1);
			entity.width = 0.35F;
			AxisAlignedBB aabb = entity.getEntityBoundingBox();
			double d0 = entity.width / 2.0D;
			
			if(entity instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) entity;
				player.eyeHeight = entity.height * 0.85F;
			}
			
			entity.stepHeight = entity.height / 3F;
			entity.jumpMovementFactor *= 1.75F;
			entity.fallDistance = 0.0F;
			
			try
			{
				setSize.invoke(entity, entity.width, entity.height);
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
				if(entity instanceof EntityPlayer)
				{
					if((ClimbingHandler.canClimb((EntityPlayer) entity) != false))
					{
						if(entity.collidedHorizontally)
	                    {
							if(!entity.isSneaking())
	                        {
	                            entity.motionY = 0.1F;
	                        }
	                        
	                        if(entity.isSneaking())
	                        {
	                            entity.motionY = 0.0F;
	                        }
	                    }
					}
				}
			}
			
			entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - d0, aabb.minY, entity.posZ - d0, 
            		entity.posX + d0, aabb.minY + entity.height, entity.posZ + d0));
		}
		
		if(entity.isPotionActive(Main.GROWTH) == false && entity.isPotionActive(Main.SHRINKING) == false)
		{
			if(entity instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) entity;
				player.eyeHeight = player.getDefaultEyeHeight();
			}
			entity.stepHeight = 0.6F;
		}
	}
	
	@SubscribeEvent
	public static void onLivingump(LivingJumpEvent event)
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
	public static void onLivingRenderPre(RenderLivingEvent.Pre event)
	{
		//----Thank you Melonslise from the MMD Discord for helping getting the rendering working properly.----//
		
		EntityLivingBase entity = event.getEntity();
		
		if(entity.isPotionActive(Main.GROWTH) || entity.isPotionActive(Main.SHRINKING))
		{
			float scale = entity.height / 1.8F;
			
			GlStateManager.pushMatrix();
			GlStateManager.scale(scale, scale, scale);
			GlStateManager.translate((event.getX() / scale) - event.getX(), 
					(event.getY() / scale) - event.getY(), (event.getZ() / scale) - event.getZ());
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onLivingRenderPost(RenderLivingEvent.Post event)
	{
		EntityLivingBase entity = event.getEntity();
		
		if(entity.isPotionActive(Main.GROWTH) || entity.isPotionActive(Main.SHRINKING))
		{
			GlStateManager.popMatrix();
		}
	}
}
