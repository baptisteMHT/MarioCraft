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

    public void addPlayer(UUID id){
        data.put(id, new PlayerData());
    }

    public void removePlayer(UUID id){
        data.remove(id);
    }


    public PlayerData getPlayer(UUID id){
        return data.get(id);
    }


    public void setPlayerLimit(int playerLimit) {
        this.playerLimit = playerLimit;
    }

    public int getPlayerLimit() {
        return playerLimit;
    }
}
