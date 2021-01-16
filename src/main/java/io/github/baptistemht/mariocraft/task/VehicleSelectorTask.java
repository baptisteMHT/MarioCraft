package io.github.baptistemht.mariocraft.task;

import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.util.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;
import java.util.UUID;

public class VehicleSelectorTask {

    private int i;

    public VehicleSelectorTask(MarioCraft instance) {
        Set<UUID> ids = instance.getPlayerManager().getPlayersData().keySet();

        ItemStack s = new ItemStack(Material.BEE_SPAWN_EGG);
        ItemMeta m = s.getItemMeta();
        m.setDisplayName("Kart selector");
        s.setItemMeta(m);

        for (UUID id : ids) {
            Bukkit.getPlayer(id).getInventory().addItem(s);
        }

        Bukkit.broadcastMessage(MessageUtils.getPrefix() + "Choose your kart!");

        new BukkitRunnable() {
            @Override
            public void run() {
                i = 0;
                for (UUID id : ids) {
                 if (instance.getPlayerManager().getPlayerData(id).getVehicle() != null) {
                        i++;
                    }
                }
                if (i == ids.size()) {

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.getInventory().clear();
                        p.updateInventory();
                    }

                    new TrackSelectionTask(instance);

                    this.cancel();
                }
            }
        }.runTaskTimer(instance, 0L, 20L);

    }


}
