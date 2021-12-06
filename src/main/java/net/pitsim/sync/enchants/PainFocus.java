package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.List;

public class PainFocus extends PitEnchant {

	public PainFocus() {
		super("Pain Focus", false, ApplyType.SWORDS,
				"painfocus", "pf", "pain-focus");
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;

		int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
		if(enchantLvl == 0) return;

		attackEvent.increasePercent += getDamage(attackEvent.attacker, enchantLvl) / 100;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Deal &c+" + getDamage(enchantLvl) + "% &7damage per &c\u2764", "&7you're missing").getLore();
	}

	public int getDamage(int enchantLvl) {
		return (int) Math.floor(Math.pow(enchantLvl, 1.5));
	}

	public double getDamage(Player player, int enchantLvl) {
		int missingHearts = (int) ((player.getMaxHealth() - player.getHealth()) / 2);
		return missingHearts * getDamage(enchantLvl);
	}
}
