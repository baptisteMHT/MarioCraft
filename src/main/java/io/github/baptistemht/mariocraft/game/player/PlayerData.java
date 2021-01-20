package io.github.baptistemht.mariocraft.game.player;

import io.github.baptistemht.mariocraft.game.Vehicle;
import org.bukkit.Material;

public class PlayerData {

    private Vehicle vehicle;
    private int laps;
    private Material lastCheckpoint;
    private int score;

    public PlayerData(){
        vehicle = null;
        laps = 0;
        lastCheckpoint = null;
        score = 0;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }


    public void updateCheckpoint(Material checkpoint){

        if(checkpoint == lastCheckpoint
                || checkpoint == Material.YELLOW_CONCRETE && lastCheckpoint != Material.RED_CONCRETE
                || checkpoint == Material.BLUE_CONCRETE && lastCheckpoint != Material.YELLOW_CONCRETE
                || checkpoint == Material.GREEN_CONCRETE && lastCheckpoint != Material.BLUE_CONCRETE) return;

        if(checkpoint == Material.RED_CONCRETE && lastCheckpoint == null) laps = 1;

        if(checkpoint == Material.RED_CONCRETE && lastCheckpoint == Material.GREEN_CONCRETE) laps++;

        lastCheckpoint = checkpoint;
    }

    public int getLaps() {
        return laps;
    }

    public void cleanRaceData(){
        laps = 0;
        lastCheckpoint = null;
    }


    public int getScore() {
        return score;
    }

    public void addScore(int score) {
        this.score = this.score + score;
    }
}
