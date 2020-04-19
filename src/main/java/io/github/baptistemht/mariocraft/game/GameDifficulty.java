package io.github.baptistemht.mariocraft.game;

import org.bukkit.Material;

public enum GameDifficulty {

    EASY("50CC", 0.5, Material.LEATHER_CHESTPLATE),
    NORMAL("100CC", 0.65, Material.IRON_CHESTPLATE),
    HARD("150CC", 0.8, Material.GOLDEN_CHESTPLATE),
    EXTREME("200CC", 1, Material.DIAMOND_CHESTPLATE);

    final String name;
    final double multiplier;
    final Material selector;

    GameDifficulty(String name, double multiplier, Material selector) {
        this.name = name;
        this.multiplier = multiplier;
        this.selector = selector;
    }

    public String getName() {
        return name;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public Material getSelector() {
        return selector;
    }

    public static GameDifficulty getDifficultyFromMaterial(Material material){
        for(GameDifficulty d : GameDifficulty.values()){
            if(d.getSelector() == material)return d;
        }
        return null;
    }
}
