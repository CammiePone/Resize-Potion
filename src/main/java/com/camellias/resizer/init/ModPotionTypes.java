package com.camellias.resizer.init;

import com.camellias.resizer.Main;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@EventBusSubscriber
public class ModPotionTypes
{
	@SubscribeEvent
	public static void onPotionRegister(RegistryEvent.Register<PotionType> event)
	{
		registerPotionTypes();
	}
	
//-----------------------------------------------------------------------------------------------------------------------//
	
	public static final PotionType SHRINKING = new PotionType("shrinking", 
			new PotionEffect[]{new PotionEffect(Main.SHRINKING, 4800)}).setRegistryName("shrinking");
	
	public static final PotionType LONG_SHRINKING = new PotionType("shrinking", 
			new PotionEffect[]{new PotionEffect(Main.SHRINKING, 9600)}).setRegistryName("long_shrinking");
	
	public static final PotionType STRONG_SHRINKING = new PotionType("shrinking",
			new PotionEffect[]{new PotionEffect(Main.SHRINKING, 3600, 1)}).setRegistryName("strong_shrinking");
	
//-----------------------------------------------------------------------------------------------------------------------//
	
	public static final PotionType GROWTH = new PotionType("growth", 
			new PotionEffect[]{new PotionEffect(Main.GROWTH, 4800)}).setRegistryName("growth");
	
	public static final PotionType LONG_GROWTH = new PotionType("growth", 
			new PotionEffect[]{new PotionEffect(Main.GROWTH, 9600)}).setRegistryName("long_growth");
	
	public static final PotionType STRONG_GROWTH = new PotionType("growth",
			new PotionEffect[]{new PotionEffect(Main.GROWTH, 3600, 1)}).setRegistryName("strong_growth");
	
//-----------------------------------------------------------------------------------------------------------------------//
	
	public static void registerPotionTypes()
	{
		ForgeRegistries.POTION_TYPES.registerAll(SHRINKING, LONG_SHRINKING, STRONG_SHRINKING,
				GROWTH, LONG_GROWTH, STRONG_GROWTH);
		
		PotionHelper.addMix(PotionTypes.MUNDANE, Item.getByNameOrId(ModConfig.shrinkingIngredient), SHRINKING);
		PotionHelper.addMix(PotionTypes.AWKWARD, Item.getByNameOrId(ModConfig.shrinkingIngredient), SHRINKING);
		PotionHelper.addMix(PotionTypes.THICK, Item.getByNameOrId(ModConfig.shrinkingIngredient), SHRINKING);
		PotionHelper.addMix(SHRINKING, Items.REDSTONE, LONG_SHRINKING);
		PotionHelper.addMix(STRONG_SHRINKING, Items.REDSTONE, LONG_SHRINKING);
		PotionHelper.addMix(SHRINKING, Items.GLOWSTONE_DUST, STRONG_SHRINKING);
		PotionHelper.addMix(LONG_SHRINKING, Items.GLOWSTONE_DUST, STRONG_SHRINKING);
		
		PotionHelper.addMix(PotionTypes.MUNDANE, Item.getByNameOrId(ModConfig.growthIngredient), GROWTH);
		PotionHelper.addMix(PotionTypes.AWKWARD, Item.getByNameOrId(ModConfig.growthIngredient), GROWTH);
		PotionHelper.addMix(PotionTypes.THICK, Item.getByNameOrId(ModConfig.growthIngredient), GROWTH);
		PotionHelper.addMix(GROWTH, Items.REDSTONE, LONG_GROWTH);
		PotionHelper.addMix(STRONG_GROWTH, Items.REDSTONE, LONG_GROWTH);
		PotionHelper.addMix(GROWTH, Items.GLOWSTONE_DUST, STRONG_GROWTH);
		PotionHelper.addMix(LONG_GROWTH, Items.GLOWSTONE_DUST, STRONG_GROWTH);
	}
}
