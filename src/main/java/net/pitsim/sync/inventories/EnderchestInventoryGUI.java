package net.pitsim.sync.inventories;

import net.pitsim.sync.PitSim;
import net.pitsim.sync.controllers.InventoryManager;
import net.pitsim.sync.hypixel.Loadout;
import net.pitsim.sync.hypixel.PlayerDataManager;
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
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class EnderchestInventoryGUI implements Listener, InventoryHolder {
	public Inventory inv;
	public Player player;
	public Loadout loadout;

	public EnderchestInventoryGUI(Player player) {
		this.player = player;
		Bukkit.getServer().getPluginManager().registerEvents(this, PitSim.INSTANCE);
		InventoryManager.enderchestInventoryGUIS.add(this);

		this.inv = Bukkit.createInventory(this, 54, "Enderchest");
		this.loadout = PlayerDataManager.getLoadout(player.getUniqueId());
	}

	protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
		final ItemStack item = new ItemStack(material, 1);
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);

		return item;
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

//		for(ItemStack conflictItem : loadout.conflictItems) {
//			event.getInventory().addItem(conflictItem);
//		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		if(event.getInventory().getHolder() != this) return;
		Player player = (Player) event.getPlayer();

		loadout.inventoryItemMap.clear();
		for(int i = 0; i < 36; i++) {
			ItemStack itemStack = player.getInventory().getItem(i);
//			TODO: Check if it is a mystic
			if(Misc.isAirOrNull(itemStack)) continue;
			loadout.inventoryItemMap.put(i, itemStack);
		}
		loadout.enderchestItemMap.clear();
		for(int i = 0; i < event.getInventory().getSize(); i++) {
			ItemStack itemStack = event.getInventory().getItem(i);
//			TODO: Check if it is a mystic
			if(Misc.isAirOrNull(itemStack)) continue;
			loadout.enderchestItemMap.put(i, itemStack);
		}

//		TODO: Armor
		loadout.save();

		player.getInventory().clear();
		player.updateInventory();
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
