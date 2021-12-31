package net.pitsim.sync.controllers;

import net.pitsim.sync.PitSim;
import net.pitsim.sync.controllers.objects.PitPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;

public class CreditManager implements Listener {
	public static int LOW_CREDIT_THRESHOLD = 100;

	static {
		new BukkitRunnable() {
			int count = 0;
			@Override
			public void run() {
				count++;
				for(PitPlayer pitPlayer : PitPlayer.pitPlayers) {
					if(!pitPlayer.player.isOnline() || pitPlayer.credits >= pitPlayer.getMaxCredits()) continue;
					if(pitPlayer.credits >= LOW_CREDIT_THRESHOLD && count % 5 != 0) continue;
					give(pitPlayer, 1, false);
				}
			}
		}.runTaskTimer(PitSim.INSTANCE, 0L, 20 * 2);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
		pitPlayer.lastLogout = new Date();
		pitPlayer.save();
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		new BukkitRunnable() {
			@Override
			public void run() {
				PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
				if(pitPlayer.lastLogout.getTime() == 0) return;
				int minutesPassed = (int) ((new Date().getTime() - pitPlayer.lastLogout.getTime()) / 1000.0 / 60.0);
				int creditsOwed = getCreditsOwed(minutesPassed);
				give(pitPlayer, creditsOwed, false);
			}
		}.runTaskLater(PitSim.INSTANCE, 1L);
	}

	public static void give(PitPlayer pitPlayer, int amount, boolean overflow) {
		if(amount == 0) return;

		pitPlayer.credits += overflow ? amount : Math.max(Math.min(amount, pitPlayer.getMaxCredits() - pitPlayer.credits), 0);
	}

	public static int getCreditsOwed(int minutesPassed) {
		return minutesPassed;
	}
}
