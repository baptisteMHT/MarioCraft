package io.github.baptistemht.mariocraft;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import io.github.baptistemht.mariocraft.controller.EntityController;
import io.github.baptistemht.mariocraft.controller.listener.ControllerListeners;
import io.github.baptistemht.mariocraft.game.GameDifficulty;
import io.github.baptistemht.mariocraft.game.listener.GameListeners;
import io.github.baptistemht.mariocraft.util.BoxUtils;
import io.github.baptistemht.mariocraft.vehicle.Vehicle;
import io.github.baptistemht.mariocraft.world.WorldListeners;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class MarioCraft extends JavaPlugin implements Listener {

    private GameDifficulty difficulty;
    private ArrayList<Entity> boxes;

    private static MarioCraft instance;

    @Override
    public void onEnable() {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        manager.addPacketListener(new EntityController(this, ListenerPriority.HIGHEST, PacketType.Play.Client.STEER_VEHICLE));

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new ControllerListeners(), this);
        getServer().getPluginManager().registerEvents(new WorldListeners(), this);
        getServer().getPluginManager().registerEvents(new GameListeners(), this);

        this.difficulty = GameDifficulty.NORMAL;
        boxes = new ArrayList<>();

        instance = this;

        BoxUtils.resetBoxes();
    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Vehicle.LIGHT_KART.summon(e.getPlayer());
        Location l = new Location(e.getPlayer().getWorld(), e.getPlayer().getLocation().getBlockX(), e.getPlayer().getLocation().getBlockY(), e.getPlayer().getLocation().getBlockZ());
        l.setX(l.getX() + 5);
        BoxUtils.generateBox(l);
    }

    public ArrayList<Entity> getBoxes() {
        return boxes;
    }

    public GameDifficulty getDifficulty() {
        return difficulty;
    }

    public static MarioCraft getInstance() {
        return instance;
    }
}
