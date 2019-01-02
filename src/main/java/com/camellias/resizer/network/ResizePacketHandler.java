package com.camellias.resizer.network;

import com.camellias.resizer.Reference;
import com.camellias.resizer.network.packets.PacketNormalSize;
import com.camellias.resizer.network.packets.PacketAlteredSize;
import com.camellias.resizer.network.packets.PacketSpawnParticles;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ResizePacketHandler
{
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID);
	
	private static int nextId = 0;
	
	public static void init()
	{
		INSTANCE.registerMessage(PacketAlteredSize.Handler.class, PacketAlteredSize.class, next(), Side.CLIENT);
		INSTANCE.registerMessage(PacketNormalSize.Handler.class, PacketNormalSize.class, next(), Side.CLIENT);
		INSTANCE.registerMessage(PacketSpawnParticles.Handler.class, PacketSpawnParticles.class, next(), Side.CLIENT);
	}
	
	public static int next()
	{
		return nextId++;
	}
}
