package com.podcrash.commissions.yandere.core.velocity.listener;


import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.podcrash.commissions.yandere.core.common.data.server.Server;
import com.podcrash.commissions.yandere.core.velocity.VMain;
import com.podcrash.commissions.yandere.core.velocity.user.VelocityUser;
import com.podcrash.commissions.yandere.core.velocity.utils.Utils;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerPing;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class PlayerEvents {
    
    private final HashMap<UUID, Long> timeOnline = new HashMap<>();
    
    public PlayerEvents(){
        
        VMain.getInstance().getProxy().getScheduler().buildTask(VMain.getInstance(), () -> {
            final long currentTime = System.currentTimeMillis();
            VMain.getInstance().getProxy().getAllPlayers().forEach(player -> {
                VelocityUser user = VMain.getInstance().getPlayers().getLocalStoredPlayer(player.getUniqueId());
                if (user == null){
                    user = VMain.getInstance().getPlayers().getPlayer(player.getUsername());
                    if (user == null){
                        return;
                    }
                    user.setUUID(player.getUniqueId());
                }
                user.getStats().addTIME_PLAYED(currentTime - timeOnline.getOrDefault(player.getUniqueId(), 0L));
                VMain.getInstance().getPlayers().savePlayer(user);
                timeOnline.remove(player.getUniqueId());
                timeOnline.put(player.getUniqueId(), currentTime);
            });
            
        }).repeat(5, TimeUnit.SECONDS).schedule();
        
    }
    
    @Subscribe
    public void onPostLoginEvent(PostLoginEvent e){
        VMain.getInstance().getPlayers().getOrCreatePlayer(e.getPlayer().getUsername(), e.getPlayer().getUniqueId(), String.valueOf(e.getPlayer().getRemoteAddress().getAddress()).replace("/", ""));
        
        timeOnline.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
    }
    
    @Subscribe
    public void onPlayerPreLogin(PreLoginEvent e){
        if (e.getUsername().contains("McDown_pw_") || e.getUsername().contains("McDown")){
            e.setResult(PreLoginEvent.PreLoginComponentResult.denied(Utils.format("PENDEJO")));
        }
    }
    
    @Subscribe
    public void onDisconnectEvent(DisconnectEvent e){
        final long currentTime = System.currentTimeMillis();
        final UUID uuid = e.getPlayer().getUniqueId();
        final VelocityUser user = VMain.getInstance().getPlayers().getPlayer(uuid);
        user.getStats().addTIME_PLAYED(currentTime - timeOnline.get(uuid));
        VMain.getInstance().getPlayers().savePlayer(user);
        timeOnline.remove(uuid);
    }
    
    @Subscribe(order = PostOrder.FIRST)
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
        
    }
    
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
                if ("GetServer" .equals(subChannel)){
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
    
}
