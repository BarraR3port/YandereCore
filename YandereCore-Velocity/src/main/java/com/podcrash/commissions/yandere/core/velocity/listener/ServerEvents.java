package com.podcrash.commissions.yandere.core.velocity.listener;

import com.podcrash.commissions.yandere.core.common.data.server.ServerType;
import com.podcrash.commissions.yandere.core.velocity.VMain;
import com.podcrash.commissions.yandere.core.velocity.utils.Utils;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.proxy.server.RegisteredServer;

public class ServerEvents {
    
    @Subscribe
    public void onKickedFromServerEvent(KickedFromServerEvent e){
        final String serverName = e.getServer().getServerInfo().getName();
        final ServerType serverType = ServerType.match(serverName);
        if (serverType.equals(ServerType.BED_WARS)){
            String lobby = VMain.getInstance().getServerManager().getRandomServerByType(ServerType.LOBBY_BED_WARS).getProxyName();
            if (lobby.equalsIgnoreCase("EMPTY")){
                lobby = VMain.getInstance().getServerManager().getRandomServerByType(ServerType.LOBBY).getProxyName();
                e.getPlayer().sendMessage(Utils.format("&cNo hay Lobbies de SkyWars disponibles!"));
                e.getPlayer().sendMessage(Utils.format("&cTe estamos redirigiendo a un Lobby Principal!"));
            }
            if (VMain.getInstance().getProxy().getServer(lobby).isPresent()){
                e.setResult(KickedFromServerEvent.RedirectPlayer.create(VMain.getInstance().getProxy().getServer(lobby).get()));
                return;
            }
        } else if (serverType.equals(ServerType.SKY_WARS)){
            String lobby = VMain.getInstance().getServerManager().getRandomServerByType(ServerType.SKY_WARS).getProxyName();
            if (lobby.equalsIgnoreCase("EMPTY")){
                lobby = VMain.getInstance().getServerManager().getRandomServerByType(ServerType.LOBBY).getProxyName();
                e.getPlayer().sendMessage(Utils.format("&cNo hay Lobbies de SkyWars disponibles!"));
                e.getPlayer().sendMessage(Utils.format("&cTe estamos redirigiendo a un Lobby Principal!"));
            }
            if (VMain.getInstance().getProxy().getServer(lobby).isPresent()){
                e.setResult(KickedFromServerEvent.RedirectPlayer.create(VMain.getInstance().getProxy().getServer(lobby).get()));
                return;
            }
        } else if (serverType.equals(ServerType.PRACTICE)){
            String lobby = VMain.getInstance().getServerManager().getRandomServerByType(ServerType.PRACTICE).getProxyName();
            if (lobby.equalsIgnoreCase("EMPTY")){
                lobby = VMain.getInstance().getServerManager().getRandomServerByType(ServerType.LOBBY).getProxyName();
                e.getPlayer().sendMessage(Utils.format("&cNo hay servers de Practice disponibles!"));
                e.getPlayer().sendMessage(Utils.format("&cTe estamos redirigiendo a un Lobby Principal!"));
            }
            if (VMain.getInstance().getProxy().getServer(lobby).isPresent()){
                e.setResult(KickedFromServerEvent.RedirectPlayer.create(VMain.getInstance().getProxy().getServer(lobby).get()));
                return;
            }
        }
        final String randomLobbyServer = VMain.getInstance().getServerManager().getRandomServerByType(ServerType.LOBBY).getProxyName();
        if (VMain.getInstance().getProxy().getServer(randomLobbyServer).isPresent()){
            RegisteredServer server = VMain.getInstance().getProxy().getServer(randomLobbyServer).get();
            e.setResult(KickedFromServerEvent.RedirectPlayer.create(server));
        } else {
            e.setResult(KickedFromServerEvent.DisconnectPlayer.create(Utils.format("&cNo hay Lobbies disponibles!")));
        }
    
    
    }
    
    
}