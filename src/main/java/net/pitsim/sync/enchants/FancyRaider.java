package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class FancyRaider extends PitEnchant {

	public FancyRaider() {
		super("Fancy Raider", false, ApplyType.SWORDS,
				"fancyraider");
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;

		int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
		if(enchantLvl == 0) return;

		for(ItemStack armorContent : attackEvent.defender.getInventory().getArmorContents()) {
			if(!(armorContent.getType() == Material.LEATHER_HELMET || armorContent.getType() == Material.LEATHER_CHESTPLATE
					|| armorContent.getType() == Material.LEATHER_LEGGINGS || armorContent.getType() == Material.LEATHER_BOOTS))
				return;
		}

		attackEvent.increasePercent += getDamage(enchantLvl);
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Deal &c+" + getDamage(enchantLvl) + "&7").getLore();
	}

	public int getDamage(int enchantLvl) {
		if(enchantLvl == 1) return 5;
		return enchantLvl * 7 - 5;
	}
}