package com.podcrash.commissions.yandere.core.spigot.commands.punish;

import com.podcrash.commissions.yandere.core.spigot.Main;
import net.lymarket.common.commands.*;
import net.lymarket.common.commands.response.CommandResponse;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class InvSeeCommand implements ILyCommand {
    
    @Command(name = "invsee", permission = "yandere.invsee", usage = "/invsee <player>", aliases = {"inv"})
    public CommandResponse command(CommandContext context){
        if (context.getSender() instanceof Player){
            Player p = (Player) context.getSender();
            if (context.getArgs().length == 1){
                if (Bukkit.getPlayer(context.getArg(0)) != null){
                    if (context.getSender().hasPermission("yandere.invsee")){
                        Player p2 = Bukkit.getPlayer(context.getArg(0));
                        Main.getLang().sendMsg(p, "inventory.inv-see.open", "player", context.getArg(0));
                        Main.getInstance().getInvManager().manage(p, p2);
                    } else {
                        return new CommandResponse("yandere.invsee");
                    }
                } else {
                    Main.getLang().sendErrorMsg(p, "player.not-online", "player", context.getArg(0));
                }
            } else {
                
                p.spigot().sendMessage(Utils.hoverOverMessageSuggestCommand(
                        Main.getLang().getMSG(
                                "error.wrong-command",
                                "command",
                                "invsee <player>"),
                        Collections.singletonList("&7Click aquí"),
                        "invsee "));
            }
        }
        return new CommandResponse();
    }
    
    @Tab
    public ArrayList<String> tabComplete(TabContext TabContext){
        ArrayList<String> list = new ArrayList<>();
        if (TabContext.getArgs().length == 1){
            list = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toCollection(ArrayList::new));
        }
        return list;
    }
}
