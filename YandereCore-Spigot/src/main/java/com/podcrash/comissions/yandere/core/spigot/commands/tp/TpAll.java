package com.podcrash.comissions.yandere.core.spigot.commands.tp;

import com.podcrash.comissions.yandere.core.spigot.Main;
import com.podcrash.comissions.yandere.core.spigot.tp.TpManager;
import net.lymarket.common.commands.*;
import net.lymarket.common.commands.response.CommandResponse;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TpAll implements ILyCommand {
    
    @Command(name = "tpall", permission = "yandere.tp.all")
    public CommandResponse command(CommandContext context){
        if (context.getArgs().length == 1){
            final Player target = Bukkit.getPlayer(context.getArg(0));
            if (target != null && target.getType().equals(EntityType.PLAYER) && target.isOnline()){
                TpManager.teleportAllToPlayer(target);
            } else {
                Utils.sendMessage(context.getSender(), "&a| &eComandos de TP &a|");
                Utils.sendMessage(context.getSender(), " ");
                Utils.sendMessage(context.getSender(), Utils.formatTC("&e> "), Utils.hoverOverMessageSuggestCommand("&b/tp &e<player>", Arrays.asList("&7Con este comando ", "&7teleport to a friend."), "/tp "));
                Utils.sendMessage(context.getSender(), Utils.formatTC("&e> "), Utils.hoverOverMessageRunCommand("&b/tp &ex y z", Arrays.asList("&7Con este comando ", "&7teleport an specific location."), "/tp "));
                return new CommandResponse();
            }
        }
        if (context.getSender() instanceof Player){
            final Player p = (Player) context.getSender();
            if (context.getArgs().length == 0){
                TpManager.teleportAllToPlayer(p);
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
        if (context.getArgs().length == 1){
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(p -> !p.equals(context.getSender().getName())).collect(Collectors.toCollection(ArrayList::new));
        } else if (context.getArgs().length == 2){
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toCollection(ArrayList::new));
        }
        return new ArrayList<>();
    }
}
