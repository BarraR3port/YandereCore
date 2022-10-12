package com.podcrash.commissions.yandere.core.spigot.commands.admin;

import com.podcrash.commissions.yandere.core.spigot.Main;
import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedList;

public class ChatClearCommand implements ILyCommand {
    
    @Command(name = "chatclear", aliases = {"clearchat", "cc"})
    public CommandResponse command(CommandContext context){
        if (!(context.getSender() instanceof Player)){
            for ( int i = 0; i < 100; i++ ){
                Bukkit.getServer().broadcastMessage("");
            }
            Bukkit.getServer().broadcastMessage(Main.getLang().getMSG("chat.clear.console-cleared"));
            return new CommandResponse();
        }
        
        Player p = (Player) context.getSender();
        
        if (context.getArgs().length == 0){
            if (p.hasPermission("yandere.chat.clear.all")){
                for ( int i = 0; i < 100; i++ ){
                    Bukkit.broadcastMessage("");
                }
                if (!(context.getArg(0).equalsIgnoreCase("-s") && p.hasPermission("yandere.chatclear.all.silent"))){
                    Bukkit.broadcastMessage(Main.getLang().getMSG("chat.clear.all", "player", p.getName()));
                }
                return new CommandResponse();
            } else {
                for ( int i = 0; i < 100; i++ ){
                    p.sendMessage("");
                }
                Main.getLang().sendMsg(p, "chat.clear.own");
            }
            return new CommandResponse();
        } else if (context.getArg(0).equalsIgnoreCase("help")){
            Utils.sendMessage(p, "&8| &eComandos Para Limpiar el Chat &8|");
            Utils.sendMessage(p, " ");
            Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageSuggestCommand("&c/cc &e<jugador>", Arrays.asList("&7Con este comando le limpias", "&7el chat a un jugador específico."), "/cc "));
            Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageSuggestCommand("&c/cc own", Arrays.asList("&7Con este comando limpias", "&7tu propio chat."), "/cc own"));
            Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageSuggestCommand("&c/cc all", Arrays.asList("&7Con este comando le limpias", "&7teleport an specific location."), "/cc all"));
            Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageSuggestCommand("&c/cc all -s", Arrays.asList("&7Con este comando le limpias", "&7teleport an specific location."), "/cc all -s"));
            return new CommandResponse();
        } else if (context.getArg(0).equalsIgnoreCase("own")){
            if (p.hasPermission("yandere.chatclear.own")){
                for ( int i = 0; i < 100; i++ ){
                    p.sendMessage("");
                }
                Main.getLang().sendMsg(p, "chat.clear.own");
                return new CommandResponse();
            }
        } else if (context.getArg(0).equalsIgnoreCase("all")){
            if (p.hasPermission("yandere.chat.clear.all")){
                for ( int i = 0; i < 100; i++ ){
                    Bukkit.broadcastMessage("");
                }
                if (!(context.getArg(0).equalsIgnoreCase("-s") && p.hasPermission("yandere.chatclear.all.silent"))){
                    Bukkit.broadcastMessage(Main.getLang().getMSG("chat.clear.all", "player", p.getName()));
                }
                return new CommandResponse();
            }
        } else {
            Utils.sendMessage(p, "&8| &eComandos Para Limpiar el Chat &8|");
            Utils.sendMessage(p, " ");
            Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageSuggestCommand("&c/cc &e<jugador>", Arrays.asList("&7Con este comando le limpias", "&7el chat a un jugador específico."), "/cc "));
            Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageSuggestCommand("&c/cc own", Arrays.asList("&7Con este comando limpias", "&7tu propio chat."), "/cc own"));
            Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageSuggestCommand("&c/cc all", Arrays.asList("&7Con este comando le limpias", "&7teleport an specific location."), "/cc all"));
            Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageSuggestCommand("&c/cc all -s", Arrays.asList("&7Con este comando le limpias", "&7teleport an specific location."), "/cc all -s"));
        }
        return new CommandResponse();
    }
    
    @Tab
    public LinkedList<String> tabComplete(TabContext TabContext){
        return new LinkedList<>();
    }
}
