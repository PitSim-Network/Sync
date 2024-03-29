package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class DiamondAllergy extends PitEnchant {

	public DiamondAllergy() {
		super("Diamond Allergy", false, ApplyType.PANTS,
				"da", "dallergy", "diamondallergy", "diamond-allergy");
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;

		int enchantLvl = attackEvent.getDefenderEnchantLevel(this);
		if(enchantLvl == 0) return;

		ItemStack weapon = attackEvent.attacker.getItemInHand();
		if(weapon == null) return;
		if(weapon.getType() != Material.DIAMOND_SWORD && weapon.getType() != Material.DIAMOND_SPADE) return;

		attackEvent.decreasePercent += getReduction(enchantLvl);
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Receive &9-" + getReduction(enchantLvl) + "% &7damage from", "&7diamond weapons").getLore();
	}

	public int getReduction(int enchantLvl) {
		return enchantLvl * 10;
	}
}
