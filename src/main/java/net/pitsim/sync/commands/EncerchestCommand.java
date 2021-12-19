package net.pitsim.sync.commands;

import lombok.SneakyThrows;
import net.pitsim.sync.controllers.InventoryManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EncerchestCommand implements CommandExecutor {

    @SneakyThrows
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        InventoryManager.getInventory(player).openInventory();
        return false;
    }
}
