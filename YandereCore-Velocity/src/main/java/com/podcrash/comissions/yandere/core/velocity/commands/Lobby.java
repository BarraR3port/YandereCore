package com.podcrash.comissions.yandere.core.velocity.commands;

import com.podcrash.comissions.yandere.core.common.data.server.Server;
import com.podcrash.comissions.yandere.core.velocity.VMain;
import com.podcrash.comissions.yandere.core.velocity.utils.Utils;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.util.concurrent.ExecutionException;

public class Lobby implements SimpleCommand {
    public Lobby(CommandManager commandManager){
        
        final CommandMeta meta = commandManager.metaBuilder("lobby").aliases("hub").build();
        commandManager.register(meta, this);
    }
    
    @Override
    public void execute(Invocation invocation){
        if (invocation.arguments().length == 0){
            if (invocation.source() instanceof final Player player){
                Server server = VMain.getInstance().getServerManager().getRandomLobbyServer();
                if (VMain.getInstance().getProxy().getServer(server.getProxyName()).isPresent()){
                    RegisteredServer lobby = VMain.getInstance().getProxy().getServer(server.getProxyName()).get();
                    try {
                        if (lobby.ping().get() != null){
                            player.createConnectionRequest(lobby).fireAndForget();
                        }
                    } catch (InterruptedException | ExecutionException ex) {
                        player.sendMessage(Utils.format("&cNo hay lobbys disponibles!"));
                    }
                }
            }
        }
    }
    
    @Override
    public boolean hasPermission(Invocation invocation){
        return true;
    }
}
