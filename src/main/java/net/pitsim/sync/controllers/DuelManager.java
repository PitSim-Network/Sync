package net.pitsim.sync.controllers;

import net.pitsim.sync.controllers.objects.Match;
import net.pitsim.sync.enums.PvpArena;
import net.pitsim.sync.events.KillEvent;
import net.pitsim.sync.events.OofEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

public class DuelManager implements Listener{
	public static List<Match> matches = new ArrayList<>();

	public static void createDuel(Player player1, Player player2, PvpArena arena)  {

		Match newMatch = new Match(player1, player2, arena, getLowestPosition());
		newMatch.onStart();
	}


	public static int getLowestPosition() {
		List<Integer> positions = new ArrayList<>();

		for(Match match : matches) {
			positions.add(match.position);
		}

		if(positions.size() == 0) return 0;

		int max = positions.get(0);
		
		for (int i = 1; i < positions.size(); i++) {
			if (positions.get(i) > max) {
				max = positions.get(i);
			}
		}

		for(int i = 0; i < max; i++) {
			if(positions.contains(i)) continue;
			return i;
		}

		return max + 1;


	}

	public static Match getMatch(Player player) {
		for(Match match : matches) {
			if(match.player1 == player.getPlayer() || match.player2 == player) return match;
		}
		return null;
	}

	@EventHandler
	public void onDeath(KillEvent event) {
		for(Match match : matches) {
			if(match.player1 == event.dead || match.player2 == event.dead) {
				match.onEnd(event.dead);
				return;
			}
		}
	}

	@EventHandler
	public void onOof(OofEvent event) {
		for(Match match : matches) {
			if(match.player1 == event.getPlayer() || match.player2 == event.getPlayer()) {
				match.onEnd(event.getPlayer());
				return;
			}
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		for(Match match : matches) {
			if(match.player1 == event.getEntity() || match.player2 == event.getEntity()) {
				match.onEnd(event.getEntity());
				return;
			}
		}
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		for(Match match : matches) {
			if(match.player1 == event.getPlayer() || match.player2 == event.getPlayer()) {
				match.onEnd(event.getPlayer());
				return;
			}
		}
	}

	@EventHandler
	public void onItemDamage(PlayerItemDamageEvent event) {
		event.setCancelled(true);
	}

}
