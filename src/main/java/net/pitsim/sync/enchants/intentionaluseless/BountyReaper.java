package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class BountyReaper extends PitEnchant {

	public BountyReaper() {
		super("Bounty Reaper", false, ApplyType.SWORDS,
				"bountyreaper");
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Deal &c+" + getDamage(enchantLvl) + "% &7damage vs. players", "&7with a bounty").getLore();
	}

	public int getDamage(int enchantLvl) {
		if(enchantLvl == 1) return 7;
		return enchantLvl * 10 - 5;
	}
}