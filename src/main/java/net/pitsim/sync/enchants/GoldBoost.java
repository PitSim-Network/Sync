package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class GoldBoost extends PitEnchant {

	public GoldBoost() {
		super("Gold Boost", false, ApplyType.ALL,
				"goldboost", "gold-boost", "gboost", "boost");
		levelStacks = true;
	}

//	@EventHandler
//	public void onKill(KillEvent killEvent) {
//
//		int enchantLvl = killEvent.getKillerEnchantLevel(this);
//		if(enchantLvl == 0) return;
//
//		killEvent.goldMultipliers.add((getGoldIncrease(enchantLvl) / 100D) + 1);
//	}

	@Override
	public List<String> getDescription(int enchantLvl) {

		return new ALoreBuilder("&7Earn &6+" + getGoldIncrease(enchantLvl) + "% gold (g) &7from kills").getLore();
	}

	public int getGoldIncrease(int enchantLvl) {

		return enchantLvl * 15;
	}
}
