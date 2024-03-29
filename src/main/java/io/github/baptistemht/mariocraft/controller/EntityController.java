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
    private final Map<UUID, Integer[]> lastLocation;
    private final Map<UUID, Long> lastBox;

    public EntityController(MarioCraft plugin, ListenerPriority listenerPriority, PacketType... types) {
        super(plugin, listenerPriority, types);

        instance = plugin;
        enginesRPM = new HashMap<>();
        enginesThr = new HashMap<>();
        lastLocation = new HashMap<>();
        lastBox = new HashMap<>();

        new BukkitRunnable() {
            @Override
            public void run() {

                if(instance.getGameState() != GameState.RACING)return;

                for(UUID id : instance.getPlayerManager().getData().keySet()) {
                    Player p = instance.getServer().getPlayer(id);
                    if (p == null) return;

                    Entity e = p.getVehicle();
                    if(e == null) return;

                    if(lastBox.get(p.getUniqueId()) != null && System.currentTimeMillis() - lastBox.get(p.getUniqueId()) < 2000) return;

                    Collection<Entity> detect = e.getWorld().getNearbyEntities(e.getBoundingBox(), entity -> entity.getType() == EntityType.ENDER_CRYSTAL);

                    if (detect.size() > 0) {
                        Entity box = (Entity) detect.toArray()[0];
                        BoxUtils.loot(p);
                        Location boxL = box.getLocation();
                        box.remove();
                        lastBox.put(p.getUniqueId(), System.currentTimeMillis());
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                BoxUtils.generateBox(boxL);
                            }
                        }.runTaskLater(instance, 100L);
                    }
                }
            }
        }.runTaskTimer(instance, 0L, 2L);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {

        final Player p = event.getPlayer();

        final PlayerData d = instance.getPlayerManager().getPlayer(p.getUniqueId());
        if(d == null) return;

        final Location l = p.getLocation();

        final Entity e = p.getVehicle();
        if(e == null) return;

        final Vehicle v = Vehicle.getVehicleFromEntityType(e.getType());
        if(v == null) return;

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


        //BANANA DETECTION

        if(standingOnMaterial == Material.YELLOW_WOOL){
            enginesRPM.replace(p.getUniqueId(), 1);
            p.getWorld().getBlockAt(l.add(0,-1,0)).setType(p.getWorld().getBlockAt(l.add(0,-4,0)).getType());
        }


        //FALL DOWN DETECTION

        if(deepMaterial != Material.OBSIDIAN && standingOnMaterial != Material.AIR && standingOnMaterial != Material.WATER){
            new BukkitRunnable() {
                @Override
                public void run() {
                    restorePlayer(p);
                }
            }.runTask(instance);
        }else{
            Integer[] loc = {l.getBlockX(), l.getBlockY(), l.getBlockZ()};
            Integer[] last = lastLocation.get(p.getUniqueId());

            if(last != null && new Location(l.getWorld(), last[0], last[1], last[2]).distance(l) < 3) return;

            int i = loc[0]-2, j = loc[2]-2;
            boolean fine = true;

            while(j < loc[2]+3 && fine){

                if(l.getWorld().getBlockAt(i,loc[1]-2,j).getType() != Material.OBSIDIAN) fine = false;

                i++;
                if(i == loc[0]+3){
                    i = loc[0]-2;
                    j++;
                }
            }

            if(fine)    lastLocation.put(p.getUniqueId(), loc);
        }

    }

    private void restorePlayer(Player p){
        enginesRPM.replace(p.getUniqueId(), 0);
        Integer[] restore = lastLocation.get(p.getUniqueId());
        if(restore==null) return;

        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 1, true));

        p.getVehicle().remove();
        p.teleport(new Location(p.getWorld(), restore[0], restore[1]+2, restore[2]));
        instance.getPlayerManager().getPlayer(p.getUniqueId()).getVehicle().summon(p);
        //SOUND
    }
}
