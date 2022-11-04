package com.podcrash.commissions.yandere.core.spigot.commands.admin;

import com.podcrash.commissions.yandere.core.spigot.Main;
import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class DeOpCommand implements ILyCommand {
    
    @Command(name = "deop", permission = "yandere.deop")
    public CommandResponse command(CommandContext context){
        if (context.getArgs().length == 0){
            Main.getLang().sendErrorMsg(context.getSender(), "player.wrong-command", "command", "/deop <jugador>");
            return CommandResponse.accept();
        }
        
        OfflinePlayer target = Bukkit.getOfflinePlayer(context.getArg(0));
        
        if (target != null && target.hasPlayedBefore()){
            if (!target.isOp()){
                Main.getLang().sendErrorMsg(context.getSender(), "player.not-op", "player", target.getName());
                return CommandResponse.accept();
            }
            target.setOp(false);
            
            Main.getLang().sendMsg(context.getSender(), "player.de-oped-other", "player", target.getName());
            if (target.isOnline()){
                Main.getLang().sendMsg((Player) target, "player.de-oped");
            }
            
        } else {
            Main.getLang().sendErrorMsg(context.getSender(), "player.not-found", "player", context.getArg(0));
        }
        return CommandResponse.accept();
    }
    
    @Tab
    public LinkedList<String> tabComplete(TabContext TabContext){
        LinkedList<String> list = new LinkedList<>();
        if (TabContext.getArgs().length == 1){
            Bukkit.getOperators().stream().filter(player -> !list.contains(player.getName())).forEachOrdered(player -> list.add(player.getName()));
            
        }
        return list;
    }
}
