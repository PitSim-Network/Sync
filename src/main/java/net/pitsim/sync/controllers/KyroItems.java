package net.pitsim.sync.controllers;

import net.pitsim.sync.commands.FreshCommand;
import net.pitsim.sync.enums.MysticType;
import net.pitsim.sync.enums.PantColor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class KyroItems {
	public static List<ItemStack> items = new ArrayList<>();

	static {
		try {
			ItemStack perun = FreshCommand.getFreshItem(MysticType.SWORD, null);
			perun = EnchantManager.addEnchant(perun, EnchantManager.getEnchant("strikegold"), 1, false);
			perun = EnchantManager.addEnchant(perun, EnchantManager.getEnchant("perun"), 3, false);
			perun = EnchantManager.addEnchant(perun, EnchantManager.getEnchant("ls"), 2, false);
			items.add(perun);
			
			ItemStack regs = FreshCommand.getFreshItem(MysticType.PANTS, PantColor.RAGE);
			regs = EnchantManager.addEnchant(regs, EnchantManager.getEnchant("reg"), 3, false);
			regs = EnchantManager.addEnchant(regs, EnchantManager.getEnchant("booboo"), 3, false);
			items.add(regs);

			ItemStack tele = FreshCommand.getFreshItem(MysticType.BOW, null);
			tele = EnchantManager.addEnchant(tele, EnchantManager.getEnchant("bottomlessquiver"), 2, false);
			tele = EnchantManager.addEnchant(tele, EnchantManager.getEnchant("firstshot"), 1, false);
			tele = EnchantManager.addEnchant(tele, EnchantManager.getEnchant("tele"), 3, false);
			items.add(tele);

			ItemStack mlb = FreshCommand.getFreshItem(MysticType.BOW, null);
			mlb = EnchantManager.addEnchant(mlb, EnchantManager.getEnchant("strikegold"), 1, false);
			mlb = EnchantManager.addEnchant(mlb, EnchantManager.getEnchant("mixedcombat"), 1, false);
			mlb = EnchantManager.addEnchant(mlb, EnchantManager.getEnchant("mlb"), 3, false);
			items.add(mlb);

			ItemStack pullDrain = FreshCommand.getFreshItem(MysticType.BOW, null);
			pullDrain = EnchantManager.addEnchant(pullDrain, EnchantManager.getEnchant("spammerandproud"), 3, false);
			pullDrain = EnchantManager.addEnchant(pullDrain, EnchantManager.getEnchant("pullbow"), 2, false);
			pullDrain = EnchantManager.addEnchant(pullDrain, EnchantManager.getEnchant("drain"), 3, false);
			items.add(pullDrain);

			ItemStack chls = FreshCommand.getFreshItem(MysticType.SWORD, null);
			chls = EnchantManager.addEnchant(chls, EnchantManager.getEnchant("ls"), 3, false);
			chls = EnchantManager.addEnchant(chls, EnchantManager.getEnchant("ch"), 3, false);
			items.add(chls);

			ItemStack rgmdagheart = FreshCommand.getFreshItem(MysticType.PANTS, PantColor.ORANGE);
			rgmdagheart = EnchantManager.addEnchant(rgmdagheart, EnchantManager.getEnchant("diamondallergy"), 3, false);
			rgmdagheart = EnchantManager.addEnchant(rgmdagheart, EnchantManager.getEnchant("gheart"), 3, false);
			rgmdagheart = EnchantManager.addEnchant(rgmdagheart, EnchantManager.getEnchant("rgm"), 2, false);
			items.add(rgmdagheart);

			ItemStack regcf = FreshCommand.getFreshItem(MysticType.PANTS, PantColor.RAGE);
			regcf = EnchantManager.addEnchant(regcf, EnchantManager.getEnchant("reg"), 3, false);
			regcf = EnchantManager.addEnchant(regcf, EnchantManager.getEnchant("cf"), 2, false);
			items.add(regcf);

			ItemStack megapin = FreshCommand.getFreshItem(MysticType.BOW, null);
			megapin = EnchantManager.addEnchant(megapin, EnchantManager.getEnchant("sweaty"), 2, false);
			megapin = EnchantManager.addEnchant(megapin, EnchantManager.getEnchant("mlb"), 2, false);
			megapin = EnchantManager.addEnchant(megapin, EnchantManager.getEnchant("pin"), 3, false);
			items.add(megapin);
		} catch(Exception ignored) {
			ignored.printStackTrace();
		}
	}
}
