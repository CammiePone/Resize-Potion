package com.camellias.resizer.potions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.camellias.resizer.Main;
import com.camellias.resizer.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

@EventBusSubscriber
public class PotionShrinking extends Potion
{
	public static final Method setSize = ReflectionHelper.findMethod(Entity.class, "setSize", "func_70105_a", float.class, float.class);
	
	public static final ResourceLocation ICON = new ResourceLocation(Reference.MODID, "textures/potions/effects.png");
	
	public PotionShrinking(String name)
	{
		super(false, 65480);
		this.setPotionName("effect." + name);
		this.setIconIndex(1, 0);
		this.setRegistryName(new ResourceLocation(Reference.MODID + name));
	}
	
	@Override
	public boolean hasStatusIcon()
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(ICON);
		return true;
	}
	
	@SubscribeEvent
	public static void onPlayerUpdate(TickEvent.PlayerTickEvent event)
	{
		EntityPlayer player = event.player;
		PotionEffect potion = player.getActivePotionEffect(Main.SHRINKING);
		
		if(player.isPotionActive(Main.SHRINKING))
		{
			float height = 0.9F / (potion.getAmplifier() + 1);
			float width = height * (1F / 3F);
			player.eyeHeight = height * 0.85F;
			
			if(player.isPotionActive(Main.GROWTH))
			{
				player.removeActivePotionEffect(Main.GROWTH);
			}
			
			AxisAlignedBB aabb = player.getEntityBoundingBox();
			
			player.stepHeight = height / 3F;
			player.setEntityBoundingBox(new AxisAlignedBB(aabb.minX, aabb.minY, aabb.minZ, aabb.minX + width,
					aabb.minY + height, aabb.minZ + width));
			
			if(!player.world.collidesWithAnyBlock(aabb))
	        {
				player.setEntityBoundingBox(new AxisAlignedBB(aabb.minX, aabb.minY, aabb.minZ, aabb.minX + width,
						aabb.minY + height, aabb.minZ + width));
				
				try
				{
					setSize.invoke(player, width, height);
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
				catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				}
				catch (InvocationTargetException e)
				{
					e.printStackTrace();
				}
	        }
		}
	}
}
