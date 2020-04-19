package io.github.baptistemht.mariocraft.controller;

import com.comphenix.packetwrapper.WrapperPlayClientSteerVehicle;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.vehicle.Vehicle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
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

        WrapperPlayClientSteerVehicle wrapper = new WrapperPlayClientSteerVehicle(event.getPacket());

        Vector forward = new Vector((instance.getDifficulty().getMultiplier()*v.getSpeed()*p.getLocation().getDirection().getX()), 0, (instance.getDifficulty().getMultiplier()*v.getSpeed()*p.getLocation().getDirection().getZ()));
        Vector backward = new Vector((-0.5*p.getLocation().getDirection().getX()), 0, (-0.5*p.getLocation().getDirection().getZ()));

        if(wrapper.getForward() > 0.1){
            e.setVelocity(forward);
        }else if(wrapper.getForward() < -0.1){
            e.setVelocity(backward);
        }

        updateOrientation(p);

    }

    private void updateOrientation(Player p){
        p.getVehicle().setRotation(p.getLocation().getYaw(), p.getLocation().getPitch());
    }
}
