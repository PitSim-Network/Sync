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
		if(player.hasPermission("group.premium")) return 2_000;
		return 1_000;
	}

	public static int getLowCreditThreshold(Player player) {
		return 100;
	}

	public static int getLowTicksPerCredit(Player player) {
		if(player.hasPermission("group.premium")) return 1;
		return 10;
	}

	public static int getTicksPerCredit(Player player) {
		if(player.hasPermission("group.premium")) return 10;
		return 20;
	}

	public static int getOfflineTicksPerCredit(Player player) {
		if(player.hasPermission("group.premium")) return 20 * 5;
		return 20 * 20;
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

					int modulus = pitPlayer.credits < getLowCreditThreshold(onlinePlayer) ? getLowTicksPerCredit(onlinePlayer) : getTicksPerCredit(onlinePlayer);
					if(seconds % modulus != 0) continue;
					give(pitPlayer, 1, false);
				}
			}
		}.runTaskTimer(PitSim.INSTANCE, 0L, 1);
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
				int ticksPassed = (int) ((new Date().getTime() - pitPlayer.lastLogout.getTime()) / 1000.0 * 20);
				int creditsOwed = getCreditsOwed(player, ticksPassed);
				give(pitPlayer, creditsOwed, false);
			}
		}.runTaskLater(PitSim.INSTANCE, 1L);
	}

	public static void give(PitPlayer pitPlayer, int amount, boolean overflow) {
		if(amount == 0) return;

		pitPlayer.credits += overflow ? amount : Math.max(Math.min(amount, getMaxCredits(pitPlayer.player) - pitPlayer.credits), 0);
	}

	public static int getCreditsOwed(Player player, int ticksPassed) {
		return ticksPassed / getOfflineTicksPerCredit(player);
	}
}
