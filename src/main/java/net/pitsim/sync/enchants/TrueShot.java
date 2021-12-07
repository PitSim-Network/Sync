package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import net.pitsim.sync.misc.Misc;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.List;

public class TrueShot extends PitEnchant {

	public TrueShot() {
		super("True Shot", true, ApplyType.BOWS,
				"trueshot", "true");
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;

		int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
		if(enchantLvl == 0) return;

		double damage = attackEvent.getFinalDamageIncrease();
		attackEvent.trueDamage += damage * (getPercent(enchantLvl) / 100.0);
		attackEvent.multiplier.add(Misc.getReductionMultiplier(getPercent(enchantLvl)));
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		if(enchantLvl == 1) {
			return new ALoreBuilder("&7Deal &c" + getPercent(enchantLvl) + "% &7of arrow damage as",
					"&7true damage (ignores armor)").getLore();
		}
		return new ALoreBuilder("&7Deal &c" + getPercent(enchantLvl) + "% + " + Misc.getHearts(getBase(enchantLvl)) + " &7of arrow",
				"&7damage as true damage (ignores", "&7armor)").getLore();
	}

	public int getPercent(int enchantLvl) {
		return Math.min(enchantLvl * 10 + 15, 100);
	}

	public double getBase(int enchantLvl) {
		return enchantLvl * 0.5 - 0.5;
	}
}