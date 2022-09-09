package com.podcrash.commissions.yandere.core.spigot.listener.skywars;

import com.podcrash.commissions.yandere.core.common.data.level.Level;
import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.spigot.Main;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.USWGameKillEvent;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.USWGameWinEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public class SWGameEvents implements Listener {
    
    @EventHandler
    public void onGameEnd(USWGameWinEvent event) {
        final ArrayList<Player> winners = (ArrayList<Player>) event.getWinner().getMembers();
        for ( Player winner : winners ){
            User user = Main.getInstance().getPlayers().getLocalStoredPlayer(winner.getUniqueId());
            Main.getInstance().getPlayers().addCoins(user, 400);
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> Main.getInstance().getPlayers().addPlayerXp(user, 800, Level.GainSource.GAME_WIN), 65L);
        }
        event.getGame().getPlayers().forEach(player -> {
            if(winners.contains(player)) return;
            User user = Main.getInstance().getPlayers().getLocalStoredPlayer(player.getUniqueId());
            Main.getInstance().getPlayers().addCoins(user, 100);
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> Main.getInstance().getPlayers().addPlayerXp(user, 200, Level.GainSource.GAME_WIN), 65L);
        });
    }
    
    @EventHandler
    public void onPlayerKill(USWGameKillEvent event) {
        User user = Main.getInstance().getPlayers().getLocalStoredPlayer(event.getPlayer().getUniqueId());
        Main.getInstance().getPlayers().addCoins(user, 5);
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> Main.getInstance().getPlayers().addPlayerXp(user, 50, Level.GainSource.PER_KILL), 65L);
    }
    
    
}
