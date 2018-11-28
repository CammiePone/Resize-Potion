package com.camellias.resizer.network;

import com.camellias.resizer.Reference;
import com.camellias.resizer.network.packets.SizePacket;

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
		INSTANCE.registerMessage(SizePacket.SizePacketHandler.class, SizePacket.class, next(), Side.SERVER);
	}
	
	public static int next()
	{
		nextId++;
		return nextId - 1;
	}
}
