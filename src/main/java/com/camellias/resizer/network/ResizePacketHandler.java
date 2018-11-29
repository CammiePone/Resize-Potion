package com.camellias.resizer.network;

import com.camellias.resizer.Reference;
import com.camellias.resizer.capability.CapabilityMessage;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
		registerSimpleMessage(CapabilityMessage.class, next(), Side.CLIENT);
	}
	
	private static <MSG extends SimpleMessage<MSG>> void registerSimpleMessage(Class<CapabilityMessage> class1, int id, Side side)
	{
		INSTANCE.registerMessage(class1, class1, id, side);
	}
	
	public static int next()
	{
		nextId++;
		return nextId - 1;
	}
}
