package com.camellias.resizer.common.items;

import java.util.List;

import com.camellias.resizer.Main;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemGrowthBauble extends ItemBauble implements IBauble
{
	public ItemGrowthBauble(String name)
	{
		super(name);
	}
	
	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player)
	{
		if(player.ticksExisted % 10 == 0)
		{
			player.addPotionEffect(new PotionEffect(Main.SHRINKING, 20, 0, true, false));
		}
		if(player.ticksExisted % 10 == 0)
		{
			player.addPotionEffect(new PotionEffect(Main.GROWTH, 20, 0, true, false));
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) 
	{
		if(!world.isRemote) 
		{
			IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			
			for(int i = 0; i < baubles.getSlots(); i++) 
			{
				if((baubles.getStackInSlot(i) == null || baubles.getStackInSlot(i).isEmpty()) && 
						baubles.isItemValidForSlot(i, player.getHeldItem(hand), player)) 
				{
					baubles.setStackInSlot(i, player.getHeldItem(hand).copy());
					
					if(!player.capabilities.isCreativeMode)
					{
						player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
					}
					
					onEquipped(player.getHeldItem(hand), player);
					break;
				}
			}
		}
		
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
	
	@Override
	public BaubleType getBaubleType(ItemStack itemstack)
	{
		return BaubleType.TRINKET;
	}
}
