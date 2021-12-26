package net.pitsim.sync.inventories;

import dev.kyro.arcticapi.builders.AItemStackBuilder;
import dev.kyro.arcticapi.builders.ALoreBuilder;
import dev.kyro.arcticapi.gui.AGUI;
import dev.kyro.arcticapi.gui.AGUIPanel;
import dev.kyro.arcticapi.misc.AOutput;
import net.pitsim.sync.controllers.HopperManager;
import net.pitsim.sync.controllers.objects.Hopper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HopperPanel extends AGUIPanel {
	public HopperGUI hopperGUI;

	public HopperPanel(AGUI gui) {
		super(gui);
		this.hopperGUI = (HopperGUI) gui;

		ItemStack border = new AItemStackBuilder(Material.STAINED_GLASS_PANE, 1, 0).getItemStack();
		for(int i = 0; i < 27; i++) {
			if(i >= 10 && i <= 16) continue;
			getInventory().setItem(i, border);
		}

		ItemStack chainHopper = new AItemStackBuilder(Material.IRON_SWORD)
				.setName("&7Chain Hopper")
				.setLore(new ALoreBuilder(

				))
				.getItemStack();
		getInventory().setItem(10, chainHopper);

		ItemStack diamondHopper = new AItemStackBuilder(Material.DIAMOND_SWORD)
				.setName("&9Diamond Hopper")
				.setLore(new ALoreBuilder(

				))
				.getItemStack();
		getInventory().setItem(11, diamondHopper);

		ItemStack mysticHopper = new AItemStackBuilder(Material.GOLD_SWORD)
				.setName("&eMystic Hopper")
				.setLore(new ALoreBuilder(

				))
				.getItemStack();
		getInventory().setItem(12, mysticHopper);

		ItemStack gsetHopper = new AItemStackBuilder(Material.FEATHER)
				.setName("&6GSet Hopper")
				.setLore(new ALoreBuilder(

				))
				.getItemStack();
		getInventory().setItem(13, gsetHopper);

		ItemStack venomHopper = new AItemStackBuilder(Material.POTION, 1, 16388)
				.setName("&2Venom Hopper")
				.setLore(new ALoreBuilder(

				))
				.getItemStack();
		ItemMeta venomMeta = venomHopper.getItemMeta();
		venomMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		venomHopper.setItemMeta(venomMeta);
		getInventory().setItem(14, venomHopper);
	}

	@Override
	public String getName() {
		return ChatColor.GRAY + "Select a Hopper";
	}

	@Override
	public int getRows() {
		return 3;
	}

	@Override
	public void onClick(InventoryClickEvent event) {
		if(event.getClickedInventory().getHolder() != this) return;
		int slot = event.getSlot();
		Hopper hopper = null;
		if(slot == 10) {
			hopper = HopperManager.callHopper("PayForTruce", Hopper.Type.CHAIN, player);
		} else if(slot == 11) {
			hopper = HopperManager.callHopper("PayForTruce", Hopper.Type.DIAMOND, player);
		} else if(slot == 12) {
			hopper = HopperManager.callHopper("PayForTruce", Hopper.Type.MYSTIC, player);
		} else if(slot == 13) {
			hopper = HopperManager.callHopper("PayForTruce", Hopper.Type.GSET, player);
		} else if(slot == 14) {
			hopper = HopperManager.callHopper("PayForTruce", Hopper.Type.VENOM, player);
		}
		if(hopper == null) return;
		hopper.lockedToTarget = true;
		player.closeInventory();
		AOutput.send(player, "&7Summoned a " + hopper.type.name + "&7 hopper");
	}

	@Override
	public void onOpen(InventoryOpenEvent event) { }

	@Override
	public void onClose(InventoryCloseEvent event) { }
}
