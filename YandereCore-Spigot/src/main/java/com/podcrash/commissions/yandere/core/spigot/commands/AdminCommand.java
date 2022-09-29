package com.podcrash.commissions.yandere.core.spigot.commands;

import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.items.Items;
import com.podcrash.commissions.yandere.core.spigot.menu.admin.AdminMenu;
import com.podcrash.commissions.yandere.core.spigot.menu.settings.ServerSettings;
import com.podcrash.commissions.yandere.core.spigot.settings.Settings;
import com.podcrash.commissions.yandere.core.spigot.sounds.Sounds;
import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import net.lymarket.lyapi.spigot.LyApi;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class AdminCommand implements ILyCommand {
    
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
                Utils.sendMessage(context.getSender(), "&c&lRegistering Commands!");
                Main.getInstance().registerCommands();
                Utils.sendMessage(context.getSender(), "&c&lYandere reloaded Successfully!");
                Main.getInstance().getGlobalServerSettings().fetch();
                return new CommandResponse();
            } else if (context.getArg(0).equalsIgnoreCase("debug")){
                Settings.DEBUG = !Settings.DEBUG;
                Main.getInstance().getConfig().set("global.debug", Settings.DEBUG);
                Main.getInstance().getConfig().saveData();
                Utils.sendMessage(context.getSender(), "&c&lYandere &cDEBUG " + (Settings.DEBUG ? "&aEnabled" : "&cDisabled"));
                return new CommandResponse();
            } else if (context.getArg(0).equalsIgnoreCase("menu")){
                if (context.getSender() instanceof Player){
                    Player player = (Player) context.getSender();
                    new ServerSettings(LyApi.getPlayerMenuUtility(player)).open();
                }
                return new CommandResponse();
            } else if (context.getArg(0).equalsIgnoreCase("fetch-global-settings")){
                Utils.sendMessage(context.getSender(), "&c&lYandere &7- Checking for Global Settings updates");
                Main.getInstance().getGlobalServerSettings().fetch();
                return new CommandResponse();
            } else if (context.getArg(0).equalsIgnoreCase("fetch-global-settings-general")){
                Utils.sendMessage(context.getSender(), "&c&lYandere &7- Checking for Global Settings updates (General)");
                Main.getInstance().getGlobalServerSettings().fetch();
                Main.getInstance().getSocket().sendCheckGlobalServerStatsFetchData();
                return new CommandResponse();
            } else if (context.getArg(0).equalsIgnoreCase("refresh-plugins")){
                Utils.sendMessage(context.getSender(), "&c&lYandere &7- Checking for plugin updates");
                Main.getInstance().getServerRepository().checkForPluginsUpdates();
                return new CommandResponse();
            } else if (context.getArg(0).equalsIgnoreCase("refresh-plugins-general")){
                Utils.sendMessage(context.getSender(), "&c&lYandere &7- Checking for plugin updates (General)");
                Main.getInstance().getSocket().sendCheckPluginUpdates();
                return new CommandResponse();
            } /*else if(context.getArg(0).equalsIgnoreCase("test-reload")){
                LyApi.reloadConfigs();
                return new CommandResponse();
            }*/
        }
        if (context.getSender() instanceof Player){
            Player p = (Player) context.getSender();
            if (context.getArgs().length == 2){
                if (context.getArg(0).equalsIgnoreCase("menu")){
                    final User target = Main.getInstance().getPlayers().getCachedPlayer(context.getArg(1));
                    new AdminMenu(LyApi.getPlayerMenuUtility(p), target).open();
                    return new CommandResponse();
                }
            }
            Utils.sendMessage(p, "&c&lYandereCore &7- &a&lV" + Main.getInstance().getDescription().getVersion());
            Utils.sendMessage(p, " ");
            Utils.sendMessage(p, "&aCommands: ");
            Utils.sendMessage(p, " ");
            Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageSuggestCommand("&c/admin menu <usuario>", Collections.singletonList("&7Con este comando adminstras al jugador de"), "/admin menu "));
            Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageSuggestCommand("&c/admin worlds <usuario>", Arrays.asList("&7Con este comando ves los mundos de", "&7un usuario específico."), "/admin worlds "));
            Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageSuggestCommand("&c/admin homes <usuario>", Arrays.asList("&7Con este comando ves los homes de", "&7un usuario específico."), "/admin homes "));
            Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageSuggestCommand("&c/admin reload", Arrays.asList("&7Con este comando se recarga", "&7la información de la config."), "/admin reload"));
            Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageRunCommand("&c/admin debug", Arrays.asList("&7Con este comando activas o desactivas", "&7el modo debug."), "/admin debug"));
            return new CommandResponse();
        }
        return new CommandResponse("yandere.admin");
    }
    
    @Tab
    public LinkedList<String> tabComplete(TabContext context){
        final LinkedList<String> list = new LinkedList<>();
        if (context.getSender().hasPermission("yandere.admin")){
            if (context.getArgs().length == 1){
                list.add("reload");
                list.add("debug");
                list.add("menu");
                list.add("refresh-plugins");
                list.add("refresh-plugins-general");
                list.add("fetch-global-settings");
                list.add("fetch-global-settings-general");
                //list.add("test-reload");
                return list;
            }
            /*if (context.getArgs().length == 2){
                if (context.getArg(0).equalsIgnoreCase("menu") || context.getArg(0).equalsIgnoreCase("worlds") || context.getArg(0).equalsIgnoreCase("homes")){
                    list.addAll(Main.getInstance().getPlayers().getPlayersName(context.getSender().getName()));
                }
            }*/
        }
        
        return list;
    }
}
