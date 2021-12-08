package net.pitsim.sync.commands;

import dev.kyro.arcticapi.misc.AOutput;
import me.clip.placeholderapi.PlaceholderAPI;
import net.pitsim.sync.controllers.DuelManager;
import net.pitsim.sync.enums.PvpArena;
import net.pitsim.sync.inventories.DuelGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class DuelCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if(args.length < 1) {
            AOutput.error(player,"&cInvalid args. /duel <player>");
            return false;
        }

        if(args[0].equalsIgnoreCase("decline"))  {
            if(args.length < 2)  {
                AOutput.error(player,"&cInvalid args. /duel decline <player>");
                return false;
            }
            String playerName = "%luckperms_prefix%" + player.getName();
            String playerString = args[1];
            Player duelPlayer = null;
            for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if(onlinePlayer.getName().equalsIgnoreCase(playerString)) duelPlayer = onlinePlayer;
            }
            for(Map.Entry<Player, Player> playerPlayerEntry : DuelManager.requests.entrySet()) {
                if(playerPlayerEntry.getKey() == duelPlayer && playerPlayerEntry.getValue() == player) {
                    DuelManager.requests.remove(player);
                    DuelManager.requests.remove(duelPlayer);
                    DuelManager.requestArenas.remove(duelPlayer);
                    AOutput.send(duelPlayer, PlaceholderAPI.setPlaceholders(player, playerName) + " &chas declined your duel request!");
                    return false;
                }
            }
            AOutput.error(player, "You do not have a pending duel request from this person. Maybe it has expired?");
            return false;
        }

        if(DuelManager.getMatch(player) != null) {
            AOutput.error(player,"&cYou cannot do this while in a match!");
            return false;
        }

        if(args[0].equalsIgnoreCase("accept"))  {
            if(args.length < 2)  {
                AOutput.error(player,"&cInvalid args. /duel accept <player>");
                return false;
            }
            String playerString =  args[1];
            Player duelPlayer = null;
            for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if(onlinePlayer.getName().equalsIgnoreCase(playerString)) duelPlayer = onlinePlayer;
            }
            for(Map.Entry<Player, Player> playerPlayerEntry : DuelManager.requests.entrySet()) {
                if(playerPlayerEntry.getKey() == duelPlayer && playerPlayerEntry.getValue() == player) {
                    DuelManager.requests.remove(player);
                    DuelManager.requests.remove(duelPlayer);
                    DuelManager.createDuel(duelPlayer, player, DuelManager.requestArenas.get(duelPlayer));
                    DuelManager.requestArenas.remove(duelPlayer);
                    return false;
                }
            }
            AOutput.error(player, "You do not have a pending duel request from this person. Maybe it has expired?");
            return false;
        }

        if(DuelManager.requests.containsKey(player)) {
            AOutput.error(player,"&cYou already have a pending duel request!");
            return false;
        }

        String playerString =  args[0];
        Player duelPlayer = null;


        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if(onlinePlayer.getName().equalsIgnoreCase(playerString)) duelPlayer = onlinePlayer;
        }


        if(duelPlayer == null) {
            AOutput.error(player,"&cThat player is not online!");
            return false;
        }

        if(DuelManager.getMatch(duelPlayer) != null) {
            AOutput.error(player,"&cThat player is currently in a match!");
            return false;
        }

        DuelGUI duelGUI = new DuelGUI(player, duelPlayer);
        duelGUI.open();

        return false;
    }

    public static void initiateDuel(Player player, Player duelPlayer, PvpArena arena) {
        if(duelPlayer == null) {
            AOutput.error(player,"&cThat player is not online!");
            return;
        }

        if(DuelManager.getMatch(duelPlayer) != null) {
            AOutput.error(player,"&cThat player is currently in a match!");
            return;
        }

        if(DuelManager.getMatch(player) != null) {
            AOutput.error(player,"&cYou cannot do this while in a match!");
            return;
        }

        player.closeInventory();
        DuelManager.requestDuel(player, duelPlayer, arena);
    }
}
