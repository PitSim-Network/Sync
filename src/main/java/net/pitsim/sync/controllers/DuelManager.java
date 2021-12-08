package net.pitsim.sync.controllers;

import dev.kyro.arcticapi.misc.AOutput;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.pitsim.sync.PitSim;
import net.pitsim.sync.controllers.objects.Match;
import net.pitsim.sync.enums.PvpArena;
import net.pitsim.sync.events.AttackEvent;
import net.pitsim.sync.events.KillEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DuelManager implements Listener{
	public static Map<Player, Player> requests = new HashMap<>();
	public static Map<Player, PvpArena> requestArenas = new HashMap<>();
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

//	@EventHandler
//	public void onOof(OofEvent event) {
//		for(Match match : matches) {
//			if(match.player1 == event.getPlayer() || match.player2 == event.getPlayer()) {
//				match.onEnd(event.getPlayer());
//				return;
//			}
//		}
//	}

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
				match.onQuit(event.getPlayer());
				return;
			}
		}
	}

	@EventHandler
	public void onAttack(AttackEvent.Pre attackEvent) {
		for(Match match : matches) {
			if(!match.isStarting) continue;
			if(match.player1 == attackEvent.attacker || match.player2 == attackEvent.attacker) attackEvent.setCancelled(true);

		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onShoot(EntityShootBowEvent event) {
		if(!(event.getEntity() instanceof Player)) return;
		for(Match match : matches) {
			if(!match.isStarting) continue;
			if(match.player1 == event.getEntity() || match.player2 == event.getEntity()) event.setCancelled(true);

		}
	}

	@EventHandler
	public void onItemDamage(PlayerItemDamageEvent event) {
		event.setCancelled(true);
	}

	public static void requestDuel(Player requester, Player challenged, PvpArena arena) {
		requests.put(requester, challenged);
		requestArenas.put(requester, arena);

		String requesterName = "%luckperms_prefix%" + requester.getName();
		String challengedName = "%luckperms_prefix%" + challenged.getName();

		TextComponent clickAccept = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&a&lACCEPT"));
		clickAccept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duel accept " + requester.getName()));
		TextComponent clickDecline = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&c&lDECLINE"));
		clickDecline.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duel decline " + requester.getName()));

		clickAccept.addExtra(" ");
		clickAccept.addExtra(clickDecline);

		AOutput.send(challenged, PlaceholderAPI.setPlaceholders(requester, requesterName) + " &ehas challenged you to a duel on &a" + arena.refName);
		challenged.sendMessage(clickAccept);

		new BukkitRunnable() {
			@Override
			public void run() {
				if(requests.containsKey(requester)) {
					AOutput.send(requester, "&cYour duel request to " + PlaceholderAPI.setPlaceholders(challenged, challengedName) + " &chas expired.");
					requests.remove(requester);
					requestArenas.remove(requester);
				}
			}
		}.runTaskLater(PitSim.INSTANCE, 15 * 20L);


	}

}
