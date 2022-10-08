public class Test {
	public static void main(String[] args) {
		System.out.println("enchants = {");
		for(Enchant value : Enchant.values()) {
			System.out.println("\t\"" + value.key + "\": {\n\t\t\"refs\": [\"" + value.refName + "\", \"temp\", \"temp\"],\n\t\t" +
					"\"display\": \"" + value.name().replaceAll("_", " ").toLowerCase() + "\"\n\t},");
		}
		System.out.println("}");

//		for(Enchant value : Enchant.values()) {
//			System.out.println(value.name().replaceAll("_", " ").toLowerCase());
//		}
	}

	public enum Enchant {

		BEAT_THE_SPAMMERS("bts", "melee_damage_vs_bows", false),
		BERSERKER("berserker", "melee_crit_midair", false),
		BILLIONAIRE("bill", "melee_literally_p2w", true),
		BOUNTY_REAPER("bountyreaper", "melee_damage_vs_bounties", false),
		BRUISER("bruiser", "increased_blocking", false),
		BULLET_TIME("bullettime", "blocking_cancels_projectiles", false),
		COMBO_DAMAGE("combodamage", "melee_combo_damage", false),
		COMBO_HEAL("comboheal", "melee_combo_heal", false),
		COMBO_PERUNS_WRATH("perun", "melee_lightning", true),
		COMBO_STUN("combostun", "melee_stun", true),
		COMBO_SWIFT("comoswift", "melee_combo_speed", false),
		COMBO_XP("comboxp", "combo_xp", false),
		COUNTER_JANITOR("counterjanitor", "resistance_on_kill", false),
		CRITICALLY_RICH("criticallyrich", "gold_per_crit", false),
		CRUSH("crush", "melee_weakness", false),
		DIAMOND_STOMP("diamondstomp", "melee_damage_vs_diamond", false),
		DUELIST("duelist", "melee_strike_after_block", false),
		EXECUTIONER("executioner", "melee_execute", true),
		FANCY_RAIDER("fancyraider", "melee_damage_vs_leather", false),
		GAMBLE("gamble", "melee_gamble", true),
		GOLD_AND_BOOSTED("gab", "melee_damage_when_absorption", false),
		GOLD_BOOST("goldboost", "gold_boost", false),
		GOLD_BUMP("goldbump", "gold_per_kill", false),
		GRASSHOPPER("grasshopper", "melee_damage_when_on_grass", false),
		GUTS("guts", "melee_heal_on_kill", false),
		HEALER("healer", "melee_healer", true),
		HEMORRHAGE("hemorrhage", "melee_bleed", true),
		KING_BUSTER("kb", "melee_damage_vs_high_hp", false),
		KNOCKBACK("knockback", "melee_knockback", true),
		LIFESTEAL("ls", "melee_heal_on_hit", false),
		MOCTEZUMA("moctezuma", "gold_strictly_kills", false),
		PAIN_FOCUS("painfocus", "melee_damage_when_low", false),
		PANTS_RADAR("pantsradar", "pants_radar", false),
		PITPOCKET("pitpocket", "pickpocket", false),
		PUNISHER("pun", "melee_damage_vs_low_hp", false),
		REVENGEANCE("revengeance", "melee_avenge", false),
		SHARK("shark", "melee_damage_when_close_low_players", false),
		SHARP("sharp", "plain_melee_damage", false),
		SIERRA("sierra", "gold_per_diamond_piece", false),
		SPAMMER_AND_PROUD("spammerandproud", "bow_spammer", false),
		SPEEDY_HIT("speedyhit", "melee_speed_on_hit", true),
		SPEEDY_KILL("speedykill", "etspeed_on_kill", false),
		SPRINT_DRAIN("sprintdrain", "bow_slow", false),
		STRIKE_GOLD("strikegold", "gold_per_hit", false),
		SWEATY("sweaty", "streak_xp", false),
		THE_PUNCH("punch", "melee_launch", true),
		XP_BOOST("xpboost", "xp_boost", false),
		XP_BUMP("xpbump", "xp_per_kill", false);

		private String refName;
		public String key;
		public boolean isRare;

		Enchant(String refName, String key, boolean isRare) {
			this.refName = refName;
			this.key = key;
			this.isRare = isRare;
		}
	}
}
