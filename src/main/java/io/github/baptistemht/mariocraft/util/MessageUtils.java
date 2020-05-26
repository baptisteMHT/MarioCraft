package io.github.baptistemht.mariocraft.util;

import io.github.baptistemht.mariocraft.MarioCraft;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MessageUtils {

    public static void sendTitle(UUID id, String title, String subtitle, int duration){
        Player p = Bukkit.getPlayer(id);
        if(p != null) p.sendTitle(title, subtitle, 2, duration, 2);
    }

    public static void sendTitleToPlayers(String title, String subtitle, int duration){
        for(UUID id : MarioCraft.getInstance().getPlayerManager().getPlayersData().keySet()){
            sendTitle(id, title, subtitle, duration);
        }
    }

    public static String getPrefix(){
        return ChatColor.YELLOW + "[" + ChatColor.RED + "MarioCraft" + ChatColor.YELLOW +  "] " + ChatColor.GRAY;
    }

}
