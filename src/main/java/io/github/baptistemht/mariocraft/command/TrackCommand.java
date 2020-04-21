package io.github.baptistemht.mariocraft.command;

import io.github.baptistemht.mariocraft.MarioCraft;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TrackCommand implements CommandExecutor {

    private final MarioCraft instance;

    public TrackCommand(MarioCraft instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!commandSender.isOp())return false;
        commandSender.sendMessage("Reloading tracks.");
        instance.getTracksManager().reloadTracks();
        return true;
    }
}
