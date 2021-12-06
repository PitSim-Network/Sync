package net.pitsim.sync.commands;

import dev.kyro.arcticapi.misc.AOutput;
import net.pitsim.sync.controllers.DuelManager;
import net.pitsim.sync.enums.PvpArena;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DuelCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if(args.length < 2) {
            AOutput.error(player,"&cInvalid args. /duel <player> <map>");
            return false;
        }

        String playerString =  args[0];
        Player duelPlayer = null;

        String arenaString =  args[1];
        PvpArena arena = null;

        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if(onlinePlayer.getName().equalsIgnoreCase(playerString)) duelPlayer = onlinePlayer;
        }

        for(PvpArena value : PvpArena.values()) {
            if(value.refName.equalsIgnoreCase(arenaString)) arena = value;
        }

        if(duelPlayer == null || arena == null) {
            AOutput.error(player,"&cInvalid args. /duel <player> <map>");
            return false;
        }

        DuelManager.createDuel(player, duelPlayer, arena);

        return false;
    }
}
