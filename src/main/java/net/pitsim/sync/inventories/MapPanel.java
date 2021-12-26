package net.pitsim.sync.inventories;

import dev.kyro.arcticapi.gui.AGUI;
import dev.kyro.arcticapi.gui.AGUIPanel;
import net.pitsim.sync.commands.DuelCommand;
import net.pitsim.sync.enums.PvpArena;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class MapPanel extends AGUIPanel {
	public Player duelPlayer;
	public MapPanel(AGUI gui, Player duelPlayer) {
		super(gui);
		this.duelPlayer = duelPlayer;
	}

	@Override
	public String getName() {
		return ChatColor.GRAY + "Select a Map";
	}

	@Override
	public int getRows() {
		return 1;
	}

	@Override
	public void onClick(InventoryClickEvent event) {
		int slot = event.getSlot();
		if(event.getClickedInventory().getHolder() == this) {
			List<PvpArena> arenas = Arrays.asList(PvpArena.values());

			if(slot > arenas.size()) return;

			DuelCommand.initiateDuel(player, duelPlayer, arenas.get(slot));
		}
	}

	@Override
	public void onOpen(InventoryOpenEvent event) {
		int i = 0;
		for(PvpArena value : PvpArena.values()) {
			ItemStack displayItem = value.displayItem.clone();

			ItemMeta meta = displayItem.getItemMeta();
			meta.setDisplayName(ChatColor.GREEN + value.refName);
			displayItem.setItemMeta(meta);
			getInventory().setItem(i, displayItem);
			i++;
		}
	}

	@Override
	public void onClose(InventoryCloseEvent event) {
		player.getInventory().clear();
	}
}
