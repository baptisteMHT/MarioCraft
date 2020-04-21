package io.github.baptistemht.mariocraft.task;

import io.github.baptistemht.mariocraft.MarioCraft;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Set;
import java.util.UUID;

public class VehicleSelectorTask {

    private MarioCraft instance;

    public VehicleSelectorTask(MarioCraft instance){
        this.instance = instance;

        ItemStack s = new ItemStack(Material.BEE_SPAWN_EGG);
        ItemMeta m = s.getItemMeta();
        m.setDisplayName("Kart selector");
        s.setItemMeta(m);

        for(UUID id : instance.getPlayerManager().getPlayersData().keySet()){
            Player p = Bukkit.getPlayer(id);
            p.getInventory().addItem(s);
        }

        Bukkit.broadcastMessage("[MarioCraft] Choose your kart!");


        Set<UUID> ids = instance.getPlayerManager().getPlayersData().keySet();
        boolean done = false;
        int i = 0;

        while(!done){
            for(UUID id : ids){
                if(instance.getPlayerManager().getPlayerData(id).getVehicle() != null){
                    i++;
                }
            }
            if(i >= instance.getPlayerManager().getPlayersData().size()){
                done = true;
                Bukkit.broadcastMessage("[MarioCraft] + Everyone chose a kart! It's racing time!");
                //WAIT 5 SEC THEN TP EVERYONE TO THE GRID AND SUMMON KARTS.
            }
        }
    }


}
