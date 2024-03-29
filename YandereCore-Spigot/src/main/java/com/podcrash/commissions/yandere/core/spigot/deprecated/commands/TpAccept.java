/*
package com.podcrash.commissions.yandere.core.spigot.commands.tp;

import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.divine.core.spigot.Main;
import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class TpAccept implements ILyCommand {
    
    @Command(name = "tpaccept", permission = "yandere.tpaccept")
    public CommandResponse command(CommandContext context){
        if (context.getSender() instanceof Player){
            final Player p = (Player) context.getSender();
            if (context.getArgs().length == 1){
                try {
                    Main.getInstance().getClaimManager().handleTpaRequest(UUID.fromString(context.getArg(0)), true);
                } catch (Exception e) {
                    Utils.sendMessage(p, "&cInvalid Tpa ID.");
                }
            } else {
                Utils.sendMessage(p, "&8| &eTp Commands &8|");
                Utils.sendMessage(p, " ");
                Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageSuggestCommand("&c/tp &e<jugador>", Arrays.asList("&7Con este comando ", "&7teleport to a friend."), "/tp "));
                Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageRunCommand("&c/tp &ex y z", Arrays.asList("&7Con este comando ", "&7teleport an specific location."), "/tp "));
                Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageRunCommand("&c/tpa <jugador>", Arrays.asList("&7Con este comando ", "&7send a TP request to a friend."), "/tpa "));
                Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessage("&c/tpacept <id>", Arrays.asList("&7Con este comando ", "&7accept a Tpa request.")));
                Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessage("&c/tpadeny <id>", Arrays.asList("&7Con este comando ", "&7deny a Tpa request.")));
                return CommandResponse.accept();
            }
        } else {
            Main.getLang().sendErrorMsg(context.getSender(), "only-players-can-use-this-command");
            return CommandResponse.accept();
        }
        return CommandResponse.accept();
    }
    
    @Tab
    public LinkedList<String> tabComplete(TabContext context){
        LinkedList<String> list = new LinkedList<>();
        if (context.getArgs().length == 1 && context.getSender() instanceof Player player){
            return Main.getInstance().getClaimManager().getMyTpaRequests(player.getUniqueId());
        }
        return list;
    }
    
}
*/
