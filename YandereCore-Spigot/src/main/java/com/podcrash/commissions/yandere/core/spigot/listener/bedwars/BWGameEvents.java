package com.podcrash.commissions.yandere.core.spigot.listener.bedwars;

import com.andrei1058.bedwars.api.events.gameplay.GameEndEvent;
import com.andrei1058.bedwars.api.events.player.PlayerKillEvent;
import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.common.data.user.props.GainSource;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class BWGameEvents implements Listener {
    
    @EventHandler
    public void onGameEnd(GameEndEvent event){
        for ( UUID winners : event.getWinners() ){
            User user = Main.getInstance().getPlayers().getCachedPlayer(winners);
            Main.getInstance().getPlayers().addCoins(user, 200, GainSource.GAME_WIN, Settings.SERVER_TYPE);
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> Main.getInstance().getPlayers().addPlayerXp(user, 800, GainSource.GAME_WIN, Settings.SERVER_TYPE), 65L);
        }
        for ( UUID losers : event.getLosers() ){
            User user = Main.getInstance().getPlayers().getCachedPlayer(losers);
            Main.getInstance().getPlayers().addCoins(user, 50, GainSource.GAME_LOSE, Settings.SERVER_TYPE);
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> Main.getInstance().getPlayers().addPlayerXp(user, 200, GainSource.GAME_LOSE, Settings.SERVER_TYPE), 65L);
        }
    }
    
    @EventHandler
    public void onPlayerKill(PlayerKillEvent event){
        User user = Main.getInstance().getPlayers().getCachedPlayer(event.getKiller().getUniqueId());
        Main.getInstance().getPlayers().addCoins(user, 5, GainSource.PER_KILL, Settings.SERVER_TYPE);
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> Main.getInstance().getPlayers().addPlayerXp(user, 50, GainSource.PER_KILL, Settings.SERVER_TYPE), 65L);
    }
    
    
}
