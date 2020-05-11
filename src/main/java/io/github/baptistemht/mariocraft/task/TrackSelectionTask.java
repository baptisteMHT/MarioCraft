package io.github.baptistemht.mariocraft.task;

import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.track.Track;
import io.github.baptistemht.mariocraft.util.GameUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class TrackSelectionTask {

    public TrackSelectionTask(MarioCraft instance){
        final Set<UUID> ids = instance.getPlayerManager().getPlayersData().keySet();

        ItemStack s = new ItemStack(Material.MUSIC_DISC_WARD);
        ItemMeta m = s.getItemMeta();
        m.setDisplayName("Track selector");
        s.setItemMeta(m);

        for(UUID id : instance.getPlayerManager().getPlayersData().keySet()){
            Bukkit.getPlayer(id).getInventory().addItem(s);
        }

        Bukkit.broadcastMessage("[MarioCraft] Choose a track!");

        new BukkitRunnable() {
            @Override
            public void run() {
                if(instance.getTracksManager().getTTR().size() >= ids.size()){

                    final Track t = instance.getTracksManager().getTracks().get(new Random().nextInt(instance.getTracksManager().getTTR().size()));

                    Bukkit.broadcastMessage("[MarioCraft] Everyone chose a track! Selected track: " + t.getName().replace("_", " "));

                    instance.getTracksManager().getTTR().clear();

                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.getInventory().clear();
                        p.updateInventory();
                    }

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            GameUtils.tpAllToTrack(t);
                            for(UUID id : instance.getPlayerManager().getPlayersData().keySet()){
                                instance.getPlayerManager().getPlayerData(id).getVehicle().summon(Bukkit.getPlayer(id));
                            }

                            new RaceTask(instance, t);
                        }
                    }.runTaskLater(instance, 40L);

                    this.cancel();
                }
            }
        }.runTaskTimer(instance, 0L, 10L);
    }


}
