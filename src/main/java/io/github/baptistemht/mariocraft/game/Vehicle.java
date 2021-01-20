package io.github.baptistemht.mariocraft.game;

import io.github.baptistemht.mariocraft.MarioCraft;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public enum Vehicle {

    LIGHT_KART(ChatColor.BLUE + "Light Kart", EntityType.PIG,2.5, 2, Material.PIG_SPAWN_EGG),
    KART(ChatColor.WHITE + "Kart", EntityType.SHEEP, 2, 3, Material.SHEEP_SPAWN_EGG),
    HEAVY_KART(ChatColor.RED + "Heavy Kart", EntityType.TURTLE, 1.5,4, Material.TURTLE_SPAWN_EGG);

    final String name;
    final EntityType type;
    final double maxSpeed;
    final int acceleration;
    final Material selector;

    Vehicle(String name, EntityType type, double maxSpeed, int acceleration, Material selector){
        this.name = name;
        this.type = type;
        this.maxSpeed = maxSpeed;
        this.acceleration = acceleration;
        this.selector = selector;
    }

    public String getName() {
        return name;
    }

    public EntityType getType() {
        return type;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public int getAcceleration() {
        return acceleration;
    }

    public Material getSelector() {
        return selector;
    }

    public void summon(Player p){
        World w = p.getWorld();
        Entity e = w.spawnEntity(p.getLocation(), type);
        e.setCustomName(name);
        e.setCustomNameVisible(true);
        e.setInvulnerable(true);
        e.setSilent(true);
        e.addPassenger(p);
    }

    public static Vehicle getVehicleFromEntityType(EntityType e){
        for(Vehicle v : Vehicle.values()){
            if(v.getType() == e)return v;
        }
        return null;
    }

    public static Vehicle getVehicleFromSelector(Material selector){
        for(Vehicle v : Vehicle.values()){
            if(v.getSelector().equals(selector))return v;
        }
        return null;
    }

    public static Vehicle getVehicleFromPlayer(Player p){
        if(!p.isInsideVehicle()) return null;
        return getVehicleFromEntityType(p.getVehicle().getType());
    }
}
