package com.podcrash.commissions.yandere.core.spigot.commands;

import com.podcrash.commissions.yandere.core.common.data.level.Level;
import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.common.error.UserNotFoundException;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.settings.Settings;
import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Collectors;

public class XPCommand implements ILyCommand {
    
    
    @Command(name = "xp", permission = "yandere.xp", aliases = {"experience", "exp"})
    public CommandResponse command(CommandContext context){
        if (context.getArgs().length == 0){
            if (context.getSender() instanceof Player){
                Player p = (Player) context.getSender();
                User user = Main.getInstance().getPlayers().getPlayer(p.getUniqueId());
                p.spigot().sendMessage(Utils.formatTC(Settings.SERVER_PREFIX), Utils.hoverOverMessage("&7Tienes: &c" +
                                user.getLevel().getFormattedCurrentXp() + " &5XP",
                        Arrays.asList(
                                "&7Nivel: &c" + user.getLevel().getLevel(),
                                "&7XP: &c" + user.getLevel().getFormattedCurrentXp(),
                                "&7XP Necesario: &c" + user.getLevel().getFormattedRequiredXp(),
                                user.getLevel().getProgressBar())));
                return new CommandResponse();
            }
            
        }
        
        if (context.getArgs().length < 2){
            Main.getLang().sendErrorMsg(context.getSender(), "player.wrong-command", "command", "/xp <add|get|set> <jugador> <cantidad>");
            return new CommandResponse();
        }
        if (context.getSender().hasPermission("yandere.xp.get") || context.getSender().hasPermission("yandere.xp.add") || context.getSender().hasPermission("yandere.xp.set")){
            if (context.getArg(0).equalsIgnoreCase("add") || context.getArg(0).equalsIgnoreCase("get") || context.getArg(0).equalsIgnoreCase("set")){
                
                try {
                    OfflinePlayer target = Bukkit.getOfflinePlayer(context.getArg(1));
                    User user = Main.getInstance().getPlayers().getPlayer(target.getUniqueId());
                    if (context.getArg(0).equalsIgnoreCase("get")){
                        final HashMap<String, String> replacements = new HashMap<>();
                        replacements.put("player", context.getArg(1));
                        replacements.put("level", String.valueOf(user.getLevel().getLevel()));
                        replacements.put("xp", user.getLevel().getFormattedCurrentXp());
                        Main.getLang().sendMsg(context.getSender(), "xp.other", replacements);
                        return new CommandResponse();
                    }
                    
                    try {
                        
                        int amount = Integer.parseInt(context.getArg(2));
                        final HashMap<String, String> replacements = new HashMap<>();
                        replacements.put("player", context.getArg(1));
                        replacements.put("xp", String.valueOf(amount));
                        
                        if (context.getArg(0).equalsIgnoreCase("add")){
                            
                            Main.getInstance().getPlayers().addPlayerXp(user, amount, Level.GainSource.COMMAND);
                            
                            if (!Objects.equals(context.getArg(1), context.getSender().getName())){
                                Main.getLang().sendMsg(context.getSender(), "xp.change.add", replacements);
                            }
                            return new CommandResponse();
                        }
                        if (context.getArg(0).equalsIgnoreCase("set")){
                            if (amount < 0){
                                Main.getLang().sendErrorMsg(context.getSender(), "player.xp.remove.not-enough", "player", context.getArg(1));
                                return new CommandResponse();
                            }
                            Main.getInstance().getPlayers().setPlayerXp(user, amount, Level.GainSource.COMMAND);
                            
                            if (!Objects.equals(context.getArg(1), context.getSender().getName())){
                                Main.getLang().sendMsg(context.getSender(), "xp.change.set", replacements);
                            }
                            return new CommandResponse();
                        }
                        
                    } catch (NumberFormatException numex) {
                        Main.getLang().sendErrorMsg(context.getSender(), "player.xp.amount.invalid");
                        return new CommandResponse();
                    }
                    
                } catch (UserNotFoundException ignored) {
                    Main.getLang().sendErrorMsg(context.getSender(), "player.not-found", "player", context.getArg(0));
                    new CommandResponse();
                }
                
                return new CommandResponse();
            }
        } else {
            return new CommandResponse("yandere.xp.get | yandere.xp.set | yandere.xp.add");
        }
        return new CommandResponse();
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
            list.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(p -> !TabContext.getSender().getName().equals(p)).collect(Collectors.toList()));
        }
        return list;
    }
}
