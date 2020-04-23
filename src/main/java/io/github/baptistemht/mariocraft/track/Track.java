package io.github.baptistemht.mariocraft.track;

import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.util.BoxUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Track {

    private final String name;
    private final World world;
    private final Location grid;
    private final Material selector;
    private final List<Location> boxes;

    public Track(String name, World world, Location grid, Material selector){
        this.name = name;
        this.world = world;
        this.grid = grid;
        this.selector = selector;

        boxes = new ArrayList<>();

        load();

    }

    private void load(){

        new BukkitRunnable() {
            @Override
            public void run() {
                int i = 0;

                boxes.clear();

                for(Entity e : world.getEntities()){
                    if(e.getType() == EntityType.ARMOR_STAND || e.getType() == EntityType.ENDER_CRYSTAL){
                        boxes.add(e.getLocation().add(0, 1, 0));
                        MarioCraft.getInstance().getLogger().log(Level.INFO, "[TrackLoader:" + name + "] Box found. (" + e.getLocation().getBlockX() + ";" + e.getLocation().getBlockY() + ";" + e.getLocation().getBlockZ() + ")");
                        i++;
                    }
                    e.remove();
                }

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for(Location l : boxes){
                            BoxUtils.generateBox(l);
                        }
                    }
                }.runTask(MarioCraft.getInstance());

                MarioCraft.getInstance().getLogger().log(Level.INFO, "[TrackLoader:" + name + "] " + i + " box generated.");
            }
        }.runTaskAsynchronously(MarioCraft.getInstance());

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
