package com.camellias.resizer.common.items;

import java.util.List;

import com.camellias.resizer.Main;
import com.camellias.resizer.init.ModItems;
import com.camellias.resizer.util.IHasModel;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemInertTrinket extends Item implements IHasModel {
	public ItemInertTrinket(String name) {
		this.setTranslationKey(name);
		this.setRegistryName(name);
		this.setCreativeTab(CreativeTabs.TOOLS);
		this.maxStackSize = 1;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		final String shift = TextFormatting.YELLOW + I18n.format("shift.prompt");
		final String info = TextFormatting.YELLOW + I18n.format(this.getTranslationKey() + ".info");

		if (GuiScreen.isShiftKeyDown()) {
			tooltip.add(info);
		} else {
			tooltip.add(shift);
		}

	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		final ItemStack stack = player.getHeldItem(hand);
		final PotionEffect shrinking = player.getActivePotionEffect(Main.SHRINKING);
		final PotionEffect growth = player.getActivePotionEffect(Main.GROWTH);

		if (player.isPotionActive(Main.SHRINKING) && (shrinking.getIsAmbient() == false)) {
			if (!world.isRemote) {
				player.addItemStackToInventory(new ItemStack(ModItems.SHRINKING_BAUBLE));
				stack.shrink(1);
			}

			player.removePotionEffect(Main.SHRINKING);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
		}

		if (player.isPotionActive(Main.GROWTH) && !growth.getIsAmbient()) {
			if (!world.isRemote) {
				player.addItemStackToInventory(new ItemStack(ModItems.GROWTH_BAUBLE));
				stack.shrink(1);
			}

			player.removePotionEffect(Main.GROWTH);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
		}

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public void registerModels() {
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
