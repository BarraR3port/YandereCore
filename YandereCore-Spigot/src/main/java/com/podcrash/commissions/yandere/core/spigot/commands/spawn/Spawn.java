package com.podcrash.commissions.yandere.core.spigot.commands.spawn;

import com.podcrash.commissions.yandere.core.spigot.settings.Settings;
import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public final class Spawn implements ILyCommand {
    
    
    @Command(name = "spawn", description = "Teleports you to the spawn", usage = "spawn")
    public CommandResponse command(CommandContext context){
        if (context.getSender() instanceof Player){
            Player p = (Player) context.getSender();
            if (Settings.SPAWN_LOCATION != null){
                p.teleport(Settings.SPAWN_LOCATION);
            }
            
        }
        
        return new CommandResponse();
    }
    
    
    @Tab
    public LinkedList<String> tabComplete(TabContext context){
        return new LinkedList<>();
    }
}
