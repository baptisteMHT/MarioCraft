package io.github.baptistemht.mariocraft.game.listener;

import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.game.BoxLoot;
import io.github.baptistemht.mariocraft.game.gui.DifficultySelectorGUI;
import io.github.baptistemht.mariocraft.game.gui.VehicleSelectorGUI;
import io.github.baptistemht.mariocraft.util.BoxUtils;
import io.github.baptistemht.mariocraft.util.LootUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class GameListeners implements Listener {

    private MarioCraft instance;

    public GameListeners(MarioCraft instance){
        this.instance = instance;
    }

    /*
    @EventHandler
    public void onRightClick(PlayerInteractEvent e){
        Action action = e.getAction();
        ItemStack item = e.getItem();
        Player p = e.getPlayer();
        if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK){
            if(item == null)return;
            if(item.getType() == BoxLoot.BANANA.getMaterial()){
                LootUtils.bananaExecutor(p);
            }else if(item.getType() == BoxLoot.MUSHROOM.getMaterial()){
                LootUtils.mushroomExecutor(p);
            }else if(item.getType() == BoxLoot.SQUID.getMaterial()){
                LootUtils.squidExecutor(p);
            }else if(item.getType() == BoxLoot.RED_SHELL.getMaterial()){
                LootUtils.redShellExecutor(p);
            }else if(item.getType() == BoxLoot.GREEN_SHELL.getMaterial()){
                LootUtils.greenShellExecutor(p);
            }

            p.getInventory().remove(item);
            p.updateInventory();
        }
    }
     */

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        instance.getPlayerManager().insertPlayerData(e.getPlayer().getUniqueId());

        new DifficultySelectorGUI().openInventory(e.getPlayer());

        Location l = new Location(e.getPlayer().getWorld(), e.getPlayer().getLocation().getBlockX(), e.getPlayer().getLocation().getBlockY(), e.getPlayer().getLocation().getBlockZ());
        l.setX(l.getX() + 5);
        BoxUtils.generateBox(l);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        instance.getPlayerManager().getDatas().remove(e.getPlayer().getUniqueId());
    }
}
