package com.podcrash.commissions.yandere.core.spigot.commands;

import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.common.error.UserNotFoundException;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.menu.lobby.LobbyMenu;
import net.lymarket.common.commands.*;
import net.lymarket.common.commands.response.CommandResponse;
import net.lymarket.lyapi.spigot.LyApi;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public final class Menu implements ILyCommand {
    
    @Command(name = "menu", permission = "yandere.menu", usage = "menu", description = "Menu", aliases = {"m", "ym"})
    public CommandResponse command(CommandContext context){
        
        if (context.getSender() instanceof Player){
            final Player player = (Player) context.getSender();
            if (context.getArgs().length == 0){
                new LobbyMenu(LyApi.getPlayerMenuUtility(player)).open();
                return new CommandResponse();
            }
            if (context.getArgs().length == 1 && player.hasPermission("yandere.menu.other")){
                final String userName = context.getArg(0);
                try {
                    final User user = Main.getInstance().getPlayers().getLocalStoredPlayer(userName);
                    new LobbyMenu(LyApi.getPlayerMenuUtility(player), user.getUUID()).open();
                } catch (UserNotFoundException e) {
                    Main.getLang().sendErrorMsg(player, "player.not-found", "player", userName);
                }
            } else {
                return new CommandResponse("yandere.menu.other");
            }
        }
        
        
        return new CommandResponse();
    }
    
    @Tab
    public ArrayList<String> tabComplete(TabContext context){
        final ArrayList<String> list = new ArrayList<>();
        
        if (context.getSender().hasPermission("yandere.admin")){
            if (context.getArgs().length == 1){
                return Main.getInstance().getPlayers().getPlayersName();
            }
        }
        
        return list;
    }
}