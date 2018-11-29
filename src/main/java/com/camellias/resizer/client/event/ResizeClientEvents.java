package com.camellias.resizer.client.event;

import com.camellias.resizer.Main;
import com.camellias.resizer.Reference;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = Reference.MODID, value = Side.CLIENT)
public class ResizeClientEvents
{
	@SubscribeEvent
	public static void onPlayerRenderPre(RenderPlayerEvent.Pre event)
	{
		EntityPlayer player = event.getEntityPlayer();
		PotionEffect growth = player.getActivePotionEffect(Main.GROWTH);
		PotionEffect shrinking = player.getActivePotionEffect(Main.SHRINKING);

		float scale = 1F;

		if(player.isPotionActive(Main.GROWTH))
		{
			if(growth.getAmplifier() == 0) scale = 1.5F;
			else scale = (float) growth.getAmplifier() + 1F;
		}
		if(player.isPotionActive(Main.SHRINKING))
		{
			if(shrinking.getAmplifier() == 0) scale = 0.5F;
			else scale = (float) Math.pow(0.25D, shrinking.getAmplifier());
		}
		if(event.getEntityPlayer().isPotionActive(Main.GROWTH) || event.getEntityPlayer().isPotionActive(Main.SHRINKING))
		{
			GlStateManager.pushMatrix();
			GlStateManager.scale(scale, scale, scale);
			GlStateManager.translate((event.getX() / scale) - event.getX(), (event.getY() / scale) - event.getY(), (event.getZ() / scale) - event.getZ());
		}
	}

	@SubscribeEvent
	public static void onPlayerRenderPost(RenderPlayerEvent.Post event)
	{
		if(event.getEntityPlayer().isPotionActive(Main.GROWTH) || event.getEntityPlayer().isPotionActive(Main.SHRINKING)) GlStateManager.popMatrix();
	}
}