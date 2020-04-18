package io.github.baptistemht.mariocraft.controller.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class ControllerListeners implements Listener {

    @EventHandler
    public void onDismount(VehicleExitEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onSprint(PlayerToggleSprintEvent e){
        e.setCancelled(true);
    }
}
