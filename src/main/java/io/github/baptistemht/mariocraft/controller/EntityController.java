package io.github.baptistemht.mariocraft.controller;

import com.comphenix.packetwrapper.WrapperPlayClientSteerVehicle;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.game.GameState;
import io.github.baptistemht.mariocraft.game.player.PlayerData;
import io.github.baptistemht.mariocraft.util.BoxUtils;
import io.github.baptistemht.mariocraft.util.TrackUtils;
import io.github.baptistemht.mariocraft.vehicle.Vehicle;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class EntityController extends PacketAdapter {

    private final MarioCraft instance;
    private final Map<Player, Location> lastBox;

    private double diffMultiplier = 0;

    public EntityController(MarioCraft plugin, ListenerPriority listenerPriority, PacketType... types) {
        super(plugin, listenerPriority, types);
        this.instance = plugin;
        this.lastBox = new HashMap<>();
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player p = event.getPlayer();
        PlayerData d = instance.getPlayerManager().getPlayerData(p.getUniqueId());
        Location l = p.getLocation();

        Entity e = p.getVehicle();
        Vehicle v = Vehicle.getVehicleFromEntityType(e.getType());

        Material standingOnMaterial = e.getWorld().getBlockAt(e.getLocation().getBlockX(), e.getLocation().getBlockY() -1, e.getLocation().getBlockZ()).getType();
        Material deepMaterial = e.getWorld().getBlockAt(e.getLocation().getBlockX(), e.getLocation().getBlockY() -2, e.getLocation().getBlockZ()).getType();

        WrapperPlayClientSteerVehicle wrapper = new WrapperPlayClientSteerVehicle(event.getPacket());

        if(diffMultiplier == 0)diffMultiplier = instance.getDifficulty().getMultiplier();

        //ENTITY ORIENTATION
        p.getVehicle().setRotation(p.getLocation().getYaw(), p.getLocation().getPitch());


        if(instance.getGameState() != GameState.RACING) return;

        double xVel = TrackUtils.getTrackAdherence(standingOnMaterial)*diffMultiplier*v.getSpeed()*l.getDirection().getX();
        double zVel = TrackUtils.getTrackAdherence(standingOnMaterial)*diffMultiplier*v.getSpeed()*l.getDirection().getZ();

        //KART CONTROL
        if(wrapper.getForward() > 0.1){
            e.setVelocity(new Vector(xVel, -0.2, zVel));
        }else if(wrapper.getForward() < -0.1){
            e.setVelocity(new Vector((-0.5*xVel), -0.2, (-0.5*zVel)));
        }

        //BOX DETECTION
        for(int x = l.getBlockX() - 1; x <= l.getBlockX() + 1; x++) {
            for(int y = l.getBlockY(); y <= l.getBlockY() + 1; y++) {
                for(int z = l.getBlockZ() - 1; z <= l.getBlockZ() + 1; z++) {

                    Entity box = BoxUtils.getBox(x, z);

                    if(box != null){
                        if(!lastBox.containsKey(p) || lastBox.containsKey(p) && x != lastBox.get(p).getBlockX() && z != lastBox.get(p).getBlockZ()){

                            final int x1 = x, y1 = y, z1 = z;

                            lastBox.remove(p);
                            lastBox.put(p, box.getLocation());

                            BoxUtils.loot(p);

                            instance.getServer().getScheduler().runTask(instance, () -> BoxUtils.delBox(x1, z1));
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    lastBox.remove(p);
                                    BoxUtils.generateBox(new Location(p.getWorld(), x1, (y1+1), z1));
                                }
                            }.runTaskLater(instance, 100L);

                        }
                    }
                }
            }
        }


        //CHECKPOINT DETECTION
        if(deepMaterial == Material.BLACK_CONCRETE || deepMaterial == Material.YELLOW_CONCRETE){
            if(d.getLastCheckpoint() == null && d.getLaps() == 1.0){
                d.setLastCheckpoint(deepMaterial);
            }else if(deepMaterial != d.getLastCheckpoint()){
                d.incrLaps();
                d.setLastCheckpoint(deepMaterial);
            }
        }

    }
}
