package net.pitsim.sync.enums;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum PvpArena {
	JUNGLE("Jungle", "plugins/WorldEdit/schematics/jungle.schematic", new Location(Bukkit.getWorld("pvp"), 44, 1, 0, 0, 90),
			new Location(Bukkit.getWorld("pvp"), -36, 2, 0, 0, -90), new ItemStack(Material.SAPLING, 1, (short) 3)),
	SWAMP("Swamp", "plugins/WorldEdit/schematics/swamp.schematic", new Location(Bukkit.getWorld("pvp"), 36, 2, 0, 0, 90),
			new Location(Bukkit.getWorld("pvp"), -33, 2, 0, 0, -90), new ItemStack(Material.DEAD_BUSH)),
	OCEAN("Ocean", "plugins/WorldEdit/schematics/ocean.schematic", new Location(Bukkit.getWorld("pvp"), 30, 2, 0, 0, 90),
			new Location(Bukkit.getWorld("pvp"), -32, 2, 0, 0, -90), new ItemStack(Material.RAW_FISH, 1, (short) 3)),
	DESERT("Desert", "plugins/WorldEdit/schematics/desert.schematic", new Location(Bukkit.getWorld("pvp"), 31, 0, 0, 0, 90),
			new Location(Bukkit.getWorld("pvp"), -29, -1, 0, 0, -90), new ItemStack(Material.CACTUS));

	public String refName;
	public String schematicName;
	public Location player1Spawn;
	public Location player2Spawn;
	public ItemStack displayItem;

	PvpArena(String refName, String schematicName, Location player1Spawn, Location player2Spawn, ItemStack displayItem) {
		this.refName = refName;
		this.schematicName = schematicName;
		this.player1Spawn = player1Spawn;
		this.player2Spawn = player2Spawn;
		this.displayItem = displayItem;
	}
}


