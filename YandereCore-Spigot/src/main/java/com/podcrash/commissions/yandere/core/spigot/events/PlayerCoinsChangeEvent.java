package com.podcrash.commissions.yandere.core.spigot.events;

import com.podcrash.commissions.yandere.core.common.data.server.ChangeType;
import com.podcrash.commissions.yandere.core.common.data.server.ServerType;
import com.podcrash.commissions.yandere.core.common.data.user.props.GainSource;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerCoinsChangeEvent extends Event implements Cancellable {
    
    private static final HandlerList HANDLERS = new HandlerList();
    
    private final Player player;
    private final ChangeType type;
    private final GainSource source;
    private final ServerType serverSource;
    private long amount;
    private boolean cancelled;
    
    /**
     * Called when a player receives new xp.
     * This only works when the internal Level System is used.
     * Developers can "inject" their own level system.
     *
     * @param player - target player.
     * @param amount - amount of xp.
     * @param source - where did the player receive xp from.
     */
    public PlayerCoinsChangeEvent(Player player, long amount, GainSource source, ServerType serverSource, ChangeType type){
        this.player = player;
        this.amount = amount;
        this.source = source;
        this.serverSource = serverSource;
        this.type = type;
    }
    
    public static HandlerList getHandlerList(){
        return HANDLERS;
    }
    
    /**
     * Get the player that have received new xp.
     */
    public Player getPlayer(){
        return player;
    }
    
    /**
     * Get the amount of xp received.
     */
    public long getAmount(){
        return amount;
    }
    
    public void setAmount(int amount){
        this.amount = amount;
    }
    
    /**
     * Get xp source
     */
    public GainSource getSource(){
        return source;
    }
    
    public ChangeType getType(){
        return type;
    }
    
    @Override
    public HandlerList getHandlers(){
        return HANDLERS;
    }
    
    @Override
    public boolean isCancelled(){
        return cancelled;
    }
    
    @Override
    public void setCancelled(boolean cancelled){
        this.cancelled = cancelled;
    }
    
    public ServerType getServerSource(){
        return serverSource;
    }
}
