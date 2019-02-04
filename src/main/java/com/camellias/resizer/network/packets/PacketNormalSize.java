package com.camellias.resizer.network.packets;

import com.camellias.resizer.Main;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketNormalSize extends PacketOnResize
{
	public PacketNormalSize() {}
	
	public PacketNormalSize(EntityLivingBase entity)
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
	
	public static class Handler implements IMessageHandler<PacketNormalSize, IMessage>
	{
		@Override
		public IMessage onMessage(PacketNormalSize message, MessageContext ctx)
		{
			Main.proxy.getThreadListener(ctx).addScheduledTask(() -> message.removePotionEffect(ctx, true, true));
			return null;
		}
	}
}
