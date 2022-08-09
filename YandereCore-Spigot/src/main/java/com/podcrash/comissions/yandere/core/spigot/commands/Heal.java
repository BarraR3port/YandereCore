/*
package com.podcrash.comissions.yandere.core.spigot.commands;

import net.lymarket.common.commands.*;
import net.lymarket.common.commands.response.CommandResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.lydark.core.spigot.Core;

import java.util.ArrayList;

public class Heal implements ILyCommand {
    
    @Command(name = "heal", permission = "yandere.heal")
    public CommandResponse command(CommandContext context){
        
        if (context.getArgs().length == 0){
            if (!(context.getSender() instanceof Player)){
                Bukkit.getConsoleSender().sendMessage(Core.getApi().getUtils().prefix() + " §cUso correcto: §3/heal (jugador)");
                return new CommandResponse();
            }
            Player p = (Player) context.getSender();
            p.setFireTicks(0);
            p.setFoodLevel(20);
            p.setHealth(p.getMaxHealth());
            
            p.sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.HealCommand.1[/lang]");
            return new CommandResponse();
        }
        
        Player target = Bukkit.getPlayer(context.getArg(0));
        if (target != null && target.getType().equals(EntityType.PLAYER) && target.isOnline()){
            target.setFireTicks(0);
            target.setFoodLevel(0);
            target.setHealth(target.getMaxHealth());
            
            target.sendMessage(Core.getApi().getUtils().prefix() + " §aEl jugador §3" + context.getSender().getName() + " §ate ha curado.");
            context.getSender().sendMessage(Core.getApi().getUtils().prefix() + " §aHas curado al jugador §3" + target.getName());
            return new CommandResponse();
        }
        
        context.getSender().sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.PlayerHasNotBeenFound[/lang]");
        return new CommandResponse();
    }
    
    @Tab
    public ArrayList<String> tabComplete(TabContext TabContext){
        return new ArrayList<>();
    }
    
}
*/
