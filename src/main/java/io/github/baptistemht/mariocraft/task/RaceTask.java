package io.github.baptistemht.mariocraft.task;

import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.game.GameState;
import io.github.baptistemht.mariocraft.game.player.PlayerData;
import io.github.baptistemht.mariocraft.track.Track;
import io.github.baptistemht.mariocraft.util.GameUtils;
import io.github.baptistemht.mariocraft.util.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;

public class RaceTask {

    private final MarioCraft instance;

    private int countdown;

    private final Set<UUID> pilots;
    private final Map<String, String> raceData;
    private final List<UUID> finishers;

    private final Track t;

    private long time;

    public RaceTask(MarioCraft instance, Track t) {
        this.instance = instance;
        this.t = t;

        pilots = new HashSet<>();
        raceData = new HashMap<>();
        finishers = new ArrayList<>();

        pilots.addAll(instance.getPlayerManager().getPlayersData().keySet());

        countdown = 10;

        new BukkitRunnable() {
            @Override
            public void run() {
                if(countdown == 10){
                    MessageUtils.sendTitleToPlayers(ChatColor.GREEN + t.getName(), ChatColor.GRAY + "" +  t.getLaps() + " laps | " + instance.getDifficulty().getName(), 80);
                }else if(countdown <= 3 && countdown > 0){
                    MessageUtils.sendTitleToPlayers(ChatColor.YELLOW + "" + countdown, null, 20);
                }else if(countdown == 0){
                    MessageUtils.sendTitleToPlayers(ChatColor.YELLOW + "GO!", null, 20);
                    instance.setGameState(GameState.RACING);
                    clock();
                    this.cancel();
                }

                countdown--;
            }
        }.runTaskTimer(instance, 20L, 20L);
    }


    private void clock(){
        time = System.currentTimeMillis();
        for(UUID id : pilots){
            raceData.put(id.toString() + ":lap", 1 + "");
            raceData.put(id.toString() + ":elapsedTime", time + "");
        }

        new BukkitRunnable() {
            @Override
            public void run() {

                for(UUID id : pilots){
                    PlayerData d = instance.getPlayerManager().getPlayerData(id);

                    if(!finishers.contains(id)){
                        int l = d.getLaps();

                        if(l > Integer.parseInt(raceData.get(id.toString() + ":lap"))){
                            raceData.replace(id.toString() + ":lap", l + "");

                            long lastElapsedTime = Long.parseLong(raceData.get(id.toString() + ":elapsedTime"));
                            long lapTime = System.currentTimeMillis() - lastElapsedTime;

                            raceData.put(id.toString() + ":lapTime:" + l, lapTime + "");
                            raceData.replace(id.toString() + ":elapsedTime", System.currentTimeMillis() + "");

                            if(l < t.getLaps()+1){
                                MessageUtils.sendTitle(id," ", "Lap " + l +  "/" + t.getLaps() + " | " + formatTime(lapTime), 50); //ADD FORMAT AND COLOR
                            }
                        }


                        if(l == t.getLaps() + 1){
                            raceData.put(id.toString() + ":time", (System.currentTimeMillis() - time) + "");
                            finishers.add(id);
                            MessageUtils.sendTitle(id,ChatColor.YELLOW + "FINISH LINE", ChatColor.WHITE + "You are "+ finishers.size() + "/" + pilots.size() + " | " + formatTime(Long.parseLong(raceData.get(id + ":time"))), 100);
                        }
                    }
                }

                if(finishers.size() == pilots.size()){
                    Bukkit.broadcastMessage(MessageUtils.getPrefix() + ChatColor.GOLD + "" + Bukkit.getPlayer(finishers.get(0)).getDisplayName() + ChatColor.GRAY + " won the race in " + formatTime(Long.parseLong(raceData.get(finishers.get(0).toString() + ":time"))) +" !");

                    instance.updateRaceCount();

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            for(UUID id : pilots){
                                instance.getPlayerManager().getPlayerData(id).cleanRaceData();
                            }
                            GameUtils.tpAllToLobby();
                            t.reset();
                            t.load();
                            if(instance.getRaceCount()-1 > 0){
                                instance.setGameState(GameState.SELECTION);
                                new TrackSelectionTask(instance);
                            }else{
                                //finish
                                instance.setGameState(GameState.POST_GAME);
                            }

                        }
                    }.runTaskLater(instance, 160L);

                    registerRaceData();

                    this.cancel();
                }
            }

        }.runTaskTimerAsynchronously(instance, 0L, 2L);
    }

    private void registerRaceData(){
        //DATABASE PUSH
    }

    private String formatTime(long time){
        SimpleDateFormat format = new SimpleDateFormat("mm:ss:SSS");
        Date d = new Date(time);
        return format.format(d);
    }
}
