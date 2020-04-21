package io.github.baptistemht.mariocraft.task;

import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.game.GameDifficulty;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Collections;

public class DifficultyVoteTask {

    private MarioCraft instance;

    private int i;

    public DifficultyVoteTask(MarioCraft instance){
        this.instance = instance;

        i = 60;

        new BukkitRunnable() {
            @Override
            public void run() {
                if(i == 60) {
                    Bukkit.broadcastMessage("Difficulty vote started! 60 seconds before the vote closes.");
                }

                for(Player p : Bukkit.getOnlinePlayers()){
                    p.setExp(i);
                }

                if(MarioCraft.getInstance().getVotes().size() >= MarioCraft.getInstance().getPlayerManager().getPlayersData().size() || i == 0){

                    calculateDiff();

                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.getInventory().clear();
                        p.updateInventory();
                    }

                    this.cancel();
                }

                i--;
            }
        }.runTaskTimer(instance, 0L, 20L);
    }

    private void calculateDiff(){

        int a = 0, b = 0, c = 0, d = 0;

        for(GameDifficulty diff : instance.getVotes()){
            switch (diff){
                case EASY:
                    a++;
                case NORMAL:
                    b++;
                case HARD:
                    c++;
                case EXTREME:
                    d++;
            }
        }

        int largest = Collections.max(Arrays.asList(a, b, c, d));

        if(largest != 0){
            if(largest == a){
                instance.setDifficulty(GameDifficulty.EASY);
            }else if(largest == b){
                instance.setDifficulty(GameDifficulty.NORMAL);
            }else if(largest == c){
                instance.setDifficulty(GameDifficulty.HARD);
            }else if(largest == d){
                instance.setDifficulty(GameDifficulty.EXTREME);
            }
        }else{
            instance.setDifficulty(GameDifficulty.NORMAL);
        }

        new VehicleSelectorTask(instance);

        Bukkit.broadcastMessage("[MarioCraft] Vote closed! Difficulty set to " + instance.getDifficulty().getName());
    }
}
