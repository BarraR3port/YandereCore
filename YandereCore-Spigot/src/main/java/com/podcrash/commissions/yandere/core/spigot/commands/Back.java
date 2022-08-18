package com.podcrash.commissions.yandere.core.spigot.commands;

import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.users.SpigotUser;
import net.lymarket.common.commands.*;
import net.lymarket.common.commands.response.CommandResponse;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Back implements ILyCommand {
    
    @Command(name = "back", description = "Teleport to your previous location", permission = "yandere.back")
    public CommandResponse command(CommandContext context){
        if (!(context.getSender() instanceof Player)){
            Main.getLang().sendErrorMsg(context.getSender(), "only-players-can-use-this-command");
            return new CommandResponse();
        }
        SpigotUser user = (SpigotUser) Main.getInstance().getPlayers().getPlayer(((Player) context.getSender()).getUniqueId());
        
        Location loc = user.getBukkitLocation();
        if (loc == null){
            Main.getLang().sendErrorMsg(context.getSender(), "player.no-previous-location-found");
            return new CommandResponse();
        }
        ((Player) context.getSender()).teleport(loc);
        
        return new CommandResponse();
    }
    
    @Tab
    public ArrayList<String> tabComplete(TabContext TabContext){
        return new ArrayList<>();
    }
}
