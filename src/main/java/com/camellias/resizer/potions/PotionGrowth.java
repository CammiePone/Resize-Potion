package com.camellias.resizer.potions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.lwjgl.opengl.GL11;

import com.camellias.resizer.Main;
import com.camellias.resizer.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

@EventBusSubscriber
public class PotionGrowth extends Potion
{
	public static final Method setSize = ReflectionHelper.findMethod(Entity.class, "setSize", "func_70105_a", float.class, float.class);
	
	public static final ResourceLocation ICON = new ResourceLocation(Reference.MODID, "textures/potions/effects.png");
	
	public PotionGrowth(String name)
	{
		super(false, 16750080);
		this.setPotionName("effect." + name);
		this.setIconIndex(0, 0);
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
		PotionEffect potion = player.getActivePotionEffect(Main.GROWTH);
		
		if(player.isPotionActive(Main.GROWTH))
		{
			float height = 1.8F + (potion.getAmplifier() + 1F);
			float width = 0.6F;
			player.eyeHeight = height * 0.9F;
			
			if(player.isPotionActive(Main.SHRINKING))
			{
				player.removeActivePotionEffect(Main.SHRINKING);
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
		
		if(player.isPotionActive(Main.GROWTH) == false && player.isPotionActive(Main.SHRINKING) == false)
		{
			player.eyeHeight = player.getDefaultEyeHeight();
		}
	}
	
	@SubscribeEvent
	public static void onPlayerRenderPre(RenderPlayerEvent.Pre event)
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
				
				GL11.glPushMatrix();
				GL11.glTranslated(0, 0, 0);
				GL11.glScaled(scale, scale, scale);
			}
			
			if(growth.getAmplifier() == 1)
			{
				float scale = 2.0F;
				
				GL11.glPushMatrix();
				GL11.glTranslated(0, 0, 0);
				GL11.glScaled(scale, scale, scale);
			}
		}
		
		if(player.isPotionActive(Main.SHRINKING))
		{
			if(shrinking.getAmplifier() == 0)
			{
				float scale = 0.5F;
				
				GL11.glPushMatrix();
				GL11.glTranslated(0, 0, 0);
				GL11.glScaled(scale, scale, scale);
			}
			
			if(shrinking.getAmplifier() == 1)
			{
				float scale = 0.25F;
				
				GL11.glPushMatrix();
				GL11.glTranslated(0, 0, 0);
				GL11.glScaled(scale, scale, scale);
			}
		}
	}
	
	@SubscribeEvent
	public static void onPlayerRenderPost(RenderPlayerEvent.Post event)
	{
		if(event.getEntityPlayer().isPotionActive(Main.GROWTH) || 
				event.getEntityPlayer().isPotionActive(Main.SHRINKING))
		{
			GL11.glPopMatrix();
		}
	}
}
