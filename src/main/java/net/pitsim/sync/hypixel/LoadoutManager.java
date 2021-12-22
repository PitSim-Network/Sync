package net.pitsim.sync.hypixel;

import dev.kyro.arcticapi.misc.AOutput;
import net.pitsim.sync.controllers.DuelManager;
import net.pitsim.sync.controllers.MapManager;
import net.pitsim.sync.controllers.objects.PitPlayer;
import net.pitsim.sync.events.KillEvent;
import net.pitsim.sync.events.OofEvent;
import net.pitsim.sync.misc.Misc;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LoadoutManager implements Listener {
	public static List<HypixelPlayer> hypixelPlayers = new ArrayList<>();
	public static List<Loadout> loadouts = new ArrayList<>();

	public static void load(Player player) {
		Loadout loadout = getLoadout(player.getUniqueId());
		loadout.fullLoad(player);
		AOutput.broadcast("loading player");
	}

	public static void save(Player player) {
		PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
		if(pitPlayer.loadout == null || pitPlayer.loadout.loadoutGUI == null) {
			System.out.println("YOU SHOULD NEVER SAVE THIS");
			return;
		}
		pitPlayer.loadout.save();
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		load(event.getPlayer());
	}

	@EventHandler
	public void onDeath(KillEvent killEvent) {
		if(DuelManager.getMatch(killEvent.dead) != null) return;
		Misc.clearInventory(killEvent.dead);
		LoadoutManager.load(killEvent.dead);
		killEvent.dead.teleport(MapManager.getLobbySpawn());
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if(DuelManager.getMatch(event.getEntity()) != null) return;
		Misc.clearInventory(event.getEntity());
		LoadoutManager.load(event.getEntity());
		event.getEntity().teleport(MapManager.getLobbySpawn());
	}

	@EventHandler
	public void onDeath(OofEvent event) {
		if(DuelManager.getMatch(event.getPlayer()) != null) return;
		Misc.clearInventory(event.getPlayer());
		LoadoutManager.load(event.getPlayer());
		event.getPlayer().teleport(MapManager.getLobbySpawn());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if(DuelManager.getMatch(event.getPlayer()) != null) return;
		Misc.clearInventory(event.getPlayer());
		LoadoutManager.load(event.getPlayer());
		event.getPlayer().teleport(MapManager.getLobbySpawn());
	}

	public static HypixelPlayer getHypixelPlayer(UUID uuid) {
		for(HypixelPlayer hypixelPlayer : hypixelPlayers) {
			if(hypixelPlayer.uuid.equals(uuid)) return hypixelPlayer;
		}
		HypixelPlayer hypixelPlayer = new HypixelPlayer(HypixelAPI.request(uuid));
		hypixelPlayers.add(hypixelPlayer);
		return hypixelPlayer;
	}

	public static Loadout getLoadout(UUID uuid) {
		for(Loadout loadout : loadouts) {
			if(loadout.uuid.equals(uuid)) return loadout;
		}
		Loadout loadout = new Loadout(uuid);
		loadouts.add(loadout);
		return loadout;
	}
}
