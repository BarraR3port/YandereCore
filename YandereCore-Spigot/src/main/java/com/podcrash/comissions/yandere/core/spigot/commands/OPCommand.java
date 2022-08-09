package com.podcrash.comissions.yandere.core.spigot.commands;

import com.podcrash.comissions.yandere.core.spigot.Main;
import net.lymarket.common.commands.*;
import net.lymarket.common.commands.response.CommandResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class OPCommand implements ILyCommand {
    
    @Command(name = "op", permission = "yandere.op")
    public CommandResponse command(CommandContext context){
        if (context.getArgs().length == 0){
            Main.getLang().sendErrorMsg(context.getSender(), "player.wrong-command", "command", "/op <player>");
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
    public ArrayList<String> tabComplete(TabContext TabContext){
        ArrayList<String> list = new ArrayList<>();
        if (TabContext.getArgs().length == 1){
            list = Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).distinct().collect(Collectors.toCollection(ArrayList::new));
        }
        return list;
    }
}
