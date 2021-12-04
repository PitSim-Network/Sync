package net.pitsim.sync.hypixel;

import me.nullicorn.nedit.type.NBTCompound;
import net.pitsim.sync.enums.PantColor;
import org.bukkit.Bukkit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mystic {

	public NBTCompound data;

	public HypixelPlayer owner;
	public MysticType type;
	public String name;
	public List<String> lore = new ArrayList<>();
	public long nonce;
	public int tier;
	public int lives;
	public PantColor color;
	public int maxLives;
	public boolean isGemmed;
	public Map<Enchant, Integer> enchantMap = new HashMap<>();

	public Mystic(HypixelPlayer owner, NBTCompound data) {

		this.owner = owner;
		this.data = data;

		try {

//			Bukkit.broadcastMessage(data + "");

			NBTCompound display = data.getCompound("tag").getCompound("display");
			NBTCompound attributes = data.getCompound("tag").getCompound("ExtraAttributes");

			type = MysticType.getMysticType(data.getInt("id", -1));

			name = display.getString("Name", "").replaceAll("[^\\x00-\\x7F]+.", "");
			for(Object line : display.getList("Lore")) {
				lore.add(((String) line).replaceAll("[^\\x00-\\x7F]+.", ""));
			}

			nonce = attributes.getLong("Nonce", -1);
			tier = attributes.getInt("UpgradeTier", -1);
			lives = attributes.getInt("Lives", -1);


			if(type == MysticType.PANTS) {
				Bukkit.broadcastMessage(display.getInt("color", -1) + "");
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
				enchantMap.put(enchant, enchantLvl);
			}

//			if(owner.prestige < 20 && enchantMap.containsKey(MysticEnchant.HIDDEN_JEWEL)) mysticList.add(this);
		} catch(Exception ignored) {

//			ignored.printStackTrace();
		}
	}

	public int addGem() {

		return isGemmed ? 0 : 1;
	}

	public int get(Enchant enchant) {

		return enchantMap.getOrDefault(enchant, 0);
	}

	public boolean has(Enchant enchant) {

		return enchantMap.containsKey(enchant);
	}

	public boolean isMystic() {

		return enchantMap.size() != 0;
	}
}
