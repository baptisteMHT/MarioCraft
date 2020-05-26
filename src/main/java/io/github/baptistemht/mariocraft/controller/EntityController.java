package io.github.baptistemht.mariocraft.controller;

import com.comphenix.packetwrapper.WrapperPlayClientSteerVehicle;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.game.GameState;
import io.github.baptistemht.mariocraft.game.player.PlayerData;
import io.github.baptistemht.mariocraft.util.BoxUtils;
import io.github.baptistemht.mariocraft.util.TrackUtils;
import io.github.baptistemht.mariocraft.vehicle.Vehicle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class EntityController extends PacketAdapter {

    private final MarioCraft instance;

    private double diffMultiplier = 0;

    private final Map<UUID, Integer> enginesRPM;
    private final Map<UUID, Integer> enginesThr;

    private final Map<UUID, Location> lastBox;
    private final Map<UUID, Location> lastLocation;

    public EntityController(MarioCraft plugin, ListenerPriority listenerPriority, PacketType... types) {
        super(plugin, listenerPriority, types);

        instance = plugin;
        enginesRPM = new HashMap<>();
        enginesThr = new HashMap<>();
        lastBox = new HashMap<>();

        lastLocation = new HashMap<>();

        /*
        new BukkitRunnable() {
            @Override
            public void run() {

                if(instance.getGameState() != GameState.RACING)return;

                for(UUID id : instance.getPlayerManager().getPlayersData().keySet()){
                    Player p = Bukkit.getPlayer(id);
                    if(lastLocation.containsKey(id)){
                        Material trackCheckerMaterial = p.getVehicle().getWorld().getBlockAt(p.getVehicle().getLocation().getBlockX(), p.getVehicle().getLocation().getBlockY() -3, p.getVehicle().getLocation().getBlockZ()).getType();

                        if(!trackCheckerMaterial.equals(Material.OBSIDIAN)){
                            instance.getLogger().info("OUT");

                            enginesRPM.replace(p.getUniqueId(), 0);

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    PotionEffect effect = new PotionEffect(PotionEffectType.BLINDNESS, 40, 2, true, true);
                                    p.addPotionEffect(effect);
                                    p.getVehicle().teleport(lastLocation.get(p.getUniqueId()));
                                    instance.getLogger().info(lastLocation.get(p.getUniqueId()).getBlockX() + " " + lastLocation.get(p.getUniqueId()).getBlockY() + " " + lastLocation.get(p.getUniqueId()).getBlockZ());
                                }
                            }.runTask(instance);

                        }else{
                            instance.getLogger().info("IN");

                            lastLocation.remove(id);
                            lastLocation.put(id, p.getLocation());
                        }
                    }else{
                        lastLocation.put(id, p.getLocation());
                    }
                }

            }
        }.runTaskTimerAsynchronously(instance, 20L, 10L);

         */
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        final Player p = event.getPlayer();
        final PlayerData d = instance.getPlayerManager().getPlayerData(p.getUniqueId());
        final Location l = p.getLocation();

        final Entity e = p.getVehicle();
        final Vehicle v = Vehicle.getVehicleFromEntityType(e.getType());

        final Material standingOnMaterial = e.getWorld().getBlockAt(e.getLocation().getBlockX(), e.getLocation().getBlockY() -1, e.getLocation().getBlockZ()).getType();
        final Material deepMaterial = e.getWorld().getBlockAt(e.getLocation().getBlockX(), e.getLocation().getBlockY() -2, e.getLocation().getBlockZ()).getType();

        WrapperPlayClientSteerVehicle wrapper = new WrapperPlayClientSteerVehicle(event.getPacket());

        if(diffMultiplier == 0)diffMultiplier = instance.getDifficulty().getMultiplier();

        //ENTITY ORIENTATION
        p.getVehicle().setRotation(p.getLocation().getYaw(), p.getLocation().getPitch());


        if(instance.getGameState() != GameState.RACING) return;

        if(!enginesThr.containsKey(p.getUniqueId())) enginesThr.put(p.getUniqueId(), 0);
        if(!enginesRPM.containsKey(p.getUniqueId())) enginesRPM.put(p.getUniqueId(), 0);

        int out = enginesRPM.get(p.getUniqueId());

        if(wrapper.getForward() > 0.1 || wrapper.getForward() < -0.1){

            if(wrapper.getForward() > 0.1){
                enginesThr.replace(p.getUniqueId(), 1);
            }else if(wrapper.getForward() < -0.1){
                enginesThr.replace(p.getUniqueId(), -1);
            }

            out = enginesRPM.get(p.getUniqueId()) + (enginesThr.get(p.getUniqueId()) * v.getAcceleration());

        }else{

            enginesThr.replace(p.getUniqueId(), 0);

            if(out > 0){
                out = enginesRPM.get(p.getUniqueId()) - 4;
            }else if(out < 0){
                out = enginesRPM.get(p.getUniqueId()) + 4;
            }

            if(out < 5 && out > -5){
                out = 0;
            }

        }

        if(out >= 100){
            out = 99;
        } else if(out <= -100){
            out = -99;
        }

        enginesRPM.replace(p.getUniqueId(), out);

        //instance.getLogger().info("Id: " + p.getUniqueId() + " || Throttle: " + enginesThr.get(p.getUniqueId()) + " || RPM: " + out);





        if(wrapper.getSideways() > 0.1){
            //left
        }else if(wrapper.getSideways() < -0.1){
            //right
        }





        double xVel = 0.01 * out * TrackUtils.getTrackAdherence(standingOnMaterial) * diffMultiplier * d.getBoost() * v.getMaxSpeed() * l.getDirection().getX();
        double zVel = 0.01 * out * TrackUtils.getTrackAdherence(standingOnMaterial) * diffMultiplier * d.getBoost() * v.getMaxSpeed() * l.getDirection().getZ();

        e.setVelocity(new Vector(xVel, -0.2, zVel));



        //CHECKPOINT DETECTION
        if(deepMaterial == Material.BLACK_CONCRETE || deepMaterial == Material.YELLOW_CONCRETE){
            if(d.getLastCheckpoint() == null && d.getLaps() == 1.0){
                d.setLastCheckpoint(deepMaterial);
            }else if(deepMaterial != d.getLastCheckpoint()){
                d.incrLaps();
                d.setLastCheckpoint(deepMaterial);
            }
        }



        //BOX DETECTION
        for(int x = l.getBlockX() - 1; x <= l.getBlockX() + 1; x++) {
            for(int y = l.getBlockY(); y <= l.getBlockY() + 1; y++) {
                for(int z = l.getBlockZ() - 1; z <= l.getBlockZ() + 1; z++) {

                    Entity box = BoxUtils.getBox(x, z);

                    if(box != null){
                        if(!lastBox.containsKey(p.getUniqueId()) || (lastBox.containsKey(p.getUniqueId()) && x != lastBox.get(p.getUniqueId()).getBlockX() && z != lastBox.get(p.getUniqueId()).getBlockZ())){

                            final int x1 = x, y1 = y, z1 = z;

                            lastBox.remove(p.getUniqueId());
                            lastBox.put(p.getUniqueId(), box.getLocation());

                            BoxUtils.loot(p);

                            instance.getServer().getScheduler().runTask(instance, () -> BoxUtils.delBox(x1, z1));
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    lastBox.remove(p.getUniqueId());
                                    BoxUtils.generateBox(new Location(p.getWorld(), x1, y1, z1));
                                }
                            }.runTaskLater(instance, 100L);

                        }
                    }
                }
            }
        }

    }
}
