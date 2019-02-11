package com.camellias.resizer.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSpawnParticles extends PacketOnResize
{
	public PacketSpawnParticles() {}
	
	public PacketSpawnParticles(EntityLivingBase entity)
	{
		super(entity, true);
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		super.toBytes(buf);
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		super.fromBytes(buf);
	}
	
	public static class Handler implements IMessageHandler<PacketSpawnParticles, IMessage>
	{
		@Override
		public IMessage onMessage(PacketSpawnParticles message, MessageContext ctx)
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> message.removePotionEffect(ctx, false, false));
			return null;
		}
	}
}
