package io.github.baptistemht.mariocraft.game.gui;

import io.github.baptistemht.mariocraft.vehicle.Vehicle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Calendar;

public class VehicleSelectorGUI implements InventoryHolder {

    private final Inventory inv;

    public VehicleSelectorGUI(){
        inv = Bukkit.createInventory(this,27 ,"Choose your vehicle");

        initializeItems();
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    public void initializeItems() {
        for(Vehicle v : Vehicle.values()){
            inv.addItem(createGuiItem(v.getSelector(), v.getName(), ChatColor.GRAY + "Speed: " + ChatColor.YELLOW + v.getMaxSpeed(), ChatColor.GRAY + "Acceleration: " + ChatColor.YELLOW + v.getAcceleration()));
        }
    }

    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);

        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    public void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }
}
