package com.podcrash.commissions.yandere.core.spigot.commands.spawn;

import com.podcrash.commissions.yandere.core.spigot.Main;
import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public final class DelSpawn implements ILyCommand {
    
    
    @Command(name = "delspawn", usage = "delspawn", description = "Set the spawn point of the server")
    public CommandResponse command(CommandContext context){
        
        if (context.getSender() instanceof Player){
            Player p = (Player) context.getSender();
            
            if (!p.hasPermission("yandere.admin")){
                if (!p.hasPermission("yandere.admin.delspawn"))
                    return new CommandResponse("yandere.admin.delspawn");
                return new CommandResponse("yandere.admin");
            }
            
            Main.getInstance().getConfig().set("spawn.location", null);
            Main.getInstance().getConfig().saveData();
            
        }
        
        
        return new CommandResponse();
    }
    
    
    @Tab
    public LinkedList<String> tabComplete(TabContext context){
        return new LinkedList<>();
    }
}
