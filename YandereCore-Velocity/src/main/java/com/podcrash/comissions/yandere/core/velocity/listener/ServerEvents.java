package com.podcrash.comissions.yandere.core.velocity.listener;

import com.podcrash.comissions.yandere.core.common.data.server.ServerType;
import com.podcrash.comissions.yandere.core.velocity.VMain;
import com.podcrash.comissions.yandere.core.velocity.utils.Utils;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.proxy.server.RegisteredServer;

public class ServerEvents {
    
    @Subscribe
    public void onPlayerJoin(KickedFromServerEvent e){
        final String serverName = e.getServer().getServerInfo().getName();
        final ServerType serverType = ServerType.match(serverName);
        if (e.getServerKickReason().isEmpty()) return;
        final String randomLobbyServer = VMain.getInstance().getServerManager().getRandomLobbyServer().getProxyName();
        if (serverType.equals(ServerType.BED_WARS)){
            final String bwLobby = VMain.getInstance().getServerManager().getRandomBedWarsLobbyServer().getProxyName();
            if (bwLobby.equals("EMPTY")){
                e.getPlayer().sendMessage(Utils.format("&cNo hay Lobbys de BedWars disponibles!"));
                connectToLobby(randomLobbyServer, e);
                return;
            }
            e.setResult(KickedFromServerEvent.RedirectPlayer.create(VMain.getInstance().getProxy().getServer(bwLobby).get()));
            return;
        }
        if (serverType.equals(ServerType.SKY_WARS)){
            final String swLobby = VMain.getInstance().getServerManager().getRandomSkyWarsServer().getProxyName();
            if (swLobby.equals("EMPTY")){
                e.getPlayer().sendMessage(Utils.format("&cNo hay Lobbys de SkyWars disponibles!"));
                connectToLobby(randomLobbyServer, e);
                return;
            }
            e.setResult(KickedFromServerEvent.RedirectPlayer.create(VMain.getInstance().getProxy().getServer(swLobby).get()));
            return;
        }
        connectToLobby(randomLobbyServer, e);
        
        
    }
    
    private void connectToLobby(String lobby, KickedFromServerEvent e){
        if (VMain.getInstance().getProxy().getServer(lobby).isPresent()){
            RegisteredServer server = VMain.getInstance().getProxy().getServer(lobby).get();
            if (server.ping().isDone()){
                e.setResult(KickedFromServerEvent.RedirectPlayer.create(server));
                return;
            }
        }
        e.setResult(KickedFromServerEvent.DisconnectPlayer.create(Utils.format("&cNo hay lobbys disponibles!")));
        
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