package io.github.baptistemht.mariocraft.game;

import org.bukkit.Material;

public enum GameDifficulty {

    EASY("50CC", 0.5, Material.IRON_BLOCK),
    NORMAL("100CC", 0.65, Material.GOLD_BLOCK),
    HARD("150CC", 0.8, Material.DIAMOND_BLOCK),
    EXTREME("200CC", 1, Material.EMERALD);

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
