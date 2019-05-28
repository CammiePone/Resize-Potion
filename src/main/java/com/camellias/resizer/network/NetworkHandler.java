package com.camellias.resizer.network;

import com.camellias.resizer.Reference;
import com.camellias.resizer.network.packets.PacketSpawnParticles;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID);

	private static int nextId = 0;

	public static void init() {
		INSTANCE.registerMessage(PacketSpawnParticles.Handler.class, PacketSpawnParticles.class, next(), Side.CLIENT);
	}

	public static int next() {
		return nextId++;
	}
}
