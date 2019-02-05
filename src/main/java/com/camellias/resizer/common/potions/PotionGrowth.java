package com.camellias.resizer.common.potions;

import java.util.UUID;

import com.camellias.resizer.Reference;
import com.camellias.resizer.init.ModConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class PotionGrowth extends Potion
{
	public static final ResourceLocation ICON = new ResourceLocation(Reference.MODID, "textures/potions/effects.png");
	public static UUID uuid = UUID.fromString("ecb12bf7-3a91-4783-9750-d62bc9b7f3f1");
	
	public PotionGrowth(String name)
	{
		super(false, 16750080);
		this.setPotionName("effect." + name);
		this.setIconIndex(0, 0);
		
		this.registerPotionAttributeModifier(SharedMonsterAttributes.KNOCKBACK_RESISTANCE, uuid.toString(), 0.5D, 2);
		this.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, uuid.toString(), 0.5D, 2);
		this.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, uuid.toString(), -0.2D, 2);
		
		this.setRegistryName(new ResourceLocation(name));
	}
	
	@Override
	public boolean hasStatusIcon()
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(ICON);
		return true;
	}
}
