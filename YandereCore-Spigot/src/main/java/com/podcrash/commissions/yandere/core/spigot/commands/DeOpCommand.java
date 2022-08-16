package com.podcrash.commissions.yandere.core.spigot.commands;

import com.podcrash.commissions.yandere.core.spigot.Main;
import net.lymarket.common.commands.*;
import net.lymarket.common.commands.response.CommandResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class DeOpCommand implements ILyCommand {
    
    @Command(name = "deop", permission = "yandere.deop")
    public CommandResponse command(CommandContext context){
        if (context.getArgs().length == 0){
            Main.getLang().sendErrorMsg(context.getSender(), "player.wrong-command", "command", "/deop <player>");
            return new CommandResponse();
        }
        
        OfflinePlayer target = Bukkit.getOfflinePlayer(context.getArg(0));
        
        if (target != null && target.hasPlayedBefore()){
            if (!target.isOp()){
                Main.getLang().sendErrorMsg(context.getSender(), "player.not-op", "player", target.getName());
                return new CommandResponse();
            }
            target.setOp(false);
            
            Main.getLang().sendMsg(context.getSender(), "player.de-oped-other", "player", target.getName());
            if (target.isOnline()){
                Main.getLang().sendMsg((Player) target, "player.de-oped");
            }
            
        } else {
            Main.getLang().sendErrorMsg(context.getSender(), "player.not-found", "player", context.getArg(0));
        }
        return new CommandResponse();
    }
    
    @Tab
    public ArrayList<String> tabComplete(TabContext TabContext){
        ArrayList<String> list = new ArrayList<>();
        if (TabContext.getArgs().length == 1){
            Bukkit.getOperators().stream().filter(player -> !list.contains(player.getName())).forEachOrdered(player -> list.add(player.getName()));
            
        }
        return list;
    }
}
