package net.pitsim.sync.enchants;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.event.EventHandler;

import java.util.List;

public class GoldAndBoosted extends PitEnchant {

	public GoldAndBoosted() {
		super("Gold and Boosted", false, ApplyType.SWORDS,
				"gab", "gold-and-boosted", "goldandboosted");
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;

		int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
		if(enchantLvl == 0) return;

		EntityLiving nmsPlayer = ((CraftLivingEntity) attackEvent.attacker).getHandle();
		if(nmsPlayer.getAbsorptionHearts() == 0) return;
		attackEvent.increasePercent += getDamage(enchantLvl) / 100D;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Deal &c+" + getDamage(enchantLvl) + "% &7damage when you have", "&7absorption hearts").getLore();
	}

	public int getDamage(int enchantLvl) {
		return (int) (Math.floor(Math.pow(enchantLvl, 1.15) * 4) + 1);
	}
}
