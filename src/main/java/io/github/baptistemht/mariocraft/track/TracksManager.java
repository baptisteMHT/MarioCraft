package io.github.baptistemht.mariocraft.track;

import io.github.baptistemht.mariocraft.MarioCraft;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

public class TracksManager {

    private final MarioCraft instance;

    private List<Track> tracks;
    private List<Track> tracksToRandomize;

    public TracksManager(MarioCraft instance) {
        this.instance = instance;
        tracks = new ArrayList<>();
        tracksToRandomize = new ArrayList<>();

        new BukkitRunnable() {
            @Override
            public void run() {
                loadTracks();
            }
        }.runTaskAsynchronously(instance);
    }

    private void loadTracks(){
        long t0 = new Date().getTime();
        instance.getLogger().log(Level.INFO, "[TrackFinder] Looking for tracks...");
        instance.getTrackListGUI().initializeItems();
        for(World w : instance.getServer().getWorlds()){
            if(!w.getName().contains("track"))return;
            String[] names = w.getName().split("-");
            if(names[0].equalsIgnoreCase("track")){
                Material m = Material.valueOf(names[1]);
                tracks.add(new Track(names[1], w, w.getSpawnLocation(), m));
                instance.getLogger().log(Level.INFO, "[TrackFinder] Track " + names[1] + " registered.");
            }
        }
        if(tracks.size() == 0){
            instance.getLogger().log(Level.WARNING, "[TrackFinder] 0 track found. Trying again in 2 minutes.");
            new BukkitRunnable() {
                @Override
                public void run() {
                    loadTracks();
                }
            }.runTaskLaterAsynchronously(instance, 2400L);
            return;
        }
        instance.getLogger().log(Level.INFO, "[TrackFinder] " + tracks.size() + "track(s) registered in " + (new Date().getTime() - t0) + "ms.");
    }

    public Track getTrackFromName(String name){
        for(Track t : tracks){
            if(t.getName().equalsIgnoreCase(name))return t;
        }
        return null;
    }

    public Track getTrackFromSelector(Material material){
        for(Track t : tracks){
            if(t.getSelector() == material)return t;
        }
        return null;
    }

    public void reloadTracks(){
        instance.getLogger().log(Level.WARNING, "[TrackFinder] Reloading tracks. Please wait.");
        tracks.clear();
        loadTracks();
        instance.getLogger().log(Level.INFO, "[TrackFinder] Tracks reloaded.");
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public List<Track> getTTR() {
        return tracksToRandomize;
    }
}
