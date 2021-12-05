package net.pitsim.sync.controllers;

import dev.kyro.arcticapi.data.AConfig;
import dev.kyro.arcticapi.data.APlayer;
import dev.kyro.arcticapi.data.APlayerData;
import dev.kyro.arcticapi.misc.AOutput;
import net.pitsim.sync.PitSim;
import net.pitsim.sync.controllers.objects.PitPlayer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class LockdownManager implements Listener {
	public static List<Player> captchaPlayers = new ArrayList<>();
	public static Map<Player, UUID> captchaAnswers = new HashMap<>();
	private static boolean requireVerification;
	private static boolean requireCaptcha;
	public static String verificationMessage = "&c&lVERIFICATION! &7Verify your account in /discord (.verify)";

	static {
		requireVerification = AConfig.getBoolean("security.require-verification");
		requireCaptcha = AConfig.getBoolean("security.require-captcha");

		new BukkitRunnable() {
			@Override
			public void run() {
				if(!requireVerification && !requireCaptcha) return;
				for(Player player : Bukkit.getOnlinePlayers()) {
					if(SpawnManager.isInSpawn(player.getLocation())) continue;
					if(requireVerification && !isVerified(player)) {
						AOutput.error(player, verificationMessage);
						player.teleport(Bukkit.getWorld("lobby").getSpawnLocation());
						continue;
					}
					if(requireCaptcha && !isCaptcha(player)) {
						sendCaptchaMessage(player);
						player.teleport(Bukkit.getWorld("lobby").getSpawnLocation());
					}
				}
			}
		}.runTaskTimer(PitSim.INSTANCE, 0L, 10L);
	}

	public static void sendCaptchaMessage(Player player) {
		if(!captchaAnswers.containsKey(player)) captchaAnswers.put(player, UUID.randomUUID());
		TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&c&lCAPTCHA! &7Click this to complete"));
		message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/captcha " + captchaAnswers.get(player)));
		player.sendMessage(message);
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		if(!isVerified(player)) {
			event.setCancelled(true);
			AOutput.error(player, verificationMessage);
			return;
		}
		if(!isCaptcha(player)) {
			event.setCancelled(true);
			sendCaptchaMessage(player);
		}
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		if(event.getMessage().toLowerCase().startsWith("/captcha") || event.getMessage().toLowerCase().startsWith("/disc")) return;
		Player player = event.getPlayer();
		if(!isVerified(player)) {
			event.setCancelled(true);
			AOutput.error(player, verificationMessage);
			return;
		}
		if(!isCaptcha(player)) {
			event.setCancelled(true);
			sendCaptchaMessage(player);
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		captchaPlayers.remove(player);
		captchaAnswers.remove(player);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
		new BukkitRunnable() {
			@Override
			public void run() {
				if(!isCaptcha(player)) {
					sendCaptchaMessage(player);
					sendCaptchaMessage(player);
					sendCaptchaMessage(player);
				}
			}
		}.runTaskLater(PitSim.INSTANCE, 1L);
	}

	public static boolean isVerified(Player player) {
		if(!requireVerification || player.hasPermission("pitsim.autoverify")) return true;
		APlayer aPlayer = APlayerData.getPlayerData(player);
		return aPlayer.playerData.getLong("discord-id") != 0;
	}

	public static boolean verify(String name, long discordId) {
		UUID uuid = null;
		APlayer aPlayer = null;
		try {
			uuid = UUID.fromString(name);
			aPlayer = APlayerData.getPlayerData(uuid);
		} catch(Exception ignored) {
			for(Map.Entry<UUID, APlayer> entry : APlayerData.getAllData().entrySet()) {
				String testName = entry.getValue().playerData.getString("name");
				if(testName == null || !testName.equalsIgnoreCase(name)) continue;
				uuid = entry.getKey();
				aPlayer = entry.getValue();
				break;
			}
			if(uuid == null || aPlayer == null) return false;
		}
		aPlayer.playerData.set("discord-id", discordId);
		aPlayer.save();
		return true;
	}

	public static boolean removeVerifiedPlayer(long discordId) {
		boolean mod = false;
		for(Map.Entry<UUID, APlayer> entry : APlayerData.getAllData().entrySet()) {
			if(entry.getValue().playerData.getLong("discord-id") != discordId) continue;
			entry.getValue().playerData.set("discord-id", null);
			APlayerData.savePlayerData(entry.getKey());
			mod = true;
		}
		return mod;
	}

	public static boolean isCaptcha(Player player) {
		if(!requireCaptcha || player.hasPermission("pitsim.autoverify")) return true;
		PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
		return captchaPlayers.contains(player);
	}

	public static boolean verificationRequired() {
		return requireVerification;
	}

	public static boolean captchaRequired() {
		return requireCaptcha;
	}

	public static void enableVerification() {
		if(requireVerification) return;
		requireVerification = true;
		AConfig.set("security.require-verification", true);
		AConfig.saveConfig();
	}

	public static void disableVerification() {
		if(!requireVerification) return;
		requireVerification = false;
		AConfig.set("security.require-verification", false);
		AConfig.saveConfig();
	}

	public static void enableCaptcha() {
		if(requireCaptcha) return;
		requireCaptcha = true;
		AConfig.set("security.require-captcha", true);
		AConfig.saveConfig();
	}

	public static void disableCaptcha() {
		if(!requireCaptcha) return;
		requireCaptcha = false;
		AConfig.set("security.require-captcha", false);
		AConfig.saveConfig();
	}

	public static void passCaptcha(Player player) {
		captchaPlayers.add(player);
		AOutput.send(player, "&a&lSUCCESS! &7Captcha Passed!");
	}
}
