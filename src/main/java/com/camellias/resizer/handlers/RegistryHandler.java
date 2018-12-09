package com.camellias.resizer.handlers;

import com.camellias.resizer.Main;
import com.camellias.resizer.init.ModConfig;
import com.camellias.resizer.init.ModItems;
import com.camellias.resizer.init.ModPotionTypes;
import com.camellias.resizer.network.ResizePacketHandler;
import com.camellias.resizer.util.IHasModel;

import net.minecraft.item.Item;
import net.minecraft.potion.PotionType;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@EventBusSubscriber
public class RegistryHandler
{
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event)
	{
		if(Loader.isModLoaded("baubles"))
		{
			event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
		}
	}
	
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event)
	{
		if(Loader.isModLoaded("baubles"))
		{
			for(Item item : ModItems.ITEMS)
			{
				if(item instanceof IHasModel)
				{
					((IHasModel)item).registerModels();
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onPotionRegister(RegistryEvent.Register<PotionType> event)
	{
		ModPotionTypes.registerPotionTypes();
	}
	
	public static void preInitRegistries(FMLPreInitializationEvent event)
	{
		ResizePacketHandler.init();
		ModConfig.registerConfig(event);
	}
}
