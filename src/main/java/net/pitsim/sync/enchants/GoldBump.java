package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class GoldBump extends PitEnchant {

	public GoldBump() {
		super("Gold Bump", false, ApplyType.ALL,
				"goldbump", "gold-bump", "bump", "gbump");
		levelStacks = true;
	}

//	@EventHandler
//	public void onKill(KillEvent killEvent) {
//
//		int enchantLvl = killEvent.getKillerEnchantLevel(this);
//		if(enchantLvl == 0) return;
//
//		killEvent.goldReward += getGoldIncrease(enchantLvl);
//	}

	@Override
	public List<String> getDescription(int enchantLvl) {

		return new ALoreBuilder("&7Earn &6+" + getGoldIncrease(enchantLvl) + "g &7per kill").getLore();
	}

	public int getGoldIncrease(int enchantLvl) {

		return enchantLvl * 4;
	}
}
