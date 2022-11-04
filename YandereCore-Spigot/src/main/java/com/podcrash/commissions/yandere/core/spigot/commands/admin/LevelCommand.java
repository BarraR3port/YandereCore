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

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Collectors;

public class LevelCommand implements ILyCommand {
    
    
    @Command(name = "level", permission = "yandere.level", aliases = {"nivel"})
    public CommandResponse command(CommandContext context){
        if (context.getArgs().length == 0){
            if (context.getSender() instanceof Player){
                Player p = (Player) context.getSender();
                User user = Main.getInstance().getPlayers().getPlayer(p.getUniqueId());
                p.spigot().sendMessage(Utils.formatTC(Settings.SERVER_PREFIX), Utils.hoverOverMessage("&7Eres nivel: &c" +
                                user.getLevel().getLevel(),
                        Arrays.asList(
                                "&7Nivel: &c" + user.getLevel().getLevel(),
                                "&7XP: &c" + user.getLevel().getFormattedCurrentXp(),
                                "&7XP Necesario: &c" + user.getLevel().getFormattedRequiredXp(),
                                user.getLevel().getProgressBarFormatted())));
                return CommandResponse.accept();
            }
            
        }
        
        if (context.getArgs().length < 2){
            Main.getLang().sendErrorMsg(context.getSender(), "player.wrong-command", "command", "/level <add|get|set> <jugador> <cantidad>");
            return CommandResponse.accept();
        }
        if (context.getSender().hasPermission("yandere.level.get") || context.getSender().hasPermission("yandere.level.add") || context.getSender().hasPermission("yandere.level.set")){
            
            if (context.getArg(0).equalsIgnoreCase("add") || context.getArg(0).equalsIgnoreCase("get") || context.getArg(0).equalsIgnoreCase("set")){
                try {
                    User user = Main.getInstance().getPlayers().getCachedPlayer(context.getArg(1));
                    
                    if (context.getArg(0).equalsIgnoreCase("get")){
                        
                        final HashMap<String, String> replacements = new HashMap<>();
                        replacements.put("player", context.getArg(1));
                        replacements.put("level", String.valueOf(user.getLevel().getLevel()));
                        Main.getLang().sendMsg(context.getSender(), "level.other", replacements);
                        return CommandResponse.accept();
                    }
                    try {
                        int amount = Integer.parseInt(context.getArg(2));
                        final HashMap<String, String> replacements = new HashMap<>();
                        replacements.put("player", context.getArg(1));
                        replacements.put("level", String.valueOf(amount));
                        if (context.getArg(0).equalsIgnoreCase("add")){
    
                            Main.getInstance().getPlayers().addPlayerLevel(user, amount, GainSource.COMMAND, Settings.SERVER_TYPE);
                            
                            if (!Objects.equals(context.getArg(1), context.getSender().getName())){
                                Main.getLang().sendMsg(context.getSender(), "level.change.add", replacements);
                            }
                            return CommandResponse.accept();
                        }
                        if (context.getArg(0).equalsIgnoreCase("set")){
                            if (amount < 0){
                                Main.getLang().sendErrorMsg(context.getSender(), "player.level.remove.not-enough", "player", context.getArg(1));
                                return CommandResponse.accept();
                            }
    
                            Main.getInstance().getPlayers().setPlayerLevel(user, amount, GainSource.COMMAND, Settings.SERVER_TYPE);
                            
                            if (!Objects.equals(context.getArg(1), context.getSender().getName())){
                                Main.getLang().sendMsg(context.getSender(), "level.change.set", replacements);
                            }
                            return CommandResponse.accept();
                        }
                        
                    } catch (NumberFormatException numex) {
                        Main.getLang().sendErrorMsg(context.getSender(), "player.level.amount.invalid");
                        return CommandResponse.accept();
                    }
                    return CommandResponse.accept();
                } catch (UserNotFoundException ignored) {
                    Main.getLang().sendErrorMsg(context.getSender(), "player.not-found", "player", context.getArg(0));
                    CommandResponse.accept();
                }
            }
        } else {
            CommandResponse.deny("yandere.level.get | yandere.level.set | yandere.level.add");
        }
        return CommandResponse.accept();
    }
    
    @Tab
    public LinkedList<String> tabComplete(TabContext TabContext){
        LinkedList<String> list = new LinkedList<>();
        if (TabContext.getArgs().length == 1){
            list.add("add");
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
