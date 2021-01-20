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
import io.github.baptistemht.mariocraft.game.Vehicle;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
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



        new BukkitRunnable() {
            @Override
            public void run() {

                if(instance.getGameState() != GameState.RACING)return;

                for(UUID id : instance.getPlayerManager().getData().keySet()) {
                    Player p = instance.getServer().getPlayer(id);
                    Entity e = p.getVehicle();
                    if (p == null) return;

                    //BOX DETECTION
                    Collection<Entity> detect = e.getWorld().getNearbyEntities(e.getBoundingBox(), entity -> entity.getType() == EntityType.ENDER_CRYSTAL);

                    if (detect.size() > 0) {
                        for (Entity box : detect) {
                            BoxUtils.loot(p);
                            final Location boxL = box.getLocation();
                            instance.getServer().getScheduler().runTask(instance, () -> box.remove());
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    BoxUtils.generateBox(boxL);
                                }
                            }.runTaskLater(instance, 100L);
                        }
                    }
                }
            }
        }.runTaskTimer(instance, 0L, 2L);

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
        final PlayerData d = instance.getPlayerManager().getPlayer(p.getUniqueId());
        final Location l = p.getLocation();

        final Entity e = p.getVehicle();
        final Vehicle v = Vehicle.getVehicleFromEntityType(e.getType());

        final Material standingOnMaterial = e.getWorld().getBlockAt(e.getLocation().getBlockX(), e.getLocation().getBlockY() -1, e.getLocation().getBlockZ()).getType();
        final Material deepMaterial = e.getWorld().getBlockAt(e.getLocation().getBlockX(), e.getLocation().getBlockY() -2, e.getLocation().getBlockZ()).getType();
        final Material checkpointMaterial = e.getWorld().getBlockAt(e.getLocation().getBlockX(), e.getLocation().getBlockY() -3, e.getLocation().getBlockZ()).getType();

        WrapperPlayClientSteerVehicle wrapper = new WrapperPlayClientSteerVehicle(event.getPacket());

        //CANCEL UNMOUNT
        if(wrapper.isUnmount()) wrapper.setUnmount(false);

        //SET DIFFICULTY
        if(diffMultiplier == 0) diffMultiplier = instance.getDifficulty().getMultiplier();

        //ENTITY ORIENTATION
        p.getVehicle().setRotation(p.getLocation().getYaw(), p.getLocation().getPitch());

        if(!enginesThr.containsKey(p.getUniqueId())) enginesThr.put(p.getUniqueId(), 0);
        if(!enginesRPM.containsKey(p.getUniqueId())) enginesRPM.put(p.getUniqueId(), 0);


        int output = enginesRPM.get(p.getUniqueId());

        if(wrapper.getForward() > 0.1 || wrapper.getForward() < -0.1){

            if(wrapper.getForward() > 0.1){
                enginesThr.replace(p.getUniqueId(), 1);
            }else if(wrapper.getForward() < -0.1){
                enginesThr.replace(p.getUniqueId(), -1);
            }

            output = enginesRPM.get(p.getUniqueId()) + (enginesThr.get(p.getUniqueId()) * v.getAcceleration());

        }else{

            enginesThr.replace(p.getUniqueId(), 0);

            if(output > 0){
                output = enginesRPM.get(p.getUniqueId()) - 4;
            }else if(output < 0){
                output = enginesRPM.get(p.getUniqueId()) + 4;
            }

            if(output < 5 && output > -5){
                output = 0;
            }

        }

        if(output >= 100){
            output = 99;
        } else if(output <= -100){
            output = -99;
        }

        enginesRPM.replace(p.getUniqueId(), output);



        if(wrapper.getSideways() > 0.1){
            //left
        }else if(wrapper.getSideways() < -0.1){
            //right
        }

        if(instance.getGameState() != GameState.RACING) return;

        double xVel = 0.01 * output * TrackUtils.getTrackAdherence(standingOnMaterial) * diffMultiplier * v.getMaxSpeed() * l.getDirection().getX();
        double zVel = 0.01 * output * TrackUtils.getTrackAdherence(standingOnMaterial) * diffMultiplier * v.getMaxSpeed() * l.getDirection().getZ();

        e.setVelocity(new Vector(xVel, -0.2, zVel));


        //CHECKPOINT DETECTION

        if(checkpointMaterial == Material.RED_CONCRETE || checkpointMaterial == Material.BLUE_CONCRETE || checkpointMaterial == Material.YELLOW_CONCRETE || checkpointMaterial == Material.GREEN_CONCRETE){
            d.updateCheckpoint(checkpointMaterial);
        }

    }
}
