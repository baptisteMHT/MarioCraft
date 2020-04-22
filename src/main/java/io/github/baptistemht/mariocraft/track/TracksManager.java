package io.github.baptistemht.mariocraft.track;

import io.github.baptistemht.mariocraft.MarioCraft;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
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
    }

    public void loadTracks(){
        long t0 = new Date().getTime();
        instance.getLogger().log(Level.INFO, "[TrackFinder] Looking for tracks...");

        List<String> wNames = new ArrayList<>();

        for(World w : instance.getServer().getWorlds()){
            wNames.add(w.getName());
            if(w.getName().contains("track")){
                tracks.add(new Track(w.getName().split("-")[1].replace("_", " "), w, w.getSpawnLocation(), Material.valueOf(w.getName().split("-")[2].toUpperCase()), w.getName().split("-")[3]));
                instance.getLogger().log(Level.INFO, "[TrackFinder] Track " + w.getName().split("-")[1] + " registered.");
            }
        }

        for(File f : instance.getServer().getWorldContainer().listFiles()){
            if(f.getName().contains("track")){

                String name = f.getName();
                String s[] = name.split("-");

                if(!wNames.contains(f.getName())){

                    World w = instance.getServer().createWorld(new WorldCreator(f.getName()));
                    Material m = Material.valueOf(s[2].toUpperCase());

                    tracks.add(new Track(s[1].replace("_", " "), w, w.getSpawnLocation(), m, s[3]));
                    instance.getLogger().log(Level.INFO, "[TrackFinder] Track " + s[1] + " registered.");

                }
            }
        }

        if(tracks.size() == 0){
            instance.getLogger().log(Level.WARNING, "[TrackFinder] 0 track found. Trying again in 2 minutes.");
            loadTracks();
        }
        instance.getLogger().log(Level.INFO, "[TrackFinder] " + tracks.size() + " track(s) registered in " + (new Date().getTime() - t0) + "ms.");

        instance.getTrackListGUI().initializeItems();
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
