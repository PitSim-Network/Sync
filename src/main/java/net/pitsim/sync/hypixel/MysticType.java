package net.pitsim.sync.hypixel;

import java.awt.*;

public enum MysticType {

	SWORD("Sword", new Color(255, 255 , 85)),
	BOW("Bow", new Color(85, 255 , 255)),
	PANTS("Pants", new Color(255, 85 , 85));

	public String displayName;
	public Color embedColor;

	MysticType(String displayName, Color color) {
		this.displayName = displayName;
		this.embedColor = color;
	}

	public static MysticType getMysticType(int id) {

		switch(id) {
			case 283:
				return SWORD;
			case 261:
				return BOW;
			case 300:
				return PANTS;
		}

		return null;
	}
}
