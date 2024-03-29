package com.podcrash.commissions.yandere.core.spigot.commands.misc;

import com.podcrash.commissions.yandere.core.spigot.Main;
import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class TiendaCommand implements ILyCommand {
    
    @Command(name = "tienda", aliases = {"shop", "store", "ecomerce"})
    public CommandResponse command(CommandContext context){
        if (context.getSender() instanceof Player){
            Player player = (Player) context.getSender();
            String link = Main.getLang().getMSG("misc.tienda.url");
            ArrayList<String> messages = new ArrayList<>(Main.getLang().getConfig().getStringList("misc.tienda.messages"));
            for ( String message : messages ){
                if (message.contains("%link%")){
                    String[] linkMessage = message.split("%link%");
                    player.spigot().sendMessage(Utils.formatTC(linkMessage[0]), Utils.hoverOverMessageURL("&e▸ &7Click Aquí &e◂", Collections.singletonList("&7Click para abrir el link"), link), Utils.formatTC(linkMessage[1]));
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
