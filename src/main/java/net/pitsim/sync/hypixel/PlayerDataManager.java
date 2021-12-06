package net.pitsim.sync.hypixel;

import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerDataManager implements Listener {
	public static List<HypixelPlayer> hypixelPlayers = new ArrayList<>();
	public static List<Loadout> loadouts = new ArrayList<>();

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
//		loadouts.add(loadout);
		return loadout;
	}
}
