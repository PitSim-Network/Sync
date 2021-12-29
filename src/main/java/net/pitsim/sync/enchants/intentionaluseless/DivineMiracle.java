package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class DivineMiracle extends PitEnchant {

	public DivineMiracle() {
		super("Divine Miracle", true, ApplyType.PANTS,
				"divinemiracle", "divine");
		isUseless = true;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&b" + getChance(enchantLvl) + "% chance &7to retain the lives", "&7on your items on death").getLore();
	}

	public int getChance(int enchantLvl) {
		return enchantLvl * 15;
	}
}