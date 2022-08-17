package com.podcrash.commissions.yandere.core.spigot.events;

import com.podcrash.commissions.yandere.core.spigot.Main;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class YandereCoreLoadedEvent extends Event {
    
    private static final HandlerList HANDLERS = new HandlerList();
    
    private final Main plugin;
    
    public YandereCoreLoadedEvent(Main yandereCore){
        this.plugin = yandereCore;
    }
    
    public static HandlerList getHandlerList(){
        return HANDLERS;
    }
    
    public Main getPlugin(){
        return plugin;
    }
    
    @Override
    public HandlerList getHandlers(){
        return HANDLERS;
    }
}