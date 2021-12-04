package net.pitsim.sync.enchants.useless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import dev.kyro.arcticapi.misc.AUtil;
import net.pitsim.sync.controllers.Cooldown;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import net.pitsim.sync.misc.Misc;
import net.pitsim.sync.misc.Sounds;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class zzUseless extends PitEnchant {

	public zzUseless() {
		super("NAME", false, ApplyType.SWORDS,
				"REFNAME");
		isUncommonEnchant = true;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {

		return new ALoreBuilder("&7More useless than Minikloon").getLore();
	}
}