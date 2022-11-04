package com.podcrash.commissions.yandere.core.spigot.commands;

import com.podcrash.commissions.yandere.core.spigot.menu.settings.player.PlayerSettingsMainMenu;
import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import net.lymarket.lyapi.spigot.LyApi;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class SettingsCommand implements ILyCommand {
    @Command(name = "settings", aliases = "ajustes")
    public CommandResponse command(CommandContext context){
        if (context.getSender() instanceof Player){
            new PlayerSettingsMainMenu(LyApi.getPlayerMenuUtility((Player) context.getSender())).open();
        }
        return CommandResponse.accept();
    }
    
    @Tab
    public LinkedList<String> tabComplete(TabContext context){
        LinkedList<String> list = new LinkedList<>();
        return list;
    }
}
