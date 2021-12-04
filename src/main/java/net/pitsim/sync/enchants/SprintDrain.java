package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import dev.kyro.arcticapi.misc.AUtil;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.controllers.objects.PitPlayer;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import net.pitsim.sync.misc.Misc;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class SprintDrain extends PitEnchant {

	public SprintDrain() {
		super("Sprint Drain", false, ApplyType.BOWS,
				"sprintdrain", "drain", "sprint", "sprint-drain", "sd");
		isUncommonEnchant = true;
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;

		int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
		if(enchantLvl == 0) return;

		if(attackEvent.attacker.equals(attackEvent.defender)) return;

		PitPlayer pitDefender = PitPlayer.getPitPlayer(attackEvent.defender);
		Misc.applyPotionEffect(attackEvent.defender, PotionEffectType.SLOW, getSlowDuration(enchantLvl) * 20, 0, true, false);

		Misc.applyPotionEffect(attackEvent.attacker, PotionEffectType.SPEED,
				getSpeedDuration(enchantLvl) * 20, getSpeedAmplifier(enchantLvl) - 1, true, false);

		PitPlayer pitAttacker = PitPlayer.getPitPlayer(attackEvent.attacker);
	}

	@Override
	public List<String> getDescription(int enchantLvl) {


		if(enchantLvl == 1) {
			return new ALoreBuilder("&7Arrow shots grant you &eSpeed " + AUtil.toRoman(getSpeedAmplifier(enchantLvl)), "&7(" +
					getSpeedDuration(enchantLvl) + "s)").getLore();
		} else {
			return new ALoreBuilder("&7Arrow shots grant you &eSpeed " + AUtil.toRoman(getSpeedAmplifier(enchantLvl)), "&7(" +
					getSpeedDuration(enchantLvl) + "s) and apply &9Slowness I ", "&7(" + getSlowDuration(enchantLvl) + "s)").getLore();
		}

	}

	public int getSlowDuration(int enchantLvl) {

		return Misc.linearEnchant(enchantLvl, 0.5, 0) * 3;
	}

	public int getSpeedAmplifier(int enchantLvl) {

		return Misc.linearEnchant(enchantLvl, 0.5, 1);
	}

	public int getSpeedDuration(int enchantLvl) {

		return enchantLvl * 2 + 1;
	}
}
