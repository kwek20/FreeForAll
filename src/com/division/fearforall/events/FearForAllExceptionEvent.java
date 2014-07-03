package com.division.fearforall.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author Evan
 */
public class FearForAllExceptionEvent extends Event {

    private static HandlerList handlers = new HandlerList();
    private Exception ex;

    public FearForAllExceptionEvent(final Exception ex) {
        this.ex = ex;
    }

    public Exception getException() {
        return ex;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
