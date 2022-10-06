package com.podcrash.commissions.yandere.core.velocity.listener;


import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.podcrash.commissions.yandere.core.common.data.logs.LogType;
import com.podcrash.commissions.yandere.core.common.data.punish.PunishState;
import com.podcrash.commissions.yandere.core.common.data.punish.PunishType;
import com.podcrash.commissions.yandere.core.common.data.punish.ban.Ban;
import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.velocity.VMain;
import com.podcrash.commissions.yandere.core.velocity.announcements.VAnnouncer;
import com.podcrash.commissions.yandere.core.velocity.manager.ServerSocketManager;
import com.podcrash.commissions.yandere.core.velocity.punish.PunishManager;
import com.podcrash.commissions.yandere.core.velocity.utils.Utils;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.*;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerPing;

import java.util.Date;
import java.util.concurrent.ExecutionException;

public class PlayerEvents {
    
    private final PunishManager pm = VMain.getInstance().getPunishManager();
    private final VAnnouncer announcements = new VAnnouncer();
    
    public PlayerEvents(){
    }
    
    @Subscribe
    public void onPlayerPreLogin(PreLoginEvent e){
        String name = e.getUsername();
        if (name.contains("McDown_pw_") || name.contains("McDown")){
            e.setResult(PreLoginEvent.PreLoginComponentResult.denied(Utils.format("PENDEJO")));
        }
    }
    
    @Subscribe
    public void onPostLoginEvent(PostLoginEvent e){
        VMain.getInstance().getPlayers().getOrCreatePlayer(e.getPlayer().getUsername(), e.getPlayer().getUniqueId(), String.valueOf(e.getPlayer().getRemoteAddress().getAddress()).replace("/", ""));
    }
    
    @Subscribe
    public void onServerConnectedEvent(PlayerChooseInitialServerEvent e){
        if (e.getInitialServer().isPresent()){
            RegisteredServer server = e.getInitialServer().get();
            VMain.getInstance().getLogs().createLog(LogType.CONNECT, server.getServerInfo().getName(), "&aConnected to &eYandere&cCraft", e.getPlayer().getUsername());
        } else {
            VMain.getInstance().getLogs().createLog(LogType.CONNECT, "UNKNOWN", "&aConnected to &eYandere&cCraft", e.getPlayer().getUsername());
        }
    }
    
    @Subscribe
    public void onDisconnectEvent(DisconnectEvent e){
        VMain.getInstance().getPlayers().unCachePlayer(e.getPlayer().getUniqueId());
        if (e.getPlayer().getCurrentServer().isPresent()){
            VMain.getInstance().getLogs().createLog(LogType.DISCONNECT, e.getPlayer().getCurrentServer().get().getServerInfo().getName(), "&cDisconnected from &eYandere&cCraft", e.getPlayer().getUsername());
        } else {
            VMain.getInstance().getLogs().createLog(LogType.DISCONNECT, "UNKNOWN", "&cDisconnected from &eYandere&cCraft", e.getPlayer().getUsername());
        }
        
    }
    
    @Subscribe
    public void onDisconnectEvent(ConnectionHandshakeEvent e){
        VMain.debug("Connection Handshake to " + e.getConnection().getRemoteAddress().getAddress());
    }
    
    /*@Subscribe(order = PostOrder.FIRST)
    public void onKickedFromServerEvent(KickedFromServerEvent e){
        
        int attempts = 5;
        
        for ( int i = 0; i < attempts; i++ ){
            Server server = VMain.getInstance().getServerManager().getRandomLobbyServer();
            if (VMain.getInstance().getProxy().getServer(server.getProxyName()).isPresent()){
                RegisteredServer lobby = VMain.getInstance().getProxy().getServer(server.getProxyName()).get();
                try {
                    if (lobby.ping().get() != null){
                        e.setResult(KickedFromServerEvent.RedirectPlayer.create(lobby));
                        return;
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    e.setResult(KickedFromServerEvent.DisconnectPlayer.create(Utils.format("&cNo hay lobbys disponibles!")));
                }
                return;
            }
        }
        e.setResult(KickedFromServerEvent.DisconnectPlayer.create(Utils.format("&cNo hay lobbys disponibles!")));
        
    }*/
    
    @Subscribe(order = PostOrder.LAST)
    public void onProxyPing(ProxyPingEvent event){
        ServerPing prev = event.getPing();
        if (prev.getPlayers().isPresent() && prev.getFavicon().isPresent()){
            if (prev.getModinfo().isPresent()){
                if (prev.getVersion().getProtocol() > 757 || prev.getVersion().getProtocol() < 46){
                    prev.asBuilder().version(new ServerPing.Version(ProtocolVersion.MINECRAFT_1_18.getProtocol(), "YandereCraft 1.8.x - 1.19.x "));
                } else {
                    prev.asBuilder().version(new ServerPing.Version(prev.getVersion().getProtocol(), "YandereCraft 1.8.x - 1.19.x "));
                }
            }
            event.setPing(prev.asBuilder().build());
        }
        
    }
    
    @Subscribe(order = PostOrder.FIRST)
    public void onPluginMessage(PluginMessageEvent e){
        e.setResult(PluginMessageEvent.ForwardResult.forward());
        if (e.getSource() instanceof ServerConnection s){
            if (e.getIdentifier().getId().equals("podcrash:yandere") || e.getIdentifier().getId().equals("podcrash") || e.getIdentifier().getId().equals("yandere")){
                RegisteredServer server = s.getServer();
                ByteArrayDataInput in = ByteStreams.newDataInput(e.getData());
                String subChannel = in.readUTF();
                if ("GetServer".equals(subChannel)){
                    try {
                        if (server.ping().get() != null){
                            ByteArrayDataOutput out = ByteStreams.newDataOutput();
                            out.writeUTF("YourServerName");
                            out.writeUTF(server.getServerInfo().getName());
                            server.sendPluginMessage(MinecraftChannelIdentifier.create("podcrash", "yandere"), out.toByteArray());
                        }
                    } catch (ExecutionException | InterruptedException ignored) {
                    }
                    
                }
            }
        }
        
    }
    
    @Subscribe
    public void onPlayerLogin(LoginEvent e){
        User player = VMain.getInstance().getPlayers().getCachedPlayer(e.getPlayer().getUsername());
        if (player == null) return;
        String address = e.getPlayer().getRemoteAddress().getAddress().toString().split(":")[0].replace("/", "");
        Date currentDate = new Date();
        for ( Ban ban : pm.getBans().getBansByIp(address) ){
            System.out.println("Current Date: " + currentDate);
            System.out.println("Ban Date: " + ban.getCreateDate());
            System.out.println("Ban Expiration: " + ban.getExpDate());
            if (currentDate.before(ban.getExpDate())){
                e.setResult(ResultedEvent.ComponentResult.denied(announcements.banPlayerKickMessage(ban)));
            } else {
                pm.getBans().updateBanState(ban.getUUID(), PunishState.EXPIRED);
                player.addPunish(ban.getUUID(), PunishType.BAN);
                VMain.getInstance().getPlayers().savePlayer(player);
                e.getPlayer().getCurrentServer().flatMap(server -> ServerSocketManager.getSocketByServer(server.getServerInfo().getName())).ifPresent(socket -> socket.sendServerUpdatePlayerFromDb(player.getName()));
            }
        }
        
        for ( Ban ban : pm.getBans().getBansByName(player.getName()) ){
            System.out.println("Current Date: " + currentDate);
            System.out.println("Ban Date: " + ban.getCreateDate());
            System.out.println("Ban Expiration: " + ban.getExpDate());
            if (currentDate.before(ban.getExpDate())){
                e.setResult(ResultedEvent.ComponentResult.denied(announcements.banPlayerKickMessage(ban)));
                //e.getPlayer().disconnect(announcements.banPlayerKickMessage(ban));
            } else {
                pm.getBans().updateBanState(ban.getUUID(), PunishState.EXPIRED);
                player.addPunish(ban.getUUID(), PunishType.BAN);
                VMain.getInstance().getPlayers().savePlayer(player);
                e.getPlayer().getCurrentServer().flatMap(server -> ServerSocketManager.getSocketByServer(server.getServerInfo().getName())).ifPresent(socket -> socket.sendServerUpdatePlayerFromDb(player.getName()));
            }
        }
    }
}
