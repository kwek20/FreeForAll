/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.fearforall.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author Evan
 */
public class PlayerKilledPlayerInArenaEvent extends Event implements Cancellable {

    public static final HandlerList handlers = new HandlerList();
    private Player killer;
    private Player victim;
    private boolean cancelled;

    public PlayerKilledPlayerInArenaEvent(final Player victim, final Player killer) {
        this.killer = killer;
        this.victim = victim;
        this.cancelled = false;
    }

    @Override
    public String toString() {
        return "PKPIAE: " + victim.getName() + " killed " + killer.getName();
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public Player getVictim() {
        return victim;
    }

    public Player getKiller() {
        return killer;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
