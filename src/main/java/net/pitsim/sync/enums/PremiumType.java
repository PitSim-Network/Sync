package net.pitsim.sync.enums;

import dev.kyro.arcticapi.builders.AItemStackBuilder;
import net.pitsim.sync.commands.FreshCommand;
import net.pitsim.sync.controllers.EnchantManager;
import org.bukkit.inventory.ItemStack;

public enum PremiumType {
	DAMAGE_SWORD("damage"),
	TRUE_DAMAGE_SWORD("true"),
	HEALING_SWORD("healing"),
	DEFENCE_PANTS("defence"),
	REGULARITY_PANTS("regularity"),
	RGM_PANTS("rgm"),
	VOLLEY_BOWS("volley"),
	MLB_BOWS("mlb"),
	MISC("misc");

	public String refName;

	PremiumType(String refName) {
		this.refName = refName;
	}

	public static PremiumType getType(String refName) {
		for(PremiumType value : values()) {
			if(refName.toLowerCase().equals(value.refName)) return value;
		}
		return null;
	}

	public ItemStack getDisplayStack() {
		ItemStack mystic;
		try {
			switch(this) {
				case DAMAGE_SWORD:
					mystic = FreshCommand.getFreshItem(MysticType.SWORD, null);
					mystic = EnchantManager.addEnchant(mystic, EnchantManager.getEnchant("bill"), 3, false);
					return new AItemStackBuilder(mystic)
							.setName("&c&lDAMAGE SWORDS")
							.getItemStack();
				case TRUE_DAMAGE_SWORD:
					mystic = FreshCommand.getFreshItem(MysticType.SWORD, null);
					mystic = EnchantManager.addEnchant(mystic, EnchantManager.getEnchant("perun"), 3, false);
					return new AItemStackBuilder(mystic)
							.setName("bc&lTRUE DAMAGE SWORDS")
							.getItemStack();
				case HEALING_SWORD:
					mystic = FreshCommand.getFreshItem(MysticType.SWORD, null);
					mystic = EnchantManager.addEnchant(mystic, EnchantManager.getEnchant("lifesteal"), 3, false);
					return new AItemStackBuilder(mystic)
							.setName("&6&lHEALING SWORDS")
							.getItemStack();
				case DEFENCE_PANTS:
					mystic = FreshCommand.getFreshItem(MysticType.PANTS, PantColor.BLUE);
					mystic = EnchantManager.addEnchant(mystic, EnchantManager.getEnchant("soli"), 3, false);
					return new AItemStackBuilder(mystic)
							.setName("&9&lDEFENCE PANTS")
							.getItemStack();
				case REGULARITY_PANTS:
					mystic = FreshCommand.getFreshItem(MysticType.PANTS, PantColor.RAGE);
					mystic = EnchantManager.addEnchant(mystic, EnchantManager.getEnchant("regularity"), 3, false);
					return new AItemStackBuilder(mystic)
							.setName("&c&lREGULARITY PANTS")
							.getItemStack();
				case RGM_PANTS:
					mystic = FreshCommand.getFreshItem(MysticType.PANTS, PantColor.RED);
					mystic = EnchantManager.addEnchant(mystic, EnchantManager.getEnchant("rgm"), 3, false);
					return new AItemStackBuilder(mystic)
							.setName("&ec&lRGM PANTS")
							.getItemStack();
				case VOLLEY_BOWS:
					mystic = FreshCommand.getFreshItem(MysticType.BOW, null);
					mystic = EnchantManager.addEnchant(mystic, EnchantManager.getEnchant("volley"), 3, false);
					return new AItemStackBuilder(mystic)
							.setName("&c&lVOLLEY BOWS")
							.getItemStack();
				case MLB_BOWS:
					mystic = FreshCommand.getFreshItem(MysticType.BOW, null);
					mystic = EnchantManager.addEnchant(mystic, EnchantManager.getEnchant("mlb"), 1, false);
					return new AItemStackBuilder(mystic)
							.setName("&a&lMLB BOWS")
							.getItemStack();
				case MISC:
					mystic = FreshCommand.getFreshItem(MysticType.BOW, null);
//					mystic = EnchantManager.addEnchant(mystic, EnchantManager.getEnchant("punch"), 3, false);
					return new AItemStackBuilder(mystic)
							.setName("&f&lMISC ITEMS")
							.getItemStack();
			}
		} catch(Exception ignored) { }
		return null;
	}

	public int getSlot() {
		for(int i = 0; i < values().length; i++) {
			if(values()[i] == this) return i;
		}
		return -1;
	}
}
