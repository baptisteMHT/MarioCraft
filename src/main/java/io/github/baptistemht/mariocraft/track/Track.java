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
    private final int laps;
    private final String author;

    private boolean isLoaded;

    public Track(String name, World world, Location grid, Material selector, int laps, String author){
        this.name = name;
        this.world = world;
        this.grid = grid;
        this.selector = selector;
        this.laps = laps;
        this.author = author;

        isLoaded = false;

        load();
    }

    public void load(){

        if(isLoaded) return;

        List<Location> locations = new ArrayList<>();

        new BukkitRunnable() {
            @Override
            public void run() {
                int i = 0;

                for(Entity e : world.getEntities()){
                    if(e.getType() == EntityType.ARMOR_STAND){
                        locations.add(e.getLocation());
                        e.remove();
                        i++;
                    }
                }

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for(Location l : locations){
                            BoxUtils.generateBox(l);
                        }
                    }
                }.runTask(MarioCraft.getInstance());

                MarioCraft.getInstance().getLogger().log(Level.INFO, "[TrackLoader:" + name + "] " + i + " box generated.");
            }
        }.runTaskAsynchronously(MarioCraft.getInstance());

        isLoaded = true;
    }

    public void reset(){
        if(!isLoaded) return;
        for(Entity e : world.getEntities()){
            if(e.getType() == EntityType.ENDER_CRYSTAL){
                Location l = e.getLocation();
                e.remove();
                world.spawnEntity(l, EntityType.ARMOR_STAND);
            }else{
                e.remove();
            }
        }
        isLoaded = false;
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
