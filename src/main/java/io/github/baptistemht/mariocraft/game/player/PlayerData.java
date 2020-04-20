package io.github.baptistemht.mariocraft.game.player;

import io.github.baptistemht.mariocraft.vehicle.Vehicle;

public class PlayerData {

    private Vehicle vehicle;
    private PlayerState state;

    public PlayerData(PlayerState state){
        this.state = state;
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
}
