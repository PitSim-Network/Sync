package net.pitsim.sync.enchants.intentionaluseless;

import dev.kyro.arcticapi.builders.ALoreBuilder;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.enums.ApplyType;
import net.pitsim.sync.misc.Misc;

import java.util.List;

public class Instaboom extends PitEnchant {

	public Instaboom() {
		super("Instaboom", true, ApplyType.PANTS,
				"instaboom");
		isUseless = true;
	}

	@Override
	public List<String> getDescription(int enchantLvl) {
		return new ALoreBuilder("&7Spawn with &c" + getSpawnTNT(enchantLvl) + " Instaboom TNT&7. It",
				"&7explodes instantly and deals", "&c" + Misc.getHearts(getDamage(enchantLvl)) + " &7to enemies in a 4 block",
				"&7radius. gain &c+" + getKillTNT(enchantLvl) + " Instaboom TNT", "&7on kilil", "&7Consecutive uses &churt &7you!").getLore();
	}

	public double getDamage(int enchantLvl) {
		return enchantLvl * 0.5;
	}

	public int getSpawnTNT(int enchantLvl) {
		return Misc.linearEnchant(enchantLvl, 0.5, 0.5);
	}

	public int getKillTNT(int enchantLvl) {
		return Misc.linearEnchant(enchantLvl, 0.5, 0.5);
	}
}