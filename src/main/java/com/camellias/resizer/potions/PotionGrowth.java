package com.camellias.resizer.potions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import com.camellias.resizer.Main;
import com.camellias.resizer.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionGrowth extends Potion
{
	public static final ResourceLocation ICON = new ResourceLocation(Reference.MODID, "textures/potions/effects.png");
	public static UUID uuid = UUID.fromString("ecb12bf7-3a91-4783-9750-d62bc9b7f3f1");
	
	public PotionGrowth(String name)
	{
		super(false, 16750080);
		this.setPotionName("effect." + name);
		this.setIconIndex(0, 0);
		this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, uuid.toString(), -0.2D, 2);
		this.registerPotionAttributeModifier(SharedMonsterAttributes.KNOCKBACK_RESISTANCE, uuid.toString(), 0.5D, 2);
		this.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, uuid.toString(), 0.5D, 2);
		this.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, uuid.toString(), -0.5D, 2);
		
		this.setRegistryName(new ResourceLocation(name));
	}
	
	@Override
	public boolean hasStatusIcon()
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(ICON);
		return true;
	}
}
