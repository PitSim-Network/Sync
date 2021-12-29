package net.pitsim.sync.enchants.useless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;

import java.util.List;

public class DangerClose extends PitEnchant {

	public DangerClose() {
		super("Danger Close", false, ApplyType.PANTS,
				"dangerclose");
		isUncommonEnchant = true;
		isUseless = true;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7More useless than Minikloon").getLore();
	}
}