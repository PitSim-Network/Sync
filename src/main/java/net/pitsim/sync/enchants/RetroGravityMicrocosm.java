package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import dev.kyro.arcticapi.misc.AOutput;
import net.pitsim.sync.PitSim;
import net.pitsim.sync.controllers.HitCounter;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.controllers.objects.PitPlayer;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import net.pitsim.sync.events.KillEvent;
import net.pitsim.sync.events.OofEvent;
import net.pitsim.sync.misc.Misc;
import net.pitsim.sync.misc.Sounds;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class RetroGravityMicrocosm extends PitEnchant {
	public static Map<Player, RGMInfo> rgmGlobalMap = new HashMap<>();
//	public static Map<UUID, Map<UUID, Integer>> rgmMap = new HashMap<>();

	public RetroGravityMicrocosm() {
		super("Retro-Gravity Microcosm", true, ApplyType.PANTS,
				"rgm", "retro", "retrogravitymicrocosm", "retro-gravitymicrocosm", "retro-gravity-microcosm");
	}

	@EventHandler
	public void onOof(OofEvent event) {
		Player player = event.getPlayer();
		clearAttacker(player);
		clearDefender(player);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		clearAttacker(player);
		clearDefender(player);
	}

	@EventHandler
	public void onChangeWorld(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		clearAttacker(player);
		clearDefender(player);
	}

	@EventHandler
	public void onKill(KillEvent killEvent) {
		clearAttacker(killEvent.dead);
		clearDefender(killEvent.dead);
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;

		int attackerEnchantLvl = attackEvent.getAttackerEnchantLevel(this);
		int defenderEnchantLvl = attackEvent.getDefenderEnchantLevel(this);

		if(attackerEnchantLvl >= 2) {

			int charge = getProcs(attackEvent.defender, attackEvent.attacker);
			attackEvent.increase += charge * 3;
		}
		if(defenderEnchantLvl != 0) {
//			if(attackEvent.attacker.getLocation().add(0, -0.1, 0).getBlock().getType() != Material.AIR) return;
			if(attackEvent.attacker.isOnGround()) return;

			HitCounter.incrementCounter(attackEvent.defender, this);
			if(!HitCounter.hasReachedThreshold(attackEvent.defender, this, 3)) return;
			add(attackEvent.attacker, attackEvent.defender);

			if(defenderEnchantLvl >= 1) {
				PitPlayer pitDefender = PitPlayer.getPitPlayer(attackEvent.defender);
				pitDefender.heal(2.5);
			}
			if(defenderEnchantLvl >= 3) {
				attackEvent.selfTrueDamage += 0.5;
			}

			int charge = getProcs(attackEvent.attacker, attackEvent.defender);
			AOutput.send(attackEvent.defender, "&d&lRGM!&7 Procced against " + attackEvent.attacker.getName() + " &8(" + charge + "x)");
			Sounds.RGM.play(attackEvent.defender);
			Sounds.RGM.play(attackEvent.attacker);
		}
	}

//	public static Map<UUID, Integer> getPlayerRGMMap(Player player) {
//
//		rgmMap.putIfAbsent(player.getUniqueId(), new HashMap<>());
//		return rgmMap.get(player.getUniqueId());
//	}
//
//	public static int getCharge(Player rgmPlayer, Player player) {
//
//		Map<UUID, Integer> playerRGMMap = getPlayerRGMMap(rgmPlayer);
//		playerRGMMap.putIfAbsent(player.getUniqueId(), 0);
//		return playerRGMMap.get(player.getUniqueId());
//	}
//
//	public static void setCharge(Player rgmPlayer, Player player, int amount) {
//
//		Map<UUID, Integer> playerRGMMap = getPlayerRGMMap(rgmPlayer);
//		playerRGMMap.put(player.getUniqueId(), amount);
//	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		ALoreBuilder loreBuilder = new ALoreBuilder("&7When a player hits you from", "&7above ground &e3 times &7in a row:");
		if(enchantLvl >= 1) loreBuilder.addLore("&7You heal &c" + Misc.getHearts(2.5));
		if(enchantLvl >= 2) loreBuilder.addLore("&7Gain &c+" + Misc.getHearts(3));
		if(enchantLvl >= 3) loreBuilder.addLore("&7They take &c" + Misc.getHearts(1) + " &7true damage");
		return loreBuilder.getLore();
	}

//	Attacker is the player that will take more damage, defender is the player that will deal more damage
	public static int getProcs(Player attacker, Player defender) {
		if(!rgmGlobalMap.containsKey(defender)) return 0;
		RGMInfo rgmInfo = rgmGlobalMap.get(defender);
		if(!rgmInfo.rgmPlayerProcMap.containsKey(attacker)) return 0;
		return rgmInfo.rgmPlayerProcMap.get(attacker).size();
	}

	public static void add(Player attacker, Player defender) {
		if(rgmGlobalMap.containsKey(defender)) {
			rgmGlobalMap.get(defender).add(attacker);
		} else {
			RGMInfo rgmInfo = new RGMInfo();
			rgmInfo.add(attacker);
			rgmGlobalMap.putIfAbsent(defender, rgmInfo);
		}
	}

	public static void clearDefender(Player defender) {
		rgmGlobalMap.remove(defender);
	}

	public static void clearAttacker(Player attacker) {
		for(Map.Entry<Player, RGMInfo> entry : rgmGlobalMap.entrySet()) entry.getValue().clear(attacker);
	}

	public static class RGMInfo {
		private final Map<Player, List<BukkitTask>> rgmPlayerProcMap = new HashMap<>();

		private void add(Player player) {
			rgmPlayerProcMap.putIfAbsent(player, new ArrayList<>());
			rgmPlayerProcMap.get(player).add(new BukkitRunnable() {
				@Override
				public void run() {
					if(rgmPlayerProcMap.get(player) == null) return;
					for(BukkitTask bukkitTask : rgmPlayerProcMap.get(player)) {
						if(bukkitTask.getTaskId() != getTaskId()) continue;
						rgmPlayerProcMap.get(player).remove(bukkitTask);
						break;
					}
				}
			}.runTaskLater(PitSim.INSTANCE, 30 * 20));
		}

		public void clear(Player player) {
			rgmPlayerProcMap.remove(player);
		}
	}
}