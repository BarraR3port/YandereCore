package com.podcrash.commissions.yandere.core.spigot.commands.punish;

import com.podcrash.commissions.yandere.core.common.data.punish.PunishType;
import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.punish.PunishManager;
import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.UUID;

public class UnBanCommand implements ILyCommand {
    
    private final PunishManager pm = Main.getInstance().getPunishments();
    
    @Command(name = "unban", permission = "yandere.staff.unban", aliases = "pardon")
    public CommandResponse command(CommandContext context){
        if (context.getSender() instanceof Player){
            Player p = (Player) context.getSender();
            if (p.hasPermission("yandere.staff.unban")){
                if (context.getArgs().length == 0){
                    p.sendMessage(Utils.format("/unban <player>"));
                } else if (context.getArgs().length == 1){
                    User lpTarget = Main.getInstance().getPlayers().getCachedPlayer(context.getArg(0));
                    for ( UUID uuid : lpTarget.getPunishments().keySet() ){
                        if (lpTarget.getPunishments().get(uuid).equals(PunishType.BAN)){
                            pm.unBanByUUID(p,
                                    uuid,
                                    lpTarget);
                        }
                    }
                }
            }
        }
        
        return CommandResponse.accept();
    }
    
    @Tab()
    public LinkedList<String> tabComplete(TabContext context){
        LinkedList<String> list = new LinkedList<>();
        if (context.getSender().hasPermission("yandere.admin")){
            if (context.getArgs().length == 1){
                list.addAll(Main.getInstance().getPunishments().getBans().getBannedPlayers());
            }
        }
        return list;
    }
}
