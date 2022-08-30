/*
package com.podcrash.commissions.yandere.core.spigot.commands.tp;

import com.podcrash.commissions.yandere.core.spigot.Main;
import tp.com.podcrash.commissions.yandere.core.spigot.TpManager;
import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Tpa implements ILyCommand {
    
    
    @Command(name = "tpa", permission = "yandere.tpa")
    public CommandResponse command(CommandContext context){
        if (context.getSender() instanceof Player){
            final Player p = (Player) context.getSender();
            if (context.getArgs().length == 1){
                if (TpManager.sendTpaRequest(p, context.getArg(0))){
                    Main.getLang().sendMsg(context.getSender(), "teleport.tpa-sent", "player", context.getArg(0));
                }
            } else {
                Utils.sendMessage(p, "&8| &eTp Commands &8|");
                Utils.sendMessage(p, " ");
                Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageSuggestCommand("&c/tp &e<jugador>", Arrays.asList("&7Con este comando ", "&7teleport to a friend."), "/tp "));
                Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageRunCommand("&c/tp &ex y z", Arrays.asList("&7Con este comando ", "&7teleport an specific location."), "/tp "));
                Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageRunCommand("&c/tpa <jugador>", Arrays.asList("&7Con este comando ", "&7send a TP request to a friend."), "/tpa "));
                Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessage("&c/tpacept <id>", Arrays.asList("&7Con este comando ", "&7accept a Tpa request.")));
                Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessage("&c/tpadeny <id>", Arrays.asList("&7Con este comando ", "&7deny a Tpa request.")));
                return new CommandResponse();
            }
        } else {
            Main.getLang().sendErrorMsg(context.getSender(), "only-players-can-use-this-command");
            return new CommandResponse();
        }
        return new CommandResponse();
    }
    
    @Tab
    public LinkedList<String> tabComplete(TabContext context){
        LinkedList<String> list = new LinkedList<>();
        if (context.getArgs().length == 1){
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(p -> !p.equals(context.getSender().getName())).collect(Collectors.toCollection(ArrayList::new));
        }
        return list;
    }
}
*/
