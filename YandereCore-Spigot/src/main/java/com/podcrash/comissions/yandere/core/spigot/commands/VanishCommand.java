package com.podcrash.comissions.yandere.core.spigot.commands;

import com.podcrash.comissions.yandere.core.spigot.Main;
import net.lymarket.common.commands.*;
import net.lymarket.common.commands.response.CommandResponse;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class VanishCommand implements ILyCommand {
    
    @Command(name = "vanish", permission = "yandere.vanish", usage = "/vanish", aliases = {"v"})
    public CommandResponse command(CommandContext context){
        if (context.getSender() instanceof Player){
            if (context.getSender().hasPermission("yandere.vanish")){
                Player p = (Player) context.getSender();
                if (context.getArgs().length == 0){
                    if (Main.getInstance().getVanishManager().isVanished(p)){
                        Main.getInstance().getVanishManager().disable(p);
                        Utils.playActionBar(p, "&7Vanish &cDesactivado");
                    } else {
                        Main.getInstance().getVanishManager().enable(p);
                        Utils.playActionBar(p, "&7Vanish &aActivado");
                    }
                    
                } else if (context.getArgs().length == 1){
                    if (Bukkit.getPlayer(context.getArg(0)) != null){
                        Player target = Bukkit.getPlayer(context.getArg(0));
                        if (Main.getInstance().getVanishManager().isVanished(target)){
                            Main.getInstance().getVanishManager().disable(target);
                            Utils.playActionBar(target, "&7Vanish &cDesactivado");
                        } else {
                            Main.getInstance().getVanishManager().enable(target);
                            Utils.playActionBar(target, "&7Vanish &aActivado");
                        }
                        
                    } else {
                        Main.getLang().sendErrorMsg(p, "player.not-online", "player", context.getArg(0));
                    }
                } else {
                    p.spigot().sendMessage(Utils.hoverOverMessageSuggestCommand(
                            Main.getLang().getMSG(
                                    "error.wrong-command",
                                    "command",
                                    "vanish | vanish <player>"),
                            Collections.singletonList("&7Click aquí"),
                            "vanish "));
                }
            } else {
                return new CommandResponse("yandere.vanish");
            }
        }
        return new CommandResponse();
    }
    
    @Tab
    public ArrayList<String> tabComplete(TabContext TabContext){
        ArrayList<String> list = new ArrayList<>();
        if (TabContext.getArgs().length == 1){
            for ( Player p : Bukkit.getOnlinePlayers() ){
                if (Objects.equals(p.getName(), TabContext.getSender().getName()))
                    continue;
                list.add(p.getName());
            }
        }
        return list;
    }
}
