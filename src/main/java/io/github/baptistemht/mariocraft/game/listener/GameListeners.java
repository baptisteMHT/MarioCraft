package io.github.baptistemht.mariocraft.game.listener;

import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.game.Loot;
import io.github.baptistemht.mariocraft.game.GameState;
import io.github.baptistemht.mariocraft.util.GameUtils;
import io.github.baptistemht.mariocraft.util.MessageUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class GameListeners implements Listener {

    private final MarioCraft instance;

    public GameListeners(MarioCraft instance){
        this.instance = instance;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e){
        e.setCancelled(true);

        Action action = e.getAction();
        ItemStack s = e.getItem();
        Player p = e.getPlayer();

        if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK){

            if(s == null)return;

            if(s.getType() == Material.DIAMOND_SWORD){
                instance.getDifficultySelectorGUI().openInventory(p);
                return;
            }

            if(s.getType() == Material.BEE_SPAWN_EGG){
                instance.getVehicleSelectorGUI().openInventory(p);
                return;
            }
            if(s.getType() == Material.MUSIC_DISC_WARD){
                instance.getTrackListGUI().openInventory(p);
                return;
            }

            if(Loot.getLootFromName(s.getItemMeta().getDisplayName()) != null){

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        s.setAmount(s.getAmount()-1);
                        p.updateInventory();
                    }
                }.runTaskLater(instance, 1L);

            }

        }
    }



    /***************************LOGIN EVENTS******************************************/     //TODO Keep working on this. Player management during the race and all



    @EventHandler
    public void onLogin(PlayerLoginEvent e){
        switch (instance.getGameState()){
            case INIT:
            case POST_GAME:
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Server not ready.");
                break;
            case PRE_GAME:
                if(instance.getPlayerManager().getData().size() < instance.getPlayerManager().getPlayerLimit()){
                    instance.getPlayerManager().addPlayer(e.getPlayer().getUniqueId());
                }
                break;
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if(instance.getPlayerManager().getPlayer(e.getPlayer().getUniqueId()) != null){
            e.getPlayer().setGameMode(GameMode.ADVENTURE);

            e.setJoinMessage(MessageUtils.getPrefix() + e.getPlayer().getName() + " joined the game! [" + instance.getPlayerManager().getData().size() + "/" + instance.getPlayerManager().getPlayerLimit()+ "]");
            MessageUtils.sendTitle(e.getPlayer().getUniqueId(), ChatColor.YELLOW + "Welcome to " + ChatColor.RED + "MarioCraft" , ChatColor.GRAY + "Made by " + ChatColor.WHITE + "Arakite", 50);
        } else{
            e.getPlayer().setGameMode(GameMode.SPECTATOR);
            e.getPlayer().sendMessage("");
            e.getPlayer().sendMessage(ChatColor.GRAY + "==============================================================");
            e.getPlayer().sendMessage(ChatColor.YELLOW + "               Welcome to " + ChatColor.BOLD + "" + ChatColor.RED+ "MarioCraft"+ ChatColor.YELLOW + "! You are in " + ChatColor.RED + "spectator mode.      ");
            e.getPlayer().sendMessage("");
            e.getPlayer().sendMessage(ChatColor.YELLOW + "You can only communicate with" + ChatColor.RED + " the other spectators.");
            e.getPlayer().sendMessage("");
            e.getPlayer().sendMessage(ChatColor.YELLOW + "Bet for the winner using the following command:");
            e.getPlayer().sendMessage(ChatColor.RED + "/bet <amount> <player>");
            e.getPlayer().sendMessage("");
            e.getPlayer().sendMessage(ChatColor.YELLOW + "Make sure to read the rule using /rules to get more information.");
            e.getPlayer().sendMessage("");
            e.getPlayer().sendMessage(ChatColor.YELLOW + "                           Enjoy watching the game!                   ");
            e.getPlayer().sendMessage(ChatColor.GRAY + "==============================================================");
            e.getPlayer().sendMessage("");
            e.setJoinMessage(null);
        }

        e.getPlayer().getInventory().clear();
        e.getPlayer().setFireTicks(0);
        e.getPlayer().setExp(0);
        e.getPlayer().setFoodLevel(20);
        e.getPlayer().setHealth(e.getPlayer().getHealthScale());

        GameUtils.tpPlayerToLobby(e.getPlayer()); //TODO tp where the show is happening
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        //TODO Quit message only if he was racing. If player and reconnect, set spec until the next race and don't give points.
    }

}
