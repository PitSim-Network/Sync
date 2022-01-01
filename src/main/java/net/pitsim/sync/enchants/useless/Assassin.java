package net.pitsim.sync.enchants.useless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.Cooldown;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.events.AttackEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.util.Vector;

import java.util.List;

public class Assassin extends PitEnchant {

	public Assassin() {
		super("Assassin", true, ApplyType.PANTS,
				"assassin");
	}

	@EventHandler
	public void onAttack(AttackEvent.Apply attackEvent) {
		if(!canApply(attackEvent)) return;

		int enchantLvl = attackEvent.getDefenderEnchantLevel(this);
		if(enchantLvl == 0 || !attackEvent.defender.isSneaking()) return;
		if(enchantLvl == 1 && attackEvent.arrow == null) return;

		Cooldown cooldown = getCooldown(attackEvent.defender, 20 * getCooldown(enchantLvl));
		if(cooldown.isOnCooldown()) return; else cooldown.reset();

		Location teleportLocation = attackEvent.attacker.getLocation()
				.subtract(attackEvent.attacker.getLocation().getDirection().setY(0).normalize());
		if(teleportLocation.getBlock().getType() != Material.AIR || teleportLocation.clone().add(0, 1, 0).getBlock().getType() != Material.AIR)
			teleportLocation = attackEvent.attacker.getLocation();
		attackEvent.defender.teleport(teleportLocation);
		attackEvent.defender.setVelocity(new Vector());
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		if(enchantLvl == 1) return new ALoreBuilder("&7Sneaking teleports you behind",
				"&7players bowing you. (" + getCooldown(enchantLvl) + "s cooldown)").getLore();
		return new ALoreBuilder("&7Sneaking teleports you behind your" , "&7attacker. (" + getCooldown(enchantLvl) + "s cooldown)").getLore();
	}

	public int getCooldown(int enchantLvl) {
		if(enchantLvl == 1) return 10;
		return Math.max(9 - enchantLvl * 2, 0);
	}
}