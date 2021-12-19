package net.pitsim.sync.inventories;

import de.tr7zw.nbtapi.NBTItem;
import net.pitsim.sync.PitSim;
import net.pitsim.sync.controllers.InventoryManager;
import net.pitsim.sync.enums.NBTTag;
import net.pitsim.sync.hypixel.Loadout;
import net.pitsim.sync.hypixel.LoadoutManager;
import net.pitsim.sync.misc.Misc;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class EnderchestInventoryGUI implements Listener, InventoryHolder {
	public Inventory inv;
	public Player player;
	public Loadout loadout;

	public EnderchestInventoryGUI(Player player) {
		this.player = player;
		Bukkit.getServer().getPluginManager().registerEvents(this, PitSim.INSTANCE);
		InventoryManager.enderchestInventoryGUIS.add(this);

		this.inv = Bukkit.createInventory(this, 54, "Enderchest");
		this.loadout = LoadoutManager.getLoadout(Misc.getUUID(player.getUniqueId()));
	}

	public void openInventory() {
		player.openInventory(inv);
	}

	@EventHandler
	public void onOpen(InventoryOpenEvent event) {
		if(event.getInventory().getHolder() != this) return;
		Player player = (Player) event.getPlayer();

		for(int i = 0; i < event.getPlayer().getInventory().getSize(); i++) {
			if(!loadout.inventoryItemMap.containsKey(i)) continue;
			ItemStack itemStack = loadout.inventoryItemMap.get(i);
			player.getInventory().setItem(i, itemStack);
		}

		for(int i = 0; i < event.getInventory().getSize(); i++) {
			if(!loadout.enderchestItemMap.containsKey(i)) continue;
			ItemStack itemStack = loadout.enderchestItemMap.get(i);
			event.getInventory().setItem(i, itemStack);
		}

//		TODO: Attempt Save
		for(ItemStack conflictItem : loadout.conflictItems) {
			player.getInventory().addItem(conflictItem);
		}
//		for(ItemStack stashItem : loadout.stash) {
//			player.getInventory().addItem(stashItem);
//		}
		for(Map.Entry<Integer, ItemStack> entry : loadout.armorItemMap.entrySet()) {
			player.getInventory().addItem(entry.getValue());
		}
		player.updateInventory();
	}

	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		if(event.getInventory().getHolder() != this) return;
		Player player = (Player) event.getPlayer();

		loadout.inventoryItemMap.clear();
		for(int i = 0; i < 36; i++) {
			ItemStack itemStack = player.getInventory().getItem(i);
//			TODO: Check if it is a mystic
			if(itemStack == null || itemStack.getType() == Material.AIR) continue;
			if(!shouldSave(itemStack)) {
				player.getInventory().setItem(i, new ItemStack(Material.AIR));
				continue;
			}
			loadout.inventoryItemMap.put(i, itemStack);
		}
		loadout.enderchestItemMap.clear();
		for(int i = 0; i < event.getInventory().getSize(); i++) {
			ItemStack itemStack = event.getInventory().getItem(i);
//			TODO: Check if it is a mystic
			if(itemStack == null || itemStack.getType() == Material.AIR) continue;
			if(!shouldSave(itemStack)) {
				event.getInventory().setItem(i, new ItemStack(Material.AIR));
				continue;
			}
			loadout.enderchestItemMap.put(i, itemStack);
		}

//		TODO: Armor
		loadout.save();

		player.getInventory().clear();
		player.updateInventory();
	}

	public boolean shouldSave(ItemStack itemStack) {
		if(Misc.isAirOrNull(itemStack)) return false;
		NBTItem nbtItem = new NBTItem(itemStack);
		String nonce = nbtItem.getString(NBTTag.PIT_NONCE.getRef());
		for(ItemStack conflictItem : loadout.conflictItems) {
			NBTItem testNBTItem = new NBTItem(conflictItem);
			String testNonce = testNBTItem.getString(NBTTag.PIT_NONCE.getRef());
			if(testNonce.equals(nonce)) return false;
		}
		for(ItemStack conflictItem : loadout.stash) {
			NBTItem testNBTItem = new NBTItem(conflictItem);
			String testNonce = testNBTItem.getString(NBTTag.PIT_NONCE.getRef());
			if(testNonce.equals(nonce)) return false;
		}
		for(Map.Entry<Integer, ItemStack> entry : loadout.armorItemMap.entrySet()) {
			NBTItem testNBTItem = new NBTItem(entry.getValue());
			String testNonce = testNBTItem.getString(NBTTag.PIT_NONCE.getRef());
			if(testNonce.equals(nonce)) return false;
		}
		return nbtItem.hasKey(NBTTag.PIT_NONCE.getRef());
	}

	// Check for clicks on items
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if(event.getInventory() != inv) return;
	}

	@Override
	public Inventory getInventory() {
		return null;
	}
}
