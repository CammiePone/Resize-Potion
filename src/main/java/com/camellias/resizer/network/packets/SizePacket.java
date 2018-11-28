package com.camellias.resizer.network.packets;

import com.camellias.resizer.Main;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SizePacket implements IMessage
{
	public SizePacket()
	{
		
	}
	
	public int toSend;
	
	public SizePacket(int toSend)
	{
		this.toSend = toSend;
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(toSend);
	}
	
//-------------------------------------------------------------------------------------------------------------------------//
	
	public static class SizePacketHandler implements IMessageHandler<SizePacket, IMessage>
	{
		@Override
		public IMessage onMessage(SizePacket message, MessageContext ctx)
		{
			EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
			int amount = message.toSend;
			
			serverPlayer.getServerWorld().addScheduledTask(() ->
			{
				serverPlayer.getActivePotionEffect(Main.GROWTH);
				serverPlayer.getActivePotionEffect(Main.SHRINKING);
			});
			
			return null;
		}
	}
}
