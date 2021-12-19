package net.pitsim.sync.commands;

import dev.kyro.arcticapi.misc.AOutput;
import net.pitsim.sync.hypixel.Loadout;
import net.pitsim.sync.hypixel.LoadoutManager;
import net.pitsim.sync.misc.Misc;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StashCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        Loadout loadout = LoadoutManager.getLoadout(Misc.getUUID(player.getUniqueId()));
        for(ItemStack stashItem : loadout.stash) {
            player.getInventory().addItem(stashItem);
        }
        player.updateInventory();

        AOutput.broadcast("&c&lSTASH! &7Everyone thank &6" + player.getName() + " &7for being a stash streaker");

        return false;
    }
}
