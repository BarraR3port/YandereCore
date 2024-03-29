package com.podcrash.commissions.yandere.core.spigot.commands.admin;

import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.common.data.user.props.GainSource;
import com.podcrash.commissions.yandere.core.common.error.UserNotFoundException;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.settings.Settings;
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
                User user = Main.getInstance().getPlayers().getCachedPlayer(context.getArg(1));
                
                if (context.getArg(0).equalsIgnoreCase("get")){
                    final HashMap<String, String> replacements = new HashMap<>();
                    replacements.put("player", context.getArg(0));
                    replacements.put("amount", String.valueOf(user.getCoins()));
                    Main.getLang().sendMsg(context.getSender(), "coins.amount.other", replacements);
                    return CommandResponse.accept();
                }
                try {
                    int amount = Integer.parseInt(context.getArg(2));
                    
                    final HashMap<String, String> replacements = new HashMap<>();
                    replacements.put("player", context.getArg(1));
                    replacements.put("amount", String.valueOf(amount));
                    if (context.getArg(0).equalsIgnoreCase("add")){
                        Main.getInstance().getPlayers().addCoins(user, amount, GainSource.COMMAND, Settings.SERVER_TYPE);
                        if (!Objects.equals(context.getArg(1), context.getSender().getName())){
                            Main.getLang().sendMsg(context.getSender(), "coins.change.add", replacements);
                        }
                        return CommandResponse.accept();
                    }
                    
                    if (context.getArg(0).equalsIgnoreCase("remove")){
                        if (user.getCoins() <= 0){
                            Main.getLang().sendErrorMsg(context.getSender(), "player.coins.remove.not-enough", "player", context.getArg(1));
                            return CommandResponse.accept();
                        }
                        Main.getInstance().getPlayers().removeCoins(user, amount, GainSource.COMMAND, Settings.SERVER_TYPE);
                        if (!Objects.equals(context.getArg(1), context.getSender().getName())){
                            Main.getLang().sendMsg(context.getSender(), "coins.change.remove", replacements);
                        }
                        return CommandResponse.accept();
                    }
                    
                    if (context.getArg(0).equalsIgnoreCase("set")){
                        Main.getInstance().getPlayers().setCoins(user, amount, GainSource.COMMAND, Settings.SERVER_TYPE);
                        if (!Objects.equals(context.getArg(1), context.getSender().getName())){
                            Main.getLang().sendMsg(context.getSender(), "coins.change.set", replacements);
                        }
                        return CommandResponse.accept();
                    }
                    
                } catch (NumberFormatException numex) {
                    Main.getLang().sendErrorMsg(context.getSender(), "player.coins.amount.invalid");
                    return CommandResponse.accept();
                }
                return CommandResponse.accept();
            } catch (UserNotFoundException ignored) {
                Main.getLang().sendErrorMsg(context.getSender(), "player.not-found", "player", context.getArg(0));
                CommandResponse.accept();
            }
            
        }
        Utils.sendMessage(context.getSender(), "&8| &eMoney Commands &8|");
        Utils.sendMessage(context.getSender(), " ");
        Main.getLang().sendErrorMsg(context.getSender(), "player.wrong-command", "command", "/coins <add|get|remove|set> <jugador> <cantidad>");
        return CommandResponse.accept();
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
