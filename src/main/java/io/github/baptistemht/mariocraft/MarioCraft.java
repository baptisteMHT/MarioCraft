package io.github.baptistemht.mariocraft;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import io.github.baptistemht.mariocraft.command.StartCommand;
import io.github.baptistemht.mariocraft.controller.EntityController;
import io.github.baptistemht.mariocraft.game.GameDifficulty;
import io.github.baptistemht.mariocraft.game.GameState;
import io.github.baptistemht.mariocraft.game.gui.DifficultySelectorGUI;
import io.github.baptistemht.mariocraft.game.gui.GUIListeners;
import io.github.baptistemht.mariocraft.game.gui.TrackListGUI;
import io.github.baptistemht.mariocraft.game.gui.VehicleSelectorGUI;
import io.github.baptistemht.mariocraft.game.listener.GameListeners;
import io.github.baptistemht.mariocraft.game.player.PlayerManager;
import io.github.baptistemht.mariocraft.task.DifficultyVoteTask;
import io.github.baptistemht.mariocraft.track.Track;
import io.github.baptistemht.mariocraft.track.TracksManager;
import io.github.baptistemht.mariocraft.util.BoxUtils;
import io.github.baptistemht.mariocraft.world.WorldListeners;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MarioCraft extends JavaPlugin {

    private GameDifficulty difficulty;
    private GameState gameState;
    private Location hub;

    private List<GameDifficulty> votes;
    private int raceCount;

    private DifficultySelectorGUI difficultySelectorGUI;
    private VehicleSelectorGUI vehicleSelectorGUI;
    private TrackListGUI trackListGUI;

    private PlayerManager playerManager;
    private TracksManager tracksManager;

    private static MarioCraft instance;

    @Override
    public void onEnable() {
        instance = this;

        setupWorld(getServer().getWorld("world"));

        difficulty = GameDifficulty.NORMAL;
        gameState = GameState.INIT;
        hub = new Location(getServer().getWorld("world"), getServer().getWorld("world").getSpawnLocation().getX(), getServer().getWorld("world").getSpawnLocation().getY(), getServer().getWorld("world").getSpawnLocation().getZ());

        votes = new ArrayList<>();
        raceCount = 3;

        difficultySelectorGUI = new DifficultySelectorGUI();
        vehicleSelectorGUI = new VehicleSelectorGUI();
        trackListGUI = new TrackListGUI();

        playerManager = new PlayerManager(9);
        tracksManager = new TracksManager(this);

        ProtocolLibrary.getProtocolManager().addPacketListener(new EntityController(this, ListenerPriority.HIGHEST, PacketType.Play.Client.STEER_VEHICLE));

        getServer().getPluginManager().registerEvents(new WorldListeners(), this);
        getServer().getPluginManager().registerEvents(new GUIListeners(this), this);
        getServer().getPluginManager().registerEvents(new GameListeners(this), this);

        getCommand("start").setExecutor(new StartCommand(this));

        tracksManager.loadTracks();

        gameState = GameState.PRE_GAME;

        lobbyDistanceChecker();
    }

    @Override
    public void onDisable() {
        tracksManager.getTracks().forEach(Track::reset);
    }


    public void setupSequence(){
        gameState = GameState.SELECTION;
        new DifficultyVoteTask(this);
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

    public List<GameDifficulty> getVotes() {
        return votes;
    }

    public int getRaceCount() {
        return raceCount;
    }

    public void updateRaceCount(){
        raceCount--;
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


    public static MarioCraft getInstance() {
        return instance;
    }

    public void setupWorld(World w){
        w.setAmbientSpawnLimit(0);
        w.setAutoSave(false);
        w.setPVP(false);
        w.setTime(12000);
        w.setDifficulty(Difficulty.PEACEFUL);
        w.setWeatherDuration(0);
    }

    private void lobbyDistanceChecker(){
        new BukkitRunnable() {
            @Override
            public void run() {
                if(gameState == GameState.PRE_GAME || gameState == GameState.SELECTION) {

                    for(UUID id : playerManager.getData().keySet()){

                        Player p = getServer().getPlayer(id);

                        if(p == null) return;

                        if(p.getLocation().getWorld().getName().equalsIgnoreCase("world")){

                            double dist = p.getLocation().distance(hub);

                            if(dist > 75){

                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        p.teleport(hub);
                                        p.sendMessage(ChatColor.DARK_AQUA + "Don't run away!");
                                    }
                                }.runTask(instance);

                            }

                        }

                    }

                }
            }
        }.runTaskTimerAsynchronously(this, 0L, 20L);
    }
}
