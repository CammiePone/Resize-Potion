package com.camellias.resizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.camellias.resizer.init.ModPotionTypes;
import com.camellias.resizer.network.ResizePacketHandler;
import com.camellias.resizer.potions.PotionGrowth;
import com.camellias.resizer.potions.PotionShrinking;
import com.camellias.resizer.potions.handler.PotionHandler;
import com.camellias.resizer.proxy.CommonProxy;

import net.minecraft.potion.Potion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Mod(
	modid = Reference.MODID, 
	name = Reference.NAME, 
	version = Reference.VERSION, 
	acceptedMinecraftVersions = Reference.ACCEPTEDVERSIONS, 
	dependencies = Reference.DEPENDENCIES)
public class Main 
{
	public static final Potion SHRINKING = new PotionShrinking("shrinking");
	public static final Potion GROWTH = new PotionGrowth("growth");
	
	public static final Logger logger = LogManager.getLogger(Reference.NAME);
	
	@Instance
	public static Main instance;
	
	//----Proxy----//
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;
	
	//----Initialization----//
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		ForgeRegistries.POTIONS.registerAll(SHRINKING, GROWTH);
		ModPotionTypes.registerPotionTypes();
		
		MinecraftForge.EVENT_BUS.register(PotionHandler.class);
		
		
		//----Thank you Zabi for helping with forcing the potion effects to sync across clients.----//
		ResizePacketHandler.init();
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
