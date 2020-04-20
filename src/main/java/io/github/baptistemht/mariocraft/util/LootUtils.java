package io.github.baptistemht.mariocraft.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
        PotionEffect effect = new PotionEffect(PotionEffectType.BLINDNESS, 60, 1, true, true);

        for(Player p : Bukkit.getOnlinePlayers()){
            //if(p.getUniqueId().toString().equalsIgnoreCase(sender.getUniqueId().toString()))return;
            p.addPotionEffect(effect);
        }
    }
}
