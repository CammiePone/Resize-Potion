package com.camellias.resizer;

import java.io.File;

import com.camellias.resizer.common.potions.PotionGrowth;
import com.camellias.resizer.common.potions.PotionShrinking;
import com.camellias.resizer.handlers.RegistryHandler;
import com.camellias.resizer.proxy.CommonProxy;

import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
	modid = Reference.MODID, 
	name = Reference.NAME, 
	version = Reference.VERSION, 
	acceptedMinecraftVersions = Reference.ACCEPTEDVERSIONS, 
	dependencies = Reference.DEPENDENCIES)
public class Main 
{
	public static File config;
	public static final Potion SHRINKING = new PotionShrinking("shrinking");
	public static final Potion GROWTH = new PotionGrowth("growth");
	
	@Instance
	public static Main instance;
	
	//----Proxy----//
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;
	
	//----Initialization----//
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		RegistryHandler.preInitRegistries(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}
}
