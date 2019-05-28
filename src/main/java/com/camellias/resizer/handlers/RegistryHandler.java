package com.camellias.resizer.handlers;

import com.camellias.resizer.init.ModItems;
import com.camellias.resizer.init.ModPotionTypes;
import com.camellias.resizer.network.NetworkHandler;

import net.minecraft.item.Item;
import net.minecraft.potion.PotionType;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class RegistryHandler {
	
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		if (Loader.isModLoaded("baubles")) {
			event.getRegistry().registerAll(ModItems.INERT_BAUBLE, ModItems.GROWTH_BAUBLE, ModItems.SHRINKING_BAUBLE);
		}
	}

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		if (Loader.isModLoaded("baubles")) {
			ModItems.INERT_BAUBLE.registerModels();
			ModItems.GROWTH_BAUBLE.registerModels();
			ModItems.SHRINKING_BAUBLE.registerModels();
		}
	}

	@SubscribeEvent
	public static void onPotionRegister(RegistryEvent.Register<PotionType> event) {
		ModPotionTypes.registerPotionTypes();
	}

	public static void preInitRegistries(FMLPreInitializationEvent event) {
		NetworkHandler.init();
	}
}
