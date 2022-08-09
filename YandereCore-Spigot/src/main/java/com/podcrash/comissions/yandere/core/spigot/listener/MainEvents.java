package com.podcrash.comissions.yandere.core.spigot.listener;

import com.podcrash.comissions.yandere.core.spigot.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.text.DecimalFormat;

public abstract class MainEvents implements Listener {
    
    public MainEvents(){
    }
    
    public abstract void subPlayerQuitEvent(PlayerQuitEvent e);
    
    public abstract void subPlayerJoinEvent(PlayerJoinEvent e);
    
    public abstract void subPlayerChatEvent(AsyncPlayerChatEvent e);
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLeft(PlayerQuitEvent e){
        subPlayerQuitEvent(e);
    }
    
    @EventHandler
    public abstract void onPlayerTeleport(PlayerTeleportEvent e);
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent e){
        Main.getInstance().getPlayers().getOrCreatePlayer(e.getName(), e.getUniqueId(), String.valueOf(e.getAddress()).replace("/", ""));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerAsyncPlayerChatEvent(AsyncPlayerChatEvent e){
        subPlayerChatEvent(e);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent e){
        e.setJoinMessage("");
        Main.getInstance().getSocket().sendUpdate();
        subPlayerJoinEvent(e);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuitEvent(PlayerQuitEvent e){
        e.setQuitMessage("");
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> Main.getInstance().getSocket().sendUpdate(), 40L);
    }
    
    public String getCoinsFormatted(long coins){
        if (coins > 1000000){
            DecimalFormat df = new DecimalFormat("#.##");
            return df.format(coins / 1000000) + "&eM ⛃";
        }
        if (coins > 10000){
            DecimalFormat df = new DecimalFormat("#.##");
            return df.format(coins / 1000) + "&eK ⛃";
        }
        return coins + "&e ⛃";
    }
}
