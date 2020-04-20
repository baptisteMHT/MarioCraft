package io.github.baptistemht.mariocraft.util;

import org.bukkit.Material;

public class TrackUtils {

    public static double getTrackAdherenceMultiplierFromMaterial(Material m){

        switch (m){
            case GRASS_BLOCK:
            case GRASS_PATH:
            case GRAVEL:
            case DIRT:
            case GREEN_CONCRETE:
                return 0.90;

            case BLACK_CONCRETE:
            case LIGHT_GRAY_CONCRETE:
            case GRAY_CONCRETE:
            case YELLOW_CONCRETE:
                return 0.95;
            default: return 1.0;
        }

    }

}
