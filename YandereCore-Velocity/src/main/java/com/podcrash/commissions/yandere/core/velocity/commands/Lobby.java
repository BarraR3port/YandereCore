package com.podcrash.commissions.yandere.core.velocity.commands;

import com.podcrash.commissions.yandere.core.common.data.server.Server;
import com.podcrash.commissions.yandere.core.common.data.server.ServerType;
import com.podcrash.commissions.yandere.core.velocity.VMain;
import com.podcrash.commissions.yandere.core.velocity.utils.Utils;
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
                final String serverName = player.getCurrentServer().get().getServerInfo().getName();
                Server server;
                if (ServerType.matchServerType(serverName, ServerType.BED_WARS)){
                    server = VMain.getInstance().getServerManager().getRandomServerByType(ServerType.LOBBY_BED_WARS);
                } else {
                    server = VMain.getInstance().getServerManager().getRandomServerByType(ServerType.LOBBY);
                }
                if (VMain.getInstance().getProxy().getServer(server.getProxyName()).isPresent()){
                    RegisteredServer bedWarsLobby = VMain.getInstance().getProxy().getServer(server.getProxyName()).get();
                    try {
                        if (bedWarsLobby.ping().get() != null){
                            player.createConnectionRequest(bedWarsLobby).fireAndForget();
                        }
                    } catch (InterruptedException | ExecutionException ex) {
                        player.sendMessage(Utils.format(" &8&l» &cNo hay Lobbies disponibles!"));
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
