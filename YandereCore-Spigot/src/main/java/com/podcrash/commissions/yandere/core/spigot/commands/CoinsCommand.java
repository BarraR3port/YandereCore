package com.podcrash.commissions.yandere.core.spigot.commands;

import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.common.error.UserNotFoundException;
import com.podcrash.commissions.yandere.core.spigot.Main;
import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Collectors;

public class CoinsCommand implements ILyCommand {
    
    @Command(name = "coins", permission = "yandere.admin", aliases = "eco")
    public CommandResponse command(CommandContext context){
        if (context.getArg(0).equalsIgnoreCase("add") || context.getArg(0).equalsIgnoreCase("remove") || context.getArg(0).equalsIgnoreCase("get") || context.getArg(0).equalsIgnoreCase("set")){
            
            try {
                User user = Main.getInstance().getPlayers().getLocalStoredPlayer(context.getArg(1));
                
                if (context.getArg(0).equalsIgnoreCase("get")){
                    final HashMap<String, String> replacements = new HashMap<>();
                    replacements.put("player", context.getArg(0));
                    replacements.put("amount", String.valueOf(user.getCoins()));
                    Main.getLang().sendMsg(context.getSender(), "coins.amount.other", replacements);
                    return new CommandResponse();
                }
                try {
                    int amount = Integer.parseInt(context.getArg(2));
                    
                    final HashMap<String, String> replacements = new HashMap<>();
                    replacements.put("player", context.getArg(1));
                    replacements.put("amount", String.valueOf(amount));
                    if (context.getArg(0).equalsIgnoreCase("add")){
                        Main.getInstance().getPlayers().addCoins(user, amount);
                        if (!Objects.equals(context.getArg(1), context.getSender().getName())){
                            Main.getLang().sendMsg(context.getSender(), "coins.change.add", replacements);
                        }
                        return new CommandResponse();
                    }
                    
                    if (context.getArg(0).equalsIgnoreCase("remove")){
                        if (user.getCoins() <= 0){
                            Main.getLang().sendErrorMsg(context.getSender(), "player.coins.remove.not-enough", "player", context.getArg(1));
                            return new CommandResponse();
                        }
                        Main.getInstance().getPlayers().removeCoins(user, amount);
                        if (!Objects.equals(context.getArg(1), context.getSender().getName())){
                            Main.getLang().sendMsg(context.getSender(), "coins.change.remove", replacements);
                        }
                        return new CommandResponse();
                    }
                    
                    if (context.getArg(0).equalsIgnoreCase("set")){
                        Main.getInstance().getPlayers().setCoins(user, amount);
                        if (!Objects.equals(context.getArg(1), context.getSender().getName())){
                            Main.getLang().sendMsg(context.getSender(), "coins.change.set", replacements);
                        }
                        return new CommandResponse();
                    }
                    
                } catch (NumberFormatException numex) {
                    Main.getLang().sendErrorMsg(context.getSender(), "player.coins.amount.invalid");
                    return new CommandResponse();
                }
                return new CommandResponse();
            } catch (UserNotFoundException ignored) {
                Main.getLang().sendErrorMsg(context.getSender(), "player.not-found", "player", context.getArg(0));
                new CommandResponse();
            }
            
        }
        Utils.sendMessage(context.getSender(), "&8| &eMoney Commands &8|");
        Utils.sendMessage(context.getSender(), " ");
        Main.getLang().sendErrorMsg(context.getSender(), "player.wrong-command", "command", "/coins <add|get|remove|set> <jugador> <cantidad>");
        return new CommandResponse();
    }
    
    @Tab
    public LinkedList<String> tabComplete(TabContext TabContext){
        LinkedList<String> list = new LinkedList<>();
        if (TabContext.getArgs().length == 1){
            list.add("add");
            list.add("remove");
            list.add("set");
            list.add("get");
        }
        if (TabContext.getArgs().length == 2){
            list.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
        }
        if (TabContext.getArgs().length == 3){
            if (TabContext.getArg(0).equalsIgnoreCase("add") || TabContext.getArg(0).equalsIgnoreCase("remove") || TabContext.getArg(0).equalsIgnoreCase("set")){
                list.add("10");
                list.add("100");
                list.add("1000");
                list.add("10000");
                list.add("100000");
                list.add("1000000");
                list.add("10000000");
            }
        }
        return list;
    }
}
