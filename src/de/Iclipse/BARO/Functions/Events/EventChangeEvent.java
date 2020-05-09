package de.Iclipse.BARO.Functions.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventChangeEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private EventState before;
    private EventState after;


    public EventChangeEvent(EventState before, EventState after) {
        super(true);
        this.before = before;
        this.after = after;
    }


    public EventState getBefore() {
        return before;
    }

    public void setBefore(EventState before) {
        this.before = before;
    }

    public EventState getAfter() {
        return after;
    }

    public void setAfter(EventState after) {
        this.after = after;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean cancel) {

    }
}
