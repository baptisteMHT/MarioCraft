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
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.logging.Level;

public class EntityController extends PacketAdapter {


    public EntityController(Plugin plugin, ListenerPriority listenerPriority, PacketType... types) {
        super(plugin, listenerPriority, types);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player p = event.getPlayer();
        Entity e = p.getVehicle();
        Vehicle v = Vehicle.getVehicleFromEntityType(e.getType());

        WrapperPlayClientSteerVehicle wrapper = new WrapperPlayClientSteerVehicle(event.getPacket());

        Vector forward = new Vector((MarioCraft.diff*v.getSpeed()*p.getLocation().getDirection().getX()), 0, (MarioCraft.diff*v.getSpeed()*p.getLocation().getDirection().getZ()));
        Vector backward = new Vector((-0.5*p.getLocation().getDirection().getX()), 0, (-0.5*p.getLocation().getDirection().getZ()));


        //Bukkit.getLogger().log(Level.INFO, "LEFT/RIGHT: " + wrapper.getSideways() + " || FORWARD/BACKWARD: " + wrapper.getForward() + " || JUMP:" + wrapper.isJump());

        if(wrapper.getForward() > 0.5){
            e.setVelocity(forward);
        }else if(wrapper.getForward() < -0.5){
            e.setVelocity(backward);
        }

        /*
        if(wrapper.getForward() > 0.5){
            //FORWARD
            Bukkit.getLogger().log(Level.INFO, "FORWARD");
            vehicle.setVelocity(new Vector(2, 0, 0));
        }else if(wrapper.getForward() < -0.5){
            vehicle.setVelocity(new Vector(-2, 0, 0));
            Bukkit.getLogger().log(Level.INFO, "BACKWARD");
        }
         */


        updateOrientation(p);

    }

    private void updateOrientation(Player p){
        p.getVehicle().setRotation(p.getLocation().getYaw(), p.getLocation().getPitch());
        //Bukkit.getLogger().log(Level.INFO, "Vehicle: " + p.getLocation().getYaw());
        //Bukkit.getLogger().log(Level.INFO, "Player: " + p.getLocation().getYaw());
        //Bukkit.getLogger().log(Level.INFO, "Delta Yaw: " + (p.getLocation().getYaw() - p.getLocation().getYaw()));
    }
}
