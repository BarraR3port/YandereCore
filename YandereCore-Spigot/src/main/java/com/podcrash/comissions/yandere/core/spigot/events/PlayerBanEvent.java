package com.podcrash.comissions.yandere.core.spigot.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerBanEvent extends Event implements Cancellable {
    
    
    @Override
    public boolean isCancelled(){
        return false;
    }
    
    @Override
    public void setCancelled(boolean b){
    
    }
    
    @Override
    public HandlerList getHandlers(){
        return null;
    }
}
