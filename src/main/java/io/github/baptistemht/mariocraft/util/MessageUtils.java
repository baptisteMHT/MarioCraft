package io.github.baptistemht.mariocraft.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MessageUtils {

    public static void sendTitle(Player p, String title, String subtitle, int duration){
        p.sendTitle(title, subtitle, 2, duration, 2);
    }

    public static void sendTitle(UUID id, String title, String subtitle, int duration){
        Player p = Bukkit.getPlayer(id);
        if(p!=null) p.sendTitle(title, subtitle, 2, duration, 2);
    }

    public static void sendTitle(String title, String subtitle, int duration){
        for(Player p : Bukkit.getOnlinePlayers()){
            sendTitle(p, title, subtitle, duration);
        }
    }

    public static String getPrefix(){
        return ChatColor.YELLOW + "[" + ChatColor.RED + "MarioCraft" + ChatColor.YELLOW +  "] " + ChatColor.GRAY;
    }

}
