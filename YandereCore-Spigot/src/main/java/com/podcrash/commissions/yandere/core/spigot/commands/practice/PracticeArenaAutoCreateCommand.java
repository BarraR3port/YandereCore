package com.podcrash.commissions.yandere.core.spigot.commands.practice;


import com.podcrash.commissions.yandere.core.spigot.practice.PracticeArenaCreator;
import ga.strikepractice.StrikePractice;
import ga.strikepractice.api.StrikePracticeAPI;
import ga.strikepractice.arena.Arena;
import net.lymarket.common.commands.Tab;
import net.lymarket.lyapi.common.commands.Command;
import net.lymarket.lyapi.common.commands.CommandContext;
import net.lymarket.lyapi.common.commands.ILyCommand;
import net.lymarket.lyapi.common.commands.TabContext;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class PracticeArenaAutoCreateCommand implements ILyCommand {
    
    @Command(name = "arenaautocreate", permission = "yandere.practice.admin")
    public CommandResponse command(CommandContext context){
        if (context.getSender() instanceof Player){
            Player p = (Player) context.getSender();
            if (context.getArgLength() == 1){
                StrikePracticeAPI sp = StrikePractice.getAPI();
                String arenaName = context.getArg(0);
                Arena arena = sp.getArena(arenaName);
                if (arena != null){
                    p.sendMessage("Initializing the Auto Create Arena System for: " + arenaName);
                    new PracticeArenaCreator(p, arenaName);
                } else {
                    p.sendMessage("Arena not found.");
                }
                return CommandResponse.accept();
            }
        }
        return CommandResponse.accept();
    }
    
    @Tab
    public LinkedList<String> tabComplete(TabContext context){
        return new LinkedList<>();
        
        
    }
}
