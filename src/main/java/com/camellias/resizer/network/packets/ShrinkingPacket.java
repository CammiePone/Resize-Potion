package com.camellias.resizer.network.packets;

import com.camellias.resizer.Main;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ShrinkingPacket implements IMessage
{
	public ShrinkingPacket()
	{
		
	}
	
	public int playerID;
	
	public ShrinkingPacket(EntityPlayer player)
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
	
	public static class ShrinkingPacketHandler implements IMessageHandler<ShrinkingPacket, IMessage>
	{
		@Override
		public IMessage onMessage(ShrinkingPacket message, MessageContext ctx)
		{
			EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
			
			serverPlayer.getServerWorld().addScheduledTask(() ->
			{
				int entityID = message.playerID;
				
				if(serverPlayer.world.getEntityByID(entityID) instanceof EntityPlayer)
				{
					EntityPlayer player = (EntityPlayer) serverPlayer.world.getEntityByID(entityID);
					
					player.addPotionEffect(new PotionEffect(Main.SHRINKING));
				}
			});
			
			return null;
		}
	}
}
