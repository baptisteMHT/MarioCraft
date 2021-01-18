package io.github.baptistemht.mariocraft.util;

import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.game.Loot;
import io.github.baptistemht.mariocraft.track.Track;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;

public class BoxUtils {

    public static void generateBox(Location l){
        World w = l.getWorld();
        if(w == null) return;
        Entity e = w.spawnEntity(l, EntityType.ENDER_CRYSTAL);
        e.setSilent(true);
        e.setInvulnerable(true);
        e.setGravity(false);
    }

    public static void loot(Player p){
        Loot loot = Loot.values()[new Random().nextInt(Loot.values().length)];
        ItemStack s = new ItemStack(loot.getMaterial(), 1);
        ItemMeta Im = s.getItemMeta();
        Im.setUnbreakable(true);
        Im.setDisplayName(loot.getName());
        s.setItemMeta(Im);

        p.getInventory().addItem(s);
    }

}
