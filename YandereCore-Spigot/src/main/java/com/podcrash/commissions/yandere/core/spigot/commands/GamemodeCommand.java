package com.podcrash.commissions.yandere.core.spigot.commands;

import com.podcrash.commissions.yandere.core.spigot.gamemode.GamemodeManager;
import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.stream.Collectors;

public class GamemodeCommand implements ILyCommand {
    
    @Command(name = "gamemode", aliases = "gm", permission = "yandere.gamemode")
    public CommandResponse command(CommandContext context){
        if (context.getArgs().length == 0){
            GamemodeManager.sendWrongSyntax(context.getSender());
            return new CommandResponse();
        }
        switch(context.getArg(0).toLowerCase()){
            case "0":
            case "s":
            case "survival":
            case "supervivencia":{
                return GamemodeManager.setGamemode(context.getSender(), context.getArg(1), GameMode.SURVIVAL, "Survival", context.getArgLength());
            }
            case "1":
            case "c":
            case "creative":
            case "creativo":{
                return GamemodeManager.setGamemode(context.getSender(), context.getArg(1), GameMode.CREATIVE, "Creativo", context.getArgLength());
            }
            case "2":
            case "a":
            case "adventure":
            case "aventura":{
                return GamemodeManager.setGamemode(context.getSender(), context.getArg(1), GameMode.ADVENTURE, "Aventura", context.getArgLength());
            }
            case "3":
            case "sp":
            case "spec":
            case "espectador":
            case "spectator":{
                return GamemodeManager.setGamemode(context.getSender(), context.getArg(1), GameMode.SPECTATOR, "Espectador", context.getArgLength());
            }
            default:{
                GamemodeManager.sendWrongSyntax(context.getSender());
                return new CommandResponse();
            }
        }
    }
    
    @Tab
    public LinkedList<String> tabComplete(TabContext context){
        LinkedList<String> list = new LinkedList<>();
        if (context.getArgs().length == 1){
            list.add("0");
            list.add("1");
            list.add("2");
            list.add("3");
        }
        if (context.getArgs().length == 2){
            list.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(p -> !context.getSender().getName().equals(p)).collect(Collectors.toList()));
        }
        return list;
    }
}
