package net.pitsim.sync.inventories.loadout;

import de.tr7zw.nbtapi.NBTItem;
import dev.kyro.arcticapi.builders.AItemStackBuilder;
import dev.kyro.arcticapi.gui.AGUI;
import dev.kyro.arcticapi.gui.AGUIPanel;
import net.pitsim.sync.controllers.PremiumItem;
import net.pitsim.sync.enums.NBTTag;
import net.pitsim.sync.enums.PremiumType;
import net.pitsim.sync.misc.Misc;
import net.pitsim.sync.misc.Sounds;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PremiumPanel extends AGUIPanel {
	public LoadoutGUI loadoutGUI;
	public PremiumType premiumType;

	public List<PremiumItem> premiumItems = new ArrayList<>();

	public PremiumPanel(AGUI gui) {
		super(gui);
		loadoutGUI = (LoadoutGUI) gui;

		for(PremiumType value : PremiumType.values()) {
			getInventory().setItem(value.getSlot(), value.getDisplayStack());
		}
		inventoryBuilder.setSlots(Material.STAINED_GLASS_PANE, 0, 9, 10, 11, 12, 13, 14, 15, 16, 17, 36, 37, 38, 39, 40, 41, 42, 43, 44);

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
		ItemStack clickedItem = event.getCurrentItem();
		if(event.getClickedInventory().getHolder() == this) {
			int slot = event.getSlot();
			if(slot >= 0 && slot <= 8) {
				if(premiumType.getSlot() == slot) {
					Sounds.NO.play(player);
					return;
				}
				setupGUI(slot);
			}

			if(slot >= 45) {
				if(Misc.isAirOrNull(clickedItem)) return;
				NBTItem nbtItem = new NBTItem(clickedItem);
				PremiumType premiumType = PremiumType.getType(nbtItem.getString(NBTTag.PREMIUM_TYPE.getRef()));
				for(PremiumItem premiumItem : premiumItems) {
					if(premiumItem.premiumType != premiumType) continue;
					premiumItems.remove(premiumItem);
					getInventory().setItem(slot, new ItemStack(Material.AIR));
					setupGUI(premiumType.getSlot());
					updateInventory();
					return;
				}
			}

			int choice = slot - 18;
			PremiumItem selectedItem = null;
			int count = 0;
			for(PremiumItem premiumItem : PremiumItem.itemList) {
				if(premiumItem.premiumType != premiumType) continue;
				if(count++ == choice) {
					selectedItem = premiumItem;
					break;
				}
			}
			if(selectedItem == null) return;

			boolean hasType = false;
			for(PremiumItem premiumItem : premiumItems) {
				if(premiumItem.premiumType != selectedItem.premiumType) continue;
				hasType = true;
				break;
			}

			if(hasType) {
				Sounds.NO.play(player);
				return;
			}

			premiumItems.add(selectedItem);
			getInventory().setItem(premiumType.getSlot() + 45, selectedItem.getItemStack());

			int insertSlot = 18;
			for(PremiumItem premiumItem : PremiumItem.itemList) {
				if(premiumItem.premiumType != premiumType) continue;
				getInventory().setItem(insertSlot++, new AItemStackBuilder(Material.BARRIER).setName("&cAlready selected").getItemStack());
			}

			updateInventory();
		} else {
			if(Misc.isAirOrNull(event.getCurrentItem())) return;
			NBTItem nbtItem = new NBTItem(event.getCurrentItem());
			if(!nbtItem.hasKey(NBTTag.PREMIUM_TYPE.getRef())) {
				Sounds.NO.play(player);
				return;
			}
		}
	}

	@Override
	public void onOpen(InventoryOpenEvent event) { }

	@Override
	public void onClose(InventoryCloseEvent event) { }

	public void setupGUI(int clickedSlot) {
		PremiumType premiumType = PremiumType.values()[clickedSlot];
		this.premiumType = premiumType;

		boolean hasType = false;
		for(PremiumItem premiumItem : premiumItems) {
			if(premiumItem.premiumType != premiumType) continue;
			hasType = true;
			break;
		}

		int insertSlot = 18;
		for(PremiumItem premiumItem : PremiumItem.itemList) {
			if(premiumItem.premiumType != premiumType) continue;
			if(hasType) {
				getInventory().setItem(insertSlot++, new AItemStackBuilder(Material.BARRIER).setName("&cAlready selected").getItemStack());
			} else {
				getInventory().setItem(insertSlot++, premiumItem.getItemStack());
			}
		}
		for(int i = insertSlot; i < 36; i++) {
			getInventory().setItem(i, new ItemStack(Material.AIR));
		}

		updateInventory();
	}
}
