package com.camellias.resizer.attributes;

import java.util.Collection;
import java.util.UUID;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class CustomAttributes extends SharedMonsterAttributes
{
	public static final IAttribute HEIGHT = (new RangedAttribute((IAttribute)null, "generic.Size", 
			1.8F, 0.0D, 1024.0D)).setDescription("Entity Size").setShouldWatch(true);
}
