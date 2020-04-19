package io.github.baptistemht.mariocraft.controller;

import com.comphenix.packetwrapper.WrapperPlayClientSteerVehicle;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.util.BoxUtils;
import io.github.baptistemht.mariocraft.vehicle.Vehicle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.logging.Level;

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

        Entity box = BoxUtils.getBox(p.getLocation().getBlockX(), p.getLocation().getBlockZ());
        if(box != null){
            p.getInventory().setItemInMainHand(BoxUtils.loot());
            box.remove();
            instance.getServer().getScheduler().runTask(instance, box::remove);
            instance.getBoxes().remove(box);
            instance.getServer().getScheduler().runTaskLater(instance, (Runnable) BoxUtils.generateBox(p.getLocation()), 200L);
        }

    }
}
