package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class Pitpocket extends PitEnchant {

	public Pitpocket() {
		super("Pitpocket", false, ApplyType.SWORDS,
				"pitpocket");
		isUncommonEnchant = true;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Steal &6" + getGold(enchantLvl) + "g &7on melee hit (" + getCooldown(enchantLvl) + "s", "&7cooldown)").getLore();
	}

	public int getGold(int enchantLvl) {
		return enchantLvl * 5 + 10;
	}

	public int getCooldown(int enchantLvl) {
		if(enchantLvl == 1) return 25;
		return Math.max(34 - enchantLvl * 7, 0);
	}
}