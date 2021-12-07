package net.pitsim.sync.controllers.objects;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.world.DataException;
import dev.kyro.arcticapi.misc.AOutput;
import me.clip.placeholderapi.PlaceholderAPI;
import net.pitsim.sync.PitSim;
import net.pitsim.sync.controllers.DuelManager;
import net.pitsim.sync.controllers.RingCalc;
import net.pitsim.sync.enums.PvpArena;
import net.pitsim.sync.hypixel.Loadout;
import net.pitsim.sync.hypixel.PlayerDataManager;
import net.pitsim.sync.misc.Misc;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Match implements Listener {

    public Player player1;
    public Player player2;

    public int position;
    public RingCalc.XYCoords arenaCoordinates;
    public PvpArena arena;
    public BukkitTask runnable;

    public Match(Player player1, Player player2, PvpArena arena, int position) {
        this.player1 = player1;
        this.player2 = player2;
        this.arena = arena;
        this.position = position;
        this.arenaCoordinates = RingCalc.getPosInRing(position);

        DuelManager.matches.add(this);
    }

    public void onStart() {
        loadSchematic(new File("plugins/WorldEdit/schematics/clear.schematic"), new Location(Bukkit.getWorld("pvp"), arenaCoordinates.x, 80, arenaCoordinates.y));
        loadSchematic(new File(arena.schematicName), new Location(Bukkit.getWorld("pvp"), arenaCoordinates.x, 60, arenaCoordinates.y));

        Location player1Spawn = new Location(Bukkit.getWorld("pvp"),arenaCoordinates.x + arena.player1Spawn.getX(), 60 + arena.player1Spawn.getY(),
                arenaCoordinates.y + arena.player1Spawn.getZ(), arena.player1Spawn.getPitch(), arena.player1Spawn.getYaw());

        Location player2Spawn = new Location(Bukkit.getWorld("pvp"),arenaCoordinates.x + arena.player2Spawn.getX(), 60 + arena.player2Spawn.getY(),
                arenaCoordinates.y + arena.player2Spawn.getZ(), arena.player2Spawn.getPitch(), arena.player2Spawn.getYaw());

        player1.setHealth(player1.getMaxHealth());
        player2.setHealth(player2.getMaxHealth());

        PitPlayer pitPlayer1 = PitPlayer.getPitPlayer(player1);
        player1.teleport(player1Spawn);
        clearInventory(player1);

        Loadout player1Loadout = PlayerDataManager.getLoadout(player1.getUniqueId());
        giveDiamond(player1);

        if(player1Loadout.inventoryItemMap.size() > 0) {
            for(Map.Entry<Integer, ItemStack> integerItemStackEntry : player1Loadout.inventoryItemMap.entrySet()) {
                player1.getInventory().setItem(integerItemStackEntry.getKey(), integerItemStackEntry.getValue());
            }
        }


        PitPlayer pitPlayer2 = PitPlayer.getPitPlayer(player2);
        player2.teleport(player2Spawn);
        clearInventory(player2);

        Loadout player2Loadout = PlayerDataManager.getLoadout(player2.getUniqueId());
        giveDiamond(player2);

        if(player2Loadout.inventoryItemMap.size() > 0) {
            for(Map.Entry<Integer, ItemStack> integerItemStackEntry : player2Loadout.inventoryItemMap.entrySet()) {
                player2.getInventory().setItem(integerItemStackEntry.getKey(), integerItemStackEntry.getValue());
            }
        }

        startMessages(player1, player2);
    }

    public void onEnd(Player loser) {
        clearInventory(player1);
        clearInventory(player2);

        if(loser == player1) endMessages(player2, player1);
        else endMessages(player1, player2);

        loser.setGameMode(GameMode.SPECTATOR);

        Match thisMatch = this;
        new BukkitRunnable() {
            @Override
            public void run() {

                loser.setGameMode(GameMode.SURVIVAL);
                player1.teleport(Bukkit.getWorld("lobby").getSpawnLocation());
                player2.teleport(Bukkit.getWorld("lobby").getSpawnLocation());

                loadSchematic(new File("plugins/WorldEdit/schematics/clear.schematic"), new Location(Bukkit.getWorld("pvp"), arenaCoordinates.x, 80, arenaCoordinates.y));
                DuelManager.matches.remove(thisMatch);

                player1 = null;
                player2  =  null;
                arena = null;
                arenaCoordinates = null;
            }
        }.runTaskLater(PitSim.INSTANCE, 5 * 20L);
    }

    private void loadSchematic(File file, Location location) {
        WorldEditPlugin worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
        EditSession session = worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(location.getWorld()), 100000);
        try
        {
            CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(file).load(file);
            clipboard.rotate2D(90);
            clipboard.paste(session, new com.sk89q.worldedit.Vector(location.getX(), location.getY(), location.getZ()), false);
        }
        catch (MaxChangedBlocksException | DataException | IOException e)
        {
            e.printStackTrace();
        }
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

        AOutput.send(winner, "&8&m-------------------------");
        AOutput.send(winner, "&6&lMatch Results");
        AOutput.send(winner, "&aWinner: " + PlaceholderAPI.setPlaceholders(winner, winnerName) + " &c" + winner.getHealth()/2 + "\u2764");
        AOutput.send(winner, "&cLoser: " + PlaceholderAPI.setPlaceholders(loser, loserName) + " &c0\u2764");
        AOutput.send(winner, "&8&m-------------------------");

        AOutput.send(loser, "&8&m-------------------------");
        AOutput.send(loser, "&6&lMatch Results");
        AOutput.send(loser, "&aWinner: " + PlaceholderAPI.setPlaceholders(winner, winnerName) + " &c" + winner.getHealth()/2 + "\u2764");
        AOutput.send(loser, "&cLoser: " + PlaceholderAPI.setPlaceholders(loser, loserName) + " &c0\u2764");
        AOutput.send(loser, "&8&m-------------------------");
    }

    public void startMessages(Player player1, Player player2)  {
        final int[] i = {0};

        new BukkitRunnable() {
            @Override
            public void run() {
                if(i[0] > 5) this.cancel();

                Misc.sendSubTitle(player1, "&c&l" + (i[0] + 1), 20);
                Misc.sendSubTitle(player2, "&c&l" + (i[0] + 1), 20);

                i[0]++;
            }
        }.runTaskTimer(PitSim.INSTANCE, 0L, 1L);
    }


}
