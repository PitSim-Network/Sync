package net.pitsim.sync.enums;

import de.tr7zw.nbtapi.NBTItem;
import net.pitsim.sync.controllers.EnchantManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public enum SpecialItem {
	GHELM("ghelm"),
	ARCH_CHEST("arch"),
	ARMA_BOOTS("armas"),

	PROT_HELMET("helmet"),
	PROT_CHESTPLATE("chestplate"),
	PROT_LEGGINGS("leggings"),
	PROT_BOOTS("boots");

	public String refName;

	SpecialItem(String refName) {
		this.refName = refName;
	}

	public ItemStack getItem() {
		switch(this) {
			case GHELM:
				ItemStack gHelmStack = new ItemStack(Material.GOLD_HELMET);
				NBTItem nbtGHelm = new NBTItem(gHelmStack);
				nbtGHelm.setBoolean(NBTTag.IS_SPECIAL.getRef(), true);
				nbtGHelm.setBoolean(NBTTag.IS_GHELM.getRef(), true);
				nbtGHelm.setString(NBTTag.PIT_NONCE.getRef(), GHELM.refName);
				EnchantManager.setItemLore(nbtGHelm.getItem());
				return nbtGHelm.getItem();
			case ARCH_CHEST:
				ItemStack archStack = new ItemStack(Material.DIAMOND_CHESTPLATE);
				NBTItem nbtArch = new NBTItem(archStack);
				nbtArch.setBoolean(NBTTag.IS_SPECIAL.getRef(), true);
				nbtArch.setBoolean(NBTTag.IS_ARCH.getRef(), true);
				nbtArch.setString(NBTTag.PIT_NONCE.getRef(), ARCH_CHEST.refName);
				EnchantManager.setItemLore(nbtArch.getItem());
				return nbtArch.getItem();
			case ARMA_BOOTS:
				ItemStack armasStack = new ItemStack(Material.LEATHER_BOOTS);
				NBTItem nbtArmas = new NBTItem(armasStack);
				nbtArmas.setBoolean(NBTTag.IS_SPECIAL.getRef(), true);
				nbtArmas.setBoolean(NBTTag.IS_ARMAS.getRef(), true);
				nbtArmas.setString(NBTTag.PIT_NONCE.getRef(), ARMA_BOOTS.refName);
				EnchantManager.setItemLore(nbtArmas.getItem());
				return nbtArmas.getItem();
			case PROT_HELMET:
				ItemStack helmetStack = new ItemStack(Material.DIAMOND_HELMET);
				helmetStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
				NBTItem nbtHelmet = new NBTItem(helmetStack);
				nbtHelmet.setBoolean(NBTTag.IS_SPECIAL.getRef(), true);
				nbtHelmet.setString(NBTTag.PIT_NONCE.getRef(), PROT_HELMET.refName);
				return nbtHelmet.getItem();
			case PROT_CHESTPLATE:
				ItemStack chestplateStack = new ItemStack(Material.DIAMOND_CHESTPLATE);
				chestplateStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
				NBTItem nbtChestplate = new NBTItem(chestplateStack);
				nbtChestplate.setBoolean(NBTTag.IS_SPECIAL.getRef(), true);
				nbtChestplate.setString(NBTTag.PIT_NONCE.getRef(), PROT_CHESTPLATE.refName);
				return nbtChestplate.getItem();
			case PROT_LEGGINGS:
				ItemStack leggingsStack = new ItemStack(Material.DIAMOND_LEGGINGS);
				leggingsStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
				NBTItem nbtLeggings = new NBTItem(leggingsStack);
				nbtLeggings.setBoolean(NBTTag.IS_SPECIAL.getRef(), true);
				nbtLeggings.setString(NBTTag.PIT_NONCE.getRef(), PROT_LEGGINGS.refName);
				return nbtLeggings.getItem();
			case PROT_BOOTS:
				ItemStack bootsStack = new ItemStack(Material.DIAMOND_BOOTS);
				bootsStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
				NBTItem nbtBoots = new NBTItem(bootsStack);
				nbtBoots.setBoolean(NBTTag.IS_SPECIAL.getRef(), true);
				nbtBoots.setString(NBTTag.PIT_NONCE.getRef(), PROT_BOOTS.refName);
				return nbtBoots.getItem();
		}
		return null;
	}

	public static SpecialItem getItem(String refName) {
		for(SpecialItem value : values()) {
			if(value.refName.equals(refName.toLowerCase())) return value;
		}
		return null;
	}
}
