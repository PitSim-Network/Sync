package net.pitsim.sync.enchants;

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

public class LastStand extends PitEnchant {

	public LastStand() {
		super("Last Stand", false, ApplyType.PANTS,
				"laststand", "last", "last-stand", "resistance");
		isUncommonEnchant = true;
	}

	@EventHandler
	public void onAttack(AttackEvent.Post attackEvent) {
		if(!canApply(attackEvent)) return;

		int enchantLvl = attackEvent.getDefenderEnchantLevel(this);
		if(enchantLvl == 0) return;

		if(attackEvent.defender.getHealth() - attackEvent.event.getFinalDamage() <= 6) {
			Cooldown cooldown = getCooldown(attackEvent.defender, 10 * 20);
			if(cooldown.isOnCooldown()) return; else cooldown.reset();
			Sounds.LAST_STAND.play(attackEvent.defender);
			Misc.applyPotionEffect(attackEvent.defender, PotionEffectType.DAMAGE_RESISTANCE, getSeconds(enchantLvl)
					* 20, getAmplifier(enchantLvl) - 1, true, false);
		}
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Gain &9Resistance " + AUtil.toRoman(getAmplifier(enchantLvl)) + " &7("
				+ getSeconds(enchantLvl) + " &7seconds)", "&7when reaching &c" + Misc.getHearts(6)).getLore();
	}

	public int getAmplifier(int enchantLvl) {
		return enchantLvl;
	}

	public int getSeconds(int enchantLvl) {
		return Misc.linearEnchant(enchantLvl, 0.5, 3);
	}
}
