package com.podcrash.commissions.yandere.core.spigot.commands.gamemode;

import com.podcrash.commissions.yandere.core.spigot.gamemode.GamemodeManager;
import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.stream.Collectors;

public class GMACommand implements ILyCommand {
    
    @Command(name = "gma", aliases = "gmadventure", permission = "yandere.gamemode.adventure")
    public CommandResponse command(CommandContext context){
        if (context.getArgs().length < 2){
            return GamemodeManager.setGamemode(context.getSender(), context.getArg(1), GameMode.ADVENTURE, "Aventura", context.getArgLength());
        }
        return new CommandResponse();
    }
    
    @Tab
    public LinkedList<String> tabComplete(TabContext context){
        LinkedList<String> list = new LinkedList<>();
        if (context.getArgs().length == 1){
            list.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(p -> !context.getSender().getName().equals(p)).collect(Collectors.toList()));
        }
        return list;
    }
}
