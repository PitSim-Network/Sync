package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.List;

public class Mirror extends PitEnchant {

	public Mirror() {
		super("Mirror", false, ApplyType.PANTS,
				"mirror", "mir");
		isUncommonEnchant = true;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;

		int attackerEnchantLvl = attackEvent.getAttackerEnchantLevel(this);
		int defenderEnchantLvl = attackEvent.getDefenderEnchantLevel(this);
		if(attackerEnchantLvl == 0 && defenderEnchantLvl == 0) return;

		if(attackerEnchantLvl > 1) {
			double trueDamage = attackEvent.selfTrueDamage;
			trueDamage *= getReflectionPercent(attackerEnchantLvl) / 100.0;
			attackEvent.trueDamage += trueDamage;
		}
		if(defenderEnchantLvl > 1) {
			double trueDamage = attackEvent.trueDamage;
			trueDamage *= getReflectionPercent(defenderEnchantLvl) / 100.0;
			attackEvent.selfTrueDamage += trueDamage;
		}

		if(attackerEnchantLvl != 0) attackEvent.selfTrueDamage = 0;
		if(defenderEnchantLvl != 0) attackEvent.trueDamage = 0;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		if(enchantLvl == 1) {
			return new ALoreBuilder("&7You are immune to true damage").getLore();
		} else {
			return new ALoreBuilder("&7You do not take true damage and",
					"&7instead reflect &e" + getReflectionPercent(enchantLvl) + "% &7of it to", "&7your attacker").getLore();
		}
	}

	public int getReflectionPercent(int enchantLvl) {
		return enchantLvl * 25 - 25;
	}
}
