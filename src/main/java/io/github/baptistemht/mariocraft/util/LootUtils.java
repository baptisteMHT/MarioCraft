package io.github.baptistemht.mariocraft.util;

import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.game.player.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class LootUtils {

    public static void bananaExecutor(Player sender){

    }

    public static void mushroomExecutor(Player sender){

    }

    public static void redShellExecutor(Player sender){

    }

    public static void greenShellExecutor(Player sender){

    }

    public static void squidExecutor(Player sender){
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
