package com.podcrash.commissions.yandere.core.spigot.commands;

import com.podcrash.commissions.yandere.core.common.error.UserNotFoundException;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.users.SpigotUser;
import net.lymarket.common.commands.*;
import net.lymarket.common.commands.response.CommandResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class EconomyCommand implements ILyCommand {
    
    @Command(name = "money", permission = "yandere.money", aliases = {"dinero", "wallet", "cartera"})
    public CommandResponse command(CommandContext context){
        
        if (context.getArgs().length == 0){
            if (!(context.getSender() instanceof Player)){
                Main.getLang().sendErrorMsg(context.getSender(), "player.wrong-command", "command", "/money <player>");
                return new CommandResponse();
            }
            
            Player p = (Player) context.getSender();
            SpigotUser user = Main.getInstance().getPlayers().getPlayer(p.getUniqueId());
            Main.getLang().sendMsg(p, "coins.amount.own", "amount", String.valueOf(user.getCoins()));
            
            return new CommandResponse();
        }
        
        if (!context.getSender().hasPermission("yandere.money.others"))
            return new CommandResponse("yandere.money.others");
        
        OfflinePlayer target = Bukkit.getOfflinePlayer(context.getArg(0));
        
        try {
            SpigotUser user = Main.getInstance().getPlayers().getPlayer(context.getArg(0));
            final HashMap<String, String> replacements = new HashMap<>();
            replacements.put("player", context.getArg(0));
            replacements.put("amount", String.valueOf(user.getCoins()));
            Main.getLang().sendMsg(context.getSender(), "coins.amount.other", replacements);
            
            if (!target.isOnline()){
                Main.getInstance().getPlayers().unloadPlayer(target.getUniqueId());
            }
            return new CommandResponse();
            
        } catch (UserNotFoundException unknown) {
            Main.getLang().sendErrorMsg(context.getSender(), "player.not-found", "player", context.getArg(0));
            return new CommandResponse();
        }
    }
    
    @Tab
    public ArrayList<String> tabComplete(TabContext TabContext){
        return new ArrayList<>();
    }
    
}
