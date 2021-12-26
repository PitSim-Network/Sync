package net.pitsim.sync.inventories.loadout;

import dev.kyro.arcticapi.builders.AItemStackBuilder;
import dev.kyro.arcticapi.builders.ALoreBuilder;
import dev.kyro.arcticapi.gui.AGUI;
import dev.kyro.arcticapi.gui.AGUIPanel;
import dev.kyro.arcticapi.misc.AOutput;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class VoidMenuPanel extends AGUIPanel {
	public LoadoutGUI loadoutGUI;

	public VoidMenuPanel(AGUI gui) {
		super(gui);
		loadoutGUI = (LoadoutGUI) gui;

		inventoryBuilder.createBorder(Material.STAINED_GLASS_PANE, 0);

		ItemStack clearStack = new AItemStackBuilder(Material.ANVIL)
				.setName("&c&lCLEAR VOID")
				.setLore(new ALoreBuilder(
						"&7Clear all voided items",
						"&7This action cannot be undone"
				))
				.getItemStack();
		getInventory().setItem(20, clearStack);

		ItemStack voidStack = new AItemStackBuilder(Material.EYE_OF_ENDER)
				.setName("&d&lVOID ITEMS")
				.setLore(new ALoreBuilder(
						"&7Click to dispose of items that you",
						"&7will &lnever &7use in your loadout",
						"&7This action cannot be undone, only cleared"
				))
				.getItemStack();
		getInventory().setItem(24, voidStack);
	}

	@Override
	public String getName() {
		return ChatColor.GRAY + "Item Void Menu";
	}

	@Override
	public int getRows() {
		return 5;
	}

	@Override
	public void onClick(InventoryClickEvent event) {
		if(event.getClickedInventory().getHolder() != this) return;
		int slot = event.getSlot();
		if(slot == 20) {
			loadoutGUI.loadout.voidNonceList.clear();
			loadoutGUI.loadout.save();
			AOutput.send(player, "Voided item list has been cleared");
			player.closeInventory();
		} else if(slot == 24) {
			openPanel(loadoutGUI.voidPanel);
		}
	}

	@Override
	public void onOpen(InventoryOpenEvent event) { }

	@Override
	public void onClose(InventoryCloseEvent event) { }
}
