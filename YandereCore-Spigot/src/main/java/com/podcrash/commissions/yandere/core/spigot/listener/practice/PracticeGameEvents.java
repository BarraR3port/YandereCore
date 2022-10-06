package com.podcrash.commissions.yandere.core.spigot.listener.practice;

import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.common.data.user.props.GainSource;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.settings.Settings;
import ga.strikepractice.events.DuelEndEvent;
import ga.strikepractice.events.FightEndEvent;
import ga.strikepractice.events.PartyFFAEndEvent;
import ga.strikepractice.events.PartyVsPartyEndEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public class PracticeGameEvents implements Listener {
    
    @EventHandler
    public void onGameEnd(FightEndEvent event){
        final ArrayList<User> winners = new ArrayList<>();
        final ArrayList<User> losers = new ArrayList<>();
        if (event instanceof DuelEndEvent){
            DuelEndEvent duelEvent = (DuelEndEvent) event;
            winners.add(Main.getInstance().getPlayers().getCachedPlayer(duelEvent.getWinner().getUniqueId()));
            losers.add(Main.getInstance().getPlayers().getCachedPlayer(duelEvent.getLoser().getUniqueId()));
        }
        
        if (event instanceof PartyFFAEndEvent){
            PartyFFAEndEvent partyFFAEndEvent = (PartyFFAEndEvent) event;
            winners.add(Main.getInstance().getPlayers().getCachedPlayer(partyFFAEndEvent.getWinner().getUniqueId()));
            partyFFAEndEvent.getParty().getPlayers().forEach(looser -> losers.add(Main.getInstance().getPlayers().getCachedPlayer(looser.getUniqueId())));
        }
        
        if (event instanceof PartyVsPartyEndEvent){
            PartyVsPartyEndEvent partyFFAEndEvent = (PartyVsPartyEndEvent) event;
            partyFFAEndEvent.getWinner().getPlayers().forEach(winner -> winners.add(Main.getInstance().getPlayers().getCachedPlayer(winner.getUniqueId())));
            partyFFAEndEvent.getLoser().getPlayers().forEach(looser -> losers.add(Main.getInstance().getPlayers().getCachedPlayer(looser.getUniqueId())));
        }
        
        
        winners.forEach(winner -> {
            Main.getInstance().getPlayers().addCoins(winner, 5, GainSource.GAME_WIN, Settings.SERVER_TYPE);
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> Main.getInstance().getPlayers().addPlayerXp(winner, 20, GainSource.GAME_WIN, Settings.SERVER_TYPE), 65L);
        });
        
        losers.forEach(looser -> {
            Main.getInstance().getPlayers().addCoins(looser, 1, GainSource.GAME_LOSE, Settings.SERVER_TYPE);
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> Main.getInstance().getPlayers().addPlayerXp(looser, 7, GainSource.GAME_LOSE, Settings.SERVER_TYPE), 65L);
        });
        
    }
    
    
}
