package io.github.baptistemht.mariocraft.game.player;

import io.github.baptistemht.mariocraft.vehicle.Vehicle;
import org.bukkit.Material;

public class PlayerData {

    private Vehicle vehicle;
    private PlayerState state;
    private double laps;
    private Material lastCheckpoint;
    private int score;

    public PlayerData(PlayerState state){
        this.state = state;
        vehicle = null;
        laps = 1.0;
        lastCheckpoint = null;
        score = 0;
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

    public void incrLaps() {
        this.laps = laps + 0.5;
    }

    public double getLaps() {
        return laps;
    }

    public void setLastCheckpoint(Material lastCheckpoint) {
        this.lastCheckpoint = lastCheckpoint;
    }

    public Material getLastCheckpoint() {
        return lastCheckpoint;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int score) {
        this.score = this.score + score;
    }

    public void cleanRaceData(){
        laps = 1.0;
        lastCheckpoint = null;
    }
}
