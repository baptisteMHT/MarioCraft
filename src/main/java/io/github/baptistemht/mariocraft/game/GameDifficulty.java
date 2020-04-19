package io.github.baptistemht.mariocraft.game;

public enum GameDifficulty {

    EASY("50CC", 0.5),
    NORMAL("100CC", 0.65),
    HARD("150CC", 0.8),
    EXTREME("200CC", 1);

    final String name;
    final double multiplier;

    GameDifficulty(String name, double multiplier) {
        this.name = name;
        this.multiplier = multiplier;
    }

    public String getName() {
        return name;
    }

    public double getMultiplier() {
        return multiplier;
    }
}
