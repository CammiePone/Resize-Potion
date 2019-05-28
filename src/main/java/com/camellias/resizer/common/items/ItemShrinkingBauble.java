package com.camellias.resizer.common.items;

import java.util.List;

import com.camellias.resizer.Main;
import com.camellias.resizer.init.ModItems;
import com.camellias.resizer.network.NetworkHandler;
import com.camellias.resizer.network.PacketSpawnParticles;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemShrinkingBauble extends ItemBauble implements IBauble {
	public ItemShrinkingBauble(String name) {
		super(name);
	}

	@Override
	public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
		return BaublesApi.isBaubleEquipped((EntityPlayer) player, ModItems.GROWTH_BAUBLE) < 0;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		final String shrinking = TextFormatting.DARK_AQUA + I18n.format("shrinking.info");
		final String shift = TextFormatting.YELLOW + I18n.format("shift.prompt");
		final String whenworn = TextFormatting.DARK_PURPLE + I18n.format("whenworn.string");

		final String info1 = TextFormatting.AQUA + I18n.format(this.getTranslationKey() + ".info1");
		final String info2 = TextFormatting.AQUA + I18n.format(this.getTranslationKey() + ".info2");

		final String speed = TextFormatting.BLUE + I18n.format(this.getTranslationKey() + ".speed");
		final String attackspeed = TextFormatting.BLUE + I18n.format(this.getTranslationKey() + ".attackspeed");
		final String jumpheight = TextFormatting.BLUE + I18n.format(this.getTranslationKey() + ".jumpheight");

		final String attackdamage = TextFormatting.RED + I18n.format(this.getTranslationKey() + ".attackdamage");
		final String knockbackresist = TextFormatting.RED + I18n.format(this.getTranslationKey() + ".knockbackresist");
		final String stepheight = TextFormatting.RED + I18n.format(this.getTranslationKey() + ".stepheight");

		tooltip.add(shrinking);

		if (GuiScreen.isShiftKeyDown()) {
			tooltip.add(info1);
			tooltip.add(info2);

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
	public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
		if (player instanceof EntityPlayerMP) {
			NetworkHandler.INSTANCE.sendToAllTracking(new PacketSpawnParticles((EntityPlayer) player), player);
			NetworkHandler.INSTANCE.sendTo(new PacketSpawnParticles((EntityPlayer) player), (EntityPlayerMP) player);
		}
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		if ((player.ticksExisted % 10) == 0) {
			player.addPotionEffect(new PotionEffect(Main.SHRINKING, 20, 0, true, false));
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if (!world.isRemote) {
			final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);

			for (int i = 0; i < baubles.getSlots(); i++) {
				if (((baubles.getStackInSlot(i) == null) || baubles.getStackInSlot(i).isEmpty()) && baubles.isItemValidForSlot(i, player.getHeldItem(hand), player)) {
					baubles.setStackInSlot(i, player.getHeldItem(hand).copy());

					if (!player.capabilities.isCreativeMode) {
						player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
					}

					this.onEquipped(player.getHeldItem(hand), player);
					break;
				}
			}
		}

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.TRINKET;
	}
}
