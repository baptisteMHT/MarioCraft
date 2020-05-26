package io.github.baptistemht.mariocraft.game.gui;

import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.game.GameDifficulty;
import io.github.baptistemht.mariocraft.vehicle.Vehicle;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class GUIListeners implements Listener {

    private final MarioCraft instance;

    public GUIListeners(MarioCraft instance){
        this.instance = instance;
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        e.setCancelled(true);

        final ItemStack s = e.getCurrentItem();
        final Player p = (Player) e.getWhoClicked();

        if (s == null || s.getType() == Material.AIR) return;

        if(e.getInventory().getHolder().equals(instance.getDifficultySelectorGUI())){

            MarioCraft.getInstance().getVotes().add(GameDifficulty.getDifficultyFromMaterial(s.getType()));

        }else if (e.getInventory().getHolder().equals(instance.getVehicleSelectorGUI())){

            instance.getPlayerManager().getDataFromPlayer(p).setVehicle(Vehicle.getVehicleFromSelector(s.getType()));

        }else if(e.getInventory().getHolder().equals(instance.getTrackListGUI())){

            if(s.getItemMeta().getDisplayName().equalsIgnoreCase("Random Track")){
                instance.getTracksManager().getVotedTracks().add(instance.getTracksManager().getTracks().get(new Random().nextInt(instance.getTracksManager().getTracks().size() - 1)));
            }else{
                instance.getTracksManager().getVotedTracks().add(instance.getTracksManager().getTrackFromSelector(s.getType()));
            }

        }

        p.closeInventory();
        p.getInventory().remove(p.getInventory().getItemInMainHand());
        p.updateInventory();
    }
}
