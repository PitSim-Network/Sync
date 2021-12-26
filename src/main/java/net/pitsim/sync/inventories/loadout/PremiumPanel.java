package net.pitsim.sync.inventories.loadout;

import de.tr7zw.nbtapi.NBTItem;
import dev.kyro.arcticapi.gui.AGUI;
import dev.kyro.arcticapi.gui.AGUIPanel;
import dev.kyro.arcticapi.misc.AOutput;
import net.pitsim.sync.enums.NBTTag;
import net.pitsim.sync.enums.PremiumType;
import net.pitsim.sync.misc.Misc;
import net.pitsim.sync.misc.Sounds;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class PremiumPanel extends AGUIPanel {
	public LoadoutGUI loadoutGUI;
	public PremiumType premiumType;

	public PremiumPanel(AGUI gui) {
		super(gui);
		loadoutGUI = (LoadoutGUI) gui;

		for(PremiumType value : PremiumType.values()) {
			getInventory().setItem(value.getSlot(), value.getDisplayStack());
		}
		inventoryBuilder.setSlots(Material.STAINED_GLASS_PANE, 0, 9, 10, 11, 12, 13, 14, 15, 16, 17);

		setupGUI(0);
	}

	@Override
	public String getName() {
		return ChatColor.GRAY + "Premium Items";
	}

	@Override
	public int getRows() {
		return ((LoadoutGUI) gui).loadout.hypixelPlayer.enderchestRows;
	}

	@Override
	public void onClick(InventoryClickEvent event) {
		if(event.getClickedInventory().getHolder() == this) {
			int slot = event.getSlot();
			if(slot >= 0 && slot <= 8) {
				setupGUI(slot);
			}

		} else {
			if(Misc.isAirOrNull(event.getCurrentItem())) return;
			NBTItem nbtItem = new NBTItem(event.getCurrentItem());
			if(!nbtItem.hasKey(NBTTag.PREMIUM_TYPE.getRef())) {
				Sounds.NO.play(player);
				AOutput.error(player, "You can only remove premium items here");
				return;
			}
		}
	}

	@Override
	public void onOpen(InventoryOpenEvent event) { }

	@Override
	public void onClose(InventoryCloseEvent event) { }

	public void setupGUI(int slot) {
		PremiumType premiumType = PremiumType.values()[slot];
		if(premiumType == this.premiumType) {
			Sounds.NO.play(player);
			return;
		}
		this.premiumType = premiumType;



		updateInventory();
	}
}
