package net.pitsim.sync.commands;

import dev.kyro.arcticapi.misc.AOutput;
import dev.kyro.arcticapi.misc.AUtil;
import lombok.SneakyThrows;
import net.pitsim.sync.controllers.CombatManager;
import net.pitsim.sync.controllers.DamageManager;
import net.pitsim.sync.controllers.EnchantManager;
import net.pitsim.sync.controllers.SpawnManager;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.controllers.objects.PitPlayer;
import net.pitsim.sync.events.AttackEvent;
import net.pitsim.sync.events.OofEvent;
import net.pitsim.sync.hypixel.Enchant;
import net.pitsim.sync.hypixel.HypixelPlayer;
import net.pitsim.sync.hypixel.Mystic;
import net.pitsim.sync.misc.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EncerchestCommand implements CommandExecutor {

    @SneakyThrows
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        HypixelPlayer hypixelPlayer = HypixelPlayer.getHypixelPlayer(player);

        for(Map.Entry<Integer, Mystic> item : hypixelPlayer.enderchestMystics.entrySet()) {

            Map<Enchant, Integer> enchantMap = item.getValue().enchantMap;
            Map<PitEnchant, Integer> updatedEnchantMap = new HashMap<>();

            for(Map.Entry<Enchant, Integer> enchant : enchantMap.entrySet()) {
                for(PitEnchant pitEnchant : EnchantManager.pitEnchants) {
                    if(pitEnchant.refNames.contains(enchant.getKey().getDisplayName())) {
                        updatedEnchantMap.put(pitEnchant, enchant.getValue());
                    }
                }
            }

            if(updatedEnchantMap.size() > 0 && updatedEnchantMap.size() < 4) {
                ItemStack mystic = FreshCommand.getFreshItem(item.getValue().type.displayName);

                try {
                    for(Map.Entry<PitEnchant, Integer> newEnchant : updatedEnchantMap.entrySet()) {
                        mystic = EnchantManager.addEnchant(mystic, EnchantManager.getEnchant(newEnchant.getKey().refNames.get(0)), newEnchant.getValue(), false);
                    }

                } catch(Exception e) {
                    e.printStackTrace();
                }
                AUtil.giveItemSafely(player, mystic);
            }
        }
        return false;
    }
}
