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

public class Crush extends PitEnchant {

	public Crush() {
		super("Crush", false, ApplyType.SWORDS,
				"crush");
		isUncommonEnchant = true;
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;

		int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
		if(enchantLvl == 0) return;

		Cooldown cooldown = getCooldown(attackEvent.attacker, 2 * 20);
		if(cooldown.isOnCooldown()) return; else cooldown.reset();

		Misc.applyPotionEffect(attackEvent.defender, PotionEffectType.WEAKNESS, getDuration(enchantLvl), enchantLvl + 3, true, false);
		Sounds.CRUSH.play(attackEvent.attacker);
		Sounds.CRUSH.play(attackEvent.defender);
	}

	@Override
	public List<String> getDescription(int enchantLvl) {

		return new ALoreBuilder("&7Strikes apply &cWeakness " + AUtil.toRoman(enchantLvl + 4), "&7(lasts " + (getDuration(enchantLvl) / 20D) +
				"s, 2s cooldown)").getLore();
	}

	public int getDuration(int enchantLvl) {

		return (int) (Math.floor(Math.pow(enchantLvl, 0.7) * 2 + enchantLvl / 2D) * 2);
	}
}
