package net.pitsim.sync.controllers.objects;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.world.DataException;
import net.pitsim.sync.PitSim;
import net.pitsim.sync.controllers.DuelManager;
import net.pitsim.sync.controllers.RingCalc;
import net.pitsim.sync.enums.PvpArena;
import net.pitsim.sync.hypixel.HypixelPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Match implements Listener {

    public Player player1;
    public Player player2;

    public int position;
    public RingCalc.XYCoords arenaCoordinates;
    public PvpArena arena;

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

        
        
        PitPlayer pitPlayer1 = PitPlayer.getPitPlayer(player1);
        player1.getInventory().clear();
        player1.teleport(player1Spawn);

        if(pitPlayer1.savedInventroy.size() > 0) {
            for(Map.Entry<Integer, ItemStack> integerItemStackEntry : pitPlayer1.savedInventroy.entrySet()) {
                player1.getInventory().setItem(integerItemStackEntry.getKey(), integerItemStackEntry.getValue());
            }
        } else {
            for(Map.Entry<Integer, ItemStack> integerItemStackEntry : HypixelPlayer.getHypixelPlayer(pitPlayer1.dataUUID).inventoryItems.entrySet()) {
                player1.getInventory().setItem(integerItemStackEntry.getKey(), integerItemStackEntry.getValue());
            }
        }


        PitPlayer pitPlayer2 = PitPlayer.getPitPlayer(player2);
        player2.teleport(player2Spawn);
        player2.getInventory().clear();

        if(pitPlayer2.savedInventroy.size() > 0) {
            for(Map.Entry<Integer, ItemStack> integerItemStackEntry : pitPlayer2.savedInventroy.entrySet()) {
                player2.getInventory().setItem(integerItemStackEntry.getKey(), integerItemStackEntry.getValue());
            }
        } else {
            for(Map.Entry<Integer, ItemStack> integerItemStackEntry : HypixelPlayer.getHypixelPlayer(pitPlayer2.dataUUID).inventoryItems.entrySet()) {
                player2.getInventory().setItem(integerItemStackEntry.getKey(), integerItemStackEntry.getValue());
            }
        }

    }

    public void onEnd(Player loser) {
        player1.getInventory().clear();
        player2.getInventory().clear();

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


}
