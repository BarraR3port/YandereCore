package com.podcrash.comissions.yandere.core.spigot.events;

import com.podcrash.comissions.yandere.core.common.data.server.ChangeType;
import com.podcrash.comissions.yandere.core.common.data.user.props.Rank;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerRankChangeEvent extends Event implements Cancellable {
    
    private static final HandlerList HANDLERS = new HandlerList();
    
    private final Player player;
    private final ChangeType changeType;
    private Rank rank;
    private boolean cancelled;
    
    /**
     * TODO:
     *
     * @param player - target player.
     * @param -      amount of xp.
     * @param -      where did the player receive xp from.
     */
    public PlayerRankChangeEvent(Player player, Rank rank, ChangeType changeType){
        this.player = player;
        this.rank = rank;
        this.changeType = changeType;
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
     * Get xp source
     */
    public Rank getRank(){
        return rank;
    }
    
    public void setRank(Rank rank){
        this.rank = rank;
    }
    
    public ChangeType getChangeType(){
        return changeType;
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
}
