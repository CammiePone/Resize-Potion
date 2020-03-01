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
public class PotionHandler {
	
	@SubscribeEvent
	public static void onPlayerUpdate(TickEvent.PlayerTickEvent event) {
		final EntityPlayer player = event.player;
		final PotionEffect growth = player.getActivePotionEffect(Main.GROWTH);
		final PotionEffect shrinking = player.getActivePotionEffect(Main.SHRINKING);

		if ((shrinking != null) || (growth != null)) {
			if (growth == null) {
				player.jumpMovementFactor *= 1.75F;
				player.fallDistance = 0.0F;
				if (shrinking.getAmplifier() >= 1) {
					updateClimbingStatus(player);
				}
			}
			player.stepHeight = player.height / 3;
		}
		
	}

	private static void updateClimbingStatus(final EntityPlayer player) {
		if ((ClimbingHandler.canClimb(player, player.getHorizontalFacing()) != false)) {
			if (player.collidedHorizontally) {
				if (!player.isSneaking()) {
					player.motionY = 0.1F;
				}
				if (player.isSneaking()) {
					player.motionY = 0.0F;
				}
			}
		}
	}

	@SubscribeEvent
	public static void onLivingJump(LivingJumpEvent event) {
		final EntityLivingBase entity = event.getEntityLiving();
		final PotionEffect shrinking = entity.getActivePotionEffect(Main.SHRINKING);
		final PotionEffect growth = entity.getActivePotionEffect(Main.GROWTH);

		if (shrinking != null) {
			if (shrinking.getAmplifier() == 0) {
				entity.motionY += shrinking.getAmplifier() + 0.25D;
			} else {
				entity.motionY += shrinking.getAmplifier() / 2.0D;
			}
		}

		if (growth != null) {
			entity.motionY += (growth.getAmplifier() + 1) * 0.2;
		}
	}
}
