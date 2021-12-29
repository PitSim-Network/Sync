package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.text.DecimalFormat;
import java.util.List;

public class SelfCheckout extends PitEnchant {

	public SelfCheckout() {
		super("Self-checkout", false, ApplyType.PANTS,
				"selfcheckout");
		isUncommonEnchant = true;
		isUseless = true;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		DecimalFormat decimalFormat = new DecimalFormat("#,##0");
		return new ALoreBuilder("&7Upon reaching a &65,000g", "&7bounty, clear it and gain",
				"&6+" + decimalFormat.format(getGold(enchantLvl)) + "&7. Consumes 1 life of", "&7this item").getLore();
	}

	public int getGold(int enchantLvl) {
		if(enchantLvl == 1) return 2000;
		return enchantLvl * 2000 - 1000;
	}
}