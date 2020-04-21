package io.github.baptistemht.mariocraft.util;

import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.game.BoxLoot;
import io.github.baptistemht.mariocraft.game.player.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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

    }

    private static void bananaExecutor(Player sender){

    }

    private static void greenShellExecutor(Player sender){

    }

    private static void redShellExecutor(Player sender){

    }

    private static void squidExecutor(Player sender){
        MarioCraft instance = MarioCraft.getInstance();

        PotionEffect effect = new PotionEffect(PotionEffectType.BLINDNESS, 60, 1, true, true);

        for(UUID id : instance.getPlayerManager().getData().keySet()){
            Player p = Bukkit.getPlayer(id);
            if(p == null)return;
            if(instance.getPlayerManager().getPlayerData(id).getState() == PlayerState.PLAYER && !p.getUniqueId().toString().equalsIgnoreCase(sender.getUniqueId().toString())){
                p.addPotionEffect(effect);
            }
        }

    }
}
