package io.github.baptistemht.mariocraft.game.gui;

import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.track.Track;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class TrackListGUI implements InventoryHolder {

    private final Inventory inv;

    public TrackListGUI(){
        inv = Bukkit.createInventory(this, 45,"Choose track");
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    public void initializeItems() {
        inv.clear();

        for(Track t : MarioCraft.getInstance().getTracksManager().getTracks()){
            if(t.isReady()) inv.addItem(createGuiItem(t.getSelector(), t.getName().replace("_", " ")));
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
