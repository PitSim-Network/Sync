package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class ComboXP extends PitEnchant {

	public ComboXP() {
		super("Combo: XP", false, ApplyType.SWORDS,
				"comboxp");
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Every &efifth &7strike rwards", "&b+" + getXP(enchantLvl) + " XP").getLore();
	}

	public int getXP(int enchantLvl) {
		return enchantLvl * 8 + 12;
	}
}