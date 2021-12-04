package net.pitsim.sync.inventories;

import dev.kyro.arcticapi.gui.AGUI;
import dev.kyro.arcticapi.gui.AGUIPanel;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class PitInventoryGUI extends AGUI {

	public PitInventoryPanel pitInventoryPanel;

	public PitInventoryGUI(Player player) {
		super(player);

		pitInventoryPanel = new PitInventoryPanel(this);
		setHomePanel(pitInventoryPanel);
	}
}
