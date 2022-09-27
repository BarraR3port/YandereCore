package com.podcrash.commissions.yandere.core.spigot.listener.skywars;

import com.podcrash.commissions.yandere.core.common.data.level.Level;
import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.spigot.Main;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.USWGameKillEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SWGameEvents implements Listener {
    
    @EventHandler
    public void onPlayerKill(USWGameKillEvent event){
        User user = Main.getInstance().getPlayers().getCachedPlayer(event.getPlayer().getUniqueId());
        Main.getInstance().getPlayers().addCoins(user, 1);
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> Main.getInstance().getPlayers().addPlayerXp(user, 5, Level.GainSource.PER_KILL), 65L);
    }
}
