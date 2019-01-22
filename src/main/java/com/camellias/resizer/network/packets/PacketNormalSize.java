package com.camellias.resizer.network.packets;

import com.camellias.resizer.Main;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketNormalSize extends PacketOnResize
{
	public PacketNormalSize() {}
	
	public PacketNormalSize(EntityPlayer player)
	{
		super(player, true);
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
