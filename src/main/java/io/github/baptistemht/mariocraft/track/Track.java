package io.github.baptistemht.mariocraft.track;

import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.util.BoxUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

public class Track {

    private final String name;
    private final World world;
    private final Location grid;
    private final Material selector;
    private boolean ready;
    private int farthestPoint;

    private List<Location> boxes;

    public Track(String name, World world, Location grid, Material selector, String farthestX_ZFromSpawn){
        this.name = name;
        this.world = world;
        this.grid = grid;
        this.selector = selector;

        ready = false;
        boxes = new ArrayList<>();

        farthestPoint = Math.max(Integer.parseInt(farthestX_ZFromSpawn.split("_")[0]), Integer.parseInt(farthestX_ZFromSpawn.split("_")[1]));

        load();
    }

    private void load(){
        new BukkitRunnable() {
            @Override
            public void run() {
                long t0 = new Date().getTime();
                MarioCraft instance = MarioCraft.getInstance();

                instance.getLogger().log(Level.INFO, "[TrackerLoader:" + name + "] Loading track...");

                BoxUtils.clearBoxesFromWorld(world);
                boxes.clear();

                instance.getLogger().log(Level.INFO, "[TrackerLoader:" + name + "] Looking for boxes...");

                int checked = 0;

                Block b;

                for(int x = grid.getBlockX() - farthestPoint; x <= grid.getBlockX() + farthestPoint; x++) {
                    for(int z = grid.getBlockZ() - farthestPoint; z <= grid.getBlockZ() + farthestPoint; z++) {
                        b = world.getBlockAt(x, (grid.getBlockY() - 1), z);
                        if(b.getType() == Material.DIAMOND_BLOCK){
                            boxes.add(new Location(world, x, grid.getBlockY(), z));
                        }
                        checked ++;
                        instance.getLogger().log(Level.INFO, checked + " | " + b.getType());
                    }
                }

                instance.getLogger().log(Level.INFO, "[TrackerLoader:" + name + "] Box research done. Checked " + checked + " blocks in " + (new Date().getTime() - t0) + "ms. " +
                       boxes.size() + " box found.");

                instance.getLogger().log(Level.INFO, "[TrackerLoader:" + name + "] Generating boxes...");

                long t1 = new Date().getTime();

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for(Location l : boxes){
                            BoxUtils.generateBox(l);
                        }
                    }
                }.runTask(instance);

                instance.getLogger().log(Level.INFO, "[TrackerLoader:" + name + "] " + boxes.size() + " boxes generated in " + (new Date().getTime() - t1) + "ms.");


                instance.getLogger().log(Level.INFO, "[TrackerLoader:" + name + "] Track ready to use.");

                ready = true;

                instance.getTrackListGUI().initializeItems();

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

    public List<Location> getBoxes() {
        return boxes;
    }

    public boolean isReady() {
        return ready;
    }
}
