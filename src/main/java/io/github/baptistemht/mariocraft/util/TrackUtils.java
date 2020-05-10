package io.github.baptistemht.mariocraft.util;

import org.bukkit.Material;

public class TrackUtils {

    public static double getTrackAdherence(Material m){

        switch (m){
            case GRASS_BLOCK:
            case GRASS_PATH:
            case GRAVEL:
            case DIRT:
            case GREEN_CONCRETE:
                return 0.92;

            case GOLD_BLOCK:
                return 1.5;

            default: return 1.0;
        }

    }

}
