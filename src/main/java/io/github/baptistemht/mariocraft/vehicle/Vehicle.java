package io.github.baptistemht.mariocraft.vehicle;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public enum Vehicle {

    LIGHT_KART("Light Kart", EntityType.CHICKEN,2.5, 1),
    KART("Kart", EntityType.SHEEP, 2, 2),
    HEAVY_KART("Heavy Kart", EntityType.TURTLE, 1.5, 3);

    final String name;
    final EntityType type;
    final double speed;
    final int weight;

    Vehicle(String name, EntityType type, double speed, int weight){
        this.name = name;
        this.type = type;
        this.speed = speed;
        this.weight = weight;
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

    public int getWeight() {
        return weight;
    }

    public Entity summon(Player p){
        World w = p.getWorld();
        Entity e = w.spawnEntity(p.getLocation(), type);
        e.setCustomName(name);
        e.setCustomNameVisible(true);
        e.setInvulnerable(true);
        e.setSilent(true);
        e.addPassenger(p);
        return e;
    }

    public static Vehicle getVehicleFromEntityType(EntityType e){
        for(Vehicle v : Vehicle.values()){
            if(v.getType() == e){
                return v;
            }
        }
        return null;
    }

    public static Vehicle getVehicleFromPlayer(Player p){
        if(!p.isInsideVehicle()) return null;
        return getVehicleFromEntityType(p.getVehicle().getType());
    }
}
