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

public class GUIListeners implements Listener {

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        e.setCancelled(true);
        if(e.getInventory().contains(Material.LEATHER_CHESTPLATE)){
            final ItemStack clickedItem = e.getCurrentItem();

            if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

            final Player p = (Player) e.getWhoClicked();

            p.closeInventory();
            p.sendMessage("You chose: " + clickedItem.getItemMeta().getDisplayName());
            MarioCraft.getInstance().setDifficulty(GameDifficulty.getDifficultyFromMaterial(clickedItem.getType()));
            new VehicleSelectorGUI().openInventory(p);

        }else if (e.getInventory().contains(Material.TURTLE_SPAWN_EGG)){

            final ItemStack clickedItem = e.getCurrentItem();

            if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

            final Player p = (Player) e.getWhoClicked();

            p.closeInventory();
            p.sendMessage("You chose: " + clickedItem.getItemMeta().getDisplayName());
            Vehicle.getVehicleFromName(clickedItem.getItemMeta().getDisplayName()).summon(p);

        }
    }
}
