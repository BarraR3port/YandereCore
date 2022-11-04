package com.podcrash.commissions.yandere.core.spigot.commands.punish;

import com.podcrash.commissions.yandere.core.common.data.punish.PunishDateType;
import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.menu.admin.punish.ban.BanMenu;
import com.podcrash.commissions.yandere.core.spigot.punish.PunishManager;
import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import net.lymarket.lyapi.spigot.LyApi;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class BanCommand implements ILyCommand {
    
    private final PunishManager pm = Main.getInstance().getPunishments();
    
    @Command(name = "ban", permission = "yandere.staff.ban")
    public CommandResponse command(CommandContext context){
        if (context.getSender() instanceof Player){
            Player p = (Player) context.getSender();
            if (p.hasPermission("yandere.staff.ban")){
                if (context.getArgs().length == 0){
                    p.sendMessage(Utils.format("/ban <player> <tiempo> <-ip> <razón>"));
                } else if (context.getArgs().length == 1){
                    User lpTarget = Main.getInstance().getPlayers().getCachedPlayer(context.getArg(0));
                    if (lpTarget == null){
                        Main.getLang().sendErrorMsg(context.getSender(), "player.not-found", "player", context.getArg(0));
                        return CommandResponse.accept();
                    }
                    new BanMenu(LyApi.getPlayerMenuUtility(p), lpTarget).open();
                } else {
                    StringBuilder reason = new StringBuilder();
                    User lp = Main.getInstance().getPlayers().getCachedPlayer(p.getName());
                    User lpTarget = Main.getInstance().getPlayers().getCachedPlayer(context.getArg(0));
                    if (isNormal(context.getArg(1))){
                        String lastWord = context.getArg(1).substring(context.getArg(1).length() - 1);
                        int amount = Integer.parseInt(context.getArg(1).substring(0, context.getArg(1).length() - 1));
                        boolean ip = false;
                        
                        for ( int i = 2; i < context.getArgs().length; i++ ){
                            if (context.getArg(i).equalsIgnoreCase("-ip")){
                                ip = true;
                            } else {
                                reason.append(context.getArg(i)).append(" ");
                            }
                        }
                        String finalReason = reason.toString();
                        pm.banPlayer(lpTarget, lp, finalReason, PunishDateType.ConvertDate(amount, lastWord), ip, Utils.getServer());
                        
                    } else {
                        boolean ip = false;
                        String finalReason = reason.toString();
                        
                        for ( int i = 1; i < context.getArgs().length; i++ ){
                            if (context.getArg(i).equalsIgnoreCase("-ip")){
                                ip = true;
                            } else {
                                reason.append(context.getArg(i)).append(" ");
                            }
                        }
                        pm.permanentBan(lpTarget, lp, finalReason, ip, Utils.getServer());
                        
                    }
                }
            }
        }
    
        return CommandResponse.accept();
    }
    
    private Boolean isNormal(String s){
        String lastWord = s.substring(s.length() - 1);
        
        return lastWord.equalsIgnoreCase("s") ||
                lastWord.equalsIgnoreCase("m") ||
                lastWord.equalsIgnoreCase("h") ||
                lastWord.equalsIgnoreCase("d");
        
    }
    
    @Tab()
    public LinkedList<String> tabComplete(TabContext context){
        LinkedList<String> list = new LinkedList<>();
        if (context.getSender().hasPermission("yandere.admin")){
            if (context.getArgs().length == 1){
                list.addAll(Main.getInstance().getPlayers().getPlayersName());
            }
        }
        return list;
    }
}
