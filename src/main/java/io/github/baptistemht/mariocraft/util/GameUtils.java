package io.github.baptistemht.mariocraft.util;

import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.game.player.PlayerManager;
import io.github.baptistemht.mariocraft.track.Track;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GameUtils {

    public static void tpPlayerToLobby(Player p){
        p.teleport(MarioCraft.getInstance().getHub());
    }

    public static void tpAllToLobby(){
        for(Player p : Bukkit.getOnlinePlayers()){
            p.leaveVehicle();
            tpPlayerToLobby(p);
        }
    }

    public static void tpAllToTrack(Track t){
        PlayerManager pm = MarioCraft.getInstance().getPlayerManager();

        Location[] positions = {t.getGrid(), null, null, null, null, null, null, null, null}; //can I make it better?

        for(int i = 0 ; i!=(pm.getData().size() -1) ; i++){

            int x = positions[i].getBlockX() - 3;
            int y = positions[i].getBlockY();
            int z = positions[i].getBlockZ() - 3;

            if(i == 2 || i == 5){
                z = z + 6;
            }

            positions[i+1] = new Location(t.getWorld(), x, y, z);
        }

        int i = 0;

        for(UUID id : pm.getData().keySet()){
            Player p = Bukkit.getPlayer(id);
            if(p != null){
                if(p.getGameMode() == GameMode.SPECTATOR){
                    p.teleport(positions[0].add(0, 10, 0));
                }else{
                    p.teleport(positions[i]);
                    i++;
                }
            }
        }
    }

}
