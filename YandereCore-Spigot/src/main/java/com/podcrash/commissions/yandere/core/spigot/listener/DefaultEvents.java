package com.podcrash.commissions.yandere.core.spigot.listener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class DefaultEvents extends MainEvents {
    @Override
    public void subPlayerQuitEvent(PlayerQuitEvent e){
    
    }
    
    @Override
    public void subPlayerJoinEvent(PlayerJoinEvent e){
        if (!Settings.IS_SERVER_LINKED){
            Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("GetServer");
                Bukkit.getServer().sendPluginMessage(Main.getInstance(), "podcrash:yandere", out.toByteArray());
            }, 60L, 60L);
        }
    }
    
    @Override
    public void subPlayerChatEvent(AsyncPlayerChatEvent e){
    
    }
    
    @Override
    public void onPlayerTeleport(PlayerTeleportEvent e){
    
    }
}
