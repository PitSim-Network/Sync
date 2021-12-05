package net.pitsim.sync.inventories;

import net.pitsim.sync.PitSim;
import net.pitsim.sync.controllers.InventoryManager;
import net.pitsim.sync.controllers.objects.PitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
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
		for(Map.Entry<Integer, ItemStack> integerItemStackEntry : pitPlayer.enderchestMystics.entrySet()) {
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
		Bukkit.broadcastMessage("1");
		Bukkit.broadcastMessage(event.getInventory() + "");
		Bukkit.broadcastMessage(inv + "");
		if (event.getInventory().getHolder() != inv.getHolder()) return;
		Bukkit.broadcastMessage("2");



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
