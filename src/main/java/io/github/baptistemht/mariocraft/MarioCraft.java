package io.github.baptistemht.mariocraft;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import io.github.baptistemht.mariocraft.command.CollisionCommand;
import io.github.baptistemht.mariocraft.controller.EntityController;
import io.github.baptistemht.mariocraft.controller.listener.ControllerListeners;
import io.github.baptistemht.mariocraft.game.GameDifficulty;
import io.github.baptistemht.mariocraft.game.GameState;
import io.github.baptistemht.mariocraft.game.gui.GUIListeners;
import io.github.baptistemht.mariocraft.game.listener.GameListeners;
import io.github.baptistemht.mariocraft.game.player.PlayerManager;
import io.github.baptistemht.mariocraft.util.BoxUtils;
import io.github.baptistemht.mariocraft.world.WorldListeners;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class MarioCraft extends JavaPlugin {

    private GameDifficulty difficulty;
    private GameState gameState;
    private boolean collision;
    private Location hub;

    private List<Entity> boxes;

    private ProtocolManager protocolManager;
    private PlayerManager playerManager;

    private static MarioCraft instance;

    @Override
    public void onEnable() {
        instance = this;

        difficulty = GameDifficulty.NORMAL;
        gameState = GameState.INIT;
        collision = false;
        hub = new Location(getServer().getWorld("world"), 0, 60, 0); //TODO ADD CONFIG SUPPORT

        boxes = new ArrayList<>();

        protocolManager = ProtocolLibrary.getProtocolManager();
        playerManager = new PlayerManager(this);

        protocolManager.addPacketListener(new EntityController(this, ListenerPriority.HIGHEST, PacketType.Play.Client.STEER_VEHICLE));

        getServer().getPluginManager().registerEvents(new ControllerListeners(this), this);
        getServer().getPluginManager().registerEvents(new WorldListeners(), this);
        getServer().getPluginManager().registerEvents(new GUIListeners(), this);
        getServer().getPluginManager().registerEvents(new GameListeners(this), this);

        getCommand("collision").setExecutor(new CollisionCommand(this));

        gameState = GameState.PRE_GAME;
    }

    @Override
    public void onDisable() {
        BoxUtils.resetBoxes();
    }


    public List<Entity> getBoxes() {
        return boxes;
    }


    public boolean isCollision() {
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }


    public GameDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(GameDifficulty difficulty) {
        this.difficulty = difficulty;
    }


    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }


    public Location getHub() {
        return hub;
    }

    public void setHub(Location hub) {
        this.hub = hub;
    }


    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }


    public static MarioCraft getInstance() {
        return instance;
    }
}
