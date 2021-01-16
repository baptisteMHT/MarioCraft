package io.github.baptistemht.mariocraft.game.player;

import io.github.baptistemht.mariocraft.vehicle.Vehicle;
import org.bukkit.Material;

public class PlayerData {

    private Vehicle vehicle;
    private PlayerState state;
    private int laps;
    private Material lastCheckpoint;
    private int score;
    private double boost;

    public PlayerData(PlayerState state){
        this.state = state;
        vehicle = null;
        laps = 0;
        lastCheckpoint = null;
        score = 0;
        boost = 1.0;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public PlayerState getState() {
        return state;
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

    public int getScore() {
        return score;
    }

    public void addScore(int score) {
        this.score = this.score + score;
    }

    public double getBoost() {
        return boost;
    }

    public void setBoost(double boost) {
        this.boost = boost;
    }

    public void cleanRaceData(){
        laps = 0;
        lastCheckpoint = null;
    }
}
