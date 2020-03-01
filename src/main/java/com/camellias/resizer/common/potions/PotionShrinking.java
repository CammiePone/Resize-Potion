package com.camellias.resizer.common.potions;

import java.util.UUID;

import com.artemis.artemislib.util.attributes.ArtemisLibAttributes;
import com.camellias.resizer.Main;
import com.camellias.resizer.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class PotionShrinking extends Potion {
	public static final ResourceLocation ICON = new ResourceLocation(Reference.MODID, "textures/potions/effects.png");
	public static UUID uuid = UUID.fromString("2a69b27f-e024-4b4f-8110-7e35c740e8d6");
	public static UUID uuid_own = UUID.fromString("3df9b27f-a0d4-4b4f-8110-7e3fc740e8d6");
	
	public PotionShrinking(String name) {
		super(false, 65480);
		this.setPotionName("effect." + name);
		this.setIconIndex(1, 0);

		this.registerPotionAttributeModifier(ArtemisLibAttributes.ENTITY_HEIGHT, uuid_own.toString(), -0.5D, AttribOperationHelper.PERCENTUAL_ADDITION);
		this.registerPotionAttributeModifier(ArtemisLibAttributes.ENTITY_WIDTH, uuid_own.toString(), -0.5D, AttribOperationHelper.PERCENTUAL_ADDITION);
		this.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, uuid.toString(), 0.5D, AttribOperationHelper.PERCENTUAL_ADDITION);
		this.registerPotionAttributeModifier(SharedMonsterAttributes.KNOCKBACK_RESISTANCE, uuid.toString(), -1.0D, AttribOperationHelper.PERCENTUAL_ADDITION);
		this.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, uuid.toString(), -0.25D, AttribOperationHelper.PERCENTUAL_ADDITION);

		this.setRegistryName(new ResourceLocation(Reference.MODID, name));
	}
	
	@Override
	public void applyAttributesModifiersToEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier) {
		entityLivingBaseIn.removePotionEffect(Main.GROWTH);
		super.applyAttributesModifiersToEntity(entityLivingBaseIn, attributeMapIn, amplifier);
	}
	
	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase entity, AbstractAttributeMap attributeMapIn, int amplifier) {
		super.removeAttributesModifiersFromEntity(entity, attributeMapIn, amplifier);
		if (entity instanceof EntityPlayer) {
			entity.stepHeight = 0.6f;
		}
	}
	
	@Override
	public double getAttributeModifierAmount(int amplifier, AttributeModifier modifier) {
		if (modifier.getID().equals(uuid_own)) {
			return -1d + (1d/(1<<(amplifier + 1))); //Shrinking now has a -1 + 1/(2^lvl) reduction in size (-0% -> -50% -> -75% -> -87.5%...)
		}
		return super.getAttributeModifierAmount(amplifier, modifier);
	}

	@Override
	public boolean hasStatusIcon() {
		Minecraft.getMinecraft().renderEngine.bindTexture(ICON);
		return true;
	}
}
