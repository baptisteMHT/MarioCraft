package io.github.baptistemht.mariocraft.game.listener;

import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.events.LootEvent;
import io.github.baptistemht.mariocraft.game.Vehicle;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;
import java.util.UUID;

public class LootListeners implements Listener {

    private final MarioCraft instance;

    public LootListeners(MarioCraft instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onLootEvent(LootEvent e){
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
            Player p = instance.getServer().getPlayer(id);
            if(id.toString().equalsIgnoreCase(sender.getUniqueId().toString()) || p == null) return;
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*3, 1, true));
        }
    }

    //REDUCE RECEIVER SPEED
    private void swapAction(Player sender){
        if(sender.getVehicle() == null || instance.getPlayerManager().getData().size() == 1)return;

        Vehicle sv = Vehicle.getVehicleFromEntityType(sender.getVehicle().getType());
        if(sv == null) return;

        Player receiver = instance.getServer().getPlayer((UUID) instance.getPlayerManager().getData().keySet().toArray()[new Random().nextInt(instance.getPlayerManager().getData().size())]);
        while(receiver == null || receiver.getUniqueId().toString().equalsIgnoreCase(sender.getUniqueId().toString()) || receiver.getVehicle() == null){
            receiver = instance.getServer().getPlayer((UUID) instance.getPlayerManager().getData().keySet().toArray()[new Random().nextInt(instance.getPlayerManager().getData().size())]);
        }

        Vehicle rv = Vehicle.getVehicleFromEntityType(receiver.getVehicle().getType());

        if(rv == null) return;

        sender.leaveVehicle();
        rv.summon(sender);

        receiver.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20*2, 1, true));

        receiver.leaveVehicle();
        sv.summon(receiver);
    }

    private void bananaAction(Player sender){
        if(sender.getVehicle() == null) return;
        Location l = sender.getVehicle().getLocation();
        Material type = sender.getWorld().getBlockAt(l.add(-1,-1,0)).getType();
        instance.getLogger().info(type.toString());

        sender.getWorld().getBlockAt(l.add(-1,-4,0)).setType(type);
        sender.getWorld().getBlockAt(l.add(-1,-1,0)).setType(Material.YELLOW_WOOL);
    }

    private void mushroomAction(Player sender){

    }
}
