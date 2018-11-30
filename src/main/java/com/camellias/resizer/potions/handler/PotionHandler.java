package com.camellias.resizer.potions.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import com.camellias.resizer.Main;
import com.camellias.resizer.network.ResizePacketHandler;
import com.camellias.resizer.network.packets.GrowthPacket;
import com.camellias.resizer.network.packets.NormalSizePacket;
import com.camellias.resizer.network.packets.ShrinkingPacket;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.StartTracking;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionHandler
{
	public static Method setSize = ReflectionHelper.findMethod(Entity.class, "setSize", "func_70105_a", float.class, float.class);
	public static UUID uuid = UUID.fromString("e9cb6e24-46e5-45ce-97e7-6c1664aed7f9");
	public boolean potionEffectGrowth = false;
	public boolean potionEffectShrinking = false;
	public boolean normal = true;
	
	@SubscribeEvent
	public void trackingEvent(StartTracking event)
	{
		if(event.getEntityPlayer().world != null)
		{
			EntityPlayer player = event.getEntityPlayer();
			
			if(event.getTarget() instanceof EntityPlayer)
			{
				EntityPlayer target = (EntityPlayer) event.getTarget();
				PotionEffect growth = player.getActivePotionEffect(Main.GROWTH);
				PotionEffect shrinking = player.getActivePotionEffect(Main.SHRINKING);
				
				if(!event.getEntityPlayer().world.isRemote && target.isPotionActive(Main.GROWTH))
				{
					GrowthPacket growthPacket = new GrowthPacket(target);
					growthPacket.duration = growth.getDuration();
					growthPacket.amplifier = growth.getAmplifier();
					
					ResizePacketHandler.INSTANCE.sendTo(growthPacket, (EntityPlayerMP) player);
				}
				if(!event.getEntityPlayer().world.isRemote && target.isPotionActive(Main.SHRINKING))
				{
					ShrinkingPacket shrinkingPacket = new ShrinkingPacket(target);
					shrinkingPacket.duration = shrinking.getDuration();
					shrinkingPacket.amplifier = shrinking.getAmplifier();
					
					ResizePacketHandler.INSTANCE.sendTo(shrinkingPacket, (EntityPlayerMP) player);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerUpdate(TickEvent.PlayerTickEvent event)
	{
		//----Thank you to XzeroAir from the MMD Discord for helping out with the hitbox changes. Life saver, that guy.----//
		
		EntityPlayer player = event.player;
		PotionEffect growth = player.getActivePotionEffect(Main.GROWTH);
		PotionEffect shrinking = player.getActivePotionEffect(Main.SHRINKING);
		
		if(player.isPotionActive(Main.GROWTH))
		{
			player.height = 1.8F + (growth.getAmplifier() + 1F);
			player.width = player.height * (1F / 3F);
			AxisAlignedBB aabb = player.getEntityBoundingBox();
			double d0 = (double)player.width / 2.0D;
			
			player.eyeHeight = player.height * 0.9F;
			player.stepHeight = player.height / 3F;
			
			if(player.isPotionActive(Main.SHRINKING))
			{
				player.removePotionEffect(Main.SHRINKING);
			}
			
			if(potionEffectGrowth == false)
			{
				if(player.world.isRemote)
				{
					GrowthPacket growthPacket = new GrowthPacket(player);
					growthPacket.duration = growth.getDuration();
					growthPacket.amplifier = growth.getAmplifier();
					
					ResizePacketHandler.INSTANCE.sendToAllTracking(growthPacket, player);
				}
				
				potionEffectGrowth = true;
			}
			
			if(normal == true)
			{
				normal = false;
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
            
			player.setEntityBoundingBox(new AxisAlignedBB(player.posX - d0, aabb.minY, player.posZ - d0, 
            		player.posX + d0, aabb.minY + (double)player.height, player.posZ + d0));
		}
		
		if(player.isPotionActive(Main.GROWTH) == false)
		{
			if(potionEffectGrowth == true)
			{
				potionEffectGrowth = false;
			}
		}
		
		if(player.isPotionActive(Main.SHRINKING))
		{
			player.height = 0.9F / (shrinking.getAmplifier() + 1);
			player.width = player.height * (1F / 3F);
			AxisAlignedBB aabb = player.getEntityBoundingBox();
			double d0 = (double)player.width / 2.0D;
			
			player.eyeHeight = player.height * 0.85F;
			player.stepHeight = player.height / 3F;
			player.jumpMovementFactor *= 1.75F;
			player.fallDistance = 0.0F;
			
			if(potionEffectShrinking == false)
			{
				if(player.world.isRemote)
				{
					ShrinkingPacket shrinkingPacket = new ShrinkingPacket(player);
					shrinkingPacket.duration = shrinking.getDuration();
					shrinkingPacket.amplifier = shrinking.getAmplifier();
					
					ResizePacketHandler.INSTANCE.sendToAllTracking(shrinkingPacket, player);
				}
				
				potionEffectShrinking = true;
			}
			
			if(normal == true)
			{
				normal = false;
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
			
			player.setEntityBoundingBox(new AxisAlignedBB(player.posX - d0, aabb.minY, player.posZ - d0, 
            		player.posX + d0, aabb.minY + (double)player.height, player.posZ + d0));
		}
		
		if(player.isPotionActive(Main.SHRINKING) == false)
		{
			if(potionEffectShrinking == true)
			{
				potionEffectShrinking = false;
			}
		}
		
		if(player.isPotionActive(Main.GROWTH) == false && player.isPotionActive(Main.SHRINKING) == false)
		{
			player.eyeHeight = player.getDefaultEyeHeight();
			player.stepHeight = 0.6F;
			
			if(normal == false)
			{
				ResizePacketHandler.INSTANCE.sendToAllTracking(new NormalSizePacket(player), player);
				
				normal = true;
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerJump(LivingJumpEvent event)
	{
		EntityLivingBase entity = event.getEntityLiving();
		PotionEffect potion = entity.getActivePotionEffect(Main.SHRINKING);
		
		if(entity.isPotionActive(Main.SHRINKING))
		{
			if(potion.getAmplifier() == 0)
			{
				entity.motionY += potion.getAmplifier() + 0.25D;
			}
			else
			{
				entity.motionY += potion.getAmplifier() / 2.0D;
			}
		}
	}
	
	
	
	
	
//-------------------------------------------------------------------------------------------------------------------------//
	
	
	
	
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onPlayerRenderPre(RenderPlayerEvent.Pre event)
	{
		//----Thank you Melonslise from the MMD Discord for helping getting the rendering working properly.----//
		
		EntityPlayer player = event.getEntityPlayer();
		
		float scale = player.height / 1.8F;
		
		GlStateManager.pushMatrix();
		GlStateManager.scale(scale, scale, scale);
		GlStateManager.translate((event.getX() / scale) - event.getX(), 
				(event.getY() / scale) - event.getY(), (event.getZ() / scale) - event.getZ());
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onPlayerRenderPost(RenderPlayerEvent.Post event)
	{
		GlStateManager.popMatrix();
	}
}
