package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class CounterJanitor extends PitEnchant {

	public CounterJanitor() {
		super("Counter-Janitor", false, ApplyType.SWORDS,
				"counterjanitor");
		isUseless = true;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Gain &eResistance I &7(" + getSeconds(enchantLvl) + "s) on").getLore();
	}

	public int getSeconds(int enchantLvl) {
		if(enchantLvl == 1) return 2;
		return enchantLvl * 2 - 1;
	}
}