package com.podcrash.commissions.yandere.core.spigot.commands.admin;

import com.podcrash.commissions.yandere.core.spigot.listener.lobby.LobbyPlayerEvents;
import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.stream.Collectors;

public class BuildCommand implements ILyCommand {
    @Command(name = "build", permission = "yandere.build")
    public CommandResponse command(CommandContext context){
        if (context.getArgLength() == 0){
            if (context.getSender() instanceof Player){
                Player player = (Player) context.getSender();
                if (LobbyPlayerEvents.builders.contains(player.getUniqueId())){
                    LobbyPlayerEvents.builders.remove(player.getUniqueId());
                    player.sendMessage("§aYa no puedes construir.");
                } else {
                    LobbyPlayerEvents.builders.add(player.getUniqueId());
                    player.sendMessage("§aYa puedes construir.");
                }
            } else {
                context.getSender().sendMessage("§cSolo puedes usar este comando en un jugador.");
            }
            return CommandResponse.accept();
        } else if (context.getArgLength() == 1){
            if (context.getSender() instanceof Player){
                Player player = (Player) context.getSender();
                Player target = Bukkit.getPlayer(context.getArg(0));
                if (LobbyPlayerEvents.builders.contains(target.getUniqueId())){
                    LobbyPlayerEvents.builders.remove(target.getUniqueId());
                    target.sendMessage("§aYa no puedes construir.");
                    player.sendMessage("§a" + target.getName() + " ya no puede construir ");
                } else {
                    LobbyPlayerEvents.builders.add(target.getUniqueId());
                    target.sendMessage("§aYa puedes construir.");
                    player.sendMessage("§a" + target.getName() + " ya puede construir ");
                }
            } else {
                context.getSender().sendMessage("§cSolo puedes usar este comando en un jugador.");
            }
        }
        return null;
    }
    
    @Tab
    public LinkedList<String> tabComplete(TabContext context){
        final LinkedList<String> list = new LinkedList<>();
        if (context.getArgLength() == 1){
            list.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
        }
        return list;
    }
}
