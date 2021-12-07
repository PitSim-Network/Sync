package net.pitsim.sync.enums;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public enum PvpArena {
	JUNGLE("Jungle", "plugins/WorldEdit/schematics/jungle.schematic", new Location(Bukkit.getWorld("pvp"), 44, 1, 0, 0, 90),
			new Location(Bukkit.getWorld("pvp"), -36, 2, 0, 0, -90)),
	SWAMP("Swamp", "plugins/WorldEdit/schematics/swamp.schematic", new Location(Bukkit.getWorld("pvp"), 36, 2, 0, 0, 90),
			new Location(Bukkit.getWorld("pvp"), -33, 2, 0, 0, -90)),
	OCEAN("Ocean", "plugins/WorldEdit/schematics/ocean.schematic", new Location(Bukkit.getWorld("pvp"), 30, 2, 0, 0, 90),
			new Location(Bukkit.getWorld("pvp"), -32, 2, 0, 0, -90)),
	DESERT("Desert", "plugins/WorldEdit/schematics/desert.schematic", new Location(Bukkit.getWorld("pvp"), 31, 0, 0, 0, 90),
			new Location(Bukkit.getWorld("pvp"), -29, -1, 0, 0, -90));

	public String refName;
	public String schematicName;
	public Location player1Spawn;
	public Location player2Spawn;

	PvpArena(String refName, String schematicName, Location player1Spawn, Location player2Spawn) {
		this.refName = refName;
		this.schematicName = schematicName;
		this.player1Spawn = player1Spawn;
		this.player2Spawn = player2Spawn;
	}
}


