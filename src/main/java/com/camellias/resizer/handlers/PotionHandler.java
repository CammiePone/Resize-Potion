package com.camellias.resizer.handlers;

import com.camellias.resizer.Main;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@EventBusSubscriber
public class PotionHandler
{
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
					if((ClimbingHandler.canClimb((EntityPlayer) player) != false))
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
