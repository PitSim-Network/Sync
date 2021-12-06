package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.Cooldown;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import net.pitsim.sync.misc.Misc;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class SpeedyHit extends PitEnchant {

	public SpeedyHit() {
		super("Speedy Hit", true, ApplyType.SWORDS,
				"speedyhit", "speedy", "speed", "sh", "speedy-hit");
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;

		int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
		if(enchantLvl == 0) return;

		Cooldown cooldown = getCooldown(attackEvent.attacker,(getCooldown(enchantLvl) * 20));
		if(cooldown.isOnCooldown()) return; else cooldown.reset();

		Misc.applyPotionEffect(attackEvent.attacker, PotionEffectType.SPEED, getDuration(enchantLvl) * 20, 0, true, false);
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Gain Speed I for &e" + getDuration(enchantLvl) + "s &7on hit (" +
				getCooldown(enchantLvl) + "s", "&7cooldown)").getLore();
	}

	public int getDuration(int enchantLvl) {
		return enchantLvl * 2 + 3;
	}

	public int getCooldown(int enchantLvl) {
		return Math.max(4 - enchantLvl, 1);
	}
}
