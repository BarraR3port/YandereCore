package com.podcrash.commissions.yandere.core.spigot.commands;

import com.podcrash.commissions.yandere.core.spigot.Main;
import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class FlyCommand implements ILyCommand {
    
    @Command(name = "fly", permission = "yandere.fly")
    public CommandResponse command(CommandContext context){
        if (!(context.getSender() instanceof Player)){
            Main.getLang().sendErrorMsg(context.getSender(), "only-players-can-use-this-command");
            return CommandResponse.accept();
        }
        Player p = (Player) context.getSender();
        if (context.getArgs().length == 0){
            boolean fly = p.getAllowFlight();
            p.setAllowFlight(!fly);
            p.setFlying(!fly);
            Main.getLang().sendMsg(p, "fly.chang", "fly", p.getAllowFlight() ? "&aActivado" : "&cDesactivado");
        } else {
            if (!p.hasPermission("yandere.fly.others")) return CommandResponse.deny("yandere.fly.others");
            Player target = Bukkit.getPlayer(context.getArg(0));
            if (target != null){
                boolean fly = target.getAllowFlight();
                target.setAllowFlight(!fly);
                target.setFlying(!fly);
                Main.getLang().sendMsg(target, "fly.change", "fly", target.getAllowFlight() ? "&aActivado" : "&cDesactivado");
                HashMap<String, String> replacements = new HashMap<>();
                replacements.put("fly", target.getAllowFlight() ? "&aActivado" : "&cDesactivado");
                replacements.put("player", target.getName());
                Main.getLang().sendMsg(target, "fly.change.other", replacements);
            } else {
                Main.getLang().sendErrorMsg(context.getSender(), "player.not-found", "player", context.getArg(0));
            }
        }
        return CommandResponse.accept();
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
