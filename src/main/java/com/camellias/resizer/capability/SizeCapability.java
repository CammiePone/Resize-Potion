package com.camellias.resizer.capability;

import com.camellias.resizer.potions.handler.PotionHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class SizeCapability extends SimpleCapability
{
	@CapabilityInject(value = SizeCapability.class)
	public static final Capability<SizeCapability> CAPABILITY = null;
	
	public static final SizeCapability INSTANCE = new SizeCapability();
	
	public float scale;
	
	@Override
	public boolean isRelevantFor(Entity entity)
	{
		return entity instanceof EntityPlayer;
	}
	
	@Override
	public SimpleCapability getNewInstance()
	{
		return INSTANCE;
	}
}
