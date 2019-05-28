package com.camellias.resizer.common.items;

import com.camellias.resizer.Main;
import com.camellias.resizer.init.ModItems;
import com.camellias.resizer.network.NetworkHandler;
import com.camellias.resizer.network.PacketSpawnParticles;
import com.camellias.resizer.util.IHasModel;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemActiveTrinket extends Item implements IBauble, IHasModel {

	public ItemActiveTrinket(String name) {
		this.setTranslationKey(name);
		this.setRegistryName(name);
		this.setCreativeTab(CreativeTabs.TOOLS);
		this.maxStackSize = 1;
		ModItems.ITEMS.add(this);
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}

	@Override
	public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
		final IBaublesItemHandler handler = BaublesApi.getBaublesHandler((EntityPlayer) player);
		for (int a = 0; a < handler.getSlots(); a++) {
			final Item i = handler.getStackInSlot(a).getItem();
			if ((i == ModItems.GROWTH_BAUBLE) || (i == ModItems.SHRINKING_BAUBLE)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
		if (player instanceof EntityPlayerMP) {
			NetworkHandler.INSTANCE.sendToAllTracking(new PacketSpawnParticles((EntityPlayer) player), player);
			NetworkHandler.INSTANCE.sendTo(new PacketSpawnParticles((EntityPlayer) player), (EntityPlayerMP) player);
		}
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.TRINKET;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if (!world.isRemote) {
			final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			for (int i = 0; i < baubles.getSlots(); i++) {
				if (((baubles.getStackInSlot(i) == null) || baubles.getStackInSlot(i).isEmpty()) && baubles.isItemValidForSlot(i, player.getHeldItem(hand), player)) {
					final ItemStack copiedStack = player.getHeldItem(hand).copy();
					baubles.setStackInSlot(i, copiedStack.splitStack(1));
					ItemStack leftInHand = copiedStack;
					if (player.capabilities.isCreativeMode) {
						leftInHand = player.getHeldItem(hand);
					}
					this.onEquipped(baubles.getStackInSlot(i), player);
					return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, leftInHand);
				}
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public void registerModels() {
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}

}
