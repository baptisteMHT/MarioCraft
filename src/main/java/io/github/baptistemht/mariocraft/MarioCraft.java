package io.github.baptistemht.mariocraft;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import io.github.baptistemht.mariocraft.command.CollisionCommand;
import io.github.baptistemht.mariocraft.command.StartCommand;
import io.github.baptistemht.mariocraft.command.TrackCommand;
import io.github.baptistemht.mariocraft.controller.EntityController;
import io.github.baptistemht.mariocraft.controller.listener.ControllerListeners;
import io.github.baptistemht.mariocraft.game.GameDifficulty;
import io.github.baptistemht.mariocraft.game.GameState;
import io.github.baptistemht.mariocraft.game.gui.DifficultySelectorGUI;
import io.github.baptistemht.mariocraft.game.gui.GUIListeners;
import io.github.baptistemht.mariocraft.game.gui.TrackListGUI;
import io.github.baptistemht.mariocraft.game.gui.VehicleSelectorGUI;
import io.github.baptistemht.mariocraft.game.listener.GameListeners;
import io.github.baptistemht.mariocraft.game.player.PlayerManager;
import io.github.baptistemht.mariocraft.task.DifficultyVoteTask;
import io.github.baptistemht.mariocraft.track.TracksManager;
import io.github.baptistemht.mariocraft.util.BoxUtils;
import io.github.baptistemht.mariocraft.world.WorldListeners;
import org.bukkit.*;
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
    private List<GameDifficulty> votes;

    private DifficultySelectorGUI difficultySelectorGUI;
    private VehicleSelectorGUI vehicleSelectorGUI;
    private TrackListGUI trackListGUI;

    private ProtocolManager protocolManager;
    private PlayerManager playerManager;
    private TracksManager tracksManager;

    private static MarioCraft instance;

    @Override
    public void onEnable() {
        instance = this;

        difficulty = GameDifficulty.NORMAL;
        gameState = GameState.INIT;
        collision = false;
        hub = new Location(getServer().getWorld("world"), getServer().getWorld("world").getSpawnLocation().getX(), getServer().getWorld("world").getSpawnLocation().getY(), getServer().getWorld("world").getSpawnLocation().getZ());

        votes = new ArrayList<>();
        boxes = new ArrayList<>();

        difficultySelectorGUI = new DifficultySelectorGUI();
        vehicleSelectorGUI = new VehicleSelectorGUI();
        trackListGUI = new TrackListGUI();

        protocolManager = ProtocolLibrary.getProtocolManager();
        playerManager = new PlayerManager(this, 9, 11);
        tracksManager = new TracksManager(this);

        protocolManager.addPacketListener(new EntityController(this, ListenerPriority.HIGHEST, PacketType.Play.Client.STEER_VEHICLE));

        getServer().getPluginManager().registerEvents(new ControllerListeners(this), this);
        getServer().getPluginManager().registerEvents(new WorldListeners(), this);
        getServer().getPluginManager().registerEvents(new GUIListeners(this), this);
        getServer().getPluginManager().registerEvents(new GameListeners(this), this);

        getCommand("collision").setExecutor(new CollisionCommand(this));
        getCommand("start").setExecutor(new StartCommand(this));
        getCommand("reloadtracks").setExecutor(new TrackCommand(this));

        tracksManager.loadTracks();

        gameState = GameState.PRE_GAME;
    }

    @Override
    public void onDisable() {
        BoxUtils.resetBoxes();
    }


    public void setupSequence(){
        gameState = GameState.GAME;

        new DifficultyVoteTask(this);
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


    public List<GameDifficulty> getVotes() {
        return votes;
    }


    public DifficultySelectorGUI getDifficultySelectorGUI() {
        return difficultySelectorGUI;
    }

    public VehicleSelectorGUI getVehicleSelectorGUI() {
        return vehicleSelectorGUI;
    }

    public TrackListGUI getTrackListGUI() {
        return trackListGUI;
    }


    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public TracksManager getTracksManager() {
        return tracksManager;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }


    public static MarioCraft getInstance() {
        return instance;
    }
}
