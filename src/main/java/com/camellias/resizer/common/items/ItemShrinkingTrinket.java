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

public class ItemShrinkingTrinket extends ItemActiveTrinket {

	public ItemShrinkingTrinket(String name) {
		super(name);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		final String shrinking = TextFormatting.DARK_AQUA + I18n.format("shrinking.info");
		final String shift = TextFormatting.YELLOW + I18n.format("shift.prompt");
		final String whenworn = TextFormatting.DARK_PURPLE + I18n.format("whenworn.string");

		final String info = TextFormatting.AQUA + I18n.format(this.getTranslationKey() + ".info");

		final String speed = TextFormatting.BLUE + I18n.format(this.getTranslationKey() + ".speed");
		final String attackspeed = TextFormatting.BLUE + I18n.format(this.getTranslationKey() + ".attackspeed");
		final String jumpheight = TextFormatting.BLUE + I18n.format(this.getTranslationKey() + ".jumpheight");

		final String attackdamage = TextFormatting.RED + I18n.format(this.getTranslationKey() + ".attackdamage");
		final String knockbackresist = TextFormatting.RED + I18n.format(this.getTranslationKey() + ".knockbackresist");
		final String stepheight = TextFormatting.RED + I18n.format(this.getTranslationKey() + ".stepheight");

		tooltip.add(shrinking);

		if (GuiScreen.isShiftKeyDown()) {
			tooltip.add(info);
			tooltip.add("");
			tooltip.add(whenworn);
			tooltip.add(speed);
			tooltip.add(attackspeed);
			tooltip.add(jumpheight);
			tooltip.add(attackdamage);
			tooltip.add(knockbackresist);
			tooltip.add(stepheight);
		} else {
			tooltip.add(shift);
		}
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		if ((player.ticksExisted % 20) == 0) {
			player.addPotionEffect(new PotionEffect(Main.SHRINKING, 101, 0, true, false));
		}
	}
}
