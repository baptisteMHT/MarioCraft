package io.github.baptistemht.mariocraft.util;

import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.game.BoxLoot;
import io.github.baptistemht.mariocraft.track.Track;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoxUtils {

    public static void generateBox(Location loc){
        World w = loc.getWorld();
        if(w == null) return;
        Entity e = w.spawnEntity(loc, EntityType.ENDER_CRYSTAL);
        loc.setY(loc.getBlockY() + 1);
        e.setSilent(true);
        e.setInvulnerable(true);
        e.setGravity(false);
        MarioCraft.getInstance().getBoxes().add(e);
    }

    public static Entity getBox(int x, int z){
        for(Entity e : MarioCraft.getInstance().getBoxes()){
            if(e.getLocation().getBlockX() == x && e.getLocation().getBlockZ() == z){
                return e;
            }
        }
        return null;
    }

    public static void resetBoxesFromAllTracks(){
        for(Entity e : MarioCraft.getInstance().getBoxes()){
            if(e.getWorld().getName().contains("track")){
                e.remove();
                e.getWorld().spawnEntity(e.getLocation(), EntityType.ARMOR_STAND);
            }
        }
        MarioCraft.getInstance().getBoxes().clear();
    }

    public static void delBox(int x, int z){
        for(Entity e : MarioCraft.getInstance().getBoxes()){
            if(e.getLocation().getBlockX() == x && e.getLocation().getBlockZ() == z){
                e.remove();
                MarioCraft.getInstance().getBoxes().remove(e);
            }
        }
    }

    public static void loot(Player p){
        BoxLoot loot = BoxLoot.values()[new Random().nextInt(BoxLoot.values().length)];
        ItemStack s = new ItemStack(loot.getMaterial(), 1);
        ItemMeta Im = s.getItemMeta();
        Im.setUnbreakable(true);
        Im.setDisplayName(loot.getName());
        s.setItemMeta(Im);

        p.getInventory().addItem(s);
    }

}
