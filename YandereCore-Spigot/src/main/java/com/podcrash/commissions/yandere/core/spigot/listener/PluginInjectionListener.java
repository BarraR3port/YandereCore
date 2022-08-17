package com.podcrash.commissions.yandere.core.spigot.listener;

import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.listener.bedwars.BWPlayerEvents;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

public class PluginInjectionListener implements Listener {
    
    
    @EventHandler
    public void onPluginLoadEvent(PluginEnableEvent e){
        if (e.getPlugin().getName().equals("PodBedWars")){
            Bukkit.getPluginManager().registerEvents(new BWPlayerEvents(), Main.getInstance());
        }
    }
}
