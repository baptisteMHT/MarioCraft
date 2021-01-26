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
import io.github.baptistemht.mariocraft.world.WorldListeners;
import org.bukkit.*;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MarioCraft extends JavaPlugin {

    private GameDifficulty difficulty;
    private GameState gameState;

    private Location hub;
    private World hubWorld;

    private int maxHubDistance;

    private List<GameDifficulty> votes;
    private int raceCount;

    private DifficultySelectorGUI difficultySelectorGUI;
    private VehicleSelectorGUI vehicleSelectorGUI;
    private TrackListGUI trackListGUI;

    private PlayerManager playerManager;
    private TracksManager tracksManager;

    private static MarioCraft instance;

    private FileConfiguration config;

    private FileConfiguration message;

    @Override
    public void onEnable() {
        instance = this;

        loadCustomConfig();

        hubWorld = getServer().getWorld(Objects.requireNonNull(getConfig().getString("hub.name", "world")));

        if(hubWorld != null){
            setupWorld(hubWorld);
            getLogger().info("[CONFIG] hub.name : " + hubWorld.getName());
            hub = new Location(hubWorld,
                    hubWorld.getSpawnLocation().getBlockX(),
                    hubWorld.getSpawnLocation().getBlockY(),
                    hubWorld.getSpawnLocation().getBlockZ()
            );
            maxHubDistance = getConfig().getInt("hub.distance", 75);
            getLogger().info("[CONFIG] hub.distance : " + maxHubDistance);
        }else{
            getLogger().severe("Failed to find the hub. Shutting down the server.");
            getServer().shutdown();
            return;
        }

        difficulty = GameDifficulty.NORMAL;
        gameState = GameState.INIT;

        votes = new ArrayList<>();

        raceCount = getConfig().getInt("race.number", 3);
        getLogger().info("[CONFIG] race.number : " + raceCount);


        difficultySelectorGUI = new DifficultySelectorGUI();
        vehicleSelectorGUI = new VehicleSelectorGUI();
        trackListGUI = new TrackListGUI();

        int pilots = getConfig().getInt("race.pilots", 9);
        int spectators = getConfig().getInt("race.spectators", 11);

        getLogger().info("[CONFIG] race.pilots : " + pilots);
        getLogger().info("[CONFIG] race.spectators : " + spectators);

        playerManager = new PlayerManager(pilots, spectators);
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

    public World getHubWorld() {
        return hubWorld;
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

                    for(Player p : getServer().getOnlinePlayers()){

                        if(p.getWorld().getName().equalsIgnoreCase(hubWorld.getName())){

                            double dist = p.getLocation().distance(hub);

                            if(dist > maxHubDistance){

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

    @Override
    public FileConfiguration getConfig() {
        return config;
    }

    public FileConfiguration getMessage() {
        return message;
    }

    private void loadCustomConfig(){
        File configFile = new File(getDataFolder(), "config.yml");
        File messageFile = new File(getDataFolder(), "message.yml");

        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }

        if (!messageFile.exists()) {
            messageFile.getParentFile().mkdirs();
            saveResource("message.yml", false);
        }

        config = new YamlConfiguration();
        message = new YamlConfiguration();
        try {
            config.load(configFile);
            message.load(messageFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }
}
