package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import net.pitsim.sync.misc.Misc;
import net.pitsim.sync.misc.NumberFormatter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.List;

public class Solitude extends PitEnchant {

	public Solitude() {
		super("Solitude", true, ApplyType.PANTS,
				"solitude", "soli");
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;

		int enchantLvl = attackEvent.getDefenderEnchantLevel(this);
		if(enchantLvl == 0) return;

		int nearbyPlayers = 0;
		for(Entity nearby : attackEvent.defender.getNearbyEntities(7, 7, 7)) {
			if(!(nearby instanceof Player) || nearby == attackEvent.defender) continue;
			nearbyPlayers++;
		}
		if(nearbyPlayers > getMaxNearbyPlayers(enchantLvl)) return;
		attackEvent.decreasePercent += getDamageReduction(enchantLvl);
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Receive &9-" + Misc.roundString(getDamageReduction(enchantLvl)) + "% &7damage when &9" +
				NumberFormatter.convert(getMaxNearbyPlayers(enchantLvl)), "&7or less players are within 7", "&7blocks").getLore();
	}

	public int getMaxNearbyPlayers(int enchantLvl) {
		return Misc.linearEnchant(enchantLvl, 0.5, 1);
	}

	public double getDamageReduction(int enchantLvl) {
		return Math.min(30 + enchantLvl * 10, 100);
	}
}
