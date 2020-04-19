package io.github.baptistemht.mariocraft.util;

import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.game.BoxLoot;
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

    public static Entity getBox(int x, int z){
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

    public static boolean delBox(int x, int z){
        for(Entity e : MarioCraft.getInstance().getBoxes()){
            if(e.getLocation().getBlockX() == x && e.getLocation().getBlockZ() == z){
                e.remove();
                MarioCraft.getInstance().getBoxes().remove(e);
                return true;
            }
        }
        return false;
    }

    public static ItemStack loot(Player p){
        BoxLoot loot = BoxLoot.values()[new Random().nextInt(BoxLoot.values().length)];
        ItemStack s = new ItemStack(loot.getMaterial(), 1);
        ItemMeta Im = s.getItemMeta();
        Im.setUnbreakable(true);
        Im.setDisplayName(loot.getName());
        s.setItemMeta(Im);

        p.getInventory().addItem(s);

        return s;
    }

    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

}
