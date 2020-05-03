package io.github.baptistemht.mariocraft.command;

import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.game.GameState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StartCommand implements CommandExecutor {

    private MarioCraft instance;

    public StartCommand(MarioCraft instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(instance.getGameState() == GameState.PRE_GAME){
            instance.setupSequence();
        }else{
            commandSender.sendMessage("Start sequence already initiated.");
        }

        return true;
    }
}
