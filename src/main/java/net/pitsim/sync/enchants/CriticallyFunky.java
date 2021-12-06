package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.HitCounter;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import net.pitsim.sync.misc.Misc;
import org.bukkit.event.EventHandler;

import java.util.List;

public class CriticallyFunky extends PitEnchant {

	public CriticallyFunky() {
		super("Critically Funky", false, ApplyType.PANTS,
				"criticallyfunky", "critically-funky", "cf", "critfunky", "crit-funky", "crit", "funky");
		isUncommonEnchant = true;
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;

		int attackerEnchantLvl = attackEvent.getAttackerEnchantLevel(this);
		int defenderEnchantLvl = attackEvent.getDefenderEnchantLevel(this);

		if(defenderEnchantLvl != 0 && Misc.isCritical(attackEvent.attacker)) {
			HitCounter.setCharge(attackEvent.defender, this, 1);
			attackEvent.multiplier.add(Misc.getReductionMultiplier(getReduction(defenderEnchantLvl)));
		}

		if(attackerEnchantLvl != 0 && HitCounter.getCharge(attackEvent.attacker, this) == 1) {
			attackEvent.increasePercent += getDamage(attackerEnchantLvl) / 100D;
			HitCounter.setCharge(attackEvent.attacker, this, 0);
		}
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		if(enchantLvl == 1) {
			return new ALoreBuilder("&7Critical hits against you deal ", "&9" +
					Misc.roundString(100 - getReduction(enchantLvl)) + "% &7of the damage they",
					"&7normally would").getLore();
		} else {
			return new ALoreBuilder("&7Critical hits against you deal ", "&9" +
					Misc.roundString(100 - getReduction(enchantLvl)) + "% &7of the damage they",
					"&7normally would and empower your", "&7next strike for &c+" +
					Misc.roundString(getDamage(enchantLvl)) + "&c% &7damage").getLore();
		}
	}

	public double getReduction(int enchantLvl) {
		switch(enchantLvl) {
			case 1:
			case 2:
				return 35;
			case 3:
				return 60;

		}
		return 100;
	}

    public double getDamage(int enchantLvl) {
		switch(enchantLvl) {
			case 1:
				return 0;
			case 2:
				return 14;
			case 3:
				return 30;
		}
		return enchantLvl * 15 - 15;
    }
}
