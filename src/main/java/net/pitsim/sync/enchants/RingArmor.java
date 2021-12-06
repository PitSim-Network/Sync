package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import org.bukkit.event.EventHandler;

import java.util.List;

public class RingArmor extends PitEnchant {

	public RingArmor() {
		super("Ring Armor", false, ApplyType.PANTS,
				"ring", "armor", "ring-armor", "ringarmor");
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;

		int enchantLvl = attackEvent.getDefenderEnchantLevel(this);
		if(enchantLvl == 0) return;

		if(attackEvent.arrow == null) return;
		attackEvent.multiplier.add(getDamageMultiplier(enchantLvl));
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Receive &9-" + getDamageReduction(enchantLvl) + "% &7damage from", "&7arrows").getLore();
	}

	public double getDamageMultiplier(int enchantLvl) {
		return (100D - getDamageReduction(enchantLvl)) / 100;
	}

	public int getDamageReduction(int enchantLvl) {
		return enchantLvl * 20;
	}
}
