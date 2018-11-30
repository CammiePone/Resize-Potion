package com.camellias.resizer.network;

import com.camellias.resizer.Reference;
import com.camellias.resizer.network.packets.GrowthPacket;
import com.camellias.resizer.network.packets.NormalSizePacket;
import com.camellias.resizer.network.packets.ShrinkingPacket;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ResizePacketHandler
{
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID);
	
	private static int nextId = 0;
	
	private ResizePacketHandler()
	{
		
	}
	
	public static void init()
	{
		INSTANCE.registerMessage(GrowthPacket.GrowthPacketHandler.class, GrowthPacket.class, next(), Side.SERVER);
		INSTANCE.registerMessage(GrowthPacket.GrowthPacketHandler.class, GrowthPacket.class, next(), Side.CLIENT);
		INSTANCE.registerMessage(ShrinkingPacket.ShrinkingPacketHandler.class, ShrinkingPacket.class, next(), Side.SERVER);
		INSTANCE.registerMessage(ShrinkingPacket.ShrinkingPacketHandler.class, ShrinkingPacket.class, next(), Side.CLIENT);
		
		INSTANCE.registerMessage(NormalSizePacket.ShrinkingPacketHandler.class, NormalSizePacket.class, next(), Side.SERVER);
		INSTANCE.registerMessage(NormalSizePacket.ShrinkingPacketHandler.class, NormalSizePacket.class, next(), Side.CLIENT);	
	}
	
	public static int next()
	{
		return nextId++;
	}
}
