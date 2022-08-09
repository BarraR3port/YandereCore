package com.podcrash.comissions.yandere.core.spigot.commands.spawn;

import com.podcrash.comissions.yandere.core.spigot.Main;
import com.podcrash.comissions.yandere.core.spigot.settings.Settings;
import net.lymarket.common.commands.*;
import net.lymarket.common.commands.response.CommandResponse;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public final class SetSpawn implements ILyCommand {
    
    
    @Command(name = "setspawn", usage = "setspawn", description = "Set the spawn point of the server")
    public CommandResponse command(CommandContext context){
        
        if (context.getSender() instanceof Player){
            Player p = (Player) context.getSender();
            
            if (!p.hasPermission("yandere.admin")){
                if (!p.hasPermission("yandere.admin.setspawn"))
                    return new CommandResponse("yandere.admin.setspawn");
                return new CommandResponse("yandere.admin");
            }
            Main.getLang().sendMsg(p, "spawn.set", "location", p.getLocation().getBlockX() + " " + p.getLocation().getBlockY() + " " + p.getLocation().getBlockZ());
            final Location loc = p.getLocation();
            p.getWorld().setSpawnLocation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
            Settings.SPAWN_LOCATION = loc;
            Main.getInstance().getConfig().set("spawn.location", loc);
            Main.getInstance().getConfig().saveData();
            
        }
        
        
        return new CommandResponse();
    }
    
    
    @Tab
    public ArrayList<String> tabComplete(TabContext context){
        return new ArrayList<>();
    }
}
