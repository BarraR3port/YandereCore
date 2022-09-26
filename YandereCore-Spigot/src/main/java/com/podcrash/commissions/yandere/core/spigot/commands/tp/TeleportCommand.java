package com.podcrash.commissions.yandere.core.spigot.commands.tp;

import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.sounds.Sounds;
import com.podcrash.commissions.yandere.core.spigot.tp.TpManager;
import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class TeleportCommand implements ILyCommand {
    
    
    @Command(name = "teleport", permission = "yandere.tp", aliases = "tp")
    public CommandResponse command(CommandContext context){
        if (context.getSender() instanceof Player){
            Player p = (Player) context.getSender();
            switch(context.getArgs().length){
                case 1:{
                    TpManager.teleportToPlayer(p, context.getArg(0));
                    return new CommandResponse();
                }
                case 2:{
                    TpManager.teleportPlayerToPlayer(p, context.getArg(0), context.getArg(1));
                    return new CommandResponse();
                }
                case 3:{
                    if (p.hasPermission("yandere.tp.coords")){
                        try {
                            final double x = Double.parseDouble(context.getArg(0));
                            final double y = Double.parseDouble(context.getArg(1));
                            final double z = Double.parseDouble(context.getArg(2));
                            final Location location = new Location(p.getWorld(), x, y, z);
                            Utils.sendMessage(p, "&aTeletransportanto hacia " + x + ", " + y + ", " + z + "&a.");
                            if (p.teleport(location, PlayerTeleportEvent.TeleportCause.COMMAND)){
                                Sounds.play(p, "tp-successfully");
                            } else {
                                Sounds.BAD_SOUND.play(p);
                                Utils.sendMessage(p, "&cNo hemos logrado teletransportarte hacia " + x + ", " + y + ", " + z + "&c.");
                            }
                            return new CommandResponse();
                        } catch (NumberFormatException numex) {
                            Sounds.BAD_SOUND.play(p);
                            Utils.sendMessage(p, "&cNo hemos logrado teletransportarte hacia " + context.getArg(0) + ", " + context.getArg(1) + ", " + context.getArg(2) + "&c.");
                            return new CommandResponse();
                        }
                    } else {
                        return new CommandResponse("yandere.tp.coords");
                    }
                    
                }
                default:{
                    Utils.sendMessage(p, "&8| &eComandos de TP &8|");
                    Utils.sendMessage(p, " ");
                    Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageSuggestCommand("&c/tp &e<jugador>", Arrays.asList("&7Con este comando ", "&7teleport to a friend."), "/tp "));
                    Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageRunCommand("&c/tp &ex y z", Arrays.asList("&7Con este comando ", "&7teleport an specific location."), "/tp "));
                   /* Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageRunCommand("&c/tpa <jugador>", Arrays.asList("&7Con este comando ", "&7send a TP request to a friend."), "/tpa "));
                    Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessage("&c/tpacept <id>", Arrays.asList("&7Con este comando ", "&7accept a Tpa request.")));
                    Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessage("&c/tpadeny <id>", Arrays.asList("&7Con este comando ", "&7deny a Tpa request.")));
                    *///Utils.sendMessage( p , Utils.formatTC( "  &8&l▸ " ) , Utils.hoverOverMessageSuggestCommand( "&c/tp <jugador> <jugador>" , Arrays.asList( "&7Con este comando " , "&7remove a friend from your FriendList." ) , "/friends remove " ) );
                    return new CommandResponse();
                }
            }
            
        } else {
            Main.getLang().sendErrorMsg(context.getSender(), "only-players-can-use-this-command");
            return new CommandResponse();
        }
    }
    
    @Tab
    public LinkedList<String> tabComplete(TabContext context){
        final LinkedList<String> list = new LinkedList<>();
        switch(context.getArgs().length){
            case 1:
            case 2:{
                list.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(p -> !context.getSender().getName().equals(p)).collect(Collectors.toList()));
            }
        }
        return list;
    }
}
