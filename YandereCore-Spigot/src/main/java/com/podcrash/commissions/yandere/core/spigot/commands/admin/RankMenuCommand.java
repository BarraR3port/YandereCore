package com.podcrash.commissions.yandere.core.spigot.commands.admin;

import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.common.data.user.props.Rank;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.menu.admin.rank.RankEditor;
import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import net.lymarket.lyapi.spigot.LyApi;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Locale;

public class RankMenuCommand implements ILyCommand {
    
    @Command(name = "rank", permission = "yandere.rank")
    public CommandResponse command(CommandContext context){
        
        if (context.getArgs().length == 0){
            context.getSender().sendMessage("§b§m----------------------------------------------------");
            context.getSender().sendMessage("                                §a§lRank System");
            context.getSender().sendMessage("");
            if (context.getSender() instanceof Player){
                Player p = (Player) context.getSender();
                p.spigot().sendMessage(Utils.formatTC("&7Establece el rango a un jugador con este "), Utils.hoverOverMessageSuggestCommand("&ccomando", Collections.singletonList("&e/rank set <jugador> <rango>"), "/rank set "));
            } else {
                context.getSender().sendMessage("§e- §dEstablece el rango a un jugador §e- §d/rank set <jugador> (rango)");
            }
            context.getSender().sendMessage("§b§m----------------------------------------------------");
            return new CommandResponse();
        }
    
        if (context.getArg(0).equalsIgnoreCase("set")){
            if (context.getArgs().length == 2){
                final String target = context.getArg(1);
                try {
                    User user = Main.getInstance().getPlayers().getCachedPlayer(target);
                    new RankEditor(LyApi.getPlayerMenuUtility((Player) context.getSender()), user).open();
                } catch (NullPointerException e) {
                    Main.getLang().sendErrorMsg(context.getSender(), "player.not-fund", "player", target);
                }
            } else if (context.getArgs().length == 3){
                final String target = context.getArg(1);
                try {
                    Rank rank = Rank.valueOf(context.getArg(2).toUpperCase(Locale.ROOT));
                    User user = Main.getInstance().getPlayers().getCachedPlayer(target);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + target + " group set " + rank.getLpName());
                    Main.getInstance().getPlayers().setPlayerRank(user, rank);
                } catch (NullPointerException e) {
                    Main.getLang().sendErrorMsg(context.getSender(), "player.not-fund", "player", target);
                }
            } else {
                Main.getLang().sendErrorMsg(context.getSender(), "wrong-command", "command", "/rank set <jugador> (rango)");
            }
            return new CommandResponse();
        } else if (context.getArg(0).equalsIgnoreCase("editor")){
            if (context.getArgs().length == 2){
                final String target = context.getArg(1);
                try {
                    User user = Main.getInstance().getPlayers().getCachedPlayer(target);
                    new RankEditor(LyApi.getPlayerMenuUtility((Player) context.getSender()), user).open();
                } catch (NullPointerException e) {
                    Main.getLang().sendErrorMsg(context.getSender(), "player.not-fund", "player", target);
                }
            } else {
                Main.getLang().sendErrorMsg(context.getSender(), "wrong-command", "command", "/rank set <jugador> (rango)");
            }
            return new CommandResponse();
        }
        return new CommandResponse("yandere.rank");
    }
    
    
    @Tab
    public LinkedList<String> tabComplete(TabContext context){
        LinkedList<String> list = new LinkedList<>();
        if (context.getSender().hasPermission("yandere.rank")){
            if (context.getArgs().length == 1){
                list.add("set");
                list.add("editor");
            }
            if (context.getArgs().length == 2){
                list.addAll(Main.getInstance().getPlayers().getPlayersName());
            }
            if (context.getArgs().length == 3){
                for ( Rank rank : Rank.values() ){
                    list.add(StringUtils.capitalize(rank.name().toLowerCase(Locale.ROOT)));
                }
            }
        }
        return list;
    }
}
