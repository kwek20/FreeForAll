/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.fearforall.events;

import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Evan
 */
public class PlayerKillstreakAwardedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private int killStreak;
    private Player player;
    private ArrayList<ItemStack> rewards;

    public PlayerKillstreakAwardedEvent(final Player p, final int killstreak, ArrayList<ItemStack> rewards) {
        this.killStreak = killstreak;
        this.player = p;
        this.rewards = rewards;
    }

    @Override
    public String toString() {
        return "PKSAE: " + player.getName() + " achieved KS " + killStreak + " and received " + rewards;
    }

    public Player getPlayer() {
        return player;
    }

    public int getKillStreak() {
        return killStreak;
    }

    public ArrayList<ItemStack> getRewards() {
        return rewards;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
