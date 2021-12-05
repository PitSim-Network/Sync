package net.pitsim.sync.enums;

import net.pitsim.sync.misc.Misc;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public enum PantColor {

	RED("Red", ChatColor.RED, 0xFF5555, 16733525),
	ORANGE("Orange", ChatColor.GOLD, 0xFFAA00, 16755200),
	YELLOW("Yellow", ChatColor.YELLOW, 0xFFFF55, 16777045),
	GREEN("Green", ChatColor.GREEN, 0x55FF55, 5635925),
	BLUE("Blue", ChatColor.BLUE, 0x5555FF, 5592575),
	//Premium Colors
	PEACE_WHITE("Peace White", ChatColor.WHITE, 0xFFFFFF, 16777215),
	BUSINESS_GRAY("Business Gray", ChatColor.GRAY, 0xA9A9A9, 11119017),
	PURE_RED("Pure Red", ChatColor.RED, 0xFF0000, 16711680),
	BLOOD_RED("Blood Red", ChatColor.DARK_RED, 0x660000, 6684672),
	SOLID_TEAL("Solid Teal", ChatColor.DARK_AQUA, 0x8080, 32896),
	DEEP_SKY("Deep Sky", ChatColor.AQUA, 0xBFFF, 49151),
	DARK_ORCHID("Dark Orchid", ChatColor.DARK_PURPLE, 0x9932CC, 10040012),
	SMOOTH_CRIMSON("Smooth Crimson", ChatColor.DARK_PURPLE, 0xC6143A, 12981306),
	HOT_PINK("Hot Pink", ChatColor.LIGHT_PURPLE, 0xFF00FF, 16711935),
	UGLY_CLASSIC("Ugly Classic", ChatColor.WHITE, 0xA06540, 10511680),
	SHADOW_GRAY("Shadow Gray", ChatColor.DARK_GRAY, 0x606060, 6316128),
	NIGHT_RIDER("Night Rider", ChatColor.DARK_PURPLE, 0x400060, 4194400),
	LIGHT_LAVENDER("Light Lavender", ChatColor.LIGHT_PURPLE, 0xDDAADD, 14527197),
	BLUEBERRY_BLUES("Blueberry Blues", ChatColor.BLUE, 0x555599, 5592473),
	SOOTHING_BEIGE("Soothing Beige", ChatColor.YELLOW, 0xFFDDAA, 16768426),
	NOT_ORANGE("Not Orange", ChatColor.GOLD, 0xFFBB00, 16759552),
	NEON_GREEN("Neon Green", ChatColor.GREEN, 0xAAE100, 11198720),
	HARVEST_RED("Harvest Red", ChatColor.RED, 0xEE3300, 15610624),
	DEFAULT("Default", ChatColor.GRAY, 0xA06540, -1),




	//Special Colors
	DARK("Dark", ChatColor.DARK_PURPLE, 0x000000, 0),
	JEWEL("Jewel", ChatColor.DARK_AQUA, 0x7DC383, 8242051),
	RAGE("Rage", ChatColor.DARK_RED, 0x780000, 7864320),
	AQUA("", ChatColor.DARK_AQUA, 0x55FFFF, 5636095);

	public String refName;
	public ChatColor chatColor;
	public int hexColor;
	public int decimalColor;

	PantColor(String refName, ChatColor chatColor, int hexColor, int decimalColor) {
		this.refName = refName;
		this.chatColor = chatColor;
		this.hexColor = hexColor;
		this.decimalColor = decimalColor;
	}

	public static PantColor getNormalRandom() {

		return values()[(int) (Math.random() * 5)];
	}

	public static PantColor getPantColor(String refName) {

		for(PantColor pantColor : values()) {

			if(pantColor.refName.equalsIgnoreCase(refName)) return pantColor;
		}
		return null;
	}

	public static PantColor getPantColor(ItemStack itemStack) {

		if(Misc.isAirOrNull(itemStack) || itemStack.getType() != Material.LEATHER_LEGGINGS) return null;
		LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();

		for(PantColor pantColor : values()) {

			if(Color.fromRGB(pantColor.hexColor).equals(leatherArmorMeta.getColor())) return pantColor;
		}

		return null;
	}

	public static void setPantColor(ItemStack itemStack, PantColor pantColor) {
		if(Misc.isAirOrNull(itemStack) || itemStack.getType() != Material.LEATHER_LEGGINGS) return;
		LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();

		leatherArmorMeta.setColor(Color.fromRGB(pantColor.hexColor));
		
		leatherArmorMeta.setDisplayName(pantColor.chatColor + (ChatColor.RESET + leatherArmorMeta.getDisplayName()));
		itemStack.setItemMeta(leatherArmorMeta);
	}
}
