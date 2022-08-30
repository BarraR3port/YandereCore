package com.podcrash.commissions.yandere.core.spigot.commands.punish;

import com.podcrash.commissions.yandere.core.spigot.Main;
import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class EnderSeeCommand implements ILyCommand {
    
    @Command(name = "endersee", permission = "yandere.endersee", usage = "/endersee <jugador>")
    public CommandResponse command(CommandContext context){
        if (context.getSender() instanceof Player){
            Player p = (Player) context.getSender();
            if (context.getArgs().length == 1){
                if (Bukkit.getPlayer(context.getArg(0)) != null){
                    if (context.getSender().hasPermission("yandere.endersee")){
                        Player p2 = Bukkit.getPlayer(context.getArg(0));
                        Main.getLang().sendMsg(p, "inventory.ender-inv-see.open", "player", context.getArg(0));
                        Main.getInstance().getEndInvManager().manage(p, p2);
                    } else {
                        return new CommandResponse("yandere.endersee");
                    }
                } else {
                    Main.getLang().sendErrorMsg(p, "player.not-online", "player", context.getArg(0));
                }
            } else {
                p.spigot().sendMessage(Utils.hoverOverMessageSuggestCommand(
                        Main.getLang().getMSG(
                                "error.wrong-command",
                                "command",
                                "endersee <jugador>"),
                        Collections.singletonList("&7Click aquí"),
                        "endersee "));
            }
        }
        
        return new CommandResponse();
    }
    
    @Tab
    public LinkedList<String> tabComplete(TabContext TabContext){
        LinkedList<String> list = new LinkedList<>();
        if (TabContext.getArgs().length == 1){
            list.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(p -> !TabContext.getSender().getName().equals(p)).collect(Collectors.toList()));
            
        }
        return list;
    }
}
