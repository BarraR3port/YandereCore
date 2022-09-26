package com.podcrash.commissions.yandere.core.spigot.commands;

import com.podcrash.commissions.yandere.core.spigot.Main;
import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class SpeedCommand implements ILyCommand {
    
    @Command(name = "speed", permission = "yandere.speed", aliases = "s")
    public CommandResponse command(CommandContext context){
        
        if (!(context.getSender() instanceof Player)){
            Main.getLang().sendErrorMsg(context.getSender(), "only-players-can-use-this-command");
            return new CommandResponse();
        }
        
        Player p = (Player) context.getSender();
        
        if (context.getArgs().length == 0){
            Main.getLang().sendErrorMsg(context.getSender(), "player.wrong-command", "command", "/speed <velocidad>");
            return new CommandResponse();
        }
        
        try {
            
            int speed = Integer.parseInt(context.getArg(0));
            
            if (!(speed > 10 || speed <= 0)){
                
                if (p.isFlying()){
                    p.setFlySpeed(speed / 10f);
                } else {
                    p.setWalkSpeed(speed / 10f);
                }
                Main.getLang().sendMsg(p, "speed.change.set-own", "speed", String.valueOf(speed));
                
            } else {
                p.sendMessage("§cEl número que has ingresado no es válido. §a(1-10)");
            }
            return new CommandResponse();
            
        } catch (NumberFormatException numex) {
            
            p.sendMessage("§cLa velocidad debe de ser un número entero.");
            return new CommandResponse();
        }
        
    }
    
    @Tab
    public LinkedList<String> tabComplete(TabContext TabContext){
        LinkedList<String> list = new LinkedList<>();
        if (TabContext.getArgs().length == 1){
            for ( int i = 1; i <= 10; i++ ){
                list.add(String.valueOf(i));
            }
        }
        return list;
    }
}
