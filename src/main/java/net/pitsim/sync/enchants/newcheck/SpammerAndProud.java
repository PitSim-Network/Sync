package net.pitsim.sync.enchants.newcheck;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import org.bukkit.event.EventHandler;

import java.util.List;

public class SpammerAndProud extends PitEnchant {

	public SpammerAndProud() {
		super("Spammer and Proud", false, ApplyType.BOWS,
				"spammerandproud");
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;

		int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
		if(enchantLvl == 0) return;

		if(attackEvent.attacker.getWorld() != attackEvent.defender.getWorld() ||
				attackEvent.attacker.getLocation().distance(attackEvent.defender.getLocation()) >= 8) return;
		attackEvent.increasePercent += getIncrease(enchantLvl) / 100.0;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Deal &c+" + getIncrease(enchantLvl) + "% &7damage when shooting", "&7within &f8 &7blocks").getLore();
	}

	public int getIncrease(int enchantLvl) {
		return enchantLvl * 6 + 3;
	}
}