package net.pitsim.sync.controllers;

import net.pitsim.sync.events.KillEvent;
import net.pitsim.sync.events.OofEvent;
import net.pitsim.sync.hypixel.Loadout;
import net.pitsim.sync.hypixel.LoadoutManager;
import net.pitsim.sync.misc.Misc;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class PortalManager implements Listener {

	@EventHandler
	public void onPortal(PlayerPortalEvent event) {
		event.getPlayer().teleport(Bukkit.getWorld("ffa").getSpawnLocation());

		Loadout loadout = LoadoutManager.getLoadout(Misc.getUUID(event.getPlayer().getUniqueId()));
		giveDiamond(event.getPlayer());

		if(loadout.inventoryItemMap.size() > 0) {
			for(Map.Entry<Integer, ItemStack> integerItemStackEntry : loadout.inventoryItemMap.entrySet()) {
				event.getPlayer().getInventory().setItem(integerItemStackEntry.getKey(), integerItemStackEntry.getValue());
			}
		}
//		System.out.println(loadout.armorItemMap.toString());
//		System.out.println();
//		System.out.println();
//		System.out.println();
//		System.out.println();
//		System.out.println();
//		System.out.println(loadout.stash.toString());
	}

	@EventHandler
	public void onDeath(KillEvent event) {
		if(DuelManager.getMatch(event.dead) != null) return;
			clearInventory(event.dead);
			event.dead.teleport(Bukkit.getWorld("lobby").getSpawnLocation());
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if(DuelManager.getMatch(event.getEntity()) != null) return;
			clearInventory(event.getEntity());
			event.getEntity().teleport(Bukkit.getWorld("lobby").getSpawnLocation());

	}

	@EventHandler
	public void onDeath(OofEvent event) {
		if(DuelManager.getMatch(event.getPlayer()) != null) return;
		clearInventory(event.getPlayer());
		event.getPlayer().teleport(Bukkit.getWorld("lobby").getSpawnLocation());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if(DuelManager.getMatch(event.getPlayer()) != null) return;
			clearInventory(event.getPlayer());
			event.getPlayer().teleport(Bukkit.getWorld("lobby").getSpawnLocation());
	}

	public void clearInventory(Player player) {
		player.getInventory().clear();
		player.getInventory().setHelmet(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setBoots(null);
	}

	public void giveDiamond(Player player) {
		player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
		player.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
		player.getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
		player.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
	}
}
