package net.pitsim.sync.controllers.objects;

import dev.kyro.arcticapi.data.APlayerData;
import net.pitsim.sync.PitSim;
import net.pitsim.sync.enchants.Hearts;
import net.pitsim.sync.enums.AChatColor;
import net.pitsim.sync.events.HealEvent;
import net.pitsim.sync.perks.NoPerk;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class PitPlayer {

	public static List<PitPlayer> pitPlayers = new ArrayList<>();

	public Player player;
	public String prefix;

	public PitPerk[] pitPerks = new PitPerk[4];

	public Map<PitEnchant, Integer> enchantHits = new HashMap<>();
	public Map<PitEnchant, Integer> enchantCharge = new HashMap<>();
	public Map<UUID, Double> recentDamageMap = new HashMap<>();
	public List<BukkitTask> assistRemove = new ArrayList<>();
	public AChatColor chatColor = null;
	public UUID lastHitUUID = null;
	public ItemStack confirmedDrop = null;

	public Map<Integer, ItemStack> enderchestMystics = new HashMap<>();
	public Map<Integer, ItemStack> inventoryMystics = new HashMap<>();

	public PitPlayer(Player player) {
		this.player = player;

			String message = "%luckperms_prefix%";
			prefix = "";

			FileConfiguration playerData = APlayerData.getPlayerData(player);

			for(int i = 0; i < pitPerks.length; i++) {

				String perkString = playerData.getString("perk-" + i);
				PitPerk savedPerk = perkString != null ? PitPerk.getPitPerk(perkString) : NoPerk.INSTANCE;

				pitPerks[i] = savedPerk != null ? savedPerk : NoPerk.INSTANCE;
			}

			String chatColorString = playerData.getString("chatcolor");
			if(chatColorString != null) {
				chatColor = AChatColor.valueOf(chatColorString);
			}

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

	public boolean hasPerk(PitPerk pitPerk) {

		for(PitPerk perk : pitPerks) if(perk == pitPerk) return true;
		return false;
	}



	public void updateMaxHealth() {

		int maxHealth = 24;
		if(Hearts.INSTANCE != null) maxHealth += Hearts.INSTANCE.getExtraHealth(this);

		if(player.getMaxHealth() == maxHealth) return;
		player.setMaxHealth(maxHealth);
	}
}
