package net.pitsim.sync.hypixel;

import net.pitsim.sync.controllers.EnchantManager;
import net.pitsim.sync.controllers.objects.PitEnchant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public enum Enchant {

//	ARROW_ARMORY("Arrow Armory", "damage_per_arrow", false),
//	ASSASSIN("Assassin", "sneak_teleport", true),
	BEAT_THE_SPAMMERS("bts", "melee_damage_vs_bows", false),
//	BERSERKER("Berserker", "melee_crit_midair", false),
	BILLIONAIRE("bill", "melee_literally_p2w", true),
//	BILLY("Billy", "less_damage_when_high_bounty", false),
	BOO_BOO("boo-boo", "passive_health_regen", false),
//	BOTTOMLESS_QUIVER("Bottomless Quiver", "gain_arrows_on_hit", false),
//	BOUNTY_REAPER("Bounty Reaper", "melee_damage_vs_bounties", false),
//	BRUISER("Bruiser", "increased_blocking", false),
	BULLET_TIME("bullettime", "blocking_cancels_projectiles", false),
	CHIPPING("chipping", "arrow_true_damage", false),
//	CLUB_ROD("Club Rod", "fishing_rod_enchant", false),
	COMBO_DAMAGE("combodamage", "melee_combo_damage", false),
	COMBO_HEAL("comboheal", "melee_combo_heal", false),
	COMBO_PERUNS_WRATH("perun", "melee_lightning", true),
	COMBO_STUN("combostun", "melee_stun", true),
	COMBO_SWIFT("comoswift", "melee_combo_speed", false),
//	COMBO_XP("Combo: XP", "combo_xp", false),
//	COUNTER_JANITOR("Counter-Janitor", "resistance_on_kill", false),
	COUNTER_OFFENSIVE("counteroffensive", "speed_when_hit_few_times", false),
//	CREATIVE("Creative", "wood_blocks", false),
//	CRICKET("Cricket", "less_damage_on_grass", false),
	CRITICALLY_FUNKY("criticallyfunky", "power_against_crits", false),
//	CRITICALLY_RICH("Critically Rich", "gold_per_crit", false),
	CRUSH("Crush", "crush", false),
//	DANGER_CLOSE("Danger Close", "superspeed_when_low", false),
//	DAVID_AND_GOLIATH("David and Goliath", "less_damage_vs_bounties", false),
//	DEVIL_CHICKS("Devil Chicks!", "explosive_chickens", true),
//	DIAMOND_ALLERGY("Diamond Allergy", "less_damage_vs_diamond_weapons", false),
	DIAMOND_STOMP("diamondstomp", "melee_damage_vs_diamond", false),
//	DIVINE_MIRACLE("Divine Miracle", "chance_dont_lose_life", true),
//	DOUBLE_JUMP("Double-jump", "double_jump", true),
//	DUELIST("Duelist", "melee_strike_after_block", false),
//	EGGS("Eggs", "eggs", false),
	ELECTROLYTES("electrolytes", "refresh_speed_on_kill", false),
//	ESCAPE_POD("Escape Pod", "escape_pod", true),
//	EVIL_WITHIN("Evil Within", "evil_within", false),
//	EXCESS("Excess", "overheal_enchant", false),
	EXECUTIONER("executioner", "melee_execute", true),
	EXPLOSIVE("explosive", "explosive_bow", true),
//	FANCY_RAIDER("Fancy Raider", "melee_damage_vs_leather", false),
	FASTER_THAN_THEIR_SHADOW("fasterthantheirshadow", "bow_combo_speed", false),
//	FIRST_SHOT("First Shot", "first_shot", false),
	FLETCHING("fletch", "bow_damage", false),
	FRACTIONAL_RESERVE("fractionalreserve", "fractional_reserve", false),
	GAMBLE("gamble", "melee_gamble", true),
	GOLDEN_HEART("goldenheart", "absorption_on_kill", false),
	GOLD_AND_BOOSTED("gab", "melee_damage_when_absorption", false),
	GOLD_BOOST("goldboost", "gold_boost", false),
	GOLD_BUMP("goldbump", "gold_per_kill", false),
//	GOMRAWS_HEART("Gomraw's Heart", "regen_when_ooc", true),
	GOTTA_GO_FAST("gotta-go-fast", "perma_speed", false),
//	GRANDMASTER("Grandmaster", "rod_true_damage", false),
//	GRASSHOPPER("Grasshopper", "melee_damage_when_on_grass", false),
//	GUARDIAN("Guardian", "guardian", false),
	GUTS("guts", "melee_heal_on_kill", false),
	HEALER("healer", "melee_healer", true),
	HEARTS("hearts", "higher_max_hp", false),
//	HEMORRHAGE("Hemorrhage", "melee_bleed", true),
//	HIDDEN_JEWEL("Hidden Jewel", "hidden_jewel", false),
//	MELE_HIDDEN_JEWEL("Hidden Jewel", "melee_hidden_jewel", false),
//	HUNT_THE_HUNTER("Hunt the Hunter", "counter_bounty_hunter", false),
//	INSTABOOM("Instaboom", "instaboom_tnt", true),
//	JUMPSPAMMER("Jumpspammer", "jump_spammer", false),
	KING_BUSTER("kb", "melee_damage_vs_high_hp", false),
//	KNOCKBACK("Knockback", "melee_knockback", true),
	LAST_STAND("laststand", "resistance_when_low", false),
	LIFESTEAL("ls", "melee_heal_on_hit", false),
//	LODBROK("Lodbrok", "increase_armor_drops", false),
	LUCKY_SHOT("luckyshot", "lucky_shot", true),
//	LUCK_OF_THE_POND("Luck of the Pond", "luck_of_the_pond", false),
//	MARTYRDOM("Martyrdom", "martyrdom", true),
//	MCSWIMMER("McSwimmer", "less_damage_when_swimming", false),
	MEGA_LONGBOW("megalongbow", "instant_shot", true),
//	MIND_ASSAULT("Mind Assault", "mind_assault", false),
	MIRROR("mirror", "immune_true_damage", false),
//	MIXED_COMBAT("Mixed Combat", "mixed_combat", false),
	MOCTEZUMA("moctezuma", "gold_strictly_kills", false),
//	NEEDLESS_SUFFERING("Needless Suffering", "needless_suffering", false),
//	NEGOTIATOR("Negotiator", "contract_rewards", false),
	NOT_GLADIATOR("notglad", "less_damage_nearby_players", false),
	PAIN_FOCUS("painfocus", "melee_damage_when_low", false),
//	PANTS_RADAR("Pants Radar", "pants_radar", false),
//	PAPARAZZI("Paparazzi", "paparazzi", true),
	PARASITE("parasite", "parasite", false),
//	PEBBLE("Pebble", "increase_gold_pickup", false),
	PEROXIDE("pero", "regen_when_hit", false),
//	PHOENIX("Phoenix", "phoenix", true),
	PIN_DOWN("pindown", "pin_down", false),
//	PITPOCKET("Pitpocket", "pickpocket", false),
//	PIT_BLOB("Pit Blob", "the_blob", true),
//	PIT_MBA("Pit MBA", "pit_mba", false),
	PRICK("prick", "thorns", false),
	PROTECTION("prot", "damage_reduction", false),
	PULLBOW("pullbow", "pullbow", true),
	PUNISHER("pun", "melee_damage_vs_low_hp", false),
//	PURPLE_GOLD("Purple Gold", "gold_break_obsidian", false),
	PUSH_COMES_TO_SHOVE("pushcomestoshove", "punch_once_in_a_while", false),
//	RESPAWN_ABSORPTION("Respawn: Absorption", "respawn_with_absorption", false),
//	RESPAWN_RESISTANCE("Respawn: Resistance", "respawn_with_resistance", false),
	RETRO_GRAVITY_MICROCOSM("rgm", "rgm", true),
//	REVENGEANCE("Revengeance", "melee_avenge", false),
//	REVITALIZE("Revitalize", "regen_speed_when_low", false),
	RING_ARMOR("ring", "less_damage_from_arrows", false),
	ROBINHOOD("robinhood", "homing", true),
//	RODBACK("Rodback", "fishing_rod_kb", false),
//	ROGUE("Rogue", "rogue", false),
//	ROYALTY("Royalty", "royalty", false),
//	SANGUISUGE("Sanguisuge", "sanguisuge", false),
//	SELF_CHECKOUT("Self-checkout", "max_bounty_self_claim", false),
	SHARK("shark", "melee_damage_when_close_low_players", false),
	SHARP("sharp", "plain_melee_damage", false),
//	SIERRA("Sierra", "gold_per_diamond_piece", false),
//	SINGULARITY("Singularity", "singularity", true),
//	SNIPER("Sniper", "sniper", false),
//	SNOWBALLS("Snowballs", "snowballs", true),
//	SNOWMEN_ARMY("Snowmen Army", "snowmen", true),
	SOLITUDE("solitude", "solitude", true),
//	SOMBER("Somber", "somber", false),
//	SPAMMER_AND_PROUD("Spammer and Proud", "bow_spammer", false),
	SPEEDY_HIT("speedyhit", "melee_speed_on_hit", true),
//	SPEEDY_KILL("Speedy Kill", "etspeed_on_kill", false),
	SPRINT_DRAIN("sprintdrain", "bow_slow", false),
//	STEAKS("Steaks", "steaks_on_kill", false),
//	STRIKE_GOLD("Strike Gold", "gold_per_hit", false),
//	SWEATY("Sweaty", "streak_xp", false), //RE-ADD
	TELEBOW("telebow", "telebow", true),
//	THE_PUNCH("The Punch", "melee_launch", true),
//	TNT("TNT", "tnt", false),
//	TRUE_SHOT("True Shot", "bow_to_true_damage", true),
	VOLLEY("volley", "volley", true),
	WASP("wasp", "bow_weakness_on_hit", false),
//	WHAT_DOESNT_KILL_YOU("What doesn't kill you", "heal_on_shoot_self", false),
//	WOLF_PACK("Wolf Pack", "wolf_pack", true),
//	XP_BOOST("XP Boost", "xp_boost", false),
	XP_BUMP("xpbump", "xp_per_kill", false),

	STEREO("stereo", "stereo", true),
//	TOUGH_CREW("Tough Crew", "tough_crew", false),
//	TROPHY("Trophy", "trophy", false),
//	UNITE("Unite", "fishers_unite", false),
//	PORTABLE_POND("Portable Pond", "water_bucket", false),

//	TRASH_PANDA("Trash Panda", "trash_panda", true),
//	WORM("Worm", "worm", false),
//	AEGIS("Aegis", "aegis", false),

//	LYCANTHROPY("Lycanthropy", "lycanthropy", false),
//	COMBO_VENOM("Combo: Venom", "venom", true),
//	NOSTALGIA("Nostalgia", "nostalgia", true),
//	SPITE("Spite", "spite", false),
//	GOLDEN_HANDCUFFS("Golden Handcuffs", "golden_handcuffs", true),
//	MISERY("Misery", "misery", false),
//	GRIM_REAPER("Grim Reaper", "grim_reaper", false),
//	HEDGE_FUND("Hedge Fund", "hedge_fund", false),
//	HEARTRIPPER("Heartripper", "heartripper", true),

	REGULARITY("regularity", "regularity", true),
//	BREACHING_CHARGE("Combo: Breaching Charge", "breaching_charge", false),
	HEIGH_HO("heighho", "heigh_ho", false),
	REALLY_TOXIC("reallytoxic", "really_toxic", false),
	NEW_DEAL("newdeal", "new_deal", false),
//	DO_IT_LIKE_THE_FRENCH("Do it like the French", "do_it_like_the_french", false),

//	Guess
	SYBLE("", "", true),
	THINK_OF_THE_PEOPLE("Think of the People", "think_of_the_people", true);

	private String displayName;
	public String key;
	public boolean isRare;

	Enchant(String displayName, String key, boolean isRare) {
		this.displayName = displayName;
		this.key = key;
		this.isRare = isRare;
	}

	public static Enchant getEnchant(String key) {

		for(Enchant enchant : values()) {
			if(enchant.key.equalsIgnoreCase(key)) return enchant;
		}

		return null;
	}

	public String getDisplayName() {

		return (isRare ? "" : "") + displayName;
	}

	public static void sort() {
		List<PitEnchant> enchants = EnchantManager.pitEnchants;
		List<String> strings = new ArrayList<>();

		enchants.forEach(enchant -> strings.add(enchant.refNames.get(0)));
		Collections.sort(strings);

		strings.forEach(System.out::println);

	}
}
