package com.podcrash.commissions.yandere.core.spigot.events;

import com.podcrash.commissions.yandere.core.common.data.level.Level;
import com.podcrash.commissions.yandere.core.common.data.server.ChangeType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class PlayerLevelChangeEvent extends Event implements Cancellable {
    
    private static final HandlerList HANDLERS = new HandlerList();
    
    private final Player player;
    private final Level level;
    private final ChangeType type;
    protected boolean canceled;
    
    
    /**
     * Called when a player levels up.
     * This only works when the internal Level System is used.
     * Developers can "inject" their own level system.
     *
     * @param player - target player.
     * @param level  - gained level.
     */
    public PlayerLevelChangeEvent(Player player, Level level, ChangeType type){
        this.player = player;
        this.level = level;
        this.type = type;
    }
    
    public static HandlerList getHandlerList(){
        return HANDLERS;
    }
    
    /**
     * Get player.
     */
    public Player getPlayer(){
        return player;
    }
    
    /**
     * Get new player level.
     */
    public Level getLevel(){
        return this.level;
    }
    
    /**
     * Get new player level.
     */
    public ChangeType getChangeType(){
        return this.type;
    }
    
    @Override
    public HandlerList getHandlers(){
        return HANDLERS;
    }
    
    @Override
    public boolean isCancelled(){
        return canceled;
    }
    
    @Override
    public void setCancelled(boolean canceled){
        this.canceled = canceled;
    }
}