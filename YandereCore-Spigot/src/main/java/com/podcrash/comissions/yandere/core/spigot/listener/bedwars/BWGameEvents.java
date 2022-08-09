package com.podcrash.comissions.yandere.core.spigot.listener.bedwars;

import com.andrei1058.bedwars.api.events.gameplay.GameEndEvent;
import com.andrei1058.bedwars.api.events.player.PlayerKillEvent;
import com.podcrash.comissions.yandere.core.common.data.level.Level;
import com.podcrash.comissions.yandere.core.spigot.Main;
import com.podcrash.comissions.yandere.core.spigot.users.SpigotUser;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class BWGameEvents implements Listener {
    
    @EventHandler
    public void onGameEnd(GameEndEvent event){
        for ( UUID winners : event.getWinners() ){
            SpigotUser user = Main.getInstance().getPlayers().getPlayer(winners);
            Main.getInstance().getPlayers().addCoins(user, 200);
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> Main.getInstance().getPlayers().addPlayerXp(user, 800, Level.GainSource.GAME_WIN), 65L);
        }
        for ( UUID losers : event.getLosers() ){
            SpigotUser user = Main.getInstance().getPlayers().getPlayer(losers);
            Main.getInstance().getPlayers().addCoins(user, 50);
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> Main.getInstance().getPlayers().addPlayerXp(user, 200, Level.GainSource.GAME_LOSE), 65L);
        }
    }
    
    @EventHandler
    public void onPlayerKill(PlayerKillEvent event){
        SpigotUser user = Main.getInstance().getPlayers().getPlayer(event.getKiller().getUniqueId());
        Main.getInstance().getPlayers().addCoins(user, 5);
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> Main.getInstance().getPlayers().addPlayerXp(user, 50, Level.GainSource.PER_KILL), 65L);
    }
    
    
}
