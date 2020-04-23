package io.github.baptistemht.mariocraft.world;

import io.github.baptistemht.mariocraft.MarioCraft;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class WorldListeners implements Listener {

    @EventHandler
    public void onBreakEvent(BlockBreakEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e){
        if(e.getEntity() instanceof Player){
            e.setCancelled(true);
        }
    }
}
