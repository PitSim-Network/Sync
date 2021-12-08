package net.pitsim.sync.inventories;

import dev.kyro.arcticapi.gui.AGUI;
import org.bukkit.entity.Player;

public class DuelGUI extends AGUI {

	public MapPanel mapPanel;

	public DuelGUI(Player player, Player duelPlayer) {
		super(player);

		mapPanel = new MapPanel(this, duelPlayer);
		setHomePanel(mapPanel);
	}
}
