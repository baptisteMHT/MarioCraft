package io.github.baptistemht.mariocraft.game.player;

import io.github.baptistemht.mariocraft.MarioCraft;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private MarioCraft instance;
    private Map<UUID, PlayerData> data;

    public PlayerManager(MarioCraft instance){
        this.instance = instance;
        data = new HashMap<>();
    }

    public Map<UUID, PlayerData> getData() {
        return data;
    }

    public PlayerData getPlayerData(UUID id){
        if(!data.containsKey(id))return null;
        return data.get(id);
    }

    public void insertPlayerData(UUID id, PlayerState state){
        if(data.containsKey(id))return;
        data.put(id, new PlayerData(state));
    }
}
