package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import org.bukkit.event.EventHandler;

import java.util.List;

public class DavidAndGoliath extends PitEnchant {

	public DavidAndGoliath() {
		super("David and Goliath", false, ApplyType.PANTS,
				"davidandgoliath");
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;

		int enchantLvl = attackEvent.getDefenderEnchantLevel(this);
		if(enchantLvl == 0) return;

		attackEvent.decreasePercent += getReduction(enchantLvl);
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Receive &9-" + getReduction(enchantLvl) + "% &7damage from", "&7players with a bounty").getLore();
	}

	public int getReduction(int enchantLvl) {
		if(enchantLvl == 1) return 15;
		return enchantLvl * 15 - 5;
	}
}