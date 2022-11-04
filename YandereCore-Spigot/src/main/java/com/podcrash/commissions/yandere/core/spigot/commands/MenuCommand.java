package com.podcrash.commissions.yandere.core.spigot.commands;

import com.podcrash.commissions.yandere.core.common.data.server.ServerType;
import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.common.error.UserNotFoundException;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.menu.lobby.LobbyMenu;
import com.podcrash.commissions.yandere.core.spigot.menu.lobby.MultiLobbyMenu;
import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import net.lymarket.lyapi.spigot.LyApi;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public final class MenuCommand implements ILyCommand {
    
    @Command(name = "menu", permission = "yandere.menu", usage = "menu", description = "Menu", aliases = {"m"})
    public CommandResponse command(CommandContext context){
        if (context.getSender() instanceof Player){
            final Player player = (Player) context.getSender();
            if (context.getArgs().length == 0){
                new LobbyMenu(LyApi.getPlayerMenuUtility(player)).open();
                return CommandResponse.accept();
            }
            if (context.getArgs().length == 1){
                switch(context.getArg(0).toUpperCase()){
                    case "LOBBY":
                        new MultiLobbyMenu(LyApi.getPlayerMenuUtility(player), ServerType.LOBBY, false).open();
                        return CommandResponse.accept();
                    case "BED_WARS":
                    case "BEDWARS":
                        new MultiLobbyMenu(LyApi.getPlayerMenuUtility(player), ServerType.LOBBY_BED_WARS, false).open();
                        return CommandResponse.accept();
                    case "SKY_WARS":
                    case "SKYWARS":
                        new MultiLobbyMenu(LyApi.getPlayerMenuUtility(player), ServerType.SKY_WARS, false).open();
                        return CommandResponse.accept();
                    case "PRACTICE":
                        new MultiLobbyMenu(LyApi.getPlayerMenuUtility(player), ServerType.PRACTICE, false).open();
                        return CommandResponse.accept();
                    default:
                        if (player.hasPermission("yandere.menu.other")){
                            final String userName = context.getArg(0);
                            try {
                                final User user = Main.getInstance().getPlayers().getCachedPlayer(userName);
                                new LobbyMenu(LyApi.getPlayerMenuUtility(player), user.getUUID()).open();
                            } catch (UserNotFoundException e) {
                                Main.getLang().sendErrorMsg(player, "player.not-found", "player", userName);
                            }
                        } else {
                            return CommandResponse.deny("yandere.menu.other");
                        }
                }
            }
        }
    
    
        return CommandResponse.accept();
    }
    
    @Tab
    public LinkedList<String> tabComplete(TabContext context){
        final LinkedList<String> list = new LinkedList<>();
        if (context.getArgs().length == 1){
            list.add("lobby");
            list.add("bed_wars");
            list.add("sky_wars");
            list.add("practice");
        }
        if (context.getSender().hasPermission("yandere.admin")){
            if (context.getArgs().length == 1){
                list.addAll(Main.getInstance().getPlayers().getPlayersName());
            }
        }
    
        return list;
    }
}