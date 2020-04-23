package io.github.baptistemht.mariocraft.track;

import io.github.baptistemht.mariocraft.util.BoxUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class Track {

    private final String name;
    private final World world;
    private final Location grid;
    private final Material selector;

    public Track(String name, World world, Location grid, Material selector){
        this.name = name;
        this.world = world;
        this.grid = grid;
        this.selector = selector;

        load();

    }

    private void load(){
        for(Entity e : world.getEntities()){
            if(e.getType() == EntityType.ARMOR_STAND){
                BoxUtils.generateBox(e.getLocation().add(0, -1, 0));
                e.remove();
            }
        }
    }

    public String getName() {
        return name;
    }

    public Location getGrid() {
        return grid;
    }

    public World getWorld() {
        return world;
    }

    public Material getSelector() {
        return selector;
    }
}
