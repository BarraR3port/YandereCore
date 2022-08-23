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

public class TpDeny implements ILyCommand {
    
    @Command(name = "tpdeny", permission = "yandere.tpdeny")
    public CommandResponse command(CommandContext context){
        if (context.getSender() instanceof Player){
            final Player p = (Player) context.getSender();
            if (context.getArgs().length == 1){
                try {
                    Main.getInstance().getClaimManager().handleTpaRequest(UUID.fromString(context.getArg(0)), false);
                } catch (Exception e) {
                    Utils.sendMessage(p, "&cInvalid Tpa ID.");
                }
            } else {
                Utils.sendMessage(p, "&a| &eTp Commands &a|");
                Utils.sendMessage(p, " ");
                Utils.sendMessage(p, Utils.formatTC("&e> "), Utils.hoverOverMessageSuggestCommand("&b/tp &e<player>", Arrays.asList("&7Con este comando ", "&7teleport to a friend."), "/tp "));
                Utils.sendMessage(p, Utils.formatTC("&e> "), Utils.hoverOverMessageRunCommand("&b/tp &ex y z", Arrays.asList("&7Con este comando ", "&7teleport an specific location."), "/tp "));
                */
/*Utils.sendMessage(p, Utils.formatTC("&e> "), Utils.hoverOverMessageRunCommand("&b/tpa <player>", Arrays.asList("&7Con este comando ", "&7send a TP request to a friend."), "/tpa "));
                Utils.sendMessage(p, Utils.formatTC("&e> "), Utils.hoverOverMessage("&b/tpacept <id>", Arrays.asList("&7Con este comando ", "&7accept a Tpa request.")));
                Utils.sendMessage(p, Utils.formatTC("&e> "), Utils.hoverOverMessage("&b/tpadeny <id>", Arrays.asList("&7Con este comando ", "&7deny a Tpa request.")));
                *//*
return new CommandResponse();
            }
        } else {
            Main.getLang().sendErrorMsg(context.getSender(), "only-players-can-use-this-command");
            return new CommandResponse();
        }
        return new CommandResponse();
    }
    
    @Tab
    public ArrayList<String> tabComplete(TabContext context){
        ArrayList<String> list = new ArrayList<>();
        if (context.getArgs().length == 1 && context.getSender() instanceof Player player){
            return Main.getInstance().getClaimManager().getMyTpaRequests(player.getUniqueId());
        }
        return list;
    }
}
*/
