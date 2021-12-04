package net.pitsim.sync.controllers;

import net.pitsim.sync.PitSim;
import net.pitsim.sync.controllers.objects.PitPerk;

import java.util.ArrayList;
import java.util.List;

public class PerkManager {

	public static List<PitPerk> pitPerks = new ArrayList<>();

	public static void registerUpgrade(PitPerk pitPerk) {

		pitPerks.add(pitPerk);
		PitSim.INSTANCE.getServer().getPluginManager().registerEvents(pitPerk, PitSim.INSTANCE);
	}

}
