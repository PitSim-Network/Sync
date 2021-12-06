package net.pitsim.sync.controllers;

import net.pitsim.sync.controllers.objects.Match;
import net.pitsim.sync.enums.PvpArena;
import net.pitsim.sync.events.KillEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.List;

public class DuelManager {
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

	@EventHandler
	public void onDeath(KillEvent event) {

	}

}
