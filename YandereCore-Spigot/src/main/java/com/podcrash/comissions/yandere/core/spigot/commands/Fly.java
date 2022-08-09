/*
package com.podcrash.comissions.yandere.core.spigot.commands;

import net.lymarket.common.commands.*;
import net.lymarket.common.commands.response.CommandResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.lydark.core.spigot.Core;

import java.util.ArrayList;

public class Fly implements ILyCommand {
    
    @Command(name = "fly", permission = "yandere.fly")
    public CommandResponse command(CommandContext context){
        if (!(context.getSender() instanceof Player)){
            Bukkit.getConsoleSender().sendMessage(Core.getApi().getUtils().prefix() + " §cNo puedes ejecutar comandos desde la consola");
            return new CommandResponse();
        }
        Player p = (Player) context.getSender();
        
        if (context.getArgs().length == 0){
            if (!p.getAllowFlight()){
                p.setAllowFlight(true);
                p.setFlying(true);
                
                p.sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.FlyCommand.1[/lang]");
            } else {
                p.setAllowFlight(false);
                p.setFlying(false);
                
                p.sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.FlyCommand.2[/lang]");
            }
            return new CommandResponse();
        } else {
            Player target = Bukkit.getPlayer(context.getArg(0));
            
            if (!p.hasPermission("yandere.fly.others")) return false;
            
            if (target != null && target.getType().equals(EntityType.PLAYER) && target.isOnline()){
                if (!target.getAllowFlight()){
                    target.setAllowFlight(true);
                    target.setFlying(true);
                    
                    target.sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.FlyCommand.1[/lang]");
                    
                    p.sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.FlyCommand.4[/lang] §3" + target.getName() + " [lang]Lydark_Core.FlyCommand.5[/lang]");
                } else {
                    target.setAllowFlight(false);
                    target.setFlying(false);
                    
                    target.sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.FlyCommand.6[/lang]");
                    
                    p.sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.FlyCommand.4[/lang] §3" + target.getName() + " [lang]Lydark_Core.FlyCommand.7[/lang]");
                }
                return new CommandResponse();
            } else {
                p.sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.PlayerHasNotBeenFound[/lang]");
                return false;
            }
        }
        
    }
    
    @Tab
    public ArrayList<String> tabComplete(TabContext TabContext){
        return new ArrayList<>();
    }
}
*/
