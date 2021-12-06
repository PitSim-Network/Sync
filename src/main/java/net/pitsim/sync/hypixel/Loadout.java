package net.pitsim.sync.hypixel;

import de.tr7zw.nbtapi.NBTItem;
import dev.kyro.arcticapi.data.APlayer;
import dev.kyro.arcticapi.data.APlayerData;
import net.pitsim.sync.enums.NBTTag;
import net.pitsim.sync.misc.Misc;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Loadout {
	public UUID uuid;
	public HypixelPlayer hypixelPlayer;

	public Map<Integer, ItemStack> inventoryItemMap = new HashMap<>();
	public Map<Integer, ItemStack> enderchestItemMap = new HashMap<>();
	public Map<Integer, ItemStack> armorItemMap = new HashMap<>();

//	For new items that already have a saved loadout location
	public List<ItemStack> conflictItems = new ArrayList<>();

	public Loadout(UUID uuid) {
		this.uuid = uuid;
		this.hypixelPlayer = PlayerDataManager.getHypixelPlayer(uuid);
		update();
	}

	public void save() {
		clean(inventoryItemMap);
		clean(enderchestItemMap);
		clean(armorItemMap);
		write(inventoryItemMap, "inventory");
		write(enderchestItemMap, "enderchest");
		write(armorItemMap, "armor");
	}

	public void write(Map<Integer, ItemStack> map, String path) {
		APlayer aPlayer = APlayerData.getPlayerData(uuid);
		ConfigurationSection playerData = aPlayer.playerData;
		for(Map.Entry<Integer, ItemStack> entry : map.entrySet()) {
			int slot = entry.getKey();
			NBTItem nbtItem = new NBTItem(entry.getValue());
			String nonce = nbtItem.getString(NBTTag.PIT_NONCE.getRef());
			if(!path.equalsIgnoreCase("inventory")) playerData.set("loadout.inventory." + nonce, null);
			if(!path.equalsIgnoreCase("enderchest")) playerData.set("loadout.enderchest." + nonce, null);
			if(!path.equalsIgnoreCase("armor")) playerData.set("loadout.armor." + nonce, null);
			if(playerData.getInt("loadout." + path + "." + nonce, -1) == slot) continue;
			playerData.set("loadout." + path + "." + nonce, slot);
		}
		aPlayer.save();
	}

	public void clean(Map<Integer, ItemStack> map) {
		List<Integer> toRemove = new ArrayList<>();
		for(Map.Entry<Integer, ItemStack> entry : map.entrySet()) {
			if(Misc.isAirOrNull(entry.getValue())) toRemove.add(entry.getKey());
			NBTItem nbtItem = new NBTItem(entry.getValue());
			if(!nbtItem.hasKey(NBTTag.PIT_NONCE.getRef())) toRemove.add(entry.getKey());
		}
		for(int slot : toRemove) {
			map.remove(slot);
		}
	}

	public void update() {
		APlayer aPlayer = APlayerData.getPlayerData(uuid);
		ConfigurationSection playerData = aPlayer.playerData.getConfigurationSection("loadout");
		Map<Mystic, HypixelPlayer.ItemLocation> mysticMap = new LinkedHashMap<>(hypixelPlayer.mysticMap);

		List<Mystic> toRemove = new ArrayList<>();
		if(playerData != null) {
			if(playerData.contains("inventory")) {
				for(String nonce : playerData.getConfigurationSection("inventory").getKeys(false)) {
					String key = "inventory." + nonce;
					int slot = playerData.getInt(key);

					Mystic mystic = getMystic(mysticMap, nonce);
					if(mystic == null) {
						playerData.set(key, null);
						continue;
					}
					toRemove.add(mystic);
					inventoryItemMap.put(slot, mystic.getItemStack());
				}
			}
			for(Mystic mystic : toRemove) mysticMap.remove(mystic);
			toRemove.clear();
			if(playerData.contains("enderchest")) {
				for(String nonce : playerData.getConfigurationSection("enderchest").getKeys(false)) {
					String key = "enderchest." + nonce;
					int slot = playerData.getInt(key);

					Mystic mystic = getMystic(mysticMap, nonce);
					if(mystic == null) {
						playerData.set(key, null);
						continue;
					}
					toRemove.add(mystic);
					enderchestItemMap.put(slot, mystic.getItemStack());
				}
			}
			aPlayer.save();
		}

		for(Mystic mystic : toRemove) mysticMap.remove(mystic);
		for(Map.Entry<Mystic, HypixelPlayer.ItemLocation> entry : mysticMap.entrySet()) {
			Mystic mystic = entry.getKey();
			HypixelPlayer.InventoryType inventoryType = entry.getValue().inventoryType;
			int slot = entry.getValue().slot;

			if(inventoryType == HypixelPlayer.InventoryType.INVENTORY) {
				attemptAdd(mystic, inventoryItemMap, slot);
			} else if(inventoryType == HypixelPlayer.InventoryType.ENDERCHEST) {
				attemptAdd(mystic, enderchestItemMap, slot);
			} else if(inventoryType == HypixelPlayer.InventoryType.ARMOR) {
				attemptAdd(mystic, armorItemMap, slot);
			}
		}
	}

	public void attemptAdd(Mystic mystic, Map<Integer, ItemStack> map, int slot) {
		if(!map.containsKey(slot)) {
			map.put(slot, mystic.getItemStack());
		} else {
//			TODO: Re-enable
//			conflictItems.add(mystic.getItemStack());
		}
	}

	public Mystic getMystic(Map<Mystic, HypixelPlayer.ItemLocation> map, String nonce) {
		for(Map.Entry<Mystic, HypixelPlayer.ItemLocation> entry : map.entrySet()) {
			if(entry.getKey().nonce.equalsIgnoreCase(nonce)) return entry.getKey();
		}
		return null;
	}
}
