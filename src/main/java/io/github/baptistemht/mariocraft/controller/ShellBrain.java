package io.github.baptistemht.mariocraft.controller;

import io.github.baptistemht.mariocraft.MarioCraft;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ShellBrain {

    private int i = 200;
    private int bounce = 3;

    public ShellBrain(Entity e, Vector velocity, Boolean red, float yaw, float pitch){

        new BukkitRunnable() {
            @Override
            public void run() {
                i--;

                e.setVelocity(velocity);

                e.setRotation(yaw, pitch);

                if(bounce == 0){
                    e.remove();
                }

                if(i == 0){
                    e.remove();
                }

            }
        }.runTaskTimer(MarioCraft.getInstance(), 0L, 1L);
    }

}
