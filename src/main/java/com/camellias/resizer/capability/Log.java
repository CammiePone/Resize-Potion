package com.camellias.resizer.capability;

import com.camellias.resizer.Main;

public class Log {

	public static void i(String s) {
		Main.logger.info(s);
	}

	public static void w(String s) {
		Main.logger.warn(s);
	}

	public static void e(String s) {
		Main.logger.error(s);
	}

	public static void i(Object s) {
		Main.logger.info(s);
	}

	public static void w(Object s) {
		Main.logger.warn(s);
	}

	public static void e(Object s) {
		Main.logger.error(s);
	}

	public static void askForReport() {
		StringBuilder sb = new StringBuilder();
		String s = "This is a bug in Resizing Potion. Report it, if this is the latest version.";
		for (int i = 0; i < s.length() + 10; i++) {
			sb.append("#");
		}
		String frame = sb.toString();
		e(frame);
		e("#    " + s + "    #");
		e(frame);
		Thread.dumpStack();
	}

	public static void d(String s) {
		if ("true".equals(System.getProperty("debug"))) {
			i("[DEBUG] -- " + s);
		} else {
			Main.logger.debug(s);
		}
	}
}
