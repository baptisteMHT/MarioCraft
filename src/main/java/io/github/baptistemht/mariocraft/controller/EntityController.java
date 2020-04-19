package io.github.baptistemht.mariocraft.controller;

import com.comphenix.packetwrapper.WrapperPlayClientSteerVehicle;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.util.BoxUtils;
import io.github.baptistemht.mariocraft.vehicle.Vehicle;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class EntityController extends PacketAdapter {

    private MarioCraft instance;

    public EntityController(MarioCraft plugin, ListenerPriority listenerPriority, PacketType... types) {
        super(plugin, listenerPriority, types);
        this.instance = plugin;
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player p = event.getPlayer();
        Entity e = p.getVehicle();
        Vehicle v = Vehicle.getVehicleFromEntityType(e.getType());
        Location l = p.getLocation();
        int x = p.getLocation().getBlockX();
        int z = p.getLocation().getBlockZ();

        WrapperPlayClientSteerVehicle wrapper = new WrapperPlayClientSteerVehicle(event.getPacket());

        if(wrapper.getForward() > 0.1){
            Vector forward = new Vector((instance.getDifficulty().getMultiplier()*v.getSpeed()*p.getLocation().getDirection().getX()), 0, (instance.getDifficulty().getMultiplier()*v.getSpeed()*p.getLocation().getDirection().getZ()));
            e.setVelocity(forward);
        }else if(wrapper.getForward() < -0.1){
            Vector backward = new Vector((-0.5*p.getLocation().getDirection().getX()), 0, (-0.5*p.getLocation().getDirection().getZ()));
            e.setVelocity(backward);
        }

        //ENTITY ORIENTATION
        p.getVehicle().setRotation(p.getLocation().getYaw(), p.getLocation().getPitch());

        //LOOT BOX CHECK //WORK ON DETECTION SYSTEM, AREA AROUND THE BOX OR THE PLAYER
        Entity box = BoxUtils.getBox(x,z);

        if(box != null){

            BoxUtils.loot(p);

            instance.getServer().getScheduler().runTask(instance, () -> BoxUtils.delBox(x, z));
            BukkitTask task = new BukkitRunnable(){
                @Override
                public void run() {
                    BoxUtils.generateBox(l);
                }
            }.runTaskLater(MarioCraft.getInstance(), 120L);

        }

    }
}
