package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import org.bukkit.event.EventHandler;

import java.util.List;

public class Sniper extends PitEnchant {

	public Sniper() {
		super("Sniper", false, ApplyType.BOWS,
				"sniper");
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;

		int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
		if(enchantLvl == 0) return;

		if(attackEvent.arrow == null || attackEvent.arrow.getLocation().distance(attackEvent.defender.getLocation()) < 24) return;
		attackEvent.increasePercent += getDamage(enchantLvl);
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Deal &c+" + getDamage(enchantLvl) + "% &7damage when shooting", "&7from over &f24 &7blocks").getLore();
	}

	public int getDamage(int enchantLvl) {
		if(enchantLvl == 1) return 9;
		return enchantLvl * 12 - 6;
	}
}