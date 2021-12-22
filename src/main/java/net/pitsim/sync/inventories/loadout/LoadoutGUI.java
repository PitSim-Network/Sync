package net.pitsim.sync.inventories.loadout;

import dev.kyro.arcticapi.gui.AGUI;
import net.pitsim.sync.hypixel.Loadout;
import org.bukkit.entity.Player;

public class LoadoutGUI extends AGUI {
	public Loadout loadout;

	public EnderchestPanel enderchestPanel;

	public LoadoutGUI(Player player, Loadout loadout) {
		super(player);
		this.loadout = loadout;

		this.enderchestPanel = new EnderchestPanel(this);

		setHomePanel(enderchestPanel);
	}
}
