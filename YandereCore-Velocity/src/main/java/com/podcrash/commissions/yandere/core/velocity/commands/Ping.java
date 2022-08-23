package com.podcrash.commissions.yandere.core.velocity.commands;


import com.podcrash.commissions.yandere.core.velocity.utils.Utils;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;

public class Ping implements SimpleCommand {
    
    public Ping(CommandManager commandManager){
        final CommandMeta meta = commandManager.metaBuilder("ping").aliases("ms").build();
        commandManager.register(meta, this);
    }
    
    @Override
    public void execute(final Invocation invocation){
        if (invocation.source() instanceof final Player player){
            long ping = player.getPing();
            String color;
            
            if (ping <= 50){
                color = "&a";
            } else if (ping <= 80){
                color = "&e";
            } else if (ping <= 100){
                color = "&6";
            } else if (ping <= 200){
                color = "&c";
            } else {
                color = "&4";
            }
            player.sendMessage(Utils.format("&aTu ping es " + color + ping));
        }
    }
    
    @Override
    public boolean hasPermission(final Invocation invocation){
        return true; /*invocation.source().hasPermission("command.test")*/
    }
}
