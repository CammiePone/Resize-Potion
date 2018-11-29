package com.camellias.resizer.potions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import com.camellias.resizer.Main;
import com.camellias.resizer.Reference;
import com.camellias.resizer.attributes.CustomAttributes;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class PotionShrinking extends Potion
{
	public static final Method setSize = ReflectionHelper.findMethod(Entity.class, "setSize", "func_70105_a", float.class, float.class);
	public static final ResourceLocation ICON = new ResourceLocation(Reference.MODID, "textures/potions/effects.png");
	public static UUID uuid = UUID.fromString("2a69b27f-e024-4b4f-8110-7e35c740e8d6");
	
	public PotionShrinking(String name)
	{
		super(false, 65480);
		this.setPotionName("effect." + name);
		this.setIconIndex(1, 0);
		this.registerPotionAttributeModifier(CustomAttributes.HEIGHT, uuid.toString(), -0.5D, 0);
		
		this.setRegistryName(new ResourceLocation(Reference.MODID + name));
	}
	
	@Override
	public boolean hasStatusIcon()
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(ICON);
		return true;
	}
	
	@SubscribeEvent
	public void onPlayerUpdate(TickEvent.PlayerTickEvent event)
	{
		EntityPlayer player = event.player;
		PotionEffect potion = player.getActivePotionEffect(Main.SHRINKING);
		
		if(player.isPotionActive(Main.SHRINKING))
		{
			player.height = 0.9F / (potion.getAmplifier() + 1);
			player.width = player.height * (1F / 3F);
			AxisAlignedBB aabb = player.getEntityBoundingBox();
			double d0 = (double)player.width / 2.0D;
			
			player.eyeHeight = player.height * 0.85F;
			player.stepHeight = player.height / 3F;
			
			if(player.isPotionActive(Main.GROWTH))
			{
				player.removeActivePotionEffect(Main.GROWTH);
			}
			
			try
			{
				setSize.invoke(player, player.width, player.height);
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
			
			if(Loader.isModLoaded("metamorph"))
			{
                player.setEntityBoundingBox(new AxisAlignedBB(player.posX - d0, aabb.minY, player.posZ - d0, 
                		player.posX + d0, aabb.minY + (double)player.height, player.posZ + d0));
            }
			else
			{
				player.setEntityBoundingBox(new AxisAlignedBB(aabb.minX, aabb.minY, aabb.minZ, 
						aabb.minX + player.width, aabb.minY + player.height, aabb.minZ + player.width));
            }
		}
	}
}
