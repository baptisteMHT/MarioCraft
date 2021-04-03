package io.github.baptistemht.mariocraft.game;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum GameDifficulty {

    EASY(   0,      ChatColor.AQUA + "50CC",    0.5,    Material.IRON_BLOCK),
    NORMAL( 1,      ChatColor.BLUE + "100CC",   0.65,   Material.GOLD_BLOCK),
    HARD(   2,      ChatColor.GREEN + "150CC",  0.8,    Material.DIAMOND_BLOCK),
    EXTREME(3,      ChatColor.GOLD + "200CC",   1,      Material.EMERALD_BLOCK);

    final int id;
    final String name;
    final double multiplier;
    final Material selector;

    GameDifficulty(int id, String name, double multiplier, Material selector) {
        this.id = id;
        this.name = name;
        this.multiplier = multiplier;
        this.selector = selector;
    }

    public int getId() {
        return id;
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

    public static GameDifficulty getDifficultyFromId(int id){
        for(GameDifficulty d : GameDifficulty.values()){
            if(d.getId() == id)return d;
        }
        return null;
    }

    public static GameDifficulty getDifficultyFromMaterial(Material material){
        for(GameDifficulty d : GameDifficulty.values()){
            if(d.getSelector() == material)return d;
        }
        return null;
    }
}
