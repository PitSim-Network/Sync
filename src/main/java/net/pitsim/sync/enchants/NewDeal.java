package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.EnchantManager;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import net.pitsim.sync.misc.Misc;
import org.bukkit.event.EventHandler;

import java.util.List;

public class NewDeal extends PitEnchant {

	public NewDeal() {
		super("New Deal", false, ApplyType.PANTS,
				"newdeal", "new-deal", "nd", "new", "deal");
		isUncommonEnchant = true;
	}

	@EventHandler
	public void onAttack(AttackEvent.Pre attackEvent) {
		if(!canApply(attackEvent)) return;

		int enchantLvl = attackEvent.getDefenderEnchantLevel(this);
		if(enchantLvl == 0) return;

		PitEnchant bill = EnchantManager.getEnchant("billionaire");
		attackEvent.getAttackerEnchantMap().remove(bill);
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;

		int enchantLvl = attackEvent.getDefenderEnchantLevel(this);
		if(enchantLvl == 0) return;

		attackEvent.decreasePercent += getDamageReduction(enchantLvl);
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		if(enchantLvl == 1) {
			return new ALoreBuilder("&7You are immune to &6Billionaire").getLore();
		} else {
			return new ALoreBuilder("&7Receive &9-" + Misc.roundString(getDamageReduction(enchantLvl)) + "% &7damage and you are",
					"&7immune to &6Billionaire").getLore();
		}
	}

	public double getDamageReduction(int enchantLvl) {
		return (enchantLvl - 1) * 4;
	}
}
