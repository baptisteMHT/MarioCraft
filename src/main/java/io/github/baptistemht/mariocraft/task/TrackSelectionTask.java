package io.github.baptistemht.mariocraft.task;

import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.track.Track;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class TrackSelectionTask {

    private final MarioCraft instance;

    public TrackSelectionTask(MarioCraft instance){
        this.instance = instance;

        Set<UUID> ids = instance.getPlayerManager().getPlayersData().keySet();


        ItemStack s = new ItemStack(Material.MUSIC_DISC_WARD);
        ItemMeta m = s.getItemMeta();
        m.setDisplayName("Track selector");
        s.setItemMeta(m);

        for(UUID id : instance.getPlayerManager().getPlayersData().keySet()){
            Bukkit.getPlayer(id).getInventory().addItem(s);
        }

        Bukkit.broadcastMessage("[MarioCraft] Choose a track!");

        boolean done = false;

        while(!done){
            if(instance.getTracksManager().getTTR().size() >= ids.size()){
                done = true;

                Track t = instance.getTracksManager().getTracks().get(new Random().nextInt(instance.getTracksManager().getTTR().size()));

                Bukkit.broadcastMessage("[MarioCraft] Everyone chose a track! Selected track: " + t.getName());

                for(Player p : Bukkit.getOnlinePlayers()){
                    p.getInventory().clear();
                    p.updateInventory();
                }

                //WAIT 5 SEC THEN TP EVERYONE TO THE TRACK
            }
        }
    }


}
