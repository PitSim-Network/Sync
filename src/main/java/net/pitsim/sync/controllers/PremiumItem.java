package net.pitsim.sync.controllers;

import de.tr7zw.nbtapi.NBTItem;
import net.pitsim.sync.commands.FreshCommand;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.NBTTag;
import net.pitsim.sync.enums.PantColor;
import net.pitsim.sync.enums.PremiumType;
import net.pitsim.sync.misc.Misc;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PremiumItem {
	public static List<PremiumItem> itemList = new ArrayList<>();

	static {
		new PremiumItem(PremiumType.DAMAGE_SWORD, 0, new Enchant("bill", 2));
		new PremiumItem(PremiumType.DAMAGE_SWORD, 4, new Enchant("bill", 3));
		new PremiumItem(PremiumType.DAMAGE_SWORD, 12, new Enchant("bill", 3), new Enchant("sharp", 3));
		new PremiumItem(PremiumType.DAMAGE_SWORD, 30, new Enchant("bill", 3), new Enchant("cd", 3));
		new PremiumItem(PremiumType.DAMAGE_SWORD, 10, new Enchant("bill", 3), new Enchant("pf", 2));
		new PremiumItem(PremiumType.DAMAGE_SWORD, 40, new Enchant("bill", 3), new Enchant("pf", 3));
		new PremiumItem(PremiumType.DAMAGE_SWORD, 70, new Enchant("bill", 3), new Enchant("stomp", 3));

		new PremiumItem(PremiumType.TRUE_DAMAGE_SWORD, 0, new Enchant("perun", 1));
		new PremiumItem(PremiumType.TRUE_DAMAGE_SWORD, 2, new Enchant("perun", 2));
		new PremiumItem(PremiumType.TRUE_DAMAGE_SWORD, 3, new Enchant("perun", 2), new Enchant("stomp", 3));
		new PremiumItem(PremiumType.TRUE_DAMAGE_SWORD, 4, new Enchant("perun", 3));
		new PremiumItem(PremiumType.TRUE_DAMAGE_SWORD, 10, new Enchant("perun", 3), new Enchant("sharp", 3));
		new PremiumItem(PremiumType.TRUE_DAMAGE_SWORD, 10, new Enchant("perun", 3), new Enchant("cd", 3));
		new PremiumItem(PremiumType.TRUE_DAMAGE_SWORD, 20, new Enchant("perun", 3), new Enchant("stomp", 3));
		new PremiumItem(PremiumType.TRUE_DAMAGE_SWORD, 70, new Enchant("perun", 3), new Enchant("ls", 3));
		new PremiumItem(PremiumType.TRUE_DAMAGE_SWORD, 90, new Enchant("perun", 3), new Enchant("bill", 1));
		new PremiumItem(PremiumType.TRUE_DAMAGE_SWORD, 225, new Enchant("perun", 2), new Enchant("gamble", 1));

		new PremiumItem(PremiumType.HEALING_SWORD, 0, new Enchant("ls", 3));
		new PremiumItem(PremiumType.HEALING_SWORD, 3, new Enchant("cd", 3), new Enchant("ls", 3));
		new PremiumItem(PremiumType.HEALING_SWORD, 3, new Enchant("stomp", 3), new Enchant("ls", 3));
		new PremiumItem(PremiumType.HEALING_SWORD, 18, new Enchant("pf", 3), new Enchant("ls", 3));
		new PremiumItem(PremiumType.HEALING_SWORD, 25, new Enchant("ch", 3), new Enchant("ls", 3));
		new PremiumItem(PremiumType.HEALING_SWORD, 25, new Enchant("bill", 1), new Enchant("ls", 3));
		new PremiumItem(PremiumType.HEALING_SWORD, 100, new Enchant("bill", 3), new Enchant("ls", 2));
		new PremiumItem(PremiumType.HEALING_SWORD, 120, new Enchant("bill", 2), new Enchant("ls", 3));
		new PremiumItem(PremiumType.HEALING_SWORD, 700, new Enchant("bill", 3), new Enchant("ls", 3));

		new PremiumItem(PremiumType.DEFENCE_PANTS, 0, new Enchant("cf", 3));
		new PremiumItem(PremiumType.DEFENCE_PANTS, 2, new Enchant("pero", 3), new Enchant("mirror", 1));
		new PremiumItem(PremiumType.DEFENCE_PANTS, 9, new Enchant("cf", 3), new Enchant("mirror", 1));
		new PremiumItem(PremiumType.DEFENCE_PANTS, 15, new Enchant("cf", 3), new Enchant("pero", 3));
		new PremiumItem(PremiumType.DEFENCE_PANTS, 2, new Enchant("soli", 3));
		new PremiumItem(PremiumType.DEFENCE_PANTS, 30, new Enchant("soli", 3), new Enchant("mirror", 1));
		new PremiumItem(PremiumType.DEFENCE_PANTS, 80, new Enchant("soli", 3), new Enchant("cf", 3));
		new PremiumItem(PremiumType.DEFENCE_PANTS, 10, new Enchant("newdeal", 3), new Enchant("mirror", 1));

		new PremiumItem(PremiumType.REGULARITY_PANTS, 2, new Enchant("regularity", 2));
		new PremiumItem(PremiumType.REGULARITY_PANTS, 6, new Enchant("regularity", 3));
		new PremiumItem(PremiumType.REGULARITY_PANTS, 15, new Enchant("regularity", 3), new Enchant("pero", 3));
		new PremiumItem(PremiumType.REGULARITY_PANTS, 50, new Enchant("regularity", 3), new Enchant("gtgf", 3));
		new PremiumItem(PremiumType.REGULARITY_PANTS, 60, new Enchant("regularity", 3), new Enchant("cf", 3));
		new PremiumItem(PremiumType.REGULARITY_PANTS, 75, new Enchant("regularity", 3), new Enchant("mirror", 1));

		new PremiumItem(PremiumType.RGM_PANTS, 2, new Enchant("rgm", 2));
		new PremiumItem(PremiumType.RGM_PANTS, 4, new Enchant("rgm", 2), new Enchant("prot", 3));
		new PremiumItem(PremiumType.RGM_PANTS, 10, new Enchant("rgm", 2), new Enchant("pero", 3));
		new PremiumItem(PremiumType.RGM_PANTS, 50, new Enchant("rgm", 2), new Enchant("cf", 3));
		new PremiumItem(PremiumType.RGM_PANTS, 100, new Enchant("rgm", 2), new Enchant("mirror", 1));

		new PremiumItem(PremiumType.VOLLEY_BOWS, 0, new Enchant("volley", 1));
		new PremiumItem(PremiumType.VOLLEY_BOWS, 1, new Enchant("volley", 3));
		new PremiumItem(PremiumType.VOLLEY_BOWS, 8, new Enchant("volley", 3), new Enchant("wasp", 3));
		new PremiumItem(PremiumType.VOLLEY_BOWS, 10, new Enchant("volley", 3), new Enchant("drain", 3));
		new PremiumItem(PremiumType.VOLLEY_BOWS, 10, new Enchant("volley", 3), new Enchant("pin", 3));
		new PremiumItem(PremiumType.VOLLEY_BOWS, 10, new Enchant("volley", 3), new Enchant("ftts", 3));
		new PremiumItem(PremiumType.VOLLEY_BOWS, 20, new Enchant("volley", 1), new Enchant("para", 3));
		new PremiumItem(PremiumType.VOLLEY_BOWS, 80, new Enchant("volley", 3), new Enchant("para", 3));

		new PremiumItem(PremiumType.MLB_BOWS, 0, new Enchant("mlb", 1));
		new PremiumItem(PremiumType.MLB_BOWS, 4, new Enchant("mlb", 3));
		new PremiumItem(PremiumType.MLB_BOWS, 8, new Enchant("mlb", 1), new Enchant("wasp", 2));
		new PremiumItem(PremiumType.MLB_BOWS, 35, new Enchant("mlb", 1), new Enchant("wasp", 3));
		new PremiumItem(PremiumType.MLB_BOWS, 6, new Enchant("mlb", 1), new Enchant("drain", 2));
		new PremiumItem(PremiumType.MLB_BOWS, 70, new Enchant("mlb", 1), new Enchant("drain", 3));
		new PremiumItem(PremiumType.MLB_BOWS, 6, new Enchant("mlb", 1), new Enchant("pin", 2));
		new PremiumItem(PremiumType.MLB_BOWS, 20, new Enchant("mlb", 1), new Enchant("pin", 3));
		new PremiumItem(PremiumType.MLB_BOWS, 5, new Enchant("mlb", 1), new Enchant("ftts", 2));
		new PremiumItem(PremiumType.MLB_BOWS, 15, new Enchant("mlb", 1), new Enchant("ftts", 3));
		new PremiumItem(PremiumType.MLB_BOWS, 85, new Enchant("mlb", 1), new Enchant("explo", 2));

		new PremiumItem(PremiumType.MISC, 0, new Enchant("tele", 1));
		new PremiumItem(PremiumType.MISC, 4, new Enchant("tele", 3));
		new PremiumItem(PremiumType.MISC, 200, new Enchant("mlb", 1), new Enchant("tele", 2));
		new PremiumItem(PremiumType.MISC, 400, new Enchant("mlb", 1), new Enchant("tele", 3));
		new PremiumItem(PremiumType.MISC, 400, new Enchant("mlb", 1), new Enchant("tele", 3));
	}

	public PremiumType premiumType;
	public int cost;
	public List<Enchant> enchants;

	PremiumItem(PremiumType premiumType, int cost, Enchant... enchants) {
		this.premiumType = premiumType;
		this.cost = cost;
		this.enchants = Arrays.asList(enchants);
		itemList.add(this);
	}

	public ItemStack getItemStack() {
		PantColor pantColor = null;
		switch(premiumType) {
			case DEFENCE_PANTS:
				pantColor = PantColor.BLUE;
				break;
			case REGULARITY_PANTS:
				pantColor = PantColor.RAGE;
				break;
			case RGM_PANTS:
				pantColor = PantColor.RED;
		}
		ItemStack mystic = FreshCommand.getFreshItem(premiumType.getApplyType(), pantColor);
		for(Map.Entry<PitEnchant, Integer> entry : getEnchants().entrySet()) {
			try {
				mystic = EnchantManager.addEnchant(mystic, entry.getKey(), entry.getValue(), false);
			} catch(Exception ignored) { }
		}

		NBTItem nbtItem = new NBTItem(mystic);
		nbtItem.setString(NBTTag.PREMIUM_TYPE.getRef(), premiumType.refName);
		nbtItem.setInteger(NBTTag.PREMIUM_COST.getRef(), cost);

		EnchantManager.setItemLore(nbtItem.getItem());
		return nbtItem.getItem();
	}

	public Map<PitEnchant, Integer> getEnchants() {
		Map<PitEnchant, Integer> enchantMap = new LinkedHashMap<>();
		for(Enchant enchant : enchants) enchantMap.put(enchant.pitEnchant, enchant.enchantLevel);
		return enchantMap;
	}

	private static class Enchant {
		public PitEnchant pitEnchant;
		public int enchantLevel;

		public Enchant(String refName, int enchantLevel) {
			this.pitEnchant = EnchantManager.getEnchant(refName);
			this.enchantLevel = enchantLevel;
		}
	}

	public static boolean isPremium(ItemStack itemStack) {
		if(Misc.isAirOrNull(itemStack)) return false;
		NBTItem nbtItem = new NBTItem(itemStack);
		return nbtItem.hasKey(NBTTag.PREMIUM_TYPE.getRef());
	}
}
