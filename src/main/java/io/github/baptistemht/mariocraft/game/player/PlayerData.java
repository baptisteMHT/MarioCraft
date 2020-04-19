package io.github.baptistemht.mariocraft.game.player;

import io.github.baptistemht.mariocraft.vehicle.Vehicle;

public class PlayerData {

    private Vehicle vehicle;

    public PlayerData(){

    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}
