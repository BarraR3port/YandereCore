package com.podcrash.commissions.yandere.core.spigot.commands.admin;

import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.common.error.UserNotFoundException;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.menu.admin.log.LogManagerMenu;
import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import net.lymarket.lyapi.spigot.LyApi;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public final class LogCommand implements ILyCommand {
    
    @Command(name = "logs", permission = "yandere.admin.log", usage = "logs", aliases = {"log"})
    public CommandResponse command(CommandContext context){
        if (context.getSender() instanceof Player){
            final Player player = (Player) context.getSender();
            if (context.getArgs().length == 0){
                new LogManagerMenu(LyApi.getPlayerMenuUtility(player)).open();
                return CommandResponse.accept();
            }
            if (context.getArgs().length == 1 && player.hasPermission("yandere.admin.log.other")){
                final String userName = context.getArg(0);
                try {
                    final User user = Main.getInstance().getPlayers().getCachedPlayer(userName);
                    new LogManagerMenu(LyApi.getPlayerMenuUtility(player), user).open();
                } catch (UserNotFoundException e) {
                    Main.getLang().sendErrorMsg(player, "player.not-found", "player", userName);
                }
            } else {
                return CommandResponse.deny("yandere.menu.other");
            }
        }
    
    
        return CommandResponse.accept();
    }
    
    @Tab
    public LinkedList<String> tabComplete(TabContext context){
        final LinkedList<String> list = new LinkedList<>();
        
        if (context.getSender().hasPermission("yandere.admin")){
            if (context.getArgs().length == 1){
                list.addAll(Main.getInstance().getPlayers().getPlayersName());
            }
        }
        
        return list;
    }
}