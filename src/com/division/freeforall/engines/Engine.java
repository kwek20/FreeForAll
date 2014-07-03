package com.division.freeforall.engines;

import org.bukkit.event.Listener;

/**
 *
 * @author Evan
 */
public abstract class Engine implements Listener {

    public abstract String getName();

    public void runStartupChecks() throws EngineException {
    }
}
