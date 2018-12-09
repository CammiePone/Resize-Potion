package com.camellias.resizer.init;

import java.io.File;

import com.camellias.resizer.Main;
import com.camellias.resizer.Reference;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModConfig
{
	public static Configuration config;
	
	public static String shrinkingIngredient = "minecraft:brown_mushroom";
	public static String growthIngredient = "minecraft:red_mushroom";
	
	public static void init(File file)
	{
		config = new Configuration(file);
		
		String category;
		
		category = "Potion Recipe Item";
		config.addCustomCategoryComment(category, "Change the items used to brew the potions.");
		
		shrinkingIngredient = config.getString("Ingredient for the Shrinking Potion", category, "minecraft:brown_mushroom", "");
		growthIngredient = config.getString("Ingredient for the Growth Potion", category, "minecraft:red_mushroom", "");
		
		config.save();
	}
	
	public static void registerConfig(FMLPreInitializationEvent event)
	{
		Main.config = new File(event.getModConfigurationDirectory() + "/" + Reference.MODID);
		Main.config.mkdirs();
		init(new File(Main.config.getPath(), Reference.MODID + ".cfg"));
	}
}
