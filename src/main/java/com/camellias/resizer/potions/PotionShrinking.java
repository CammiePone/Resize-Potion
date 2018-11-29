package com.camellias.resizer.potions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import com.camellias.resizer.Main;
import com.camellias.resizer.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class PotionShrinking extends Potion
{
	public static final ResourceLocation ICON = new ResourceLocation(Reference.MODID, "textures/potions/effects.png");
	public static UUID uuid = UUID.fromString("2a69b27f-e024-4b4f-8110-7e35c740e8d6");
	
	public PotionShrinking(String name)
	{
		super(false, 65480);
		this.setPotionName("effect." + name);
		this.setIconIndex(1, 0);
		this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, uuid.toString(), 0.5D, 2);
		this.registerPotionAttributeModifier(SharedMonsterAttributes.KNOCKBACK_RESISTANCE, uuid.toString(), -1.0D, 2);
		this.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, uuid.toString(), -0.5D, 2);
		this.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, uuid.toString(), 0.5D, 2);
		
		this.setRegistryName(new ResourceLocation(name));
	}
	
	@Override
	public boolean hasStatusIcon()
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(ICON);
		return true;
	}
}
