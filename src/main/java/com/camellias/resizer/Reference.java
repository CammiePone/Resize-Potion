package com.camellias.resizer;

public class Reference {
	// Essentials
	public static final String MODID = "resize";
	public static final String NAME = "Resizing Potion";
	public static final String VERSION = "@VERSION@";
	public static final String ACCEPTEDVERSIONS = "[1.12.2]";
	public static final String DEPENDENCIES = "required-after:forge@[14.23.5.2795,];" + "required-after:artemislib@[1.0.0,];";
	public static final String FINGERPRINT = "@FINGERPRINT@";

	// Proxies
	public static final String CLIENT_PROXY_CLASS = "com.camellias.resizer.proxy.ClientProxy";
	public static final String COMMON_PROXY_CLASS = "com.camellias.resizer.proxy.CommonProxy";
}
