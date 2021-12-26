package net.pitsim.sync.inventories;

import dev.kyro.arcticapi.gui.AGUI;
import org.bukkit.entity.Player;

public class HopperGUI extends AGUI {
	public HopperPanel hopperPanel;

	public HopperGUI(Player player) {
		super(player);

		setHomePanel(new HopperPanel(this));
	}
}
