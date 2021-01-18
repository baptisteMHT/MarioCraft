package io.github.baptistemht.mariocraft.game;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum Loot {
    MUSHROOM(ChatColor.BLUE + "Mushroom", Material.RED_MUSHROOM_BLOCK),
    BANANA(ChatColor.YELLOW + "Banana", Material.YELLOW_CARPET),
    SQUID(ChatColor.BLACK + "Squid", Material.INK_SAC),
    SWAP(ChatColor.LIGHT_PURPLE + "Swap", Material.AZURE_BLUET);

    final String name;
    final Material material;

    Loot(String name, Material material) {
        this.name = name;
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public static Loot getLootFromName(String name){
        for(Loot l : Loot.values()){
            if(l.getName().equalsIgnoreCase(name))return l;
        }
        return null;
    }
}
