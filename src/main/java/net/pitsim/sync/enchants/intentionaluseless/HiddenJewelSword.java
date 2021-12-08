package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class HiddenJewelSword extends PitEnchant {

	public HiddenJewelSword() {
		super("Hidden Jewel", false, ApplyType.SWORDS,
				"hiddenjewelsword");
		isUncommonEnchant = true;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Kill &c117 &7players to recycle", "&7into Tier I pants with a Tier III", "&7enchant.", "&7Kills: &30").getLore();
	}
}