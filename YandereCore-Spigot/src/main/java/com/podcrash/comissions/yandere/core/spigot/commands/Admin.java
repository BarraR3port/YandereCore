package com.podcrash.comissions.yandere.core.spigot.commands;

import com.podcrash.comissions.yandere.core.spigot.Main;
import com.podcrash.comissions.yandere.core.spigot.items.Items;
import com.podcrash.comissions.yandere.core.spigot.menu.admin.AdminMenu;
import com.podcrash.comissions.yandere.core.spigot.settings.Settings;
import com.podcrash.comissions.yandere.core.spigot.sounds.Sounds;
import com.podcrash.comissions.yandere.core.spigot.users.SpigotUser;
import net.lymarket.common.commands.*;
import net.lymarket.common.commands.response.CommandResponse;
import net.lymarket.lyapi.spigot.LyApi;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Admin implements ILyCommand {
    
    @Command(name = "admin", permission = "yandere.admin", usage = "admin", description = "Admin Command")
    public CommandResponse command(CommandContext context){
        if (context.getArgs().length == 1){
            if (context.getArg(0).equalsIgnoreCase("reload")){
                Utils.sendMessage(context.getSender(), "&c&lYandere &7- Reloading");
                Main.getInstance().getConfig().reloadConfig();
                Main.getInstance().getItems().reloadConfig();
                Main.getLang().reloadLang();
                Settings.init(Main.getInstance().getConfig());
                Items.init(Main.getInstance().getItems());
                Sounds.init(Main.getInstance().getSounds());
                Main.getInstance().reconnectToProxy();
                Utils.sendMessage(context.getSender(), "&c&lYandere reloaded Successfully!");
                return new CommandResponse();
            } else if (context.getArg(0).equalsIgnoreCase("debug")){
                Settings.DEBUG = !Settings.DEBUG;
                Main.getInstance().getConfig().set("global.debug", Settings.DEBUG);
                Main.getInstance().getConfig().saveData();
                Utils.sendMessage(context.getSender(), "&c&lYandere &dDEBUG " + (Settings.DEBUG ? "&aEnabled" : "&cDisabled"));
                return new CommandResponse();
            }
        }
        if (context.getSender() instanceof Player){
            Player p = (Player) context.getSender();
            if (context.getArgs().length == 2){
                if (context.getArg(0).equalsIgnoreCase("menu")){
                    final SpigotUser target = Main.getInstance().getPlayers().getPlayer(context.getArg(1));
                    new AdminMenu(LyApi.getPlayerMenuUtility(p), target).open();
                    return new CommandResponse();
                }
            }
            Utils.sendMessage(p, "&c&lYandereCore &7- &a&lV" + Main.getInstance().getDescription().getVersion());
            Utils.sendMessage(p, " ");
            Utils.sendMessage(p, "&aCommands: ");
            Utils.sendMessage(p, " ");
            Utils.sendMessage(p, Utils.formatTC("&e> "), Utils.hoverOverMessageSuggestCommand("&b/admin menu <usuario>", Collections.singletonList("&7Con este comando adminstras al jugador de"), "/admin menu "));
            Utils.sendMessage(p, Utils.formatTC("&e> "), Utils.hoverOverMessageSuggestCommand("&b/admin worlds <usuario>", Arrays.asList("&7Con este comando ves los mundos de", "&7un usuario específico."), "/admin worlds "));
            Utils.sendMessage(p, Utils.formatTC("&e> "), Utils.hoverOverMessageSuggestCommand("&b/admin homes <usuario>", Arrays.asList("&7Con este comando ves los homes de", "&7un usuario específico."), "/admin homes "));
            Utils.sendMessage(p, Utils.formatTC("&e> "), Utils.hoverOverMessageSuggestCommand("&b/admin reload", Arrays.asList("&7Con este comando se recarga", "&7la información de la config."), "/admin reload"));
            Utils.sendMessage(p, Utils.formatTC("&e> "), Utils.hoverOverMessageRunCommand("&b/admin debug", Arrays.asList("&7Con este comando activas o desactivas", "&7el modo debug."), "/admin debug"));
            return new CommandResponse();
        }
        return new CommandResponse("yandere.admin");
    }
    
    @Tab
    public ArrayList<String> tabComplete(TabContext context){
        final ArrayList<String> list = new ArrayList<>();
        if (context.getSender().hasPermission("yandere.admin")){
            if (context.getArgs().length == 1){
                list.add("reload");
                list.add("worlds");
                list.add("homes");
                list.add("worlds");
                list.add("debug");
                list.add("menu");
                return list;
            }
            if (context.getArgs().length == 2){
                if (context.getArg(0).equalsIgnoreCase("menu") || context.getArg(0).equalsIgnoreCase("worlds") || context.getArg(0).equalsIgnoreCase("homes")){
                    list.addAll(Main.getInstance().getPlayers().getPlayersName(context.getSender().getName()));
                }
            }
        }
        
        return list;
    }
}
