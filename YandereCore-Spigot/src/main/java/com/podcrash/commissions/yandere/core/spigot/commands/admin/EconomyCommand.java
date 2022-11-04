package com.podcrash.commissions.yandere.core.spigot.commands.admin;

import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.common.error.UserNotFoundException;
import com.podcrash.commissions.yandere.core.spigot.Main;
import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedList;

public class EconomyCommand implements ILyCommand {
    
    @Command(name = "money", permission = "yandere.money", aliases = {"dinero", "wallet", "cartera"})
    public CommandResponse command(CommandContext context){
        
        if (context.getArgs().length == 0){
            if (!(context.getSender() instanceof Player)){
                Main.getLang().sendErrorMsg(context.getSender(), "player.wrong-command", "command", "/money <jugador>");
                return CommandResponse.accept();
            }
            
            Player p = (Player) context.getSender();
            User user = Main.getInstance().getPlayers().getPlayer(p.getUniqueId());
            Main.getLang().sendMsg(p, "coins.amount.own", "amount", String.valueOf(user.getCoins()));
    
            return CommandResponse.accept();
        }
        
        if (!context.getSender().hasPermission("yandere.money.others"))
            return CommandResponse.deny("yandere.money.others");
        
        OfflinePlayer target = Bukkit.getOfflinePlayer(context.getArg(0));
        
        try {
            User user = Main.getInstance().getPlayers().getCachedPlayer(context.getArg(0));
            final HashMap<String, String> replacements = new HashMap<>();
            replacements.put("player", context.getArg(0));
            replacements.put("amount", String.valueOf(user.getCoins()));
            Main.getLang().sendMsg(context.getSender(), "coins.amount.other", replacements);
            
            if (!target.isOnline()){
                Main.getInstance().getPlayers().unloadPlayer(target.getUniqueId());
            }
            return CommandResponse.accept();
            
        } catch (UserNotFoundException unknown) {
            Main.getLang().sendErrorMsg(context.getSender(), "player.not-found", "player", context.getArg(0));
            return CommandResponse.accept();
        }
    }
    
    @Tab
    public LinkedList<String> tabComplete(TabContext TabContext){
        return new LinkedList<>();
    }
    
}
