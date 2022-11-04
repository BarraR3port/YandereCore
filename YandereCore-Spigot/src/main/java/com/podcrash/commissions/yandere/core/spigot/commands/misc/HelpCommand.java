package com.podcrash.commissions.yandere.core.spigot.commands.misc;

import com.podcrash.commissions.yandere.core.spigot.Main;
import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class HelpCommand implements ILyCommand {
    
    @Command(name = "ayuda", aliases = {"help", "info"})
    public CommandResponse command(CommandContext context){
        if (context.getSender() instanceof Player){
            Player player = (Player) context.getSender();
            ArrayList<String> messages = new ArrayList<>(Main.getLang().getConfig().getStringList("misc.help.messages"));
            String commandColor = Main.getLang().getMSG("misc.help.command-color");
            for ( String message : messages ){
                if (message.contains("[command=")){
                    String[] realText = message.split("[\\[].*?]");
                    String command = message.split("\\[command=")[1].split("\\]")[0].trim();
                    String commandHover = message.split("\\[subcommand=")[1].split("\\]")[0].trim();
                    player.spigot().sendMessage(Utils.formatTC(realText[0]), Utils.hoverOverMessageRunCommand(commandColor + command, Collections.singletonList(commandHover), "/" + command));
                } else {
                    Utils.sendMessage(player, message);
                }
            }
        }
        return CommandResponse.accept();
    }
    
    @Tab
    public LinkedList<String> tabComplete(TabContext context){
        return new LinkedList<>();
    }
}
