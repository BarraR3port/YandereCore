package com.podcrash.commissions.yandere.core.spigot.gamemode;

import com.podcrash.commissions.yandere.core.spigot.Main;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;

public final class GamemodeManager {
    
    public static CommandResponse setGamemode(CommandSender sender, String targetName, GameMode gamemode, String gamemodeText, int argsLength){
        if (!(sender instanceof Player)){
            if (argsLength == 1){
                sendWrongSyntax(sender);
                return new CommandResponse();
            }
            Player target = Bukkit.getPlayer(targetName);
            if (target != null){
                target.setGameMode(gamemode);
                Main.getLang().sendMsg(target, "gamemode.change", "gamemode", gamemodeText);
                HashMap<String, String> map = new HashMap<>();
                map.put("player", target.getName());
                map.put("gamemode", gamemodeText);
                Main.getLang().sendMsg(sender, "gamemode.change-other", map);
                return new CommandResponse();
            }
            Main.getLang().sendErrorMsg(sender, "player.not-found", "player", targetName);
            return new CommandResponse();
        }
        
        Player p = (Player) sender;
        
        if (p.hasPermission("yandere.gamemode." + gamemode.name().toLowerCase()) || p.hasPermission("yandere.gamemode.all")){
            if (argsLength == 1 || (argsLength == 0 && targetName == null)){
                p.setGameMode(gamemode);
                Main.getLang().sendMsg(p, "gamemode.change", "gamemode", gamemodeText);
                return new CommandResponse();
            }
            
            Player target = Bukkit.getPlayer(targetName);
            if (target != null){
                target.setGameMode(gamemode);
                Main.getLang().sendMsg(target, "gamemode.change", "gamemode", gamemodeText);
                HashMap<String, String> map = new HashMap<>();
                map.put("player", target.getName());
                map.put("gamemode", gamemodeText);
                Main.getLang().sendMsg(p, "gamemode.change-other", map);
                return new CommandResponse();
            }
            Main.getLang().sendErrorMsg(sender, "player.not-found", "player", targetName);
            return new CommandResponse();
        } else {
            return new CommandResponse("yandere.gamemode." + gamemode.name().toLowerCase());
        }
    }
    
    public static void sendWrongSyntax(CommandSender sender){
        if (!(sender instanceof Player)){
            Utils.sendMessage(sender, "&8| &eComandos de GameMode &8|");
            Utils.sendMessage(sender, " ");
            Utils.sendMessage(sender, "  &8&l▸ &c/gm 0 <jugador>");
            Utils.sendMessage(sender, "  &8&l▸ &c/gms <jugador>");
            Utils.sendMessage(sender, "  &8&l▸ &c/gm 1 <jugador>");
            Utils.sendMessage(sender, "  &8&l▸ &c/gmc <jugador>");
            Utils.sendMessage(sender, "  &8&l▸ &c/gm 2 <jugador>");
            Utils.sendMessage(sender, "  &8&l▸ &c/gma <jugador>");
            Utils.sendMessage(sender, "  &8&l▸ &c/gm 3 <jugador>");
            Utils.sendMessage(sender, "  &8&l▸ &c/gmsp <jugador>");
            Utils.sendMessage(sender, " ");
        } else {
            Player p = (Player) sender;
            Utils.sendMessage(p, "&8| &eComandos de GameMode &8|");
            Utils.sendMessage(p, " ");
            if (p.hasPermission("yandere.gamemode.all") || p.hasPermission("yandere.gamemode.survival")){
                Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageRunCommand("&c/gm 0", Arrays.asList("&7Con este comando cambias", "&7al modo de juego &eSupervivencia."), "/gm 0"));
                Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageRunCommand("&c/gms", Arrays.asList("&7Con este comando cambias", "&7al modo de juego &eSupervivencia."), "/gms"));
            }
            if (p.hasPermission("yandere.gamemode.all") || p.hasPermission("yandere.gamemode.creative")){
                Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageRunCommand("&c/gm 1", Arrays.asList("&7Con este comando cambias", "&7al modo de juego &eCreativo."), "/gm 1"));
                Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageRunCommand("&c/gmc", Arrays.asList("&7Con este comando cambias", "&7al modo de juego &eCreativo."), "/gmc"));
                
            }
            if (p.hasPermission("yandere.gamemode.all") || p.hasPermission("yandere.gamemode.adventure")){
                Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageRunCommand("&c/gm 2", Arrays.asList("&7Con este comando cambias", "&7al modo de juego &eAventura."), "/gm 2"));
                Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageRunCommand("&c/gma", Arrays.asList("&7Con este comando cambias", "&7al modo de juego &eAventura."), "/gma"));
                
            }
            if (p.hasPermission("yandere.gamemode.all") || p.hasPermission("yandere.gamemode.spectator")){
                Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageRunCommand("&c/gm 3", Arrays.asList("&7Con este comando cambias", "&7al modo de juego &eEspectador."), "/gm 3"));
                Utils.sendMessage(p, Utils.formatTC("  &8&l▸ "), Utils.hoverOverMessageRunCommand("&c/gmsp", Arrays.asList("&7Con este comando cambias", "&7al modo de juego &eEspectador."), "/gmsp"));
                
            }
        }
    }
    
}
