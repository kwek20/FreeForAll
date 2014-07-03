package com.division.fearforall.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author Evan
 */
public class PlayerPreCheckEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Player player;

    public PlayerPreCheckEvent(final Player player) {
        this.player = player;
    }
    
    public Player getPlayer(){
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
