package net.pitsim.sync.inventories.loadout;

import de.tr7zw.nbtapi.NBTItem;
import dev.kyro.arcticapi.gui.AGUI;
import dev.kyro.arcticapi.gui.AGUIPanel;
import dev.kyro.arcticapi.misc.AOutput;
import net.pitsim.sync.enums.NBTTag;
import net.pitsim.sync.misc.Misc;
import net.pitsim.sync.misc.Sounds;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class EnderchestPanel extends AGUIPanel {
	public LoadoutGUI loadoutGUI;

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
		return ChatColor.GRAY + Misc.getFormattedName(player) + " Enderchest";
	}

	@Override
	public int getRows() {
		return ((LoadoutGUI) gui).loadout.hypixelPlayer.enderchestRows;
	}

	@Override
	public void onClick(InventoryClickEvent event) {
		if(event.getClick() == ClickType.NUMBER_KEY) {
			event.setCancelled(true);
			AOutput.error(player, "Please do not use hotkeys");
			Sounds.NO.play(player);
			return;
		}
		ItemStack clickedItem = event.getCurrentItem();
		if(Misc.isAirOrNull(clickedItem)) return;
		NBTItem nbtItem = new NBTItem(clickedItem);
		if(nbtItem.hasKey(NBTTag.PREMIUM_TYPE.getRef())) {
			event.setCancelled(true);
			return;
		}
	}

	@Override
	public void onOpen(InventoryOpenEvent event) {
	}

	@Override
	public void onClose(InventoryCloseEvent event) {
	}
}
