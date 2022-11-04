package com.podcrash.commissions.yandere.core.spigot.commands;

import com.podcrash.commissions.yandere.core.spigot.Main;
import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class VanishCommand implements ILyCommand {
    
    @Command(name = "vanish", permission = "yandere.vanish", usage = "/vanish", aliases = {"v"})
    public CommandResponse command(CommandContext context){
        if (context.getSender() instanceof Player){
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
                                "vanish | vanish <jugador>"),
                        Collections.singletonList("&7Click aquí"),
                        "vanish "));
            }
            
        }
        return CommandResponse.accept();
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
