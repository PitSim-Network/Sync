package net.pitsim.sync.commands;

import dev.kyro.arcticapi.misc.AOutput;
import net.pitsim.sync.controllers.CombatManager;
import net.pitsim.sync.controllers.DamageManager;
import net.pitsim.sync.controllers.SpawnManager;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.controllers.objects.PitPlayer;
import net.pitsim.sync.events.AttackEvent;
import net.pitsim.sync.events.OofEvent;
import net.pitsim.sync.misc.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OofCommand implements CommandExecutor {
    public static OofCommand INSTANCE;

    public OofCommand() {
        INSTANCE = this;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if(SpawnManager.isInSpawn(player.getLocation())) {

            AOutput.send(player, "&c&lNOPE! &7Cant /oof in spawn!");
            Sounds.ERROR.play(player);
        } else {

            if(!CombatManager.taggedPlayers.containsKey(player.getUniqueId())) {
                DamageManager.death(player);
                OofEvent oofEvent = new OofEvent(player);
                Bukkit.getPluginManager().callEvent(oofEvent);
                return false;
            }

            PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
            UUID attackerUUID = pitPlayer.lastHitUUID;
            for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if(onlinePlayer.getUniqueId().equals(attackerUUID)) {

                    Map<PitEnchant, Integer> attackerEnchant = new HashMap<>();
                    Map<PitEnchant, Integer> defenderEnchant = new HashMap<>();
                    EntityDamageByEntityEvent ev = new EntityDamageByEntityEvent(onlinePlayer, player, EntityDamageEvent.DamageCause.CUSTOM, 0);
                    AttackEvent attackEvent = new AttackEvent(ev, attackerEnchant, defenderEnchant, false);


                    DamageManager.kill(attackEvent, onlinePlayer, player, false);
                    return false;
                }
            }
            DamageManager.death(player);
            OofEvent oofEvent = new OofEvent(player);
            Bukkit.getPluginManager().callEvent(oofEvent);
        }
        return false;
    }
}
