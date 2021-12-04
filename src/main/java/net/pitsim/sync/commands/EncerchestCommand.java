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
//        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
//
//        if(args.length > 0) {
//            player.sendMessage("Inventory:");
//            for(Map.Entry<Integer, ItemStack> integerItemStackEntry : pitPlayer.inventoryMystics.entrySet()) {
//                AUtil.giveItemSafely(player, integerItemStackEntry.getValue());
//            }
//            return false;
//        } else {
//            player.sendMessage("Ender Chest:");
//            for(Map.Entry<Integer, ItemStack> integerItemStackEntry : pitPlayer.enderchestMystics.entrySet()) {
//                AUtil.giveItemSafely(player, integerItemStackEntry.getValue());
//            }
//        }

//        HypixelPlayer hypixelPlayer = HypixelPlayer.getHypixelPlayer(player);
//
//        for(Map.Entry<Integer, Mystic> item : hypixelPlayer.enderchestMystics.entrySet()) {
//
//            Map<Enchant, Integer> enchantMap = item.getValue().enchantMap;
//            Map<PitEnchant, Integer> updatedEnchantMap = new HashMap<>();
//
//            for(Map.Entry<Enchant, Integer> enchant : enchantMap.entrySet()) {
//                for(PitEnchant pitEnchant : EnchantManager.pitEnchants) {
//                    if(pitEnchant.refNames.contains(enchant.getKey().getDisplayName())) {
//                        updatedEnchantMap.put(pitEnchant, enchant.getValue());
//                    }
//                }
//            }
//
//            if(updatedEnchantMap.size() > 0 && updatedEnchantMap.size() < 4) {
//                String mysticString = item.getValue().type.displayName.equals("Pants") ? item.getValue().color.refName : item.getValue().type.displayName;
//                ItemStack mystic = FreshCommand.getFreshItem(mysticString);
//
//                try {
//                    for(Map.Entry<PitEnchant, Integer> newEnchant : updatedEnchantMap.entrySet()) {
//                        mystic = EnchantManager.addEnchant(mystic, EnchantManager.getEnchant(newEnchant.getKey().refNames.get(0)), newEnchant.getValue(), false);
//                    }
//
//                } catch(Exception e) {
//                    e.printStackTrace();
//                }
//                AUtil.giveItemSafely(player, mystic);
//            }
//        }
        return false;
    }
}
