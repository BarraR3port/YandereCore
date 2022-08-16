/*
package com.podcrash.commissions.yandere.core.spigot.commands;

import net.lymarket.common.commands.*;
import net.lymarket.common.commands.response.CommandResponse;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.lydark.core.spigot.Core;

import java.util.ArrayList;
import java.util.List;

public class Gamemode implements ILyCommand {
    
    @Command(name = "gamemode", aliases = "gm", permission = "yandere.gamemode")
    public CommandResponse command(CommandContext context){
        if (context.getArgs().length == 0){
            if (!(context.getSender() instanceof Player)){
                Bukkit.getConsoleSender().sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.GamemodeCommand.WrongSyntax[/lang]");
                return new CommandResponse();
            }
            
            Player p = (Player) context.getSender();
            
            if (p.hasPermission("yandere.gamemode.all")){
                
                p.sendMessage("§b§m----------------------------------------------------");
                p.spigot().sendMessage(text("[lang]Lydark_Core.GamemodeCommand.GamemodeCommand.1[/lang]", "/gm 0"));
                p.spigot().sendMessage(text("[lang]Lydark_Core.GamemodeCommand.GamemodeCommand.2[/lang]", "/gm 1"));
                p.spigot().sendMessage(text("[lang]Lydark_Core.GamemodeCommand.GamemodeCommand.3[/lang]", "/gm 2"));
                p.spigot().sendMessage(text("[lang]Lydark_Core.GamemodeCommand.GamemodeCommand.4[/lang]", "/gm 3"));
                p.sendMessage("");
                p.sendMessage("    [lang]Lydark_Core.GamemodeCommand.GamemodeCommand.5[/lang]");
                p.sendMessage("§b§m----------------------------------------------------");
                return new CommandResponse();
            } else if (p.hasPermission("yandere.gamemode")){
                p.sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.GamemodeCommand.WrongSyntax[/lang]");
                return new CommandResponse();
            }
            return false;
        }
        
        
        List<String> creativo = new ArrayList<>();
        creativo.add("1");
        creativo.add("c");
        creativo.add("creative");
        
        //CREATIVO
        if (creativo.contains(context.getArg(0).toLowerCase())){
            
            if (!(context.getSender() instanceof Player)){
                GameMode gamemode = GameMode.CREATIVE;
                String textgamemode = "[lang]Lydark_Core.GamemodeCommand.Creative[/lang]"; //Creativo
                
                if (context.getArgs().length == 1){
                    context.getSender().sendMessage("[lang]Lydark_Core.GamemodeCommand.WrongSyntax[/lang]");
                    return new CommandResponse();
                }
                
                Player target = Bukkit.getPlayer(context.getArg(1));
                if (target != null && target.getType().equals(EntityType.PLAYER)){
                    if (setGamemode(target, gamemode, textgamemode)){
                        context.getSender().sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.GamemodeCommand.GamemodeOfThePlayer[args][arg]%target[/arg][arg]%gamemode[/arg][/args][/lang]".replace("%target", target.getName()).replace("%gamemode", textgamemode));
                        setGamemode(target, gamemode, textgamemode);
                        return new CommandResponse();
                    }
                    context.getSender().sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.GamemodeCommand.ChangeGamemode.3[args][arg]%gamemode[/arg][/args][/lang]".replace("%gamemode", textgamemode));
                    return new CommandResponse();
                }
                context.getSender().sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.PlayerHasNotBeenFound[/lang]");
                return new CommandResponse();
            }
            
            Player p = (Player) context.getSender();
            
            if (p.hasPermission("yandere.gamemode.creative") || p.hasPermission("yandere.gamemode.all")){
                
                GameMode gamemode = GameMode.CREATIVE;
                String textgamemode = "[lang]Lydark_Core.GamemodeCommand.Creative[/lang]"; //Creativo
                
                if (context.getArgs().length == 1){
                    return setGamemode(p, gamemode, textgamemode);
                }
                
                Player target = Bukkit.getPlayer(context.getArg(1));
                if (target != null && target.getType().equals(EntityType.PLAYER)){
                    if (setGamemode(target, gamemode, textgamemode)){
                        p.sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.GamemodeCommand.GamemodeOfThePlayer[args][arg]%target[/arg][arg]%gamemode[/arg][/args][/lang]".replace("%target", target.getName()).replace("%gamemode", textgamemode));
                        setGamemode(target, gamemode, textgamemode);
                        return new CommandResponse();
                    }
                    p.sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.GamemodeCommand.ChangeGamemode.3[args][arg]%gamemode[/arg][/args][/lang]".replace("%gamemode", textgamemode));
                    return new CommandResponse();
                }
                p.sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.PlayerHasNotBeenFound[/lang]");
            }
            return false;
        }
        
        List<String> aventura = new ArrayList<>();
        aventura.add("2");
        aventura.add("a");
        aventura.add("adventure");
        
        //AVENTURA
        if (aventura.contains(context.getArg(0).toLowerCase())){
            
            if (!(context.getSender() instanceof Player)){
                GameMode gamemode = GameMode.ADVENTURE;
                String textgamemode = "[lang]Lydark_Core.GamemodeCommand.Adventure[/lang]"; //AVENTURA
                
                if (context.getArgs().length == 1){
                    context.getSender().sendMessage("[lang]Lydark_Core.GamemodeCommand.WrongSyntax[/lang]");
                    return new CommandResponse();
                }
                
                Player target = Bukkit.getPlayer(context.getArg(1));
                if (target != null && target.getType().equals(EntityType.PLAYER)){
                    if (setGamemode(target, gamemode, textgamemode)){
                        context.getSender().sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.GamemodeCommand.GamemodeOfThePlayer[args][arg]%target[/arg][arg]%gamemode[/arg][/args][/lang]".replace("%target", target.getName()).replace("%gamemode", textgamemode));
                        setGamemode(target, gamemode, textgamemode);
                        return new CommandResponse();
                    }
                    context.getSender().sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.GamemodeCommand.ChangeGamemode.3[args][arg]%gamemode[/arg][/args][/lang]".replace("%gamemode", textgamemode));
                    return new CommandResponse();
                }
                context.getSender().sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.PlayerHasNotBeenFound[/lang]");
                return new CommandResponse();
            }
            
            Player p = (Player) context.getSender();
            
            if (p.hasPermission("yandere.gamemode.adventure") || p.hasPermission("yandere.gamemode.all")){
                
                GameMode gamemode = GameMode.ADVENTURE;
                String textgamemode = "[lang]Lydark_Core.GamemodeCommand.Adventure[/lang]"; //AVENTURA
                
                if (context.getArgs().length == 1){
                    return setGamemode(p, gamemode, textgamemode);
                }
                
                Player target = Bukkit.getPlayer(context.getArg(1));
                if (target != null && target.getType().equals(EntityType.PLAYER)){
                    if (setGamemode(target, gamemode, textgamemode)){
                        p.sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.GamemodeCommand.GamemodeOfThePlayer[args][arg]%target[/arg][arg]%gamemode[/arg][/args][/lang]".replace("%target", target.getName()).replace("%gamemode", textgamemode));
                        setGamemode(target, gamemode, textgamemode);
                        return new CommandResponse();
                    }
                    p.sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.GamemodeCommand.ChangeGamemode.3[args][arg]%gamemode[/arg][/args][/lang]".replace("%gamemode", textgamemode));
                    return new CommandResponse();
                }
                p.sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.PlayerHasNotBeenFound[/lang]");
                
            }
            return false;
        }
        
        
        List<String> espectador = new ArrayList<>();
        espectador.add("3");
        espectador.add("sp");
        espectador.add("spectator");
        
        //ESPECTADOR
        if (espectador.contains(context.getArg(0).toLowerCase())){
            
            if (!(context.getSender() instanceof Player)){
                GameMode gamemode = GameMode.SPECTATOR;
                String textgamemode = "[lang]Lydark_Core.GamemodeCommand.Spectator[/lang]"; //ESPECTADOR
                
                if (context.getArgs().length == 1){
                    context.getSender().sendMessage("[lang]Lydark_Core.GamemodeCommand.WrongSyntax[/lang]");
                    return new CommandResponse();
                }
                
                Player target = Bukkit.getPlayer(context.getArg(1));
                if (target != null && target.getType().equals(EntityType.PLAYER)){
                    if (setGamemode(target, gamemode, textgamemode)){
                        context.getSender().sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.GamemodeCommand.GamemodeOfThePlayer[args][arg]%target[/arg][arg]%gamemode[/arg][/args][/lang]".replace("%target", target.getName()).replace("%gamemode", textgamemode));
                        setGamemode(target, gamemode, textgamemode);
                        return new CommandResponse();
                    }
                    context.getSender().sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.GamemodeCommand.ChangeGamemode.3[args][arg]%gamemode[/arg][/args][/lang]".replace("%gamemode", textgamemode));
                    return new CommandResponse();
                }
                context.getSender().sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.PlayerHasNotBeenFound[/lang]");
                return new CommandResponse();
            }
            
            Player p = (Player) context.getSender();
            
            if (p.hasPermission("yandere.gamemode.spectator") || p.hasPermission("yandere.gamemode.all")){
                
                GameMode gamemode = GameMode.SPECTATOR;
                String textgamemode = "[lang]Lydark_Core.GamemodeCommand.Spectator[/lang]"; //ESPECTADOR
                
                if (context.getArgs().length == 1){
                    return setGamemode(p, gamemode, textgamemode);
                }
                
                Player target = Bukkit.getPlayer(context.getArg(1));
                if (target != null && target.getType().equals(EntityType.PLAYER)){
                    if (setGamemode(target, gamemode, textgamemode)){
                        p.sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.GamemodeCommand.GamemodeOfThePlayer[args][arg]%target[/arg][arg]%gamemode[/arg][/args][/lang]".replace("%target", target.getName()).replace("%gamemode", textgamemode));
                        setGamemode(target, gamemode, textgamemode);
                        return new CommandResponse();
                    }
                    p.sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.GamemodeCommand.ChangeGamemode.3[args][arg]%gamemode[/arg][/args][/lang]".replace("%gamemode", textgamemode));
                    return new CommandResponse();
                }
                p.sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.PlayerHasNotBeenFound[/lang]");
                
            }
            return false;
        }
        
        
        List<String> supervivencia = new ArrayList<>();
        supervivencia.add("0");
        supervivencia.add("s");
        supervivencia.add("survival");
        
        //SUPERVIVENCIA
        if (supervivencia.contains(context.getArg(0).toLowerCase())){
            
            if (!(context.getSender() instanceof Player)){
                GameMode gamemode = GameMode.SURVIVAL;
                String textgamemode = "[lang]Lydark_Core.GamemodeCommand.Survival[/lang]"; //SUPERVIVENCIA
                
                if (context.getArgs().length == 1){
                    context.getSender().sendMessage("[lang]Lydark_Core.GamemodeCommand.WrongSyntax[/lang]");
                    return new CommandResponse();
                }
                
                Player target = Bukkit.getPlayer(context.getArg(1));
                if (target != null && target.getType().equals(EntityType.PLAYER)){
                    if (setGamemode(target, gamemode, textgamemode)){
                        context.getSender().sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.GamemodeCommand.GamemodeOfThePlayer[args][arg]%target[/arg][arg]%gamemode[/arg][/args][/lang]".replace("%target", target.getName()).replace("%gamemode", textgamemode));
                        setGamemode(target, gamemode, textgamemode);
                        return new CommandResponse();
                    }
                    context.getSender().sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.GamemodeCommand.ChangeGamemode.3[args][arg]%gamemode[/arg][/args][/lang]".replace("%gamemode", textgamemode));
                    return new CommandResponse();
                }
                context.getSender().sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.PlayerHasNotBeenFound[/lang]");
                return new CommandResponse();
            }
            
            Player p = (Player) context.getSender();
            
            if (p.hasPermission("yandere.gamemode.survival") || p.hasPermission("yandere.gamemode.all")){
                
                GameMode gamemode = GameMode.SURVIVAL;
                String textgamemode = "[lang]Lydark_Core.GamemodeCommand.Survival[/lang]"; //SUPERVIVENCIA
                
                if (context.getArgs().length == 1){
                    return setGamemode(p, gamemode, textgamemode);
                }
                
                Player target = Bukkit.getPlayer(context.getArg(1));
                if (target != null && target.getType().equals(EntityType.PLAYER)){
                    if (setGamemode(target, gamemode, textgamemode)){
                        p.sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.GamemodeCommand.GamemodeOfThePlayer[args][arg]%target[/arg][arg]%gamemode[/arg][/args][/lang]".replace("%target", target.getName()).replace("%gamemode", textgamemode));
                        setGamemode(target, gamemode, textgamemode);
                        return new CommandResponse();
                    }
                    p.sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.GamemodeCommand.ChangeGamemode.3[args][arg]%gamemode[/arg][/args][/lang]".replace("%gamemode", textgamemode));
                    return false;
                }
                p.sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.PlayerHasNotBeenFound[/lang]");
                
            }
            return false;
        }
        
        return false;
    }
    
    
    private boolean setGamemode(Player jugador, GameMode gamemode, String textgamemode){
        if (!jugador.getGameMode().equals(gamemode)){
            jugador.setGameMode(gamemode);
            
            jugador.sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.GamemodeCommand.ChangeGamemode.1[args][arg]%gamemode[/arg][/args][/lang]".replace("%gamemode", textgamemode));
            
            return new CommandResponse();
        }
        jugador.sendMessage(Core.getApi().getUtils().prefix() + " [lang]Lydark_Core.GamemodeCommand.ChangeGamemode.2[args][arg]%gamemode[/arg][/args][/lang]".replace("%gamemode", textgamemode));
        return new CommandResponse();
    }
    
    private TextComponent text(String text, String command){
        TextComponent component = new TextComponent(text);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        
        return component;
    }
    
    @Tab
    public ArrayList<String> tabComplete(TabContext TabContext){
        return new ArrayList<>();
    }
}
*/
