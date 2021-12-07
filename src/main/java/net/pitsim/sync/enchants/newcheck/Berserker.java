package net.pitsim.sync.enchants.newcheck;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import net.pitsim.sync.misc.Misc;
import org.bukkit.event.EventHandler;

import java.util.List;

public class Berserker extends PitEnchant {

	public Berserker() {
		super("Berserker", false, ApplyType.SWORDS,
				"berserker");
		isUncommonEnchant = true;
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;
		int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
		if(!Misc.isCritical(attackEvent.attacker) || Math.random() > getChance(enchantLvl) / 100.0) return;
		attackEvent.multiplier.add(1.5);
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7You can now critical hit on the",
				"&7ground. &a" + getChance(enchantLvl) + "% chance &7to crit for", "&c50% extra &7damage").getLore();
	}

	public int getChance(int enchantLvl) {
		if(enchantLvl == 1) return 12;
		return enchantLvl * 10;
	}
}