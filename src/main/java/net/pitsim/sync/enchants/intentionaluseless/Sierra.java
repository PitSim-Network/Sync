package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class Sierra extends PitEnchant {

	public Sierra() {
		super("Sierra", false, ApplyType.SWORDS,
				"sierra");
		isUncommonEnchant = true;
		isUseless = true;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Earn &6+" + getGold(enchantLvl) + "g &7per &bdiamond", "&7piece your victim is wearing").getLore();
	}

	public int getGold(int enchantLvl) {
		return enchantLvl * 30;
	}
}