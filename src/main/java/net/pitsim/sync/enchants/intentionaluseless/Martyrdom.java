package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class Martyrdom extends PitEnchant {

	public Martyrdom() {
		super("Martyrdom", true, ApplyType.PANTS,
				"martyrdom");
		isUseless = true;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		ALoreBuilder loreBuilder = new ALoreBuilder();
		if(enchantLvl == 1) loreBuilder.addLore("&7Leave a handful of &acreepers", "&7behind on death");
		if(enchantLvl == 2) loreBuilder.addLore("&7Leave lots of &acreepers &7behind", "&7on death");
		if(enchantLvl >= 3) loreBuilder.addLore("&7Leave a ridiculous amount of", "&acreepers &7behind on death");
		return loreBuilder.addLore("&7Ericka's fav").getLore();
	}
}