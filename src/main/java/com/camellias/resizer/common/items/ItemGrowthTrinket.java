package com.camellias.resizer.common.items;

import java.util.List;

import com.camellias.resizer.Main;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemGrowthTrinket extends ItemActiveTrinket {
	public ItemGrowthTrinket(String name) {
		super(name);
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		if ((player.ticksExisted % 20) == 0) {
			player.addPotionEffect(new PotionEffect(Main.GROWTH, 21, 0, true, false));
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		final String growth = TextFormatting.GOLD + I18n.format("growth.info");
		final String shift = TextFormatting.YELLOW + I18n.format("shift.prompt");
		final String whenworn = TextFormatting.DARK_PURPLE + I18n.format("whenworn.string");
		final String info1 = TextFormatting.YELLOW + I18n.format(this.getTranslationKey() + ".info1");
		final String info2 = TextFormatting.YELLOW + I18n.format(this.getTranslationKey() + ".info2");
		final String attackdamage = TextFormatting.BLUE + I18n.format(this.getTranslationKey() + ".attackdamage");
		final String knockbackresist = TextFormatting.BLUE + I18n.format(this.getTranslationKey() + ".knockbackresist");
		final String stepheight = TextFormatting.BLUE + I18n.format(this.getTranslationKey() + ".stepheight");
		final String jumpheight = TextFormatting.BLUE + I18n.format(this.getTranslationKey() + ".jumpheight");
		final String attackspeed = TextFormatting.RED + I18n.format(this.getTranslationKey() + ".attackspeed");

		tooltip.add(growth);

		if (GuiScreen.isShiftKeyDown()) {
			tooltip.add(info1);
			tooltip.add(info2);

			tooltip.add("");
			tooltip.add(whenworn);
			tooltip.add(attackdamage);
			tooltip.add(knockbackresist);
			tooltip.add(stepheight);
			tooltip.add(jumpheight);

			tooltip.add(attackspeed);
		} else {
			tooltip.add(shift);
		}
	}

}
