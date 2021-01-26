package io.github.baptistemht.mariocraft.game.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private final Map<UUID, PlayerData> data;
    private final int pilots;
    private final int spectators;

    public PlayerManager(int pilots, int spectators){
        this.pilots = pilots;
        this.spectators = spectators;
        data = new HashMap<>();
    }


    public Map<UUID, PlayerData> getData() {
        return data;
    }

    public void addPlayer(UUID id){
        data.put(id, new PlayerData());
    }

    public void removePlayer(UUID id){
        data.remove(id);
    }


    public PlayerData getPlayer(UUID id){
        return data.get(id);
    }


    public int getPilotsLimit() {
        return pilots;
    }

    public int getSpectatorsLimit(){return spectators; }
}
