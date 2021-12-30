package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import org.bukkit.event.EventHandler;

import java.util.List;

public class Jumpspammer extends PitEnchant {

	public Jumpspammer() {
		super("Jumpspammer", false, ApplyType.BOWS,
				"jumpspammer");
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;
		int attackerEnchantLvl = attackEvent.getAttackerEnchantLevel(this);
		int defenderEnchantLvl = attackEvent.getDefenderEnchantLevel(this);

		if(attackerEnchantLvl != 0 && !attackEvent.attacker.isOnGround()) {
			attackEvent.increasePercent += getDamageIncrease(attackerEnchantLvl);
		}

		if(defenderEnchantLvl != 0 && !attackEvent.defender.isOnGround()) {
			attackEvent.decreasePercent += getDamageDecrease(defenderEnchantLvl);
		}
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		if(enchantLvl == 1) return new ALoreBuilder("&7Deal &c+" + getDamageIncrease(enchantLvl) + "% &7damage while midair",
				"&7on arrow hit").getLore();
		return new ALoreBuilder("&7While midair, your arrows deal", "&c+" + getDamageIncrease(enchantLvl) + " &7damage. While midair,",
				"&7receive &9-" + getDamageDecrease(enchantLvl) + " &7damage from melee", "&7and ranged attacks").getLore();
	}

	public int getDamageIncrease(int enchantLvl) {
		if(enchantLvl == 1) return 10;
		return enchantLvl * 8;
	}

	public int getDamageDecrease(int enchantLvl) {
		return enchantLvl * 10 - 10;
	}
}