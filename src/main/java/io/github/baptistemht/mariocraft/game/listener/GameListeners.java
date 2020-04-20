package io.github.baptistemht.mariocraft.game.listener;

import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.game.BoxLoot;
import io.github.baptistemht.mariocraft.game.GameState;
import io.github.baptistemht.mariocraft.game.gui.DifficultySelectorGUI;
import io.github.baptistemht.mariocraft.game.gui.VehicleSelectorGUI;
import io.github.baptistemht.mariocraft.game.player.PlayerState;
import io.github.baptistemht.mariocraft.util.BoxUtils;
import io.github.baptistemht.mariocraft.util.LootUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class GameListeners implements Listener {

    private MarioCraft instance;

    public GameListeners(MarioCraft instance){
        this.instance = instance;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e){
        Action action = e.getAction();
        ItemStack item = e.getItem();
        Player p = e.getPlayer();

        if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK){

            if(item == null)return;

            if(item.getType() == BoxLoot.SQUID.getMaterial()){
                LootUtils.squidExecutor(p);
            }

            p.updateInventory();

            new BukkitRunnable() {
                @Override
                public void run() {
                    p.getInventory().remove(item);
                }
            }.runTaskLater(instance, 1L);
        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e){
        switch (instance.getGameState()){
            case INIT:
            case POST_GAME:
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Server not ready.");
                break;
            case PRE_GAME:
                instance.getPlayerManager().insertPlayerData(e.getPlayer().getUniqueId(), PlayerState.PLAYER);
                break;
            case GAME:
                if(!instance.getPlayerManager().getData().containsKey(e.getPlayer().getUniqueId())){
                    instance.getPlayerManager().insertPlayerData(e.getPlayer().getUniqueId(), PlayerState.SPECTATOR);
                }
                break;
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if(instance.getPlayerManager().getPlayerData(e.getPlayer().getUniqueId()).getState() == PlayerState.PLAYER){
            if(instance.getGameState() == GameState.GAME)return;
            e.getPlayer().setGameMode(GameMode.SURVIVAL);
            new DifficultySelectorGUI().openInventory(e.getPlayer());

            Location l = new Location(e.getPlayer().getWorld(), e.getPlayer().getLocation().getBlockX(), e.getPlayer().getLocation().getBlockY(), e.getPlayer().getLocation().getBlockZ());
            l.setX(l.getX() + 5);
            BoxUtils.generateBox(l);
        } else{
            e.getPlayer().setGameMode(GameMode.SPECTATOR);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        switch (instance.getGameState()){
            case PRE_GAME:
            case POST_GAME:
                instance.getPlayerManager().getData().remove(e.getPlayer().getUniqueId());
                break;
        }
    }
}
