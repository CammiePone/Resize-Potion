package com.camellias.resizer.network.packets;

import java.util.Random;

import javax.annotation.Nullable;

import com.camellias.resizer.Main;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class PacketOnResize implements IMessage
{
	public int playerID;
	private boolean shouldSpawnParticles;
	
	public PacketOnResize() {}
	
	public PacketOnResize(EntityPlayer player, boolean shouldSpawnParticles)
	{
		playerID = player.getEntityId();
		this.shouldSpawnParticles = shouldSpawnParticles;
	}
	
	public boolean shouldSpawnParticles()
	{
		return shouldSpawnParticles;
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(playerID);
		buf.writeBoolean(shouldSpawnParticles);
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		playerID = buf.readInt();
		shouldSpawnParticles = buf.readBoolean();
	}
	
	/**
	 * Removes the specified resize effects from the player and, if allowed by the packet, spawns particles at the resized player's location
	 * 
	 * @param ctx handler context the packet is in
	 * @param removeGrowth removes growth effect from player
	 * @param removeShrinking removes shrinking effect from player
	 * @return resized player, if that player was found, or null if not found
	 */
	@Nullable
	protected EntityPlayer removePotionEffect(MessageContext ctx, boolean removeGrowth, boolean removeShrinking)
	{
		if (Main.proxy.getPlayer(ctx) != null)
		{
			EntityPlayer player = (EntityPlayer) Main.proxy.getPlayer(ctx).world.getEntityByID(playerID);
			if (player != null)
			{
				if (removeGrowth)
				{
					player.removePotionEffect(Main.GROWTH);
				}
				if (removeShrinking)
				{
					player.removePotionEffect(Main.SHRINKING);
				}
				if (shouldSpawnParticles)
				{
					// Emulate EntityLiving#spawnExplosionParticle to spawn particles at the resized player's location
					Random rand = new Random();
					double xSpeed = rand.nextGaussian() * 0.02;
					double ySpeed = rand.nextGaussian() * 0.02;
					double zSpeed = rand.nextGaussian() * 0.02;
					for (int k = 0; k < 30; ++k)
					{
						player.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, 
								player.posX + rand.nextFloat() * player.width * 2.0 - player.width,
								player.posY + rand.nextFloat() * player.height,
								player.posZ + rand.nextFloat() * player.width * 2.0 - player.width,
								xSpeed, ySpeed, zSpeed);
					}
				}
				return player;
			}
		}
		return null;
	}
}