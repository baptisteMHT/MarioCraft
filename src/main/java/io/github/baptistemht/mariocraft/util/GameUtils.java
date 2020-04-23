package io.github.baptistemht.mariocraft.util;

import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.game.player.PlayerData;
import io.github.baptistemht.mariocraft.game.player.PlayerManager;
import io.github.baptistemht.mariocraft.game.player.PlayerState;
import io.github.baptistemht.mariocraft.track.Track;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
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
        Location l = MarioCraft.getInstance().getHub();
        for(UUID id : MarioCraft.getInstance().getPlayerManager().getPlayersData().keySet()){
            Bukkit.getPlayer(id).teleport(l);
        }
        return true;
    }

    public static boolean tpSpectatorsToLobby(){
        Location l = MarioCraft.getInstance().getHub();
        for(UUID id : MarioCraft.getInstance().getPlayerManager().getSpecsData().keySet()){
            Bukkit.getPlayer(id).teleport(l);
        }
        return true;
    }

    public static boolean tpAllToTrack(Track t){
        PlayerManager pm = MarioCraft.getInstance().getPlayerManager();

        List<Location> positions = new ArrayList<>();

        positions.add(t.getGrid().add(0, 10, 0));

        for(int i = 0 ; i!=(pm.getPlayersData().size() -1) ; i++){

            int x = positions.get(i).getBlockX() - 3;
            int y = positions.get(i).getBlockY();
            int z = positions.get(i).getBlockZ() - 3;

            if(i == 2 || i == 5){
                z = z + 6;
            }

            positions.add(new Location(t.getWorld(), x, y, z));
        }

        int i = 0;

        for(UUID id : pm.getData().keySet()){
            PlayerData data = pm.getPlayerData(id);
            if(data.getState() == PlayerState.SPECTATOR){
                Bukkit.getPlayer(id).teleport(positions.get(0));
            }else{
                Bukkit.getPlayer(id).teleport(positions.get(i));
                i++;
            }
        }

        return true;
    }

}
