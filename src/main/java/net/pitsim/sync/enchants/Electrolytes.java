package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import net.pitsim.sync.events.KillEvent;
import net.pitsim.sync.misc.Misc;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class Electrolytes extends PitEnchant {

	public Electrolytes() {
		super("Electrolytes", false, ApplyType.PANTS,
				"electrolytes", "electrolyte", "electro", "elec");
		isUncommonEnchant = true;
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;

		int attackerEnchantLevel = attackEvent.getAttackerEnchantLevel(this);
		for (PotionEffect effect : attackEvent.attacker.getActivePotionEffects()) {
			if(effect.getType().equals(PotionEffectType.SPEED) && effect.getAmplifier() == 3 && attackerEnchantLevel == 0 && effect.getDuration() > 85) {
				attackEvent.attacker.removePotionEffect(PotionEffectType.SPEED);
			}
		}
	}


	@EventHandler
	public void onKill(KillEvent killEvent) {
		int enchantLvl = killEvent.getKillerEnchantLevel(this);
		if(killEvent.killer.hasPotionEffect(PotionEffectType.SPEED)) {
			for(PotionEffect activePotionEffect : killEvent.killer.getActivePotionEffects()) {
				if(activePotionEffect.getType().equals(PotionEffectType.SPEED)) {
					if(activePotionEffect.getAmplifier() > 0) {
						if(activePotionEffect.getDuration() + (getSeconds(enchantLvl) * 20) / 2> getMaxSeconds(enchantLvl) * 20) {
						Misc.applyPotionEffect(killEvent.killer, PotionEffectType.SPEED,getMaxSeconds(enchantLvl) * 20,
								activePotionEffect.getAmplifier(), false, false);
						} else {
						Misc.applyPotionEffect(killEvent.killer, PotionEffectType.SPEED,(activePotionEffect.getDuration()
								+ (getSeconds(enchantLvl) * 20) / 2), activePotionEffect.getAmplifier(), false, false);
						}
					} else {
						if(activePotionEffect.getDuration() + (getSeconds(enchantLvl) * 20) > getMaxSeconds(enchantLvl) * 20) {
							Misc.applyPotionEffect(killEvent.killer, PotionEffectType.SPEED,getMaxSeconds(enchantLvl) * 20,
									activePotionEffect.getAmplifier(), false, false);
						} else {
							Misc.applyPotionEffect(killEvent.killer, PotionEffectType.SPEED,(int)
											activePotionEffect.getDuration() + (getSeconds(enchantLvl) * 20),
									activePotionEffect.getAmplifier(), false, false);
						}
					}
				}
			}
		}
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7If you have &eSpeed &7on kill, add", "&e" + getSeconds(enchantLvl) +
				" &7seconds to its duration.", "&7(Halved for Speed II+, Max " + getMaxSeconds(enchantLvl) + "s)").getLore();
	}

	public int getSeconds(int enchantLvl) {
		return enchantLvl * 2;
	}

	public int getMaxSeconds(int enchantLvl) {
		return 12 + (6 * enchantLvl);
	}
}
