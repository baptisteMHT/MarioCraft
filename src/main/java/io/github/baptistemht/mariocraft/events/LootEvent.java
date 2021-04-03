package io.github.baptistemht.mariocraft.events;

import io.github.baptistemht.mariocraft.game.Loot;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LootEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Loot loot;
    private final Player sender;

    public LootEvent(Loot loot, Player sender){
        this.loot = loot;
        this.sender = sender;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Loot getLoot() {
        return loot;
    }

    public Player getSender() {
        return sender;
    }
}
