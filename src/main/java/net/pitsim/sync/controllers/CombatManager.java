package net.pitsim.sync.controllers;

import dev.kyro.arcticapi.data.APlayerData;
import dev.kyro.arcticapi.misc.AOutput;
import net.pitsim.sync.PitSim;
import net.pitsim.sync.controllers.objects.PitEnchant;
import net.pitsim.sync.controllers.objects.PitPlayer;
import net.pitsim.sync.events.AttackEvent;
import net.pitsim.sync.events.KillEvent;
import net.pitsim.sync.events.OofEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class CombatManager implements Listener {

    int combatTime = 15 * 20;

    public static HashMap<UUID, Integer> taggedPlayers = new HashMap<>();
    public static List<UUID> bannedPlayers = new ArrayList<>();

    static {

        new BukkitRunnable() {
            @Override
            public void run() {

                List<UUID> toRemove = new ArrayList<>();
                for(Map.Entry<UUID, Integer> entry : taggedPlayers.entrySet()) {
                    int time = entry.getValue();
                    time = time - 1;

                    if(time > 0) taggedPlayers.put(entry.getKey(), time);
                    else toRemove.add(entry.getKey());
                }

                for(UUID uuid : toRemove) {
                    taggedPlayers.remove(uuid);
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        if(player.getUniqueId().equals(uuid)) {
                            PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
                            pitPlayer.lastHitUUID = null;
                        }
                    }
                }

            }
        }.runTaskTimer(PitSim.INSTANCE, 0L, 1L);
    }

   @EventHandler
   public void onAttack(AttackEvent.Apply attackEvent) {
        Player attacker = attackEvent.attacker;
        Player defender = attackEvent.defender;

        taggedPlayers.put(attacker.getUniqueId(), combatTime);
        taggedPlayers.put(defender.getUniqueId(), combatTime);

   }

   @EventHandler
    public static void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        APlayerData.savePlayerData(event.getPlayer());
        event.getPlayer().closeInventory();


       PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
       UUID attackerUUID = pitPlayer.lastHitUUID;
       if(taggedPlayers.containsKey(player.getUniqueId())) {
           for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
               if(onlinePlayer.getUniqueId().equals(attackerUUID)) {

                   Map<PitEnchant, Integer> attackerEnchant = new HashMap<>();
                   Map<PitEnchant, Integer> defenderEnchant = new HashMap<>();
                   EntityDamageByEntityEvent ev = new EntityDamageByEntityEvent(onlinePlayer, player, EntityDamageEvent.DamageCause.CUSTOM, 0);
                   AttackEvent attackEvent = new AttackEvent(ev, attackerEnchant, defenderEnchant, false);


                   DamageManager.kill(attackEvent, onlinePlayer, player, false);
                   return;
               }
           }
           DamageManager.death(player);
       }


//        Player player = event.getPlayer();
//
//        if(taggedPlayers.containsKey(player.getUniqueId()) && !player.hasPermission("pitsim.combatlog") && !player.isOp()) {
//            player.teleport(Bukkit.getWorld("pit").getSpawnLocation());
//            taggedPlayers.remove(player.getUniqueId());
//
//            bannedPlayers.add(player.getUniqueId());
//            new BukkitRunnable() {
//                @Override
//                public void run() {
//                    bannedPlayers.remove(player.getUniqueId());
//                }
//            }.runTaskLater(PitSim.INSTANCE, 60 * 20);
//        }
   }

   @EventHandler
   public static void onDeath(KillEvent event) {
       taggedPlayers.remove(event.dead.getUniqueId());
       PitPlayer.getPitPlayer(event.dead).lastHitUUID = null;
   }

    @EventHandler
    public static void onOof(OofEvent event) {
        taggedPlayers.remove(event.getPlayer().getUniqueId());
        PitPlayer.getPitPlayer(event.getPlayer()).lastHitUUID = null;
    }

   @EventHandler
    public static void onCommandSend(PlayerCommandPreprocessEvent event) {

        List<String> BlockedCommands = new ArrayList<>();
        BlockedCommands.add("/ec");
        BlockedCommands.add("/echest");
        BlockedCommands.add("/enderchest");
        BlockedCommands.add("/perks");
        BlockedCommands.add("/spawn");


        if(taggedPlayers.containsKey(event.getPlayer().getUniqueId())) {
            for(String cmd : BlockedCommands) {
                if(cmd.equalsIgnoreCase(event.getMessage())) {
                    event.setCancelled(true);
                    AOutput.error(event.getPlayer(), "&c&c&lNOPE! &7You cannot use that while in combat!");
                }
            }
        }


   }
}
