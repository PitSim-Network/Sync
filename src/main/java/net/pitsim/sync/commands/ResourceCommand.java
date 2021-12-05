package net.pitsim.sync.commands;

import dev.kyro.arcticapi.data.APlayer;
import dev.kyro.arcticapi.data.APlayerData;
import dev.kyro.arcticapi.misc.AOutput;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ResourceCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if(args.length > 0) {
            if(args[0].equals("toggle")) {
                APlayer aPlayer = APlayerData.getPlayerData(player);
                if(aPlayer.playerData.getBoolean("promptPack")) {
                    aPlayer.playerData.set("promptPack", false);
                    AOutput.send(player, "&cYou will no longer automatically receive the resource pack on join.");
                } else {
                    aPlayer.playerData.set("promptPack", true);
                    AOutput.send(player, "&aYou will now automatically receive the resource pack on join.");
                }
                aPlayer.save();
            } else {
                AOutput.error(player, "&cCorrect usage: /resource toggle");
            }
        } else player.setResourcePack("https://cdn.discordapp.com/attachments/803483152630677524/903075400442314772/PitSim.zip");



        return false;
    }
}
