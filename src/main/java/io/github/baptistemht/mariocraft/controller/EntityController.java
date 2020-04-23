package io.github.baptistemht.mariocraft.controller;

import com.comphenix.packetwrapper.WrapperPlayClientSteerVehicle;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.util.BoxUtils;
import io.github.baptistemht.mariocraft.util.TrackUtils;
import io.github.baptistemht.mariocraft.vehicle.Vehicle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
        Entity e = p.getVehicle();
        Vehicle v = Vehicle.getVehicleFromEntityType(e.getType());
        Location l = p.getLocation();
        Block standingOnBlock = p.getWorld().getBlockAt(l.getBlockX(), l.getBlockY() - 1, l.getBlockZ());

        WrapperPlayClientSteerVehicle wrapper = new WrapperPlayClientSteerVehicle(event.getPacket());


        double xSpeed = TrackUtils.getTrackAdherenceMultiplierFromMaterial(standingOnBlock.getType())*instance.getDifficulty().getMultiplier()*v.getSpeed()*p.getLocation().getDirection().getX();
        double zSpeed = TrackUtils.getTrackAdherenceMultiplierFromMaterial(standingOnBlock.getType())*instance.getDifficulty().getMultiplier()*v.getSpeed()*p.getLocation().getDirection().getZ();

        if(wrapper.getForward() > 0.1){
            Vector forward = new Vector(xSpeed, -0.9, zSpeed);
            e.setVelocity(forward);
        }else if(wrapper.getForward() < -0.1){
            Vector backward = new Vector((-0.5*xSpeed), -0.9, (-0.5*zSpeed));
            e.setVelocity(backward);
        }

        //ENTITY ORIENTATION
        p.getVehicle().setRotation(p.getLocation().getYaw(), p.getLocation().getPitch());

        //BOX detection
        for (Block b : BoxUtils.getNearbyBlocks(l, 1)){

            int x = b.getX();
            int y = b.getY() + 2;
            int z = b.getZ();

            Entity box = BoxUtils.getBox(x, z);
            if(box != null){
                if(!lastBox.containsKey(p) || lastBox.containsKey(p) && x != lastBox.get(p).getBlockX() && z != lastBox.get(p).getBlockZ()){

                    lastBox.remove(p);
                    lastBox.put(p, b.getLocation());

                    instance.getServer().getScheduler().runTask(instance, () -> BoxUtils.delBox(x, z));
                    BukkitTask task = new BukkitRunnable(){
                        @Override
                        public void run() {
                            lastBox.remove(p);
                            BoxUtils.generateBox(new Location(p.getWorld(), x, y, z));
                        }
                    }.runTaskLater(MarioCraft.getInstance(), 100L);

                    BoxUtils.loot(p);
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
}
