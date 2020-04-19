package io.github.baptistemht.mariocraft;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import io.github.baptistemht.mariocraft.controller.EntityController;
import io.github.baptistemht.mariocraft.controller.listener.ControllerListeners;
import io.github.baptistemht.mariocraft.game.GameDifficulty;
import io.github.baptistemht.mariocraft.game.gui.GUIListeners;
import io.github.baptistemht.mariocraft.game.listener.GameListeners;
import io.github.baptistemht.mariocraft.game.player.PlayerManager;
import io.github.baptistemht.mariocraft.util.BoxUtils;
import io.github.baptistemht.mariocraft.world.WorldListeners;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class MarioCraft extends JavaPlugin {

    private GameDifficulty difficulty;
    private ArrayList<Entity> boxes;

    private PlayerManager playerManager;

    private static MarioCraft instance;

    @Override
    public void onEnable() {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        manager.addPacketListener(new EntityController(this, ListenerPriority.HIGHEST, PacketType.Play.Client.STEER_VEHICLE));

        getServer().getPluginManager().registerEvents(new ControllerListeners(this), this);
        getServer().getPluginManager().registerEvents(new WorldListeners(), this);
        getServer().getPluginManager().registerEvents(new GUIListeners(), this);
        getServer().getPluginManager().registerEvents(new GameListeners(this), this);

        this.difficulty = GameDifficulty.NORMAL;
        boxes = new ArrayList<>();

        playerManager = new PlayerManager(this);

        instance = this;
    }

    @Override
    public void onDisable() {
        BoxUtils.resetBoxes();
    }

    public ArrayList<Entity> getBoxes() {
        return boxes;
    }

    public GameDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(GameDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public static MarioCraft getInstance() {
        return instance;
    }
}
