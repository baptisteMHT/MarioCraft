package io.github.baptistemht.mariocraft.track;

import io.github.baptistemht.mariocraft.MarioCraft;
import org.bukkit.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;
import java.util.logging.Level;

public class TracksManager {

    private final MarioCraft instance;

    private final List<Track> tracks;
    private final List<Track> votedTracks;

    private boolean loaded;

    public TracksManager(MarioCraft instance) {
        this.instance = instance;

        tracks = new ArrayList<>();
        votedTracks = new ArrayList<>();

        loaded = false;
    }

    public void loadTracks() {
        if(loaded){
            instance.getLogger().log(Level.WARNING, "[TrackFinder] Tracks are already loaded.");
            return;
        }

        instance.getLogger().log(Level.INFO, "[TrackFinder] Looking for tracks...");

        final long t0 = System.currentTimeMillis();

        for (File f : instance.getServer().getWorldContainer().listFiles()) {
            if (f.getName().contains("track")) {
                final String name = f.getName();
                final String[] s = name.split("-");
                final World w = instance.getServer().createWorld(new WorldCreator(f.getName()));

                instance.setupWorld(w);

                tracks.add(new Track(s[1].replace("_", " "), w, w.getSpawnLocation(), Material.valueOf(s[2].toUpperCase()), Integer.parseInt(s[3]), s[4]));

                instance.getLogger().log(Level.INFO, "[TrackFinder] Track " + s[1] + " registered.");
            }
        }

        if (tracks.size() == 0) {
            instance.getLogger().log(Level.WARNING, "[TrackFinder] 0 track found. Trying again in 2 minutes.");
            new BukkitRunnable() {
                @Override
                public void run() {
                    loadTracks();
                }
            }.runTaskLater(instance, 2400L);
            return;
        }

        instance.getTrackListGUI().initializeItems();

        loaded = true;

        instance.getLogger().log(Level.INFO, "[TrackFinder] " + tracks.size() + " track(s) registered in " + (System.currentTimeMillis() - t0) + "ms.");
    }

    public Track getTrackFromSelector(Material material){
        for(Track t : tracks){
            if(t.getSelector() == material)return t;
        }
        return null;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public List<Track> getVotedTracks() {
        return votedTracks;
    }
}
