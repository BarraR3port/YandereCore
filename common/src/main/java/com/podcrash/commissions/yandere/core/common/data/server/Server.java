package com.podcrash.commissions.yandere.core.common.data.server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;
import java.util.stream.Collectors;

public class Server {
    private final String proxyName;
    private final int onlinePlayers;
    private final ServerType serverType;
    private final UUID uuid;
    private final ArrayList<UUID> plugins;
    
    public Server(String proxyName, int onlinePlayers, ServerType serverType){
        this.uuid = UUID.randomUUID();
        this.proxyName = proxyName;
        this.onlinePlayers = onlinePlayers;
        this.serverType = serverType;
        this.plugins = new ArrayList<>();
    }
    
    public Server(UUID uuid, String proxyName, int onlinePlayers, ServerType serverType){
        this.uuid = uuid;
        this.proxyName = proxyName;
        this.onlinePlayers = onlinePlayers;
        this.serverType = serverType;
        this.plugins = new ArrayList<>();
    }
    
    public String getProxyName(){
        return proxyName;
    }
    
    public int getOnlinePlayers(){
        return onlinePlayers;
    }
    
    public ServerType getServerType(){
        return serverType;
    }
    
    public ArrayList<UUID> getPlugins(){
        return plugins;
    }
    
    public LinkedList<String> getPluginsFormatted(){
        return plugins.stream().map(UUID::toString).collect(Collectors.toCollection(LinkedList::new));
    }
    
    public void addPlugin(UUID plugin){
        if (!plugins.contains(plugin)){
            plugins.add(plugin);
        }
    }
    
    public void removePlugin(UUID plugin){
        plugins.remove(plugin);
    }
    
    public UUID getUuid(){
        return uuid;
    }
    
    public void clearPlugins(){
        plugins.clear();
    }
}
