package io.github.baptistemht.mariocraft.command;

import io.github.baptistemht.mariocraft.MarioCraft;
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

        instance.setupSequence();

        return true;
    }
}
