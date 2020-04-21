package io.github.baptistemht.mariocraft.track;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

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
