package com.podcrash.commissions.yandere.core.spigot.commands.punish;

import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.menu.admin.punish.mod.ModeratorInv;
import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import net.lymarket.lyapi.spigot.LyApi;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class Moderator implements ILyCommand {
    
    @Command(name = "moderator", aliases = "mod", permission = "yandere.moderator")
    public CommandResponse command(CommandContext context){
        if (!(context.getSender() instanceof Player)){
            Bukkit.getConsoleSender().sendMessage(" §cNo puedes ejecutar comandos desde la consola");
            return new CommandResponse();
        }
        Player p = (Player) context.getSender();
        
        if (context.getArgs().length == 0){
            p.sendMessage(" §cUso correcto: /mod (jugador)");
            return new CommandResponse();
        }
        Player target = Bukkit.getPlayer(context.getArg(0));
        if (target != null && target.getType().equals(EntityType.PLAYER)){
            new ModeratorInv(LyApi.getPlayerMenuUtility(p), target).open();
        } else {
            Main.getLang().sendErrorMsg(context.getSender(), "player.not-found", "player", context.getArg(0));
        }
        return new CommandResponse();
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
