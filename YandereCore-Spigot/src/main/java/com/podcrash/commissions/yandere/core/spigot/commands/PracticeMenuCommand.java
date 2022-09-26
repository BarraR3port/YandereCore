package com.podcrash.commissions.yandere.core.spigot.commands;


import com.podcrash.commissions.yandere.core.spigot.menu.practice.PracticeMenu;
import net.lymarket.common.commands.Tab;
import net.lymarket.lyapi.common.commands.Command;
import net.lymarket.lyapi.common.commands.CommandContext;
import net.lymarket.lyapi.common.commands.ILyCommand;
import net.lymarket.lyapi.common.commands.TabContext;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import net.lymarket.lyapi.spigot.LyApi;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class PracticeMenuCommand implements ILyCommand {
    
    @Command(name = "practicemenu")
    public CommandResponse command(CommandContext context){
        if (context.getSender() instanceof Player){
            Player p = (Player) context.getSender();
            new PracticeMenu(LyApi.getPlayerMenuUtility(p)).open();
        }
        return new CommandResponse();
    }
    
    @Tab
    public LinkedList<String> tabComplete(TabContext context){
        return new LinkedList<>();
    }
}
