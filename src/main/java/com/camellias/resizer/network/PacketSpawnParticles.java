package com.camellias.resizer.network;

import java.util.UUID;

import com.camellias.resizer.Main;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSpawnParticles implements IMessage {

	public UUID p;

	public PacketSpawnParticles() {
		// NO_OP
	}

	public PacketSpawnParticles(EntityPlayer player) {
		this.p = player.getPersistentID();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.p = new UUID(buf.readLong(), buf.readLong());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(this.p.getMostSignificantBits());
		buf.writeLong(this.p.getLeastSignificantBits());
	}

	public static IMessage handle(PacketSpawnParticles msg, MessageContext ctx) {
		Main.proxy.spawnParticlesAt(msg.p);
		return null;
	}

}
