package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class HiddenJewelPants extends PitEnchant {

	public HiddenJewelPants() {
		super("Hidden Jewel", false, ApplyType.PANTS,
				"hiddenjewelpants");
		isUncommonEnchant = true;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Kill &c117 &7players to recycle", "&7into a Tier I sword with a Tier", "&7III enchant.", "&7Kills: &30").getLore();
	}
}