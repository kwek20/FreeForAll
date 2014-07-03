/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.fearforall.events;

import static com.division.fearforall.util.LocationTools.toVector;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author Evan
 */
public class PlayerQuitInArenaEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Player player;

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public String toString() {
        return "PQIAE: " + player.getName() + " quit arena at " + ChatColor.LIGHT_PURPLE + toVector(player.getLocation());
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public PlayerQuitInArenaEvent(Player p) {
        this.player = p;
    }

    public Player getPlayer() {
        return player;
    }
}
