package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class PitMBA extends PitEnchant {

	public PitMBA() {
		super("Pit MBA", false, ApplyType.PANTS,
				"pitmba");
		isUncommonEnchant = true;
		isUseless = true;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Receive &6+" + getGold(enchantLvl) + "% gold &7and &b+" + getXP(enchantLvl) + "%",
				"&bXP &on kill when there are at", "&7least 5 players within 15 blocks").getLore();
	}

	public int getGold(int enchantLvl) {
		return enchantLvl * 20 - 10;
	}

	public int getXP(int enchantLvl) {
		return enchantLvl * 10;
	}
}