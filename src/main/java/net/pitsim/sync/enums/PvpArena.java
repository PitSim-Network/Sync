package net.pitsim.sync.enums;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public enum PvpArena {

	JUNGLE("Jungle", "plugins/WorldEdit/schematics/jungle.schematic", new Location(Bukkit.getWorld("pvp"), 36, 1, 0, -180, 0),
			new Location(Bukkit.getWorld("pvp"), -47, 2, 0, 0, 0)),
	SWAMP("Swamp", "plugins/WorldEdit/schematics/swamp.schematic", new Location(Bukkit.getWorld("pvp"), 40, 2, 0, -180, 0),
			new Location(Bukkit.getWorld("pvp"), -36, 2, 0, 0, 0)),
	OCEAN("Ocean", "plugins/WorldEdit/schematics/ocean.schematic", new Location(Bukkit.getWorld("pvp"), 33, 2, 0, -180, 0),
			new Location(Bukkit.getWorld("pvp"), -29, 2, 0, 0, 0)),
	DESERT("Desert", "plugins/WorldEdit/schematics/desert.schematic", new Location(Bukkit.getWorld("pvp"), 30, 3, 0, -180, 0),
			new Location(Bukkit.getWorld("pvp"), -32, -1, 0, 0, 0));


	public String refName;
	public String schematicName;
	public Location player1Spawn;
	public Location player2Spawn;


	PvpArena(String refName, String schematicName, Location player2Spawn, Location player1Spawn) {
		this.refName = refName;
		this.schematicName = schematicName;
		this.player1Spawn = player1Spawn;
		this.player2Spawn = player2Spawn;
	}
}


