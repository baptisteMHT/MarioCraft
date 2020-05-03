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
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class EntityController extends PacketAdapter {

    private MarioCraft instance;
    private Map<Player, Location> lastBox;

    public EntityController(MarioCraft plugin, ListenerPriority listenerPriority, PacketType... types) {
        super(plugin, listenerPriority, types);
        this.instance = plugin;
        this.lastBox = new HashMap<>();
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player p = event.getPlayer();
        PlayerData d = instance.getPlayerManager().getPlayerData(p.getUniqueId());
        Entity e = p.getVehicle();
        Vehicle v = Vehicle.getVehicleFromEntityType(e.getType());
        Location l = p.getLocation();
        Block standingOnBlock = e.getWorld().getBlockAt(e.getLocation().getBlockX(), e.getLocation().getBlockY() -1, e.getLocation().getBlockZ());
        Block deepBlock = e.getWorld().getBlockAt(e.getLocation().getBlockX(), e.getLocation().getBlockY() -2, e.getLocation().getBlockZ());

        WrapperPlayClientSteerVehicle wrapper = new WrapperPlayClientSteerVehicle(event.getPacket());

        double xSpeed = TrackUtils.getTrackAdherenceMultiplierFromMaterial(standingOnBlock.getType())*instance.getDifficulty().getMultiplier()*v.getSpeed()*p.getLocation().getDirection().getX();
        double zSpeed = TrackUtils.getTrackAdherenceMultiplierFromMaterial(standingOnBlock.getType())*instance.getDifficulty().getMultiplier()*v.getSpeed()*p.getLocation().getDirection().getZ();

        //ENTITY ORIENTATION
        p.getVehicle().setRotation(p.getLocation().getYaw(), p.getLocation().getPitch());


        if(instance.getGameState() != GameState.RACING) return;

        //KART CONTROL
        if(wrapper.getForward() > 0.1){
            Vector forward = new Vector(xSpeed, -0.2, zSpeed);
            e.setVelocity(forward);
        }else if(wrapper.getForward() < -0.1){
            Vector backward = new Vector((-0.5*xSpeed), -0.2, (-0.5*zSpeed));
            e.setVelocity(backward);
        }



        //BOX DETECTION
        for (Block b : BoxUtils.getNearbyBlocks(l, 1)){

            int x = b.getX();
            int y = b.getY();
            int z = b.getZ();

            Entity box = BoxUtils.getBox(x, z);
            if(box != null){
                if(!lastBox.containsKey(p) || lastBox.containsKey(p) && x != lastBox.get(p).getBlockX() && z != lastBox.get(p).getBlockZ()){

                    lastBox.remove(p);
                    lastBox.put(p, b.getLocation());

                    BoxUtils.loot(p);

                    instance.getServer().getScheduler().runTask(instance, () -> BoxUtils.delBox(x, z));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            lastBox.remove(p);
                            BoxUtils.generateBox(new Location(p.getWorld(), x, (y+1), z));
                        }
                    }.runTaskLater(instance, 100L);
                }
            }

        }

        //CHECKPOINT DETECTION
        if(deepBlock.getType() == Material.BLACK_CONCRETE || deepBlock.getType() == Material.YELLOW_CONCRETE){
            if(d.getLastCheckpoint() == null && d.getLaps() == 1.0){
                d.setLastCheckpoint(deepBlock.getType());
            }else if(deepBlock.getType() != d.getLastCheckpoint()){
                d.incrLaps();
                d.setLastCheckpoint(deepBlock.getType());
            }
        }

        //COLLISION DETECTION (NOT TESTED YET BUT IT'LL BE WEIRD)

        if(instance.isCollision()){
            for(UUID id : instance.getPlayerManager().getPlayersData().keySet()){

                Player ps = Bukkit.getPlayer(id);

                if(ps.getLocation().getBlockX() == l.getBlockX() && ps.getLocation().getBlockY() == l.getBlockY() && ps.getLocation().getBlockZ() == l.getBlockZ()){

                    if(Vehicle.getVehicleFromPlayer(p).getWeight() > Vehicle.getVehicleFromPlayer(ps).getWeight()){
                        Vector vector = new Vector(-ps.getLocation().getDirection().getX()*0.1, ps.getLocation().getDirection().getY(), -ps.getLocation().getDirection().getZ()*0.1);
                        ps.setVelocity(vector);
                    }else if(Vehicle.getVehicleFromPlayer(p).getWeight() < Vehicle.getVehicleFromPlayer(ps).getWeight()){
                        Vector vector = new Vector(-p.getLocation().getDirection().getX()*0.1, p.getLocation().getDirection().getY(), -p.getLocation().getDirection().getZ()*0.1);
                        p.setVelocity(vector);
                    }else {
                        Vector vector = new Vector(-p.getLocation().getDirection().getX()*0.05, p.getLocation().getDirection().getY(), -p.getLocation().getDirection().getZ()*0.05);
                        p.setVelocity(vector);
                        ps.setVelocity(vector);
                    }

                }
            }

        }
    }
}
