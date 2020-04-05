package com.camellias.resizer.init;

import com.camellias.resizer.Reference;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Reference.MODID, category = "", name = Reference.MODID + "/" + Reference.MODID)
@LangKey(Reference.MODID + ".config.title")
@EventBusSubscriber
public class ModConfig {
	private static final String PREFIX = "config." + Reference.MODID;

	@Name("Potion Recipe Item") 
	@Comment("Change the items used to brew the potions") 
	@LangKey(Recipe.PREFIX) 
	public static final Recipe RECIPE = new Recipe();
	
	@SubscribeEvent
	public static void onConfigChanged(OnConfigChangedEvent event) {
		if (event.getModID().equalsIgnoreCase(Reference.MODID)) {
			ConfigManager.sync(Reference.MODID, Type.INSTANCE);
		}
	}

	public static class Recipe {
		private static final String PREFIX = ModConfig.PREFIX + ".recipe";

		@Name("Ingredient for the Shrinking Potion")
		@LangKey(PREFIX + ".potion.shrinking") 
		public String shrinkingIngredient = "minecraft:brown_mushroom";

		@Name("Ingredient for the Growth Potion")
		@LangKey(PREFIX + ".potion.growth") 
		public String growthIngredient = "minecraft:red_mushroom";
	}
}