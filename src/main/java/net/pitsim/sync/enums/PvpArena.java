package net.pitsim.sync.enums;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public enum PvpArena {

	JUNGLE("Jungle", "plugins/WorldEdit/schematics/jungle.schematic", new Location(Bukkit.getWorld("pvp"), 0, 1, 36, -180, 0),
			new Location(Bukkit.getWorld("pvp"), 0, 2, -47, 0, 0)),
	SWAMP("Swamp", "plugins/WorldEdit/schematics/swamp.schematic", new Location(Bukkit.getWorld("pvp"), 0, 2, 40, -180, 0),
			new Location(Bukkit.getWorld("pvp"), 0, 2, -36, 0, 0)),
	OCEAN("Ocean", "plugins/WorldEdit/schematics/ocean.schematic", new Location(Bukkit.getWorld("pvp"), 0, 2, 33, -180, 0),
			new Location(Bukkit.getWorld("pvp"), 0, 2, -29, 0, 0)),
	DESERT("Desert", "plugins/WorldEdit/schematics/desert.schematic", new Location(Bukkit.getWorld("pvp"), 0, 3, 30, -180, 0),
			new Location(Bukkit.getWorld("pvp"), 0, -1, -32, 0, 0));


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


