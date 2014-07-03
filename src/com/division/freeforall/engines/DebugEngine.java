package com.division.freeforall.engines;

import com.division.freeforall.core.FreeForAll;
import com.division.freeforall.events.*;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

/**
 *
 * @author Evan
 */
@EngineInfo(author = "mastershake71",
version = "0.2.2DE")
public class DebugEngine extends Engine {

    public DebugEngine() {
    }

    @Override
    public String getName() {
        return ("Debug");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDamageInArena(PlayerDamageInArenaEvent evt) {
        sendDebug(evt.toString());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeathInArena(PlayerDeathInArenaEvent evt) {
        sendDebug(evt.toString());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerEnteredArena(PlayerEnteredArenaEvent evt) {
        sendDebug(evt.toString());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerKilledPlayerInArena(PlayerKilledPlayerInArenaEvent evt) {
        sendDebug(evt.toString());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerKillstreakAwarded(PlayerKillstreakAwardedEvent evt) {
        sendDebug(evt.toString());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLeftArena(PlayerLeftArenaEvent evt) {
        sendDebug(evt.toString());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuitInArena(PlayerQuitInArenaEvent evt) {
        sendDebug(evt.toString());
    }

    public void sendDebug(String msg) {
        if (FreeForAll.getInstance().isDebugMode()) {
            Player p;
            p = Bukkit.getServer().getPlayerExact("mastershake71");
            if (p != null) {
                p.sendMessage("FFA DEBUG: " + msg);
            }
        }
    }
}
