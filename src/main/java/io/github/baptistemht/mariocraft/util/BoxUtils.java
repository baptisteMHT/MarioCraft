package io.github.baptistemht.mariocraft.util;

import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.game.BoxLoot;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class BoxUtils {

    public static Entity generateBox(Location loc){
        World w = loc.getWorld();
        Entity e = w.spawnEntity(loc, EntityType.ENDER_CRYSTAL);
        loc.setY(loc.getBlockY() + 1);
        e.setSilent(true);
        e.setInvulnerable(true);
        e.setGravity(false);
        MarioCraft.getInstance().getBoxes().add(e);

        return e;
    }

    public static Entity getBox(double x, double z){
        for(Entity e : MarioCraft.getInstance().getBoxes()){
            if(e.getLocation().getBlockX() == x && e.getLocation().getBlockZ() == z){
                return e;
            }
        }
        return null;
    }

    public static void resetBoxes(){
        for(Entity e : MarioCraft.getInstance().getBoxes()){
            e.remove();
        }
        MarioCraft.getInstance().getBoxes().clear();
    }

    public static ItemStack loot(){
        BoxLoot loot = BoxLoot.values()[new Random().nextInt(BoxLoot.values().length - 1)];
        ItemStack s = new ItemStack(loot.getMaterial(), 1);
        ItemMeta Im = s.getItemMeta();
        Im.setUnbreakable(true);
        Im.setDisplayName(loot.getName());
        s.setItemMeta(Im);
        return s;
    }

}
