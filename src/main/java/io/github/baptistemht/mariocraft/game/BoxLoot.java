package io.github.baptistemht.mariocraft.game;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum BoxLoot {
    MUSHROOM(ChatColor.BLUE + "Mushroom", Material.RED_MUSHROOM_BLOCK),
    GREEN_SHELL(ChatColor.GREEN + "Green Shell", Material.GREEN_CONCRETE),
    RED_SHELL(ChatColor.RED + "Red Shell", Material.RED_CONCRETE),
    BANANA(ChatColor.YELLOW + "Banana", Material.YELLOW_CARPET),
    SQUID(ChatColor.BLACK + "Squid", Material.INK_SAC);

    final String name;
    final Material material;

    BoxLoot(String name, Material material) {
        this.name = name;
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public static BoxLoot getLootFromName(String name){
        for(BoxLoot l : BoxLoot.values()){
            if(l.getName().equalsIgnoreCase(name))return l;
        }
        return null;
    }
}
