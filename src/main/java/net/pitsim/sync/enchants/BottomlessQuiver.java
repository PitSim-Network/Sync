package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BottomlessQuiver extends PitEnchant {

	public BottomlessQuiver() {
		super("Bottomless Quiver", false, ApplyType.BOWS,
				"bq", "bottomless-quiver", "bottom", "quiver");
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;

		int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
		if(enchantLvl == 0) return;

		if(attackEvent.attacker.equals(attackEvent.defender)) return;

		ItemStack arrows = new ItemStack(Material.ARROW);
		arrows.setAmount(getArrowAmount(enchantLvl));
		attackEvent.attacker.getInventory().addItem(arrows);
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Get &f" + getArrowAmount(enchantLvl) + " arrow" + (getArrowAmount(enchantLvl) == 1 ? "" : "s") + " &7on arrow hit").getLore();
	}

	public int getArrowAmount(int enchantLvl) {
		return (int) (Math.pow(enchantLvl, 1.9));
	}
}
