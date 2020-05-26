package io.github.baptistemht.mariocraft.util;

import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.controller.ShellBrain;
import io.github.baptistemht.mariocraft.game.BoxLoot;
import io.github.baptistemht.mariocraft.game.player.PlayerManager;
import io.github.baptistemht.mariocraft.game.player.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.UUID;

public class LootUtils {

    public static void executeLootAction(Player sender, BoxLoot loot){
        switch (loot){
            case MUSHROOM:
                mushroomExecutor(sender);
                break;
            case BANANA:
                bananaExecutor(sender);
                break;
            case GREEN_SHELL:
                greenShellExecutor(sender);
                break;
            case RED_SHELL:
                redShellExecutor(sender);
                break;
            case SQUID:
                squidExecutor(sender);
                break;
        }
    }

    private static void mushroomExecutor(Player sender){
        final UUID id = sender.getUniqueId();
        final PlayerManager pm = MarioCraft.getInstance().getPlayerManager();
        new BukkitRunnable() {
            @Override
            public void run() {
                for(int i = 1000; i>0; i--){
                   pm.getPlayerData(id).setBoost(1 + i*0.0015);
                }
                pm.getPlayerData(id).setBoost(1.0);
            }
        }.runTaskAsynchronously(MarioCraft.getInstance());
    }

    private static void bananaExecutor(Player sender){

    }

    private static void greenShellExecutor(Player sender){
        new ShellBrain(
                sender.getWorld().spawnEntity(sender.getLocation(),
                EntityType.SILVERFISH),
                sender.getVehicle().getVelocity(),
                false,
                sender.getLocation().getYaw(),
                sender.getLocation().getPitch()
        );
    }

    private static void redShellExecutor(Player sender){
        new ShellBrain(
                sender.getWorld().spawnEntity(sender.getLocation(),
                EntityType.SILVERFISH),
                sender.getVehicle().getVelocity(),
                true,
                sender.getLocation().getYaw(),
                sender.getLocation().getPitch()
        );
    }

    private static void squidExecutor(Player sender){
        MarioCraft instance = MarioCraft.getInstance();

        PotionEffect effect = new PotionEffect(PotionEffectType.BLINDNESS, 40, 1, true, true);

        for(UUID id : instance.getPlayerManager().getData().keySet()){
            Player p = Bukkit.getPlayer(id);
            if(p == null)return;
            if(instance.getPlayerManager().getPlayerData(id).getState() == PlayerState.PLAYER && !p.getUniqueId().toString().equalsIgnoreCase(sender.getUniqueId().toString())){
                p.addPotionEffect(effect);
            }
        }

    }
}
