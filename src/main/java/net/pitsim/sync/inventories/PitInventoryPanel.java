package net.pitsim.sync.inventories;

import dev.kyro.arcticapi.gui.AGUI;
import dev.kyro.arcticapi.gui.AGUIPanel;
import net.pitsim.sync.controllers.objects.PitPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class PitInventoryPanel extends AGUIPanel {
	public PitInventoryPanel(AGUI gui) {
		super(gui);
	}

	@Override
	public String getName() {
		return "Pit Inventory and Enderchest";
	}

	@Override
	public int getRows() {
		return 8;
	}

	@Override
	public void onClick(InventoryClickEvent event) {

	}

	@Override
	public void onOpen(InventoryOpenEvent event) {
		PitPlayer pitPlayer = PitPlayer.getPitPlayer((Player) event.getPlayer());

		for(Map.Entry<Integer, ItemStack> integerItemStackEntry : pitPlayer.enderchestMystics.entrySet()) {
			getInventory().setItem(integerItemStackEntry.getKey(), integerItemStackEntry.getValue());

		}

		player.getInventory().clear();

		for(Map.Entry<Integer, ItemStack> integerItemStackEntry : pitPlayer.inventoryMystics.entrySet()) {
			getInventory().setItem(integerItemStackEntry.getKey(), integerItemStackEntry.getValue());
			player.getInventory().setItem(integerItemStackEntry.getKey(), integerItemStackEntry.getValue());
		}

	}

	@Override
	public void onClose(InventoryCloseEvent event) {
		player.getInventory().clear();
	}
}
