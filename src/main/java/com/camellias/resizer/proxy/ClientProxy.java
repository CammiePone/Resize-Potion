package com.camellias.resizer.proxy;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerItemRenderer(Item item, int meta, String id)
	{
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
	}
	
	@Override
	@Nullable
	public EntityLivingBase getEntityLivingBase(MessageContext context, int entityID)
	{
		EntityPlayer player = context.side.isClient() ? Minecraft.getMinecraft().player : context.getServerHandler().player;
		Entity entity = player.world.getEntityByID(entityID);
		return entity instanceof EntityLivingBase ? (EntityLivingBase) entity : null;
	}
	
	@Override
	public IThreadListener getThreadListener(final MessageContext context)
	{
		if(context.side.isClient())
		{
			return Minecraft.getMinecraft();
		}
		else
		{
			return context.getServerHandler().player.server;
		}
	}
}
