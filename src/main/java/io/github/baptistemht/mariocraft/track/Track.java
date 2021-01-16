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
    private final int laps;
    private final String author;

    public Track(String name, World world, Location grid, Material selector, int laps, String author){
        this.name = name;
        this.world = world;
        this.grid = grid;
        this.selector = selector;
        this.laps = laps;
        this.author = author;

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
                    if(e.getType() == EntityType.ARMOR_STAND){
                        boxes.add(e.getLocation());
                        e.remove();
                        i++;
                    }
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

    public void reset(){
        BoxUtils.resetBoxes(this);
        for(Entity e : world.getEntities()){
            if(e.getType() != EntityType.ARMOR_STAND) e.remove();
        }
        load();
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

    public int getLaps() {
        return laps;
    }

    public String getAuthor() {
        return author;
    }
}
