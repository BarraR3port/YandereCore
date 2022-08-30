package com.podcrash.commissions.yandere.core.spigot.commands;

import com.podcrash.commissions.yandere.core.spigot.Main;
import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.stream.Collectors;

public class Heal implements ILyCommand {
    
    @Command(name = "heal", permission = "yandere.heal")
    public CommandResponse command(CommandContext context){
        
        if (context.getArgs().length == 0){
            if (!(context.getSender() instanceof Player)){
                Bukkit.getConsoleSender().sendMessage("§cUso correcto: §3/heal <jugador>");
                return new CommandResponse();
            }
            Player p = (Player) context.getSender();
            p.setFireTicks(0);
            p.setFoodLevel(20);
            p.setHealth(p.getMaxHealth());
            Main.getLang().sendMsg(p, "health.set");
            return new CommandResponse();
        }
    
        Player target = Bukkit.getPlayer(context.getArg(0));
        if (target != null){
            if (context.getSender().hasPermission("yandere.health.other")){
                target.setFireTicks(0);
                target.setFoodLevel(0);
                target.setHealth(target.getMaxHealth());
                Main.getLang().sendMsg(target, "health.set");
                Main.getLang().sendMsg(context.getSender(), "health.set-other", "player", target.getName());
                return new CommandResponse();
            } else {
                return new CommandResponse("yandere.health.set.other");
            }
        }
    
        Main.getLang().sendErrorMsg(context.getSender(), "player.not-found", "player", context.getArg(0));
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
