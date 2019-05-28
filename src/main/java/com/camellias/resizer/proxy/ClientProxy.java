package com.camellias.resizer.proxy;

import java.util.Random;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerItemRenderer(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
	}

	@Override
	public void spawnParticlesAt(UUID playerUUID) {
		final Random rand = new Random();
		final EntityPlayer player = Minecraft.getMinecraft().world.getPlayerEntityByUUID(playerUUID);
		if (player != null) {
			final double speedX = rand.nextGaussian() * 0.02D;
			final double speedY = rand.nextGaussian() * 0.02D;
			final double speedZ = rand.nextGaussian() * 0.02D;
			for (int k = 0; k < 20; ++k) {
				double partX = player.posX + (rand.nextGaussian() * player.width * 2.0F);
				double partY = player.posY + (rand.nextFloat() * player.height * 2.0F);
				double partZ = player.posZ + (rand.nextGaussian() * player.width * 2.0F);
				player.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, partX, partY, partZ, speedX, speedY, speedZ);
				partX = player.posX + (rand.nextGaussian() * player.width * 2.0F);
				partY = player.posY + (rand.nextFloat() * player.height * 2.0F);
				partZ = player.posZ + (rand.nextGaussian() * player.width * 2.0F);
				player.world.spawnParticle(EnumParticleTypes.CLOUD, partX, partY, partZ, speedX, speedY, speedZ);
			}
		}
	}

}
