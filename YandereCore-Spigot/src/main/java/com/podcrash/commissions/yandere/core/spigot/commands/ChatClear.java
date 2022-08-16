package com.podcrash.commissions.yandere.core.spigot.commands;

import com.podcrash.commissions.yandere.core.spigot.Main;
import net.lymarket.common.commands.*;
import net.lymarket.common.commands.response.CommandResponse;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class ChatClear implements ILyCommand {
    
    @Command(name = "chatclear", permission = "yandere.chat.clear", aliases = {"clearchat", "cc"})
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
            Utils.sendMessage(p, "&a| &eComandos Para Limpiar el Chat &a|");
            Utils.sendMessage(p, " ");
            Utils.sendMessage(p, Utils.formatTC("&e> "), Utils.hoverOverMessageSuggestCommand("&b/chatclear &e<player>", Arrays.asList("&7Con este comando le limpias", "&7el chat a un jugador específico."), "/chatclear "));
            Utils.sendMessage(p, Utils.formatTC("&e> "), Utils.hoverOverMessageSuggestCommand("&b/chatclear own", Arrays.asList("&7Con este comando limpias", "&7tu propio chat."), "/chatclear own"));
            Utils.sendMessage(p, Utils.formatTC("&e> "), Utils.hoverOverMessageSuggestCommand("&b/chatclear all", Arrays.asList("&7Con este comando le limpias", "&7teleport an specific location."), "/chatclear all"));
            Utils.sendMessage(p, Utils.formatTC("&e> "), Utils.hoverOverMessageSuggestCommand("&b/chatclear all -s", Arrays.asList("&7Con este comando le limpias", "&7teleport an specific location."), "/chatclear all -s"));
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
            for ( int i = 0; i < 100; i++ ){
                Bukkit.broadcastMessage("");
            }
            if (!(context.getArg(1).equalsIgnoreCase("-s") && p.hasPermission("yandere.chatclear.all.silent"))){
                Bukkit.broadcastMessage(Main.getLang().getMSG("chat.clear.all", "player", p.getName()));
            }
            return new CommandResponse();
            
        } else {
            Utils.sendMessage(p, "&a| &eComandos Para Limpiar el Chat &a|");
            Utils.sendMessage(p, " ");
            Utils.sendMessage(p, Utils.formatTC("&e> "), Utils.hoverOverMessageSuggestCommand("&b/chatclear &e<player>", Arrays.asList("&7Con este comando le limpias", "&7el chat a un jugador específico."), "/chatclear "));
            Utils.sendMessage(p, Utils.formatTC("&e> "), Utils.hoverOverMessageSuggestCommand("&b/chatclear own", Arrays.asList("&7Con este comando limpias", "&7tu propio chat."), "/chatclear own"));
            Utils.sendMessage(p, Utils.formatTC("&e> "), Utils.hoverOverMessageSuggestCommand("&b/chatclear all", Arrays.asList("&7Con este comando le limpias", "&7teleport an specific location."), "/chatclear all"));
            Utils.sendMessage(p, Utils.formatTC("&e> "), Utils.hoverOverMessageSuggestCommand("&b/chatclear all -s", Arrays.asList("&7Con este comando le limpias", "&7teleport an specific location."), "/chatclear all -s"));
        }
        return new CommandResponse();
    }
    
    @Tab
    public ArrayList<String> tabComplete(TabContext TabContext){
        return new ArrayList<>();
    }
}
