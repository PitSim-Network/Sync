package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class PurpleGold extends PitEnchant {

	public PurpleGold() {
		super("Purple Gold", false, ApplyType.PANTS,
				"purplegold");
		isUncommonEnchant = true;
		isUseless = true;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		if(enchantLvl == 1) return new ALoreBuilder("&7Gain &6+" + getGold(enchantLvl) + "g &7from breaking", "&7obsidian").getLore();
		return new ALoreBuilder("&7Gain &6+" + getGold(enchantLvl) + "g &7and &cRegen III",
				"&7(" + getRegenSeconds(enchantLvl) + "s) from breaking obsidian").getLore();
	}

	public int getGold(int enchantLvl) {
		return enchantLvl * 4 + 3;
	}

	public int getRegenSeconds(int enchantLvl) {
		return enchantLvl;
	}
}