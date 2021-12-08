package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class GomrawsHeart extends PitEnchant {

	public GomrawsHeart() {
		super("Gomraw's Heart", true, ApplyType.PANTS,
				"gomrawsheart");
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		if(enchantLvl == 1) return new ALoreBuilder("&7Regain all &c\u2764 &7when out of", "&7combat").getLore();
		return new ALoreBuilder("&7Regain all &c\u2764 &7when out of", "&7combat for 15 seconds. Gain",
				"&cRegen IV &7(" + getSeconds(enchantLvl) + "s) when entering", "&7combat").getLore();
	}

	public int getSeconds(int enchantLvl) {
		return enchantLvl - 1;
	}
}