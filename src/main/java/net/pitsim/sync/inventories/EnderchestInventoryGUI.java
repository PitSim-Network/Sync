package net.pitsim.sync.inventories;

import net.pitsim.sync.PitSim;
import net.pitsim.sync.controllers.InventoryManager;
import net.pitsim.sync.controllers.objects.PitPlayer;
import net.pitsim.sync.hypixel.HypixelPlayer;
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
import java.util.Map;

public class EnderchestInventoryGUI implements Listener, InventoryHolder {
	public Inventory inv;
	public Player player;

	public EnderchestInventoryGUI(Player player) {
		this.player = player;
		Bukkit.getServer().getPluginManager().registerEvents(this, PitSim.INSTANCE);
		InventoryManager.enderchestInventoryGUIS.add(this);

		inv = Bukkit.createInventory(this, 54, "Enderchest");

		PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
		for(Map.Entry<Integer, ItemStack> integerItemStackEntry : HypixelPlayer.getHypixelPlayer(pitPlayer.dataUUID).enderchestItems.entrySet()) {
			inv.setItem(integerItemStackEntry.getKey(), integerItemStackEntry.getValue());
		}
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
		if (event.getInventory().getHolder() != inv.getHolder()) return;
		PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);

		if(pitPlayer.savedEnderchest.size() > 0) {
			event.getInventory().clear();
			for(Map.Entry<Integer, ItemStack> integerItemStackEntry : pitPlayer.savedEnderchest.entrySet()) {
				inv.setItem(integerItemStackEntry.getKey(), integerItemStackEntry.getValue());
			}
		}

		if(pitPlayer.savedInventroy.size() > 0) {
			event.getPlayer().getInventory().clear();
			for(Map.Entry<Integer, ItemStack> integerItemStackEntry : pitPlayer.savedInventroy.entrySet()) {
				event.getPlayer().getInventory().setItem(integerItemStackEntry.getKey(), integerItemStackEntry.getValue());
			}
		} else {
			for(Map.Entry<Integer, ItemStack> integerItemStackEntry : HypixelPlayer.getHypixelPlayer(pitPlayer.dataUUID).inventoryItems.entrySet()) {
				event.getPlayer().getInventory().setItem(integerItemStackEntry.getKey(), integerItemStackEntry.getValue());
			}
		}

	}

	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		if (event.getInventory().getHolder() != inv.getHolder()) return;

		PitPlayer pitPlayer = PitPlayer.getPitPlayer((Player) event.getPlayer());
		pitPlayer.savedEnderchest.clear();
		for(int i = 0; i < event.getInventory().getSize(); i++) {
			pitPlayer.savedEnderchest.put(i, event.getInventory().getItem(i));
		}

		pitPlayer.savedInventroy.clear();
		for(int i = 0; i < event.getPlayer().getInventory().getSize(); i++) {
			pitPlayer.savedInventroy.put(i, event.getPlayer().getInventory().getItem(i));
		}

		event.getPlayer().getInventory().clear();


	}

	// Check for clicks on items
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getInventory() != inv) return;

	}


	@Override
	public Inventory getInventory() {
		return null;
	}
}
