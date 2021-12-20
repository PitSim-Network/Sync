package net.pitsim.sync.controllers.objects;

import dev.kyro.arcticapi.misc.AOutput;
import me.clip.placeholderapi.PlaceholderAPI;
import net.pitsim.sync.PitSim;
import net.pitsim.sync.controllers.DuelManager;
import net.pitsim.sync.controllers.MapManager;
import net.pitsim.sync.controllers.RingCalc;
import net.pitsim.sync.enums.PvpArena;
import net.pitsim.sync.hypixel.Loadout;
import net.pitsim.sync.hypixel.LoadoutManager;
import net.pitsim.sync.misc.Countdown;
import net.pitsim.sync.misc.Misc;
import net.pitsim.sync.misc.SchematicPaste;
import net.pitsim.sync.misc.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.Map;

public class Match implements Listener {

    public Player player1;
    public Player player2;

    public int position;
    public RingCalc.XYCoords arenaCoordinates;
    public PvpArena arena;

    public boolean isStarting;

    public Match(Player player1, Player player2, PvpArena arena, int position) {
        this.player1 = player1;
        this.player2 = player2;
        this.arena = arena;
        this.position = position;
        this.arenaCoordinates = RingCalc.getPosInRing(position);

        DuelManager.matches.add(this);
    }

    public void onStart() {
        SchematicPaste.loadSchematic(new File("plugins/WorldEdit/schematics/clear.schematic"), new Location(MapManager.getPvP(), arenaCoordinates.x, 80, arenaCoordinates.y));
        SchematicPaste.loadSchematic(new File(arena.schematicName), new Location(MapManager.getPvP(), arenaCoordinates.x, 60, arenaCoordinates.y));

        Location player1Spawn = new Location(MapManager.getPvP(),arenaCoordinates.x + arena.player1Spawn.getX(), 60 + arena.player1Spawn.getY(),
                arenaCoordinates.y + arena.player1Spawn.getZ(), arena.player1Spawn.getPitch(), arena.player1Spawn.getYaw());

        Location player2Spawn = new Location(MapManager.getPvP(),arenaCoordinates.x + arena.player2Spawn.getX(), 60 + arena.player2Spawn.getY(),
                arenaCoordinates.y + arena.player2Spawn.getZ(), arena.player2Spawn.getPitch(), arena.player2Spawn.getYaw());

        player1.setHealth(player1.getMaxHealth());
        player2.setHealth(player2.getMaxHealth());

        PitPlayer pitPlayer1 = PitPlayer.getPitPlayer(player1);
        player1.teleport(player1Spawn);
        clearInventory(player1);

        Loadout player1Loadout = LoadoutManager.getLoadout(Misc.getUUID(player1.getUniqueId()));
        giveDiamond(player1);

        if(player1Loadout.inventoryItemMap.size() > 0) {
            for(Map.Entry<Integer, ItemStack> integerItemStackEntry : player1Loadout.inventoryItemMap.entrySet()) {
                player1.getInventory().setItem(integerItemStackEntry.getKey(), integerItemStackEntry.getValue());
            }
        }


        PitPlayer pitPlayer2 = PitPlayer.getPitPlayer(player2);
        player2.teleport(player2Spawn);
        clearInventory(player2);

        Loadout player2Loadout = LoadoutManager.getLoadout(Misc.getUUID(player2.getUniqueId()));
        giveDiamond(player2);

        if(player2Loadout.inventoryItemMap.size() > 0) {
            for(Map.Entry<Integer, ItemStack> integerItemStackEntry : player2Loadout.inventoryItemMap.entrySet()) {
                player2.getInventory().setItem(integerItemStackEntry.getKey(), integerItemStackEntry.getValue());
            }
        }

        startMessages(player1, player2);
    }

    public void onEnd(Player loser) {
        Player winner;
        if(loser == player1) winner = player2;
        else winner = player1;

        clearInventory(player1);
        clearInventory(player2);
        endMessages(winner, loser);
        winner.setHealth(winner.getMaxHealth());

        loser.setGameMode(GameMode.SPECTATOR);

        Sounds.CTF_FLAG_CAPTURED.play(loser);
        Sounds.LEVEL_UP.play(winner);

        Match thisMatch = this;
        new BukkitRunnable() {
            @Override
            public void run() {

                loser.setGameMode(GameMode.SURVIVAL);
                player1.teleport(MapManager.getLobbySpawn());
                player2.teleport(MapManager.getLobbySpawn());

                Misc.sendSubTitle(winner, "", 10);
                Misc.sendSubTitle(loser, "", 10);

                SchematicPaste.loadSchematic(new File("plugins/WorldEdit/schematics/clear.schematic"), new Location(Bukkit.getWorld("pvp"), arenaCoordinates.x, 80, arenaCoordinates.y));
                DuelManager.matches.remove(thisMatch);

            }
        }.runTaskLater(PitSim.INSTANCE, 5 * 20L);
    }

    public void onPluginDisable() {
        if(player1 != null) clearInventory(player1);
        if(player2 != null) clearInventory(player2);
        if(player1 != null) player1.teleport(MapManager.getLobbySpawn());
        if(player2 != null) player2.teleport(MapManager.getLobbySpawn());

        DuelManager.matches.remove(this);
    }

    public void onQuit(Player loser) {
        Player winner;
        if(loser == player1) winner = player2;
        else winner = player1;

        clearInventory(player1);
        clearInventory(player2);
        endMessages(winner, loser);
        winner.setHealth(winner.getMaxHealth());

        loser.setGameMode(GameMode.SPECTATOR);

        Sounds.CTF_FLAG_CAPTURED.play(loser);
        Sounds.LEVEL_UP.play(winner);
        winner.teleport(MapManager.getLobbySpawn());
        DuelManager.matches.remove(this);
        player1 = null;
        player2 = null;
    }

    public void clearInventory(Player player) {
        player.getInventory().clear();
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
    }

    public void giveDiamond(Player player) {
        player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
    }

    public void endMessages(Player winner, Player loser) {

        String winnerName = "%luckperms_prefix%" + winner.getName();
        String loserName = "%luckperms_prefix%" + loser.getName();

        Misc.sendTitle(winner, "&a&lVICTORY!", 60);
        Misc.sendSubTitle(winner, PlaceholderAPI.setPlaceholders(winner, winnerName) + " &7won the Match!", 60);

        Misc.sendTitle(loser, "&c&lDEFEAT!", 60);
        Misc.sendSubTitle(loser, PlaceholderAPI.setPlaceholders(winner, winnerName) + " &7won the Match!", 60);

        AOutput.send(winner, "&8&m-------------------------");
        AOutput.send(winner, "&6&lMatch Results");
        AOutput.send(winner, "&aWinner: " + PlaceholderAPI.setPlaceholders(winner, winnerName) + " &c" + Misc.round(winner.getHealth() / 2, 1) + "\u2764");
        AOutput.send(winner, "&cLoser: " + PlaceholderAPI.setPlaceholders(loser, loserName) + " &c0\u2764");
        AOutput.send(winner, "&8&m-------------------------");

        AOutput.send(loser, "&8&m-------------------------");
        AOutput.send(loser, "&6&lMatch Results");
        AOutput.send(loser, "&aWinner: " + PlaceholderAPI.setPlaceholders(winner, winnerName) + " &c" + Misc.round(winner.getHealth()/2, 1) + "\u2764");
        AOutput.send(loser, "&cLoser: " + PlaceholderAPI.setPlaceholders(loser, loserName) + " &c0\u2764");
        AOutput.send(loser, "&8&m-------------------------");
    }

    Match thisMatch = this;

    public void startMessages(Player player1, Player player2)  {
        player1.setPassenger(player1);
        player2.setPassenger(player2);
        isStarting = true;
        new Countdown(5, PitSim.INSTANCE) {

            @Override
            public void count(int current) {
                if(thisMatch.player1 != player1 || thisMatch.player2 != player2) return;
                if(current == 0) {
                    Misc.sendTitle(player1, "&a&l FIGHT!", 20);
                    Misc.sendTitle(player2, "&a&l FIGHT!", 20);
                    Sounds.SUCCESS.play(player1);
                    Sounds.SUCCESS.play(player2);
                    isStarting = false;
                } else {
                    Misc.sendTitle(player1, "", 20);
                    Misc.sendTitle(player2, "", 20);
                    Misc.sendTitle(player1, "&c&l" + current, 20);
                    Misc.sendTitle(player2, "&c&l" + current, 20);
                    Sounds.TIMER_TICK.play(player1);
                    Sounds.TIMER_TICK.play(player2);
                }
            }
        }.start();
    }





}
