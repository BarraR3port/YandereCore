package com.podcrash.commissions.yandere.core.spigot.commands.tp;

import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.tp.TpManager;
import net.lymarket.common.commands.*;
import net.lymarket.common.commands.response.CommandResponse;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TpHere implements ILyCommand {
    
    @Command(name = "tphere", permission = "yandere.tphere")
    public CommandResponse command(CommandContext context){
        
        
        if (context.getSender() instanceof Player){
            final Player p = (Player) context.getSender();
            if (context.getArgs().length == 1){
                TpManager.teleportPlayerToPlayer(p, context.getArg(0), p.getName());
            } else {
                Utils.sendMessage(p, "&a| &eComandos de TP &a|");
                Utils.sendMessage(p, " ");
                Utils.sendMessage(p, Utils.formatTC("&e> "), Utils.hoverOverMessageSuggestCommand("&b/tp &e<player>", Arrays.asList("&7Con este comando ", "&7teleport to a friend."), "/tp "));
                Utils.sendMessage(p, Utils.formatTC("&e> "), Utils.hoverOverMessageRunCommand("&b/tp &ex y z", Arrays.asList("&7Con este comando ", "&7teleport an specific location."), "/tp "));
                /*Utils.sendMessage(p, Utils.formatTC("&e> "), Utils.hoverOverMessageRunCommand("&b/tpa <player>", Arrays.asList("&7Con este comando ", "&7send a TP request to a friend."), "/tpa "));
                Utils.sendMessage(p, Utils.formatTC("&e> "), Utils.hoverOverMessage("&b/tpacept <id>", Arrays.asList("&7Con este comando ", "&7accept a Tpa request.")));
                Utils.sendMessage(p, Utils.formatTC("&e> "), Utils.hoverOverMessage("&b/tpadeny <id>", Arrays.asList("&7Con este comando ", "&7deny a Tpa request.")));
                */
                return new CommandResponse();
            }
        } else {
            Main.getLang().sendErrorMsg(context.getSender(), "only-players-can-use-this-command");
            return new CommandResponse();
        }
        return new CommandResponse();
    }
    
    @Tab
    public ArrayList<String> tabComplete(TabContext context){
        ArrayList<String> list = new ArrayList<>();
        if (context.getArgs().length == 1){
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(p -> !p.equals(context.getSender().getName())).collect(Collectors.toCollection(ArrayList::new));
        }
        return list;
    }
    
}
