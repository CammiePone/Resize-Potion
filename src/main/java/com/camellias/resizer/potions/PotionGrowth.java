package com.camellias.resizer.potions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import org.lwjgl.opengl.GL11;

import com.camellias.resizer.Main;
import com.camellias.resizer.Reference;
import com.camellias.resizer.attributes.CustomAttributes;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class PotionGrowth extends Potion
{
	public static final Method setSize = ReflectionHelper.findMethod(Entity.class, "setSize", "func_70105_a", float.class, float.class);
	public static final ResourceLocation ICON = new ResourceLocation(Reference.MODID, "textures/potions/effects.png");
	public static UUID uuid = UUID.fromString("ecb12bf7-3a91-4783-9750-d62bc9b7f3f1");
	
	public PotionGrowth(String name)
	{
		super(false, 16750080);
		this.setPotionName("effect." + name);
		this.setIconIndex(0, 0);
		this.registerPotionAttributeModifier(CustomAttributes.HEIGHT, uuid.toString(), 0.9D, 0);
		
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
		PotionEffect potion = player.getActivePotionEffect(Main.GROWTH);
		
		if(player.isPotionActive(Main.GROWTH))
		{
			player.height = 1.8F + (potion.getAmplifier() + 1F);
			player.width = 0.6F;//player.height * (1F / 3F);
			AxisAlignedBB aabb = player.getEntityBoundingBox();
			double d0 = (double)player.width / 2.0D;
			
			player.eyeHeight = player.height * 0.9F;
			player.stepHeight = player.height / 3F;
			
			if(player.isPotionActive(Main.SHRINKING))
			{
				player.removeActivePotionEffect(Main.SHRINKING);
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
                		player.posX + d0, aabb.minY+ (double)player.height, player.posZ + d0));
            }
			else
			{
				player.setEntityBoundingBox(new AxisAlignedBB(aabb.minX, aabb.minY, aabb.minZ, 
						aabb.minX + player.width, aabb.minY + player.height, aabb.minZ + player.width));
            }
		}
		
		if(player.isPotionActive(Main.GROWTH) == false && player.isPotionActive(Main.SHRINKING) == false)
		{
			player.eyeHeight = player.getDefaultEyeHeight();
		}
	}
	
	@SubscribeEvent
	public void onPlayerRenderPre(RenderPlayerEvent.Pre event)
	{
		EntityPlayer player = event.getEntityPlayer();
		ModelPlayer model = event.getRenderer().getMainModel();
		PotionEffect growth = player.getActivePotionEffect(Main.GROWTH);
		PotionEffect shrinking = player.getActivePotionEffect(Main.SHRINKING);
		
		if(player.isPotionActive(Main.GROWTH))
		{
			if(growth.getAmplifier() == 0)
			{
				float scale = 1.5F;
				
				GlStateManager.pushMatrix();
				GlStateManager.scale(scale, scale, scale);
			}
			
			if(growth.getAmplifier() == 1)
			{
				float scale = 2.0F;
				
				GlStateManager.pushMatrix();
				GlStateManager.scale(scale, scale, scale);
			}
		}
		
		if(player.isPotionActive(Main.SHRINKING))
		{
			if(shrinking.getAmplifier() == 0)
			{
				float scale = 0.5F;
				
				GlStateManager.pushMatrix();
				GlStateManager.scale(scale, scale, scale);
			}
			
			if(shrinking.getAmplifier() == 1)
			{
				float scale = 0.25F;
				
				GlStateManager.pushMatrix();
				GlStateManager.scale(scale, scale, scale);
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerRenderPost(RenderPlayerEvent.Post event)
	{
		if(event.getEntityPlayer().isPotionActive(Main.GROWTH) || 
				event.getEntityPlayer().isPotionActive(Main.SHRINKING))
		{
			GL11.glPopMatrix();
		}
	}
}
