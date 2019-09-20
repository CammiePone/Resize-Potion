package com.camellias.resizer;

import java.io.File;

import com.camellias.resizer.common.potions.PotionGrowth;
import com.camellias.resizer.common.potions.PotionShrinking;
import com.camellias.resizer.handlers.RegistryHandler;
import com.camellias.resizer.proxy.CommonProxy;

import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, acceptedMinecraftVersions = Reference.ACCEPTEDVERSIONS, dependencies = Reference.DEPENDENCIES, certificateFingerprint = Reference.FINGERPRINT)
public class Main {
	public static File config;
	public static final Potion SHRINKING = new PotionShrinking("shrinking");
	public static final Potion GROWTH = new PotionGrowth("growth");

	@Instance public static Main instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS) public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		RegistryHandler.preInitRegistries(event);
	}

	@EventHandler
	public void fingerprintViolated(FMLFingerprintViolationEvent event) {
		
		System.err.println("\n\n\nInvalid signature for "+Reference.NAME
		+ "\nSomeone might have messed with the JAR file of the mod"
		+"\nMake sure to only download from the official source"
		+"\nhttps://minecraft.curseforge.com/projects/resizing-potion"
		+ "\n\n");
		
	}
}
