package io.github.baptistemht.mariocraft.game.player;

import io.github.baptistemht.mariocraft.MarioCraft;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private MarioCraft instance;
    private Map<UUID, PlayerData> datas;

    public PlayerManager(MarioCraft instance){
        this.instance = instance;
        datas = new HashMap<>();
    }

    public Map<UUID, PlayerData> getDatas() {
        return datas;
    }

    public PlayerData getPlayerData(UUID id){
        if(!datas.containsKey(id))return null;
        return datas.get(id);
    }

    public void insertPlayerData(UUID id){
        if(datas.containsKey(id))return;
        datas.put(id, new PlayerData());
    }
}
