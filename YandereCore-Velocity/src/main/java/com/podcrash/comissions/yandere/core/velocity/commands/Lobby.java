package com.podcrash.comissions.yandere.core.velocity.commands;

import com.podcrash.comissions.yandere.core.velocity.VMain;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;

public class Lobby implements SimpleCommand {
    public Lobby(CommandManager commandManager){
        
        final CommandMeta meta = commandManager.metaBuilder("lobby").aliases("hub").build();
        commandManager.register(meta, this);
    }
    
    @Override
    public void execute(Invocation invocation){
        if (invocation.arguments().length == 0){
            if (invocation.source() instanceof Player){
                final Player player = (Player) invocation.source();
                VMain.getInstance().getProxy().getServer("lobby").ifPresent(server -> player.createConnectionRequest(server).fireAndForget());
            }
        }
    }
    
    @Override
    public boolean hasPermission(Invocation invocation){
        return true;
    }
}
