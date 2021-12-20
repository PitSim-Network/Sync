package net.pitsim.sync.controllers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Listener;

public class MapManager implements Listener {

	public static World getLobby() {
		return Bukkit.getWorld("lobby");
	}

	public static Location getLobbySpawn() {
		return new Location(getLobby(), 0.5, 125, 0.5, 0, 0);
	}

	public static World getFFA() {
		return Bukkit.getWorld("ffa");
	}

	public static Location getFFASpawn() {
		return new Location(getFFA(), 0.5, 100, 0.5, 35, 50);
	}

	public static World getPvP() {
		return Bukkit.getWorld("pvp");
	}
}
