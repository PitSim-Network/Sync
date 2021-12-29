package net.pitsim.sync.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        OofCommand.INSTANCE.onCommand(sender, cmd, label, args);

//        Player player = (Player) sender;
//        player.teleport(MapManager.getLobbySpawn());

        return false;
    }
}
