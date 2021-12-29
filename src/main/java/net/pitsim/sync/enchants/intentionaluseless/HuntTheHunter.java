package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class HuntTheHunter extends PitEnchant {

	public HuntTheHunter() {
		super("Hunt The Hunter", false, ApplyType.PANTS,
				"huntthehunter", "hth");
		isUncommonEnchant = true;
		isUseless = true;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		ALoreBuilder aLoreBuilder = new ALoreBuilder("&7Players using the &7Bounty Hunter");
		if(enchantLvl == 1) return aLoreBuilder.addLore("&7perk only deal half their bonus", "&7damage against you").getLore();
		if(enchantLvl == 2) return aLoreBuilder.addLore("&7perk do not deal bonus damage", "&7against you").getLore();
		return aLoreBuilder.addLore("&7perk do not deal bonus damage", "&7against you and overall deal",
				"&9-" + getReduction(enchantLvl) + "% &7damage to you").getLore();
	}

	public int getReduction(int enchantLvl) {
		return enchantLvl * 20 - 40;
	}
}