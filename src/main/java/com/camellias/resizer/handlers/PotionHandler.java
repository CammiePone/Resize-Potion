package com.camellias.resizer.handlers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

import com.camellias.resizer.Main;
import com.camellias.resizer.network.ResizePacketHandler;
import com.camellias.resizer.network.packets.GrowthPacket;
import com.camellias.resizer.network.packets.NormalSizePacket;
import com.camellias.resizer.network.packets.ShrinkingPacket;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionAddedEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionExpiryEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionRemoveEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.StartTracking;
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
		if(event.getEntityLiving() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			Random rand = new Random();
			double d2 = rand.nextGaussian() * 0.02D;
			double d0 = rand.nextGaussian() * 0.02D;
			double d1 = rand.nextGaussian() * 0.02D;
			
			if(event.getPotionEffect().getPotion() == Main.SHRINKING)
			{
				PotionEffect potion = event.getPotionEffect();
                if (!event.getEntityLiving().world.isRemote)
                {
                	ShrinkingPacket shrinkingPacket = new ShrinkingPacket(player, potion.getDuration(), potion.getAmplifier());
    				ResizePacketHandler.INSTANCE.sendToAllTracking(shrinkingPacket, player);
                }
                else if(!potion.getIsAmbient())
				{
					for (int k = 0; k < 30; ++k)
					{
						player.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, 
								player.posX + (double)(rand.nextFloat() * player.width * 2.0F) - (double)player.width,
								player.posY + (double)(rand.nextFloat() * player.height),
								player.posZ + (double)(rand.nextFloat() * player.width * 2.0F) - (double)player.width,
								d2, d0, d1);
					}
				}
			}
			
			if(event.getPotionEffect().getPotion() == Main.GROWTH)
			{
				PotionEffect potion = event.getPotionEffect();
				if (!event.getEntityLiving().world.isRemote)
                {
					GrowthPacket growthPacket = new GrowthPacket(player, potion.getDuration(), potion.getAmplifier());
					ResizePacketHandler.INSTANCE.sendToAllTracking(growthPacket, player);
                }
				else if(!potion.getIsAmbient())
				{
					for (int k = 0; k < 40; ++k)
					{
						player.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, 
								player.posX + (double)(rand.nextFloat() * player.width * 2.0F) - (double)player.width,
								player.posY + (double)(rand.nextFloat() * player.height * 2.0F),
								player.posZ + (double)(rand.nextFloat() * player.width * 2.0F) - (double)player.width,
								d2, d0, d1);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onPotionRemoved(PotionRemoveEvent event)
	{
		if(!event.getEntityLiving().world.isRemote && event.getEntityLiving() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			
			if(event.getPotion() == Main.GROWTH || event.getPotion() == Main.SHRINKING)
			{
				NormalSizePacket normalSizePacket = new NormalSizePacket(player);
				
				ResizePacketHandler.INSTANCE.sendToAllTracking(normalSizePacket, player);
			}
		}
	}
	
	@SubscribeEvent
	public static void onPotionEnd(PotionExpiryEvent event)
	{
		if(!event.getEntityLiving().world.isRemote && event.getEntityLiving() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			
			if(event.getPotionEffect().getPotion() == Main.GROWTH || event.getPotionEffect().getPotion() == Main.SHRINKING)
			{
				NormalSizePacket normalSizePacket = new NormalSizePacket(player);
				
				ResizePacketHandler.INSTANCE.sendToAllTracking(normalSizePacket, player);
			}
		}
	}
	
	@SubscribeEvent
	public static void isPotionApplicable(PotionApplicableEvent event)
	{
		if(event.getEntityLiving() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			
			if(event.getPotionEffect().getPotion() == Main.SHRINKING)
			{
				if(player.isPotionActive(Main.GROWTH))
				{
					event.setResult(Event.Result.DENY);
				}
				else
				{
					event.setResult(Event.Result.ALLOW);
				}
			}
			if(event.getPotionEffect().getPotion() == Main.GROWTH)
			{
				if(player.isPotionActive(Main.SHRINKING))
				{
					event.setResult(Event.Result.DENY);
				}
				else
				{
					event.setResult(Event.Result.ALLOW);
				}
			}
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
