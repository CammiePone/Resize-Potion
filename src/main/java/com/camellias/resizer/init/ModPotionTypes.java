package com.camellias.resizer.init;

import com.camellias.resizer.Main;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModPotionTypes {
	// -----------------------------------------------------------------------------------------------------------------------//

	public static final PotionType SHRINKING = new PotionType("shrinking", new PotionEffect[]{
			new PotionEffect(Main.SHRINKING, 4800)
	}).setRegistryName("shrinking");

	public static final PotionType LONG_SHRINKING = new PotionType("shrinking", new PotionEffect[]{
			new PotionEffect(Main.SHRINKING, 9600)
	}).setRegistryName("long_shrinking");

	public static final PotionType STRONG_SHRINKING = new PotionType("shrinking", new PotionEffect[]{
			new PotionEffect(Main.SHRINKING, 3600, 1)
	}).setRegistryName("strong_shrinking");

	// -----------------------------------------------------------------------------------------------------------------------//

	public static final PotionType GROWTH = new PotionType("growth", new PotionEffect[]{
			new PotionEffect(Main.GROWTH, 4800)
	}).setRegistryName("growth");

	public static final PotionType LONG_GROWTH = new PotionType("growth", new PotionEffect[]{
			new PotionEffect(Main.GROWTH, 9600)
	}).setRegistryName("long_growth");

	public static final PotionType STRONG_GROWTH = new PotionType("growth", new PotionEffect[]{
			new PotionEffect(Main.GROWTH, 3600, 1)
	}).setRegistryName("strong_growth");

	// -----------------------------------------------------------------------------------------------------------------------//

	public static void registerPotionTypes() {
		ForgeRegistries.POTIONS.registerAll(Main.SHRINKING, Main.GROWTH);
		ForgeRegistries.POTION_TYPES.registerAll(SHRINKING, LONG_SHRINKING, STRONG_SHRINKING, GROWTH, LONG_GROWTH, STRONG_GROWTH);

		final Item shrinking = Item.getByNameOrId(ModConfig.RECIPE.shrinkingIngredient);
		PotionHelper.addMix(PotionTypes.MUNDANE, shrinking, SHRINKING);
		PotionHelper.addMix(PotionTypes.AWKWARD, shrinking, SHRINKING);
		PotionHelper.addMix(PotionTypes.THICK, shrinking, SHRINKING);
		PotionHelper.addMix(SHRINKING, Items.REDSTONE, LONG_SHRINKING);
		PotionHelper.addMix(STRONG_SHRINKING, Items.REDSTONE, LONG_SHRINKING);
		PotionHelper.addMix(SHRINKING, Items.GLOWSTONE_DUST, STRONG_SHRINKING);
		PotionHelper.addMix(LONG_SHRINKING, Items.GLOWSTONE_DUST, STRONG_SHRINKING);

		final Item growth = Item.getByNameOrId(ModConfig.RECIPE.growthIngredient);
		PotionHelper.addMix(PotionTypes.MUNDANE, growth, GROWTH);
		PotionHelper.addMix(PotionTypes.AWKWARD, growth, GROWTH);
		PotionHelper.addMix(PotionTypes.THICK, growth, GROWTH);
		PotionHelper.addMix(GROWTH, Items.REDSTONE, LONG_GROWTH);
		PotionHelper.addMix(STRONG_GROWTH, Items.REDSTONE, LONG_GROWTH);
		PotionHelper.addMix(GROWTH, Items.GLOWSTONE_DUST, STRONG_GROWTH);
		PotionHelper.addMix(LONG_GROWTH, Items.GLOWSTONE_DUST, STRONG_GROWTH);
	}
}
