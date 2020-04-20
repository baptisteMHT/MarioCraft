package io.github.baptistemht.mariocraft.util;

import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.game.player.PlayerData;
import io.github.baptistemht.mariocraft.game.player.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class GameUtils {

    public static boolean tpPlayerToLobby(Player p){
        p.teleport(MarioCraft.getInstance().getHub());
        return true;
    }

    public static boolean tpAllToLobby(){
        Location l = MarioCraft.getInstance().getHub();
        for(Player p : Bukkit.getOnlinePlayers()){
            p.teleport(l);
        }
        return true;
    }

    public static boolean tpPlayersToLobby(){
        Map<UUID, PlayerData> map = MarioCraft.getInstance().getPlayerManager().getData();
        Location l = MarioCraft.getInstance().getHub();
        for(UUID id : map.keySet()){
            if(map.get(id).getState() == PlayerState.PLAYER && Bukkit.getPlayer(id) != null){
                Bukkit.getPlayer(id).teleport(l);
            }
        }
        return true;
    }

    public static boolean tpSpectatorsToLobby(){
        Map<UUID, PlayerData> map = MarioCraft.getInstance().getPlayerManager().getData();
        Location l = MarioCraft.getInstance().getHub();
        for(UUID id : map.keySet()){
            if(map.get(id).getState() == PlayerState.SPECTATOR && Bukkit.getPlayer(id) != null){
                Bukkit.getPlayer(id).teleport(l);
            }
        }
        return true;
    }

}
