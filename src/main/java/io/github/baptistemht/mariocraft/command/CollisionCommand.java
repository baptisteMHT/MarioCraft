package io.github.baptistemht.mariocraft.command;

import io.github.baptistemht.mariocraft.MarioCraft;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CollisionCommand implements CommandExecutor {

    private final MarioCraft instance;

    public CollisionCommand(MarioCraft instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!commandSender.isOp())return false;

        if(instance.isCollision()){
            instance.setCollision(false);
        }else{
            instance.setCollision(true);
        }

        commandSender.sendMessage("Collision: " + instance.isCollision());


        return true;
    }
}
