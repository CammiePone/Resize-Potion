package com.camellias.resizer.handlers;

import javax.annotation.Nullable;

import com.camellias.resizer.Main;
import com.camellias.resizer.network.NetworkHandler;
import com.camellias.resizer.network.packets.PacketOnResize;
import com.camellias.resizer.network.packets.PacketSpawnParticles;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionAddedEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionExpiryEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionRemoveEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@EventBusSubscriber
public class PotionHandler
{
//	@SubscribeEvent
//	public static void onPotionAdded(PotionAddedEvent event)
//	{
//		if(!event.getEntityLiving().world.isRemote)
//		{
//			EntityLivingBase entity = event.getEntityLiving();
//			PacketOnResize packet = getResizePacketAdded(event, entity, Main.SHRINKING);
//			
//			if (packet == null)
//			{
//				packet = getResizePacketAdded(event, entity, Main.GROWTH);
//			}
//			
//			if (packet != null)
//			{
//				sendResizePacket(entity, packet);
//			}
//		}
//	}
//	
//	/**
//	 * Gets packet to resize a player when a shrinking or growth effect is added to them
//	 * 
//	 * @param event potion addition event
//	 * @param player resized player
//	 * @param potionTarget shrinking or growth resize potion
//	 * @return resize packet, if the new effect's potion matches the target potion, or null if it does not
//	 */
//	@Nullable
//	private static PacketOnResize getResizePacketAdded(PotionAddedEvent event, EntityLivingBase entity, Potion potionTarget)
//	{
//		PotionEffect effectNew = event.getPotionEffect();
//		
//		if (effectNew.getPotion() == potionTarget)
//		{
//			boolean shouldSpawnParticles = false;
//			
//			// Prevent particle spawning if the effect was added by a beacon
//			if (!effectNew.getIsAmbient())
//			{
//				// Only spawn particles if the added effect is not the same type and level
//				PotionEffect effectOld = event.getOldPotionEffect();
//				
//				if (effectOld == null || effectOld.getPotion() != potionTarget || effectOld.getAmplifier() != effectNew.getAmplifier())
//				{
//					shouldSpawnParticles = true;
//				}
//			}
//		}
//		
//		return null;
//	}
//	
//	/**
//	 * Sends resize/particle-spawning packet to all players tracking the resized player, and sends particle-spawning packet to the resized player if allowed
//	 * 
//	 * @param entity resized player
//	 * @param packet {@link PacketAlteredSize shrinking/growth} or {@link PacketNormalSize size-restoring} packet for resized player
//	 */
//	private static void sendResizePacket(EntityLivingBase entity, PacketOnResize packet)
//	{
//		NetworkHandler.INSTANCE.sendToAllTracking(packet, entity);
//		
//		if (packet.shouldSpawnParticles() && entity instanceof EntityPlayerMP)
//		{
//			NetworkHandler.INSTANCE.sendTo(new PacketSpawnParticles(entity), (EntityPlayerMP) entity);
//		}
//	}
	
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
	public static void onPlayerUpdate(TickEvent.PlayerTickEvent event)
	{
		//----Thank you to XzeroAir from the MMD Discord for helping out with the hitbox changes. Life saver, that guy.----//
		
		EntityPlayer player = event.player;
		PotionEffect growth = player.getActivePotionEffect(Main.GROWTH);
		PotionEffect shrinking = player.getActivePotionEffect(Main.SHRINKING);
		
		if(shrinking != null || growth != null)
		{
			player.stepHeight = player.height / 3F;
			
			if(growth == null)
			{
				if(shrinking.getAmplifier() >= 1)
				{
					if((ClimbingHandler.canClimb(player, player.getHorizontalFacing()) != false))
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
			}
		}
		
		if(growth == null && shrinking == null)
		{
			player.stepHeight = 0.6F;
		}
	}
	
	@SubscribeEvent
	public static void onLivingump(LivingJumpEvent event)
	{
		EntityLivingBase entity = event.getEntityLiving();
		PotionEffect shrinking = entity.getActivePotionEffect(Main.SHRINKING);
		PotionEffect growth = entity.getActivePotionEffect(Main.GROWTH);
		
		if(shrinking != null)
		{
			if(shrinking.getAmplifier() == 0)
			{
				entity.motionY += shrinking.getAmplifier() + 0.25D;
			}
			else
			{
				entity.motionY += shrinking.getAmplifier() / 2.0D;
			}
		}
		
		if(growth != null)
		{
			entity.motionY += (growth.getAmplifier() + 1) * 0.2;
		}
	}
}
