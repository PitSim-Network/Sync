package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.EnchantManager;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import net.pitsim.sync.misc.Misc;
import org.bukkit.event.EventHandler;

import java.util.List;

public class HeighHo extends PitEnchant {

	public HeighHo() {
		super("Heigh-Ho", false, ApplyType.PANTS,
				"heighho", "heigh-ho", "hiho", "hi-ho", "antimirror", "nomirror");
		isUncommonEnchant = true;
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;

		int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
		if(enchantLvl == 0) return;

		int mirrorLvl = attackEvent.getDefenderEnchantLevel(EnchantManager.getEnchant("mirror"));
		if(mirrorLvl == 0) return;

		attackEvent.increase += getDamage(enchantLvl);
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Deal &c+" + Misc.roundString(getDamage(enchantLvl)) + " &7damage against", "&fMirror &7wearers").getLore();
	}

	public int getDamage(int enchantLvl) {
		return enchantLvl;
	}
}
