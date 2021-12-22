package net.pitsim.sync.controllers;

import net.pitsim.sync.controllers.objects.PitPlayer;
import net.pitsim.sync.hypixel.LoadoutManager;
import net.pitsim.sync.misc.Misc;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

public class PortalManager implements Listener {

	@EventHandler
	public void onPortal(PlayerPortalEvent event) {
		Player player = event.getPlayer();

		PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
		if(pitPlayer.loadout != null && pitPlayer.loadout.loadoutGUI != null) LoadoutManager.save(player);

		Misc.giveDiamond(player);
		player.teleport(MapManager.getFFASpawn());

//		Loadout loadout = LoadoutManager.getLoadout(Misc.getUUID(event.getPlayer().getUniqueId()));
//		giveDiamond(event.getPlayer());

//		if(loadout.inventoryItemMap.size() > 0) {
//			for(Map.Entry<Integer, ItemStack> integerItemStackEntry : loadout.inventoryItemMap.entrySet()) {
//				event.getPlayer().getInventory().setItem(integerItemStackEntry.getKey(), integerItemStackEntry.getValue());
//			}
//		}
//		System.out.println(loadout.armorItemMap.toString());
//		System.out.println();
//		System.out.println();
//		System.out.println();
//		System.out.println();
//		System.out.println();
//		System.out.println(loadout.stash.toString());
	}
}
