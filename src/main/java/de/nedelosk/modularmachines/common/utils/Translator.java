package de.nedelosk.modularmachines.common.utils;

import java.util.IllegalFormatException;

import net.minecraft.util.text.translation.I18n;

public class Translator {
	private Translator() {

	}

	public static String translateToLocal(String key) {
		if (I18n.canTranslate(key)) {
			return I18n.translateToLocal(key);
		} else {
			return I18n.translateToFallback(key);
		}
	}

	public static boolean canTranslateToLocal(String key) {
		return I18n.canTranslate(key);
	}

	public static String translateToLocalFormatted(String key, Object... format) {
		String s = translateToLocal(key);
		try {
			return String.format(s, format);
		} catch (IllegalFormatException e) {
			String errorMessage = "Format error: " + s;
			return errorMessage;
		}
	}
}
