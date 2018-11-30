package com.camellias.resizer.network.packets;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.camellias.resizer.Main;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class NormalSizePacket implements IMessage
{
	public NormalSizePacket()
	{
		
	}
	
	public static Method setSize = ReflectionHelper.findMethod(Entity.class, "setSize", "func_70105_a", float.class, float.class);
	public int playerID;
	
	public NormalSizePacket(EntityPlayer player)
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
	
	public static class ShrinkingPacketHandler implements IMessageHandler<NormalSizePacket, IMessage>
	{
		@Override
		public IMessage onMessage(NormalSizePacket message, MessageContext ctx)
		{
			Main.proxy.getThreadListener(ctx).addScheduledTask(() ->
			{
				if(Main.proxy.getPlayer(ctx) != null)
				{
					EntityPlayer player = (EntityPlayer) Main.proxy.getPlayer(ctx).world.getEntityByID(message.playerID);
					
					player.removePotionEffect(Main.GROWTH);
					player.removePotionEffect(Main.SHRINKING);
				}
			});
			
			return null;
		}
	}
}
