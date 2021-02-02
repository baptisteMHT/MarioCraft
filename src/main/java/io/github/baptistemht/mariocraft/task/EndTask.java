package io.github.baptistemht.mariocraft.task;

import io.github.baptistemht.mariocraft.MarioCraft;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class EndTask {

    public EndTask(MarioCraft instance){

        Location podium = instance.getPodium();

        for(int i = 0; i<instance.getLeaderboard().size(); i++){
            Player p = instance.getServer().getPlayer(instance.getLeaderboard().get(i));
            if(p==null) return;

            switch (i){
                case 0:
                    p.teleport(podium);
                    break;
                case 1:
                    p.teleport(podium.add(-3,0,0));
                    break;
                case 2:
                    p.teleport(podium.add(3,0,0));
                    break;
                default:
                    p.teleport(instance.getHub());
            }
        }

        //Write messages + FX.

        new BukkitRunnable() {
            @Override
            public void run() {
                instance.getServer().shutdown();
            }
        }.runTaskLater(instance, 20*1200);

    }

}
