package net.pitsim.sync.enchants.needtoinspect;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import dev.kyro.arcticapi.misc.AOutput;
import me.clip.placeholderapi.PlaceholderAPI;
import net.pitsim.sync.PitSim;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import net.pitsim.sync.misc.Sounds;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PinDown extends PitEnchant {
	public static Map<Player, PinInfo> pinMap = new HashMap<>();

	static {
		new BukkitRunnable() {
			@Override
			public void run() {
				List<Player> toRemove = new ArrayList<>();
				for(Map.Entry<Player, PinInfo> entry : pinMap.entrySet()) {
					if(entry.getValue().secondsLeft == 0) {
						toRemove.add(entry.getKey());
						continue;
					}
					entry.getValue().secondsLeft--;
				}
				for(Player player : toRemove) pinMap.remove(player);
			}
		}.runTaskTimer(PitSim.INSTANCE, 0L, 20L);
	}

	public PinDown() {
		super("Pin down", false, ApplyType.BOWS,
				"pindown", "pin", "pd", "pin-down");
		isUncommonEnchant = true;
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;

		int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
		if(enchantLvl == 0) return;

		if(attackEvent.attacker == attackEvent.defender) return;
		if(!attackEvent.arrow.isCritical()) return;

		pin(attackEvent.attacker, attackEvent.defender, enchantLvl);
	}

	@Override
	public List<String> getDescription(int enchantLvl) {

		return new ALoreBuilder("&7Fully charged shots pin the victim", "&7down, removing their Speed and", "&7Jump Boost (" + getSeconds(enchantLvl) + "s cd)").getLore();
	}

	public static int getSeconds(int enchantLvl) {
		if(enchantLvl == 1) return 3;
		return enchantLvl * 5 - 5;
	}

//	Returns true if the pin is successful and the potion should be cancelled
	public static boolean attemptPin(Player defender, PotionEffectType potionEffectType) {
		if(!pinMap.containsKey(defender)) return false;
		Player attacker = pinMap.get(defender).attacker;

		if(potionEffectType == PotionEffectType.SPEED) {
			String pinMessage = "&c&lPINNED! &7by %luckperms_prefix%%player_name%&7, Speed cancelled!";
			String pinMessage2 = "&a&lITS A PIN! &7you removed Speed from %luckperms_prefix%%player_name%&7!";
			AOutput.send(defender, PlaceholderAPI.setPlaceholders(attacker, pinMessage));
			AOutput.send(attacker, PlaceholderAPI.setPlaceholders(defender, pinMessage2));
			Sounds.PIN_DOWN.play(defender);
			Sounds.PIN_DOWN.play(attacker);
			return true;
		}
		if(potionEffectType == PotionEffectType.JUMP) {
			String pinMessage = "&c&lPINNED! &7by %luckperms_prefix%%player_name%&7, Jump Boost cancelled!";
			String pinMessage2 = "&a&lITS A PIN! &7you removed Jump Boost from %luckperms_prefix%%player_name%&7!";
			AOutput.send(defender, PlaceholderAPI.setPlaceholders(attacker, pinMessage));
			AOutput.send(attacker, PlaceholderAPI.setPlaceholders(defender, pinMessage2));
			return true;
		}
		return false;
	}

	public static void pin(Player attacker, Player defender, int enchantLvl) {
		int seconds = getSeconds(enchantLvl);

		if(defender.hasPotionEffect(PotionEffectType.SPEED)) {
			defender.removePotionEffect(PotionEffectType.SPEED);
			String pinMessage = "&c&lPINNED! &7by %luckperms_prefix%%player_name%&7, Speed cancelled!";
			String pinMessage2 = "&a&lITS A PIN! &7you removed Speed from %luckperms_prefix%%player_name%&7!";
			AOutput.send(defender, PlaceholderAPI.setPlaceholders(attacker, pinMessage));
			AOutput.send(attacker, PlaceholderAPI.setPlaceholders(defender, pinMessage2));
			Sounds.PIN_DOWN.play(defender);
			Sounds.PIN_DOWN.play(attacker);
		}
		if(defender.hasPotionEffect(PotionEffectType.JUMP)) {
			defender.removePotionEffect(PotionEffectType.JUMP);
			String pinMessage = "&c&lPINNED! &7by %luckperms_prefix%%player_name%&7, Jump Boost cancelled!";
			String pinMessage2 = "&a&lITS A PIN! &7you removed Jump Boost from %luckperms_prefix%%player_name%&7!";
			AOutput.send(defender, PlaceholderAPI.setPlaceholders(attacker, pinMessage));
			AOutput.send(attacker, PlaceholderAPI.setPlaceholders(defender, pinMessage2));
		}

		if(!pinMap.containsKey(defender) || seconds > pinMap.get(defender).secondsLeft) pinMap.put(defender, new PinInfo(attacker, seconds));
	}

	public static class PinInfo {
		public Player attacker;
		public int secondsLeft;

		public PinInfo(Player attacker, int secondsLeft) {
			this.attacker = attacker;
			this.secondsLeft = secondsLeft;
		}
	}
}
