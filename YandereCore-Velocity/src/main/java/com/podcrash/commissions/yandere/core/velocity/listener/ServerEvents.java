package com.podcrash.commissions.yandere.core.velocity.listener;

import com.podcrash.commissions.yandere.core.common.data.server.ServerType;
import com.podcrash.commissions.yandere.core.velocity.VMain;
import com.podcrash.commissions.yandere.core.velocity.utils.Utils;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.lymarket.lyapi.common.Api;

public class ServerEvents {
    
    @Subscribe
    public void onPlayerJoin(KickedFromServerEvent e){
        final String serverName = e.getServer().getServerInfo().getName();
        final ServerType serverType = ServerType.match(serverName);
        if (serverType.equals(ServerType.BED_WARS)){
            final String bwLobby = VMain.getInstance().getServerManager().getRandomBedWarsLobbyServer().getProxyName();
            if (bwLobby.equalsIgnoreCase("EMPTY")){
                e.getPlayer().sendMessage(Utils.format("&cNo hay Lobbies de BedWars disponibles!"));
            } else {
                if (VMain.getInstance().getProxy().getServer(bwLobby).isPresent()){
                    e.setResult(KickedFromServerEvent.RedirectPlayer.create(VMain.getInstance().getProxy().getServer(bwLobby).get()));
                }
            }
        } else if (serverType.equals(ServerType.SKY_WARS)){
            final String swLobby = VMain.getInstance().getServerManager().getRandomSkyWarsServer().getProxyName();
            if (swLobby.equalsIgnoreCase("EMPTY")){
                e.getPlayer().sendMessage(Utils.format("&cNo hay Lobbies de SkyWars disponibles!"));
            } else {
                if (VMain.getInstance().getProxy().getServer(swLobby).isPresent()){
                    e.setResult(KickedFromServerEvent.RedirectPlayer.create(VMain.getInstance().getProxy().getServer(swLobby).get()));
                }
            }
        } else {
            final String randomLobbyServer = VMain.getInstance().getServerManager().getRandomLobbyServer().getProxyName();
            System.out.println("Server name: " + serverName);
            System.out.println("Server type: " + serverType);
            System.out.println("Random lobby: " + randomLobbyServer);
            System.out.println("Servers: " + Api.getGson().toJson(VMain.getInstance().getServerManager().getServers()));
            if (VMain.getInstance().getProxy().getServer(randomLobbyServer).isPresent()){
                System.out.println("Redirecting to: " + randomLobbyServer);
                RegisteredServer server = VMain.getInstance().getProxy().getServer(randomLobbyServer).get();
                e.setResult(KickedFromServerEvent.RedirectPlayer.create(server));
            } else {
                e.setResult(KickedFromServerEvent.DisconnectPlayer.create(Utils.format("&cNo hay Lobbies disponibles!")));
            }
        }
    
    }
    
    private void connectToLobby(String lobby, KickedFromServerEvent e){
    
        
    }
    
    /*@Subscribe
    public void onPlayerPing( ProxyPingEvent event ){
        ServerPing prev = event.getPing( );
        ServerPing copy = new LunarClientServerPing( prev.getVersion( ) , prev.getPlayers( ).isPresent( ) ? prev.getPlayers( ).get( ) : null , prev.getDescriptionComponent( ) , prev.getFavicon( ).isPresent( ) ? prev.getFavicon( ).get( ) : null , prev.getModinfo( ).isPresent( ) ? prev.getModinfo( ).get( ) : null );
        event.setPing( copy );
    }*/
    
    /*static class LunarClientServerPing extends ServerPing {
        private final String lcServer = "sanity";
        
        LunarClientServerPing( ServerPing.Version version , ServerPing.Players players , Component description , @Nullable Favicon favicon , ModInfo mods ){
            super( version , players , description , favicon , mods );
        }
    }*/
    
    
    /*@EventHandler
    public void onPlayerJoin( ServerDisconnectEvent e ){
        final String serverName = e.getTarget( ).getName( );
        final String serverType = serverName.substring( 0 , 2 );
        if ( !serverName.equalsIgnoreCase( "tblobby" ) ) return;
        if ( !serverType.equalsIgnoreCase( "tb" ) ) return;
        e.getPlayer( ).init( ProxyServer.getInstance( ).getServerInfo( "tblobby" ) , ServerConnectEvent.Reason.KICK_REDIRECT );
        
    }*/
    
    
}