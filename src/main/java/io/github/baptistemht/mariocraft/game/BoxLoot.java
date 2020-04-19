package io.github.baptistemht.mariocraft.game;

import org.bukkit.Material;

public enum BoxLoot {
    MUSHROOM("Mushroom", Material.RED_MUSHROOM_BLOCK),
    GREEN_SHELL("Green Shell", Material.GREEN_CONCRETE),
    RED_SHELL("Red Shell", Material.RED_CONCRETE),
    BANANA("Banana", Material.YELLOW_DYE),
    SQUID("Squid", Material.INK_SAC);

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
}
