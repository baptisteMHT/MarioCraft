package io.github.baptistemht.mariocraft.game.gui;

import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.game.GameDifficulty;
import io.github.baptistemht.mariocraft.track.Track;
import io.github.baptistemht.mariocraft.game.Vehicle;
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

            instance.getPlayerManager().getPlayer(p.getUniqueId()).setVehicle(Vehicle.getVehicleFromSelector(s.getType()));

        }else if(e.getInventory().getHolder().equals(instance.getTrackListGUI())){
            if(s.getItemMeta().getDisplayName().equalsIgnoreCase("Random Track")){
                int index = new Random().nextInt(instance.getTracksManager().getVotedTracks().size());
                instance.getTracksManager().getVotedTracks().set(index, instance.getTracksManager().getVotedTracks().get(index)+1);
            }else{
                Track t = instance.getTracksManager().getTrackFromSelector(s.getType());
                instance.getTracksManager().getVotedTracks().set(instance.getTracksManager().getTracks().indexOf(t), instance.getTracksManager().getVotedTracks().get(instance.getTracksManager().getTracks().indexOf(t))+1);
            }
        }

        p.closeInventory();
        p.getInventory().remove(p.getInventory().getItemInMainHand());
        p.updateInventory();
    }
}
