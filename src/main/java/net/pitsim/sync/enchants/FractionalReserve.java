package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import org.bukkit.event.EventHandler;

import java.util.List;

public class FractionalReserve extends PitEnchant {

	public FractionalReserve() {
		super("Fractional Reserve", false, ApplyType.PANTS,
				"fractionalreserve", "frac", "frac-reserve", "fractional-reserve", "fracreserve");
		isUncommonEnchant = true;
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;

		int enchantLvl = attackEvent.getDefenderEnchantLevel(this);
		if(enchantLvl == 0) return;

		attackEvent.decreasePercent += getMaxDamageReduction(enchantLvl);
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Receive &9-1% damage per",
				"&610,000g &7you have (&9-" + getMaxDamageReduction(enchantLvl) + "%", "&7max)").getLore();
	}

	public double getMaxDamageReduction(int enchantLvl) {
		return (int) Math.max(Math.floor(Math.pow(enchantLvl, 1.65) * 3) + 12, 0);
	}
}
