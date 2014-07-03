/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.freeforall.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author Evan
 */
public class PlayerDeathInArenaEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Player victim;
    private DeathCause cause;

    public PlayerDeathInArenaEvent(final Player victim, final DeathCause cause) {
        this.victim = victim;
        this.cause = cause;
    }

    @Override
    public String toString() {
        return "PDTIAE: " + victim.getName() + " died because of " + cause.name();
    }

    public Player getVictim() {
        return victim;
    }

    public DeathCause getCause() {
        return cause;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public enum DeathCause {

        ENVIRONMENT,
        PLAYER,
    }
}
