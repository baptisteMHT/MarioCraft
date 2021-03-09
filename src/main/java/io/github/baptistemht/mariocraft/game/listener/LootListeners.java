package io.github.baptistemht.mariocraft.game.listener;

import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.events.LootEvent;
import io.github.baptistemht.mariocraft.game.Loot;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class LootListeners implements Listener {

    private final MarioCraft instance;

    public LootListeners(MarioCraft instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onLootEvent(LootEvent e){
        instance.getLogger().info("LOOT");
        switch (e.getLoot()){
            case SQUID:
                squidAction(e.getSender());
                break;
            case SWAP:
                swapAction(e.getSender());
                break;
            case BANANA:
                bananaAction(e.getSender());
                break;
            case MUSHROOM:
                mushroomAction(e.getSender());
                break;
        }
    }

    private void squidAction(Player sender){
        for(UUID id : instance.getPlayerManager().getData().keySet()){
            if(/*id.toString().equalsIgnoreCase(sender.getUniqueId().toString()) || */instance.getServer().getPlayer(id) == null) return;
            instance.getServer().getPlayer(id).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*3, 1, true));
        }
    }

    private void swapAction(Player sender){

    }

    private void bananaAction(Player sender){
        Location l = sender.getVehicle().getLocation();
        Material type = sender.getWorld().getBlockAt(l.add(-1,-1,0)).getType();
        instance.getLogger().info(type.toString());

        sender.getWorld().getBlockAt(l.add(-1,-4,0)).setType(type);
        sender.getWorld().getBlockAt(l.add(-1,-1,0)).setType(Material.YELLOW_WOOL);
    }

    private void mushroomAction(Player sender){

    }
}
