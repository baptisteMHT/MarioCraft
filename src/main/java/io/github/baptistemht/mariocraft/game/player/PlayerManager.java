package io.github.baptistemht.mariocraft.game.player;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private final Map<UUID, PlayerData> data;
    private int playerLimit;

    public PlayerManager(int playerLimit){
        this.playerLimit = playerLimit;
        data = new HashMap<>();
    }

    public Map<UUID, PlayerData> getData() {
        return data;
    }

    public PlayerData getPlayerData(UUID id){
        return data.get(id);
    }

    public void insertPlayerData(UUID id, PlayerState state){
        if(data.containsKey(id))return;
        data.put(id, new PlayerData(state));
    }

    public PlayerData getDataFromPlayer(Player p){
        for(UUID id : data.keySet()){
            if(p.getUniqueId().toString().equalsIgnoreCase(id.toString())) return data.get(id);
        }
        return null;
    }

    public Map<UUID, PlayerData> getPlayersData(){
        Map<UUID, PlayerData> d = new HashMap<>();
        for(UUID id : data.keySet()){
            if(data.get(id).getState() == PlayerState.PLAYER) d.put(id, data.get(id));
        }
        return d;
    }

    public Map<UUID, PlayerData> getSpecsData() {
        Map<UUID, PlayerData> d = new HashMap<>();
        for (UUID id : data.keySet()) {
            if (data.get(id).getState() == PlayerState.SPECTATOR) d.put(id, data.get(id));
        }
        return d;
    }


    public int getPlayerLimit() {
        return playerLimit;
    }

    public void setPlayerLimit(int playerLimit) {
        this.playerLimit = playerLimit;
    }
}
