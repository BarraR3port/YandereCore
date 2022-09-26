package com.podcrash.commissions.yandere.core.spigot.commands;

import com.podcrash.commissions.yandere.core.spigot.menu.lobby.MultiLobbyMenu;
import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import net.lymarket.lyapi.spigot.LyApi;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public final class LobbiesCommand implements ILyCommand {
    
    @Command(name = "lobbies", usage = "menu", description = "Menu", aliases = {"m", "ym"})
    public CommandResponse command(CommandContext context){
        
        if (context.getSender() instanceof Player){
            final Player player = (Player) context.getSender();
            if (context.getArgs().length == 0){
                new MultiLobbyMenu(LyApi.getPlayerMenuUtility(player)).open();
                return new CommandResponse();
            }
        }
        
        
        return new CommandResponse();
    }
    
    @Tab
    public LinkedList<String> tabComplete(TabContext context){
        return new LinkedList<>();
    }
}