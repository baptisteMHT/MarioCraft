package io.github.baptistemht.mariocraft.game.gui;

import io.github.baptistemht.mariocraft.game.GameDifficulty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class DifficultySelectorGUI implements InventoryHolder {

    private final Inventory inv;

    public DifficultySelectorGUI(){
        inv = Bukkit.createInventory(this, 9 ,"Choose difficulty");

        initializeItems();
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    public void initializeItems() {
        for(GameDifficulty d : GameDifficulty.values()){
            inv.addItem(createGuiItem(d.getSelector(), d.getName()));
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
