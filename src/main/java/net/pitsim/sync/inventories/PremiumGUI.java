package net.pitsim.sync.inventories;

import dev.kyro.arcticapi.gui.AGUI;
import net.pitsim.sync.controllers.PremiumItem;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PremiumGUI extends AGUI {
	public List<PremiumItem> premiumItems = new ArrayList<>();
	public Map<Integer, PremiumItem> premiumItemMap = new HashMap<>();

	public PremiumPanel premiumPanel;

	public PremiumGUI(Player player) {
		super(player);

		this.premiumPanel = new PremiumPanel(this);

		setHomePanel(premiumPanel);
	}
}
