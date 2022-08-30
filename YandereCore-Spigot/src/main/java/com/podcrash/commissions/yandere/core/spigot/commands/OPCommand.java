package com.podcrash.commissions.yandere.core.spigot.commands;

import com.podcrash.commissions.yandere.core.spigot.Main;
import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class OPCommand implements ILyCommand {
    
    @Command(name = "op", permission = "yandere.op")
    public CommandResponse command(CommandContext context){
        if (context.getArgs().length == 0){
            Main.getLang().sendErrorMsg(context.getSender(), "player.wrong-command", "command", "/op <jugador>");
            return new CommandResponse();
        }
        
        OfflinePlayer target = Bukkit.getOfflinePlayer(context.getArg(0));
        
        if (target != null && (target.hasPlayedBefore() || target.isOnline())){
            if (target.isOp()){
                Main.getLang().sendErrorMsg(context.getSender(), "player.already-op", "player", target.getName());
                return new CommandResponse();
            }
            target.setOp(true);
            Main.getLang().sendMsg(context.getSender(), "player.oped-other", "player", target.getName());
            
            if (target.isOnline()){
                Main.getLang().sendMsg((Player) target, "player.oped");
            }
            
        } else {
            Main.getLang().sendErrorMsg(context.getSender(), "player.not-found", "player", context.getArg(0));
        }
        return new CommandResponse();
    }
    
    @Tab
    public LinkedList<String> tabComplete(TabContext TabContext){
        LinkedList<String> list = new LinkedList<>();
        if (TabContext.getArgs().length == 1){
            list = Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).distinct().collect(Collectors.toCollection(LinkedList::new));
        }
        return list;
    }
}
