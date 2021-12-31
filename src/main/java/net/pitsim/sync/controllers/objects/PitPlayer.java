package net.pitsim.sync.controllers.objects;

import dev.kyro.arcticapi.data.APlayer;
import dev.kyro.arcticapi.data.APlayerData;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.pitsim.sync.PitSim;
import net.pitsim.sync.enchants.Hearts;
import net.pitsim.sync.events.HealEvent;
import net.pitsim.sync.hypixel.Loadout;
import net.pitsim.sync.inventories.PremiumGUI;
import net.pitsim.sync.perks.NoPerk;
import net.pitsim.sync.perks.Vampire;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class PitPlayer {

	public static List<PitPlayer> pitPlayers = new ArrayList<>();

	public Player player;
	public String prefix = "";

	public PitPerk[] pitPerks = new PitPerk[] { Vampire.INSTANCE, NoPerk.INSTANCE, NoPerk.INSTANCE, NoPerk.INSTANCE };

	public Map<PitEnchant, Integer> enchantHits = new HashMap<>();
	public Map<PitEnchant, Integer> enchantCharge = new HashMap<>();
	public Map<UUID, Double> recentDamageMap = new HashMap<>();
	public List<BukkitTask> assistRemove = new ArrayList<>();
	public UUID lastHitUUID = null;

	public Loadout loadout;
	public PremiumGUI premiumGUI;

//	Data that gets saved
	public int credits;
	public Date lastLogout;

	public PitPlayer(Player player) {
		this.player = player;

		this.premiumGUI = new PremiumGUI(player);

		APlayer aPlayer = APlayerData.getPlayerData(player);
		FileConfiguration playerData = aPlayer.playerData;

		this.credits = playerData.getInt("credits");
		this.lastLogout = new Date(playerData.getLong("lastlogout"));
	}

	public static PitPlayer getPitPlayer(Player player) {

		PitPlayer pitPlayer = null;
		for(PitPlayer testPitPlayer : pitPlayers) {

			if(testPitPlayer.player != player) continue;
			pitPlayer = testPitPlayer;
			break;
		}
		if(pitPlayer == null) {

			pitPlayer = new PitPlayer(player);
			pitPlayers.add(pitPlayer);
		}

		return pitPlayer;
	}

	public void addDamage(Player player, double damage) {
		if(player == null) return;

		recentDamageMap.putIfAbsent(player.getUniqueId(), 0D);
		recentDamageMap.put(player.getUniqueId(), recentDamageMap.get(player.getUniqueId()) + damage);

		BukkitTask bukkitTask = new BukkitRunnable() {
			@Override
			public void run() {
				for(BukkitTask pendingTask : Bukkit.getScheduler().getPendingTasks()) {
					if(pendingTask.getTaskId() != getTaskId()) continue;
					assistRemove.remove(pendingTask);
					break;
				}
				recentDamageMap.putIfAbsent(player.getUniqueId(), 0D);
				if(recentDamageMap.get(player.getUniqueId()) - damage != 0)
					recentDamageMap.put(player.getUniqueId(), recentDamageMap.get(player.getUniqueId()) - damage); else recentDamageMap.remove(player.getUniqueId());
			}
		}.runTaskLater(PitSim.INSTANCE, 200L);
		assistRemove.add(bukkitTask);
	}

	public HealEvent heal(double amount) {

		return heal(amount, HealEvent.HealType.HEALTH, -1);
	}

	public HealEvent heal(double amount, HealEvent.HealType healType, int max) {
		if(max == -1) max = Integer.MAX_VALUE;

		HealEvent healEvent = new HealEvent(player, amount, healType, max);
		Bukkit.getServer().getPluginManager().callEvent(healEvent);

		if(healType == HealEvent.HealType.HEALTH) {
			player.setHealth(Math.min(player.getHealth() + healEvent.getFinalHeal(), player.getMaxHealth()));
		} else {
			EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
			if(nmsPlayer.getAbsorptionHearts() < healEvent.max)
				nmsPlayer.setAbsorptionHearts(Math.min((float) (nmsPlayer.getAbsorptionHearts() + healEvent.getFinalHeal()), max));
		}
		return healEvent;
	}

	public void updateMaxHealth() {
		int maxHealth = 24;
		if(Hearts.INSTANCE != null) maxHealth += Hearts.INSTANCE.getExtraHealth(this);

		if(loadout != null && loadout.hypixelPlayer.hasThick) maxHealth += 4;

		if(player.getMaxHealth() == maxHealth) return;
		player.setMaxHealth(maxHealth);
	}

	public int getMaxCredits() {
		return 100;
	}

	public void save() {
		APlayer aPlayer = APlayerData.getPlayerData(player);
		FileConfiguration playerData = aPlayer.playerData;

		playerData.set("credits", credits);
		playerData.set("lastlogout", lastLogout.getTime());

		aPlayer.save();
	}
}
