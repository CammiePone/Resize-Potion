package com.camellias.resizer.common.items;

import java.util.Random;

import com.camellias.resizer.Main;
import com.camellias.resizer.init.ModItems;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class ItemShrinkingBauble extends ItemBauble implements IBauble
{
	public ItemShrinkingBauble(String name)
	{
		super(name);
	}
	
	@Override
	public boolean canEquip(ItemStack itemstack, EntityLivingBase player)
	{
		if(BaublesApi.isBaubleEquipped((EntityPlayer) player, ModItems.GROWTH_BAUBLE) > -1)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	@Override
	public void onEquipped(ItemStack itemstack, EntityLivingBase player)
	{
		Random rand = new Random();
		double d2 = rand.nextGaussian() * 0.02D;
        double d0 = rand.nextGaussian() * 0.02D;
        double d1 = rand.nextGaussian() * 0.02D;
		
		for (int k = 0; k < 30; ++k)
        {
			player.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, 
					player.posX + (double)(rand.nextFloat() * player.width * 2.0F) - (double)player.width,
					player.posY + (double)(rand.nextFloat() * player.height),
					player.posZ + (double)(rand.nextFloat() * player.width * 2.0F) - (double)player.width,
					d2, d0, d1);
        }
	}
	
	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player)
	{
		if(player.ticksExisted % 10 == 0)
		{
			player.addPotionEffect(new PotionEffect(Main.SHRINKING, 20, 0, true, false));
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
