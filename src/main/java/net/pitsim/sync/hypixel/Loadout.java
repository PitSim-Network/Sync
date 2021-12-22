package net.pitsim.sync.hypixel;

import de.tr7zw.nbtapi.NBTItem;
import dev.kyro.arcticapi.data.APlayer;
import dev.kyro.arcticapi.data.APlayerData;
import net.pitsim.sync.controllers.objects.PitPlayer;
import net.pitsim.sync.enums.NBTTag;
import net.pitsim.sync.inventories.loadout.LoadoutGUI;
import net.pitsim.sync.misc.Misc;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Loadout {
	public UUID uuid;
	public HypixelPlayer hypixelPlayer;

	public LoadoutGUI loadoutGUI;

	public Map<Integer, ItemStack> inventoryItemMap = new HashMap<>();
	public Map<Integer, ItemStack> enderchestItemMap = new HashMap<>();
	public Map<Integer, ItemStack> armorItemMap = new HashMap<>();
	public List<ItemStack> stash = new ArrayList<>();

//	For new items that already have a saved loadout location
	public List<ItemStack> conflictItems = new ArrayList<>();

	public Loadout(UUID uuid) {
		this.uuid = uuid;
		this.hypixelPlayer = LoadoutManager.getHypixelPlayer(uuid);
		update();
	}

//	Loads all information and editing gui to a player
	public void fullLoad(Player player) {
		loadoutGUI = new LoadoutGUI(player, this);

		partialLoad(player);
	}

//	Loads all information to a player
	public void partialLoad(Player player) {
		PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
		pitPlayer.loadout = this;

		for(Map.Entry<Integer, ItemStack> integerItemStackEntry : inventoryItemMap.entrySet()) {
			player.getInventory().setItem(integerItemStackEntry.getKey(), integerItemStackEntry.getValue());
		}

		for(int i = 0; i < 4; i++) {
			if(!armorItemMap.containsKey(i)) continue;
			ItemStack itemStack = armorItemMap.get(i);
			if(i == 0) player.getEquipment().setBoots(itemStack);
			if(i == 1) player.getEquipment().setLeggings(itemStack);
			if(i == 2) player.getEquipment().setChestplate(itemStack);
			if(i == 3) player.getEquipment().setHelmet(itemStack);
		}
	}

	public void save() {
		if(loadoutGUI == null) return;
		Player player = loadoutGUI.player;

		inventoryItemMap.clear();
		for(int i = 0; i < 36; i++) {
			ItemStack itemStack = player.getInventory().getItem(i);
//			TODO: Check if it is a mystic
			if(itemStack == null || itemStack.getType() == Material.AIR) continue;
			if(!shouldSave(itemStack)) {
				player.getInventory().setItem(i, new ItemStack(Material.AIR));
				continue;
			}
			inventoryItemMap.put(i, itemStack);
		}
		enderchestItemMap.clear();
		for(int i = 0; i < loadoutGUI.enderchestPanel.getInventory().getSize(); i++) {
			ItemStack itemStack = loadoutGUI.enderchestPanel.getInventory().getItem(i);
//			TODO: Check if it is a mystic
			if(itemStack == null || itemStack.getType() == Material.AIR) continue;
			if(!shouldSave(itemStack)) {
				loadoutGUI.enderchestPanel.getInventory().setItem(i, new ItemStack(Material.AIR));
				continue;
			}
			enderchestItemMap.put(i, itemStack);
		}
		armorItemMap.clear();
		for(int i = 0; i < player.getEquipment().getArmorContents().length; i++) {
			ItemStack itemStack = player.getEquipment().getArmorContents()[i];
			if(itemStack == null || itemStack.getType() == Material.AIR) continue;
			if(!shouldSave(itemStack)) {
				player.getInventory().setItem(i, new ItemStack(Material.AIR));
				continue;
			}
			armorItemMap.put(i, itemStack);
		}

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
			for(Mystic mystic : toRemove) mysticMap.remove(mystic);
			toRemove.clear();
			if(playerData.contains("armor")) {
				for(String nonce : playerData.getConfigurationSection("armor").getKeys(false)) {
					String key = "armor." + nonce;
					int slot = playerData.getInt(key);

					Mystic mystic = getMystic(mysticMap, nonce);
					if(mystic == null) {
						playerData.set(key, null);
						continue;
					}
					toRemove.add(mystic);
					armorItemMap.put(slot, mystic.getItemStack());
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
			} else if(inventoryType == HypixelPlayer.InventoryType.STASH) {
				stash.add(mystic.getItemStack());
			}
		}
	}

	public void attemptAdd(Mystic mystic, Map<Integer, ItemStack> map, int slot) {
		if(!map.containsKey(slot)) {
			map.put(slot, mystic.getItemStack());
		} else {
//			TODO: Re-enable
			conflictItems.add(mystic.getItemStack());
		}
	}

	public Mystic getMystic(Map<Mystic, HypixelPlayer.ItemLocation> map, String nonce) {
		for(Map.Entry<Mystic, HypixelPlayer.ItemLocation> entry : map.entrySet()) {
			if(entry.getKey().nonce.equalsIgnoreCase(nonce)) return entry.getKey();
		}
		return null;
	}

	public boolean isOwner(Player player) {
		return player.getUniqueId().equals(uuid);
	}

	public boolean shouldSave(ItemStack itemStack) {
		if(Misc.isAirOrNull(itemStack)) return false;
		NBTItem nbtItem = new NBTItem(itemStack);
		String nonce = nbtItem.getString(NBTTag.PIT_NONCE.getRef());
		for(ItemStack conflictItem : conflictItems) {
			NBTItem testNBTItem = new NBTItem(conflictItem);
			String testNonce = testNBTItem.getString(NBTTag.PIT_NONCE.getRef());
			if(testNonce.equals(nonce)) return false;
		}
		for(ItemStack conflictItem : stash) {
			NBTItem testNBTItem = new NBTItem(conflictItem);
			String testNonce = testNBTItem.getString(NBTTag.PIT_NONCE.getRef());
			if(testNonce.equals(nonce)) return false;
		}
		for(Map.Entry<Integer, ItemStack> entry : armorItemMap.entrySet()) {
			NBTItem testNBTItem = new NBTItem(entry.getValue());
			String testNonce = testNBTItem.getString(NBTTag.PIT_NONCE.getRef());
			if(testNonce.equals(nonce)) return false;
		}
		return nbtItem.hasKey(NBTTag.PIT_NONCE.getRef());
	}
}
