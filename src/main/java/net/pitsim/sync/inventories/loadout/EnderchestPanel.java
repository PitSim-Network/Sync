package net.pitsim.sync.inventories.loadout;

import dev.kyro.arcticapi.gui.AGUI;
import dev.kyro.arcticapi.gui.AGUIPanel;
import net.pitsim.sync.misc.Misc;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class EnderchestPanel extends AGUIPanel {
	LoadoutGUI loadoutGUI;

	public EnderchestPanel(AGUI gui) {
		super(gui);
		cancelClicks = false;
		loadoutGUI = (LoadoutGUI) gui;

		for(Map.Entry<Integer, ItemStack> entry : loadoutGUI.loadout.enderchestItemMap.entrySet()) {
			ItemStack itemStack = entry.getValue();
			getInventory().setItem(entry.getKey(), itemStack);
		}
	}

	@Override
	public String getName() {
		return Misc.getFormattedName(player) + " Enderchest";
	}

	@Override
	public int getRows() {
		return ((LoadoutGUI) gui).loadout.hypixelPlayer.enderchestRows;
	}

	@Override
	public void onClick(InventoryClickEvent event) {

	}

	@Override
	public void onOpen(InventoryOpenEvent event) {
	}

	@Override
	public void onClose(InventoryCloseEvent event) {
	}
}
