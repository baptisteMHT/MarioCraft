package io.github.baptistemht.mariocraft.vehicle;

import io.github.baptistemht.mariocraft.MarioCraft;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public enum Vehicle {

    LIGHT_KART("Light Kart", EntityType.RABBIT,2.5, 1.5, Material.RABBIT_SPAWN_EGG),
    KART("Kart", EntityType.SHEEP, 2, 2, Material.SHEEP_SPAWN_EGG),
    HEAVY_KART("Heavy Kart", EntityType.TURTLE, 1.5, 2.5, Material.TURTLE_SPAWN_EGG);

    final String name;
    final EntityType type;
    final double speed;
    final double weight;
    final Material selector;

    Vehicle(String name, EntityType type, double speed, double weight, Material selector){
        this.name = name;
        this.type = type;
        this.speed = speed;
        this.weight = weight;
        this.selector = selector;
    }

    public String getName() {
        return name;
    }

    public EntityType getType() {
        return type;
    }

    public double getSpeed() {
        return speed;
    }

    public double getWeight() {
        return weight;
    }

    public Material getSelector() {
        return selector;
    }

    public Entity summon(Player p){
        World w = p.getWorld();
        Entity e = w.spawnEntity(p.getLocation(), type);
        e.setCustomName(name);
        e.setCustomNameVisible(true);
        e.setInvulnerable(true);
        e.setSilent(true);
        e.addPassenger(p);
        MarioCraft.getInstance().getPlayerManager().getPlayerData(p.getUniqueId()).setVehicle(this);
        return e;
    }

    public static Vehicle getVehicleFromEntityType(EntityType e){
        for(Vehicle v : Vehicle.values()){
            if(v.getType() == e)return v;
        }
        return null;
    }

    public static Vehicle getVehicleFromName(String name){
        for(Vehicle v : Vehicle.values()){
            if(v.getName().equalsIgnoreCase(name))return v;
        }
        return null;
    }

    public static Vehicle getVehicleFromPlayer(Player p){
        if(!p.isInsideVehicle()) return null;
        return getVehicleFromEntityType(p.getVehicle().getType());
    }
}
