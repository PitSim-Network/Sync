package net.pitsim.sync.hypixel;

import de.tr7zw.nbtapi.NBTItem;
import me.nullicorn.nedit.type.NBTCompound;
import net.pitsim.sync.commands.FreshCommand;
import net.pitsim.sync.controllers.EnchantManager;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.NBTTag;
import net.pitsim.sync.enums.PantColor;
import net.pitsim.sync.enums.SpecialItem;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Mystic {
	public NBTCompound data;

	public HypixelPlayer owner;
	public String nonce;

	public MysticType type;
	public String name;
	public List<String> lore = new ArrayList<>();
	public int tier;
	public int lives;
	public PantColor color;
	public int maxLives;
	public boolean isGemmed;
	public Map<PitEnchant, Integer> enchantMap = new LinkedHashMap<>();

	public SpecialItem specialItem;

	public Mystic(HypixelPlayer owner, SpecialItem specialItem) {
		this.owner = owner;
		this.specialItem = specialItem;
		this.nonce = specialItem.refName;
	}

	public Mystic(HypixelPlayer owner, NBTCompound data) {
		this.owner = owner;
		this.data = data;

		try {
			NBTCompound display = data.getCompound("tag").getCompound("display");
			NBTCompound attributes = data.getCompound("tag").getCompound("ExtraAttributes");

			type = MysticType.getMysticType(data.getInt("id", -1));

			name = display.getString("Name", "").replaceAll("[^\\x00-\\x7F]+.", "");
			for(Object line : display.getList("Lore")) {
				lore.add(((String) line).replaceAll("[^\\x00-\\x7F]+.", ""));
			}

			tier = attributes.getInt("UpgradeTier", -1);
			lives = attributes.getInt("Lives", -1);

			if(type == MysticType.PANTS) {
//				Bukkit.broadcastMessage(display.getInt("color", -1) + "");
				for(PantColor value : PantColor.values()) {
					if(value.decimalColor == display.getInt("color", -1)) color = value;
				}
			}

			maxLives = attributes.getInt("MaxLives", -1);
			isGemmed = attributes.containsKey("UpgradeGemsUses");
			for(Object enchantObject : attributes.getList("CustomEnchants")) {

				NBTCompound enchantInfo = (NBTCompound) enchantObject;
				Enchant enchant = Enchant.getEnchant(enchantInfo.getString("Key", ""));
				if(enchant == null) continue;
				int enchantLvl = enchantInfo.getInt("Level", -1);

				for(PitEnchant pitEnchant : EnchantManager.pitEnchants) {
					if(pitEnchant.refNames.contains(enchant.getRefName())) enchantMap.put(pitEnchant, enchantLvl);
				}
			}

			if(color == PantColor.DARK || color == PantColor.RAGE) {
				generateNonce();
			} else {
				nonce = attributes.getString("Nonce", "");
			}
		} catch(Exception ignored) {
//			ignored.printStackTrace();
		}
	}

	public void generateNonce() {
		String stringNonce = "";
		stringNonce += isGemmed ? "1" : "0";
		stringNonce += " " + maxLives;
		for(Map.Entry<PitEnchant, Integer> entry : enchantMap.entrySet()) {
			stringNonce += " " + entry.getKey().refNames.get(0) + "," + entry.getValue();
		}
		nonce = stringNonce;
	}

	public boolean isMystic() {
		return enchantMap.size() != 0;
	}

	public ItemStack getItemStack() {
		if(specialItem != null) return specialItem.getItem();

		String mysticString = type.displayName.equalsIgnoreCase("pants") ? color.refName : type.displayName;
		ItemStack mystic = FreshCommand.getFreshItem(mysticString);
		try {
			for(Map.Entry<PitEnchant, Integer> newEnchant : enchantMap.entrySet()) {
				mystic = EnchantManager.addEnchant(mystic, EnchantManager.getEnchant(newEnchant.getKey().refNames.get(0)), newEnchant.getValue(), false);
			}
		} catch(Exception ignored) { }
		NBTItem nbtItem = new NBTItem(mystic);
		nbtItem.setString(NBTTag.PIT_NONCE.getRef(), nonce);
		nbtItem.setInteger(NBTTag.CURRENT_LIVES.getRef(), lives);
		nbtItem.setInteger(NBTTag.MAX_LIVES.getRef(), maxLives);
		nbtItem.setBoolean(NBTTag.IS_GEMMED.getRef(), isGemmed);

		EnchantManager.setItemLore(nbtItem.getItem());
		return nbtItem.getItem();
	}
}
