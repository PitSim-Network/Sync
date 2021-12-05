package net.pitsim.sync.controllers;

import net.pitsim.sync.inventories.EnderchestInventoryGUI;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class InventoryManager {

	public static List<EnderchestInventoryGUI> enderchestInventoryGUIS = new ArrayList<>();

	public static EnderchestInventoryGUI getInventory(Player player) {
		for(EnderchestInventoryGUI enderchestInventoryGUI : enderchestInventoryGUIS) {
			if(enderchestInventoryGUI.player.getUniqueId() == player.getUniqueId()) return enderchestInventoryGUI;
		}

		return new EnderchestInventoryGUI(player);
	}
}
