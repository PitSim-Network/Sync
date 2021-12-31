package net.pitsim.sync.controllers;

import net.pitsim.sync.PitSim;
import net.pitsim.sync.controllers.objects.PitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;

public class CreditManager implements Listener {
	public static int LOW_CREDIT_THRESHOLD = 100;

	public static int getMaxCredits(Player player) {
		if(player.hasPermission("group.premium")) return 1000;
		return 500;
	}

	public static int getLowCreditThreshold(Player player) {
		return 50;
	}

	public static int getLowSecondsPerCredit(Player player) {
		if(player.hasPermission("group.premium")) return 2;
		return 4;
	}

	public static int getSecondsPerCredit(Player player) {
		if(player.hasPermission("group.premium")) return 5;
		return 20;
	}

	public static int getOfflineMinutesPerCredit(Player player) {
		if(player.hasPermission("group.premium")) return 1;
		return 2;
	}

	static {
		new BukkitRunnable() {
			int seconds = 0;
			@Override
			public void run() {
				seconds++;
				for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
					PitPlayer pitPlayer = PitPlayer.getPitPlayer(onlinePlayer);
					if(pitPlayer.credits >= getMaxCredits(onlinePlayer)) continue;

					int modulus = pitPlayer.credits < getLowCreditThreshold(onlinePlayer) ? getLowSecondsPerCredit(onlinePlayer) : getSecondsPerCredit(onlinePlayer);
					if(seconds % modulus != 0) continue;
					give(pitPlayer, 1, false);
				}
			}
		}.runTaskTimer(PitSim.INSTANCE, 0L, 20);
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
				int creditsOwed = getCreditsOwed(player, minutesPassed);
				give(pitPlayer, creditsOwed, false);
			}
		}.runTaskLater(PitSim.INSTANCE, 1L);
	}

	public static void give(PitPlayer pitPlayer, int amount, boolean overflow) {
		if(amount == 0) return;

		pitPlayer.credits += overflow ? amount : Math.max(Math.min(amount, getMaxCredits(pitPlayer.player) - pitPlayer.credits), 0);
	}

	public static int getCreditsOwed(Player player, int minutesPassed) {
		return minutesPassed / getOfflineMinutesPerCredit(player);
	}
}
