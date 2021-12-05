package net.pitsim.sync.inventories;

import dev.kyro.arcticapi.gui.AGUI;
import dev.kyro.arcticapi.gui.AGUIPanel;
import net.pitsim.sync.controllers.objects.PitPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

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

	}

	@Override
	public void onClose(InventoryCloseEvent event) {
		player.getInventory().clear();
	}
}
