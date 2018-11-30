package com.camellias.resizer.network.packets;

import com.camellias.resizer.Main;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class GrowthPacket implements IMessage
{
	public GrowthPacket()
	{
		
	}
	
	public int playerID;
	public int duration;
	public int amplifier;
	
	public GrowthPacket(EntityPlayer player)
	{
		this.playerID = player.getEntityId();
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(playerID);
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.playerID = buf.readInt();
	}
	
//-------------------------------------------------------------------------------------------------------------------------//
	
	public static class GrowthPacketHandler implements IMessageHandler<GrowthPacket, IMessage>
	{
		@Override
		public IMessage onMessage(GrowthPacket message, MessageContext ctx)
		{
			Main.proxy.getThreadListener(ctx).addScheduledTask(() ->
			{
				if(Main.proxy.getPlayer(ctx) != null)
				{
					EntityPlayer player = (EntityPlayer) Main.proxy.getPlayer(ctx).world.getEntityByID(message.playerID);
					
					player.removePotionEffect(Main.SHRINKING);
					player.addPotionEffect(new PotionEffect(Main.GROWTH, message.duration, message.amplifier));
				}
			});
			
			return null;
		}
	}
}
