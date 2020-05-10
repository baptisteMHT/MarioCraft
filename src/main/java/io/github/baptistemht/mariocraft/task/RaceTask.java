package io.github.baptistemht.mariocraft.task;

import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.game.GameState;
import io.github.baptistemht.mariocraft.game.player.PlayerData;
import io.github.baptistemht.mariocraft.track.Track;
import io.github.baptistemht.mariocraft.util.GameUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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

        countdown = 6;

        new BukkitRunnable() {
            @Override
            public void run() {
                if(countdown == 6){
                    sendTitle(ChatColor.GREEN + t.getName(), ChatColor.GRAY + "" +  t.getLaps() + " laps | " + instance.getDifficulty().getName(), 50);
                }else if(countdown <= 3 && countdown > 0){
                    sendTitle(ChatColor.YELLOW + "" + countdown, null, 20);
                }else if(countdown == 0){
                    sendTitle(ChatColor.YELLOW + "GO!", null, 20);
                    instance.setGameState(GameState.RACING);
                    clock();
                    this.cancel();
                }

                countdown--;
            }
        }.runTaskTimer(instance, 20L, 20L);
    }

    private void registerRaceData(){
        //REDIS PUSH
    }

    private void clock(){
        time = System.currentTimeMillis();
        for(UUID id : pilots){
            raceData.put(id.toString() + ":lap", 1.0 + "");
            raceData.put(id.toString() + ":elapsedTime", 0 + "");
        }

        new BukkitRunnable() {
            @Override
            public void run() {

                for(UUID id : pilots){
                    PlayerData d = instance.getPlayerManager().getPlayerData(id);

                    if(!finishers.contains(id)){
                        double l = d.getLaps();

                        instance.getLogger().log(Level.INFO, "Check: " + l);

                        if(l == (Double.parseDouble(raceData.get(id.toString() + ":lap")) + 1.0)){
                            raceData.replace(id.toString() + ":lap", l + "");

                            instance.getLogger().log(Level.INFO, "Lap updated " + l);

                            long lastElapsedTime = Long.parseLong(raceData.get(id.toString() + ":elapsedTime"));
                            long lapTime = System.currentTimeMillis() - lastElapsedTime;

                            raceData.put(id.toString() + ":lapTime:" + l, lapTime + "");
                            raceData.replace(id.toString() + ":elapsedTime", (System.currentTimeMillis() - time) + "");

                            sendTitle(id," ", "Lap " + l +  "/" + t.getLaps() + " | " + lapTime, 20); //ADD FORMAT AND COLOR
                        }


                        if(l == (t.getLaps() + 1.0)){
                            raceData.put(id.toString() + ":time", (System.currentTimeMillis() - time) + "");
                            finishers.add(id);
                            d.cleanRaceData();
                            sendTitle(id,ChatColor.YELLOW + "FINISH LINE", ChatColor.WHITE + "You are " + ChatColor.YELLOW + "" +finishers.size() + ChatColor.WHITE + "/" + pilots.size(), 100);

                            if(finishers.size() >= pilots.size()){
                                Bukkit.broadcastMessage("[MarioCraft] " + Bukkit.getPlayer(finishers.get(0)).getDisplayName() + " won the race!");

                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        instance.setGameState(GameState.SELECTION);
                                        GameUtils.tpAllToLobby();
                                        t.reset();
                                        new TrackSelectionTask(instance);
                                    }
                                }.runTaskLater(instance, 160L);

                                registerRaceData();

                                this.cancel();
                            }
                        }
                    }
                }
            }

        }.runTaskTimerAsynchronously(instance, 0L, 2L);
    }

    private void sendTitle(UUID id, String title, String subtitle, int duration){
        Player p = Bukkit.getPlayer(id);
        if(p != null) p.sendTitle(title, subtitle, 2, duration, 2);
    }

    private void sendTitle(String title, String subtitle, int duration){
        for(UUID id : pilots){
            sendTitle(id, title, subtitle, duration);
        }
    }
}
