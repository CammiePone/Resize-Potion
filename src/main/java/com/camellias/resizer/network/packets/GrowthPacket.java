package com.camellias.resizer.network.packets;

import com.camellias.resizer.Main;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class GrowthPacket implements IMessage
{
	public GrowthPacket()
	{
		
	}
	
	public EntityPlayer toSend;
	
	public GrowthPacket(EntityPlayer toSend)
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
		buf.writeInt(toSend.getEntityId());
	}
	
//-------------------------------------------------------------------------------------------------------------------------//
	
	public static class GrowthPacketHandler implements IMessageHandler<GrowthPacket, IMessage>
	{
		@Override
		public IMessage onMessage(GrowthPacket message, MessageContext ctx)
		{
			EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
			int amount = message.toSend.getEntityId();
			
			serverPlayer.getServerWorld().addScheduledTask(() ->
			{
				EntityPlayer player = (EntityPlayer) serverPlayer.world.getEntityByID(amount);
				
				player.addPotionEffect(new PotionEffect(Main.GROWTH));
			});
			
			return null;
		}
	}
}
