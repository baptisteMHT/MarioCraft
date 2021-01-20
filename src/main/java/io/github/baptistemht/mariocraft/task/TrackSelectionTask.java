package io.github.baptistemht.mariocraft.task;

import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.track.Track;
import io.github.baptistemht.mariocraft.util.GameUtils;
import io.github.baptistemht.mariocraft.util.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class TrackSelectionTask {

    public TrackSelectionTask(MarioCraft instance){
        final Set<UUID> ids = instance.getPlayerManager().getData().keySet();

        ItemStack s = new ItemStack(Material.MUSIC_DISC_WARD);
        ItemMeta m = s.getItemMeta();
        m.setDisplayName("Track selector");
        s.setItemMeta(m);

        for (UUID id : ids) {
            Player p = instance.getServer().getPlayer(id);
            if(p!=null) p.getInventory().addItem(s);
        }

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(MessageUtils.getPrefix() + "Choose a track!");

        new BukkitRunnable() {
            @Override
            public void run() {
                int size = 0;
                for(int i=0;i<instance.getTracksManager().getVotedTracks().size();i++){
                    size = size+instance.getTracksManager().getVotedTracks().get(i);
                }

                if(size == ids.size()){

                    final Track t = getSelectedTrack(instance);

                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage(MessageUtils.getPrefix() + "Selected track: " + t.getName());

                    for(int i=0;i<instance.getTracksManager().getVotedTracks().size();i++){
                        instance.getTracksManager().getVotedTracks().set(i,0);
                    }

                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.getInventory().clear();
                        p.updateInventory();
                    }

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            GameUtils.tpAllToTrack(t);
                            for(UUID id : instance.getPlayerManager().getData().keySet()){
                                Player p = instance.getServer().getPlayer(id);
                                if(p!=null) instance.getPlayerManager().getPlayer(id).getVehicle().summon(p);
                            }
                            new RaceTask(instance, t);
                        }
                    }.runTaskLater(instance, 50L);

                    this.cancel();
                }
            }
        }.runTaskTimer(instance, 0L, 10L);

    }

    private Track getSelectedTrack(MarioCraft instance){
        List<Integer> tracks = instance.getTracksManager().getVotedTracks();
        int index = 0;
        for(int i=0;i<tracks.size();i++){
            if(i>=tracks.get(index)){
                index = i;
            }
        }
        return instance.getTracksManager().getTracks().get(index);
    }


}
