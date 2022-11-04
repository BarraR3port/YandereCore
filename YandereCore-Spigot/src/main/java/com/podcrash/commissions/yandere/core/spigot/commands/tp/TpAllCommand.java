package com.podcrash.commissions.yandere.core.spigot.commands.tp;

import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.tp.TpManager;
import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class TpAllCommand implements ILyCommand {
    
    @Command(name = "tpall", permission = "yandere.tp.all")
    public CommandResponse command(CommandContext context){
        if (context.getArgs().length == 1){
            final Player target = Bukkit.getPlayer(context.getArg(0));
            if (target != null && target.getType().equals(EntityType.PLAYER) && target.isOnline()){
                TpManager.teleportAllToPlayer(target);
            } else {
                Utils.sendMessage(context.getSender(), "&8| &eComandos de TP &8|");
                Utils.sendMessage(context.getSender(), " ");
                Utils.sendMessage(context.getSender(), Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageSuggestCommand("&c/tp &e<jugador>", Arrays.asList("&7Con este comando ", "&7teleport to a friend."), "/tp "));
                Utils.sendMessage(context.getSender(), Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageRunCommand("&c/tp &ex y z", Arrays.asList("&7Con este comando ", "&7teleport an specific location."), "/tp "));
                return CommandResponse.accept();
            }
        }
        if (context.getSender() instanceof Player){
            final Player p = (Player) context.getSender();
            if (context.getArgs().length == 0){
                TpManager.teleportAllToPlayer(p);
            } else {
                Utils.sendMessage(p, "&8| &eComandos de TP &8|");
                Utils.sendMessage(p, " ");
                Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageSuggestCommand("&c/tp &e<jugador>", Arrays.asList("&7Con este comando ", "&7teleport to a friend."), "/tp "));
                Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageRunCommand("&c/tp &ex y z", Arrays.asList("&7Con este comando ", "&7teleport an specific location."), "/tp "));
                /*Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageRunCommand("&c/tpa <jugador>", Arrays.asList("&7Con este comando ", "&7send a TP request to a friend."), "/tpa "));
                Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessage("&c/tpacept <id>", Arrays.asList("&7Con este comando ", "&7accept a Tpa request.")));
                Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessage("&c/tpadeny <id>", Arrays.asList("&7Con este comando ", "&7deny a Tpa request.")));
                */
                return CommandResponse.accept();
            }
        } else {
            Main.getLang().sendErrorMsg(context.getSender(), "only-players-can-use-this-command");
            return CommandResponse.accept();
        }
        return CommandResponse.accept();
    }
    
    @Tab
    public LinkedList<String> tabComplete(TabContext context){
        LinkedList<String> list = new LinkedList<>();
        if (context.getArgs().length == 1){
            list.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(p -> !context.getSender().getName().equals(p)).collect(Collectors.toList()));
        } else if (context.getArgs().length == 2){
            list.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
        }
        return list;
    }
}
