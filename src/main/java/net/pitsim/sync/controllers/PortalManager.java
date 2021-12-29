package net.pitsim.sync.controllers;

import dev.kyro.arcticapi.misc.AUtil;
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
		if(pitPlayer.loadout != null && pitPlayer.loadout.loadoutGUI != null) {
			LoadoutManager.save(player);
			for(PremiumItem premiumItem : pitPlayer.loadout.loadoutGUI.premiumPanel.premiumItems) {
				AUtil.giveItemSafely(player, premiumItem.getItemStack());
			}
		}

		Misc.giveDiamond(player);
		player.teleport(MapManager.getFFASpawn());
	}
}
