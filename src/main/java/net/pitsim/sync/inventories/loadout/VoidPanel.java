package net.pitsim.sync.inventories.loadout;

import de.tr7zw.nbtapi.NBTItem;
import dev.kyro.arcticapi.gui.AGUI;
import dev.kyro.arcticapi.gui.AGUIPanel;
import dev.kyro.arcticapi.misc.AOutput;
import net.pitsim.sync.controllers.MapManager;
import net.pitsim.sync.enums.NBTTag;
import net.pitsim.sync.misc.Misc;
import net.pitsim.sync.misc.Sounds;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class VoidPanel extends AGUIPanel {
	public LoadoutGUI loadoutGUI;

	public VoidPanel(AGUI gui) {
		super(gui);
		cancelClicks = false;
		loadoutGUI = (LoadoutGUI) gui;
	}

	@Override
	public String getName() {
		return ChatColor.GRAY + "Removes all items from loadout";
	}

	@Override
	public int getRows() {
		return 6;
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
	public void onOpen(InventoryOpenEvent event) { }

	@Override
	public void onClose(InventoryCloseEvent event) {
		for(int i = 0; i < event.getInventory().getSize(); i++) {
			ItemStack itemStack = event.getInventory().getItem(i);
			event.getInventory().setItem(i, new ItemStack(Material.AIR));
			if(Misc.isAirOrNull(itemStack)) continue;
			NBTItem nbtItem = new NBTItem(itemStack);
			if(!nbtItem.hasKey(NBTTag.PIT_NONCE.getRef())) continue;
			if(!loadoutGUI.loadout.voidNonceList.contains(nbtItem.getString(NBTTag.PIT_NONCE.getRef())))
				loadoutGUI.loadout.voidNonceList.add(nbtItem.getString(NBTTag.PIT_NONCE.getRef()));
		}
		if(player.getWorld() == MapManager.getLobby()) loadoutGUI.loadout.save();
	}
}
