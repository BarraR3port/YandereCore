package com.podcrash.commissions.yandere.core.common.data.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public final class ProxyStats {
    private final HashMap<String, Server> servers = new HashMap<>();
    
    private final EmptyServer emptyServer = new EmptyServer();
    
    public HashMap<String, Server> getServers(){
        return servers;
    }
    
    public void addServer(Server server){
        servers.put(server.getProxyName(), server);
    }
    
    public void removeServer(String serverName){
        servers.remove(serverName);
    }
    
    public Server getRandomServerByType(ServerType type){
        List<Server> list = servers.values().stream().filter(server -> server.getServerType() == type).collect(Collectors.toList());
        Collections.shuffle(list);
        return list.stream().findFirst().orElse(emptyServer);
    }
    
    public Server getRandomServer(){
        return servers.values().stream().findAny().orElse(emptyServer);
    }
    
    public int getServerSizeByType(ServerType type){
        return servers.values().stream().filter(server -> server.getServerType() == type).mapToInt(Server::getOnlinePlayers).sum();
    }
    
    public int getAllPlayerSize(){
        return servers.values().stream().mapToInt(Server::getOnlinePlayers).sum();
    }
    
    public int getTargetServerSize(String serverProxyName){
        return servers.values().stream().filter(server -> server.getProxyName().equals(serverProxyName)).mapToInt(Server::getOnlinePlayers).sum();
    }
    
    public boolean isServerByTypeOnline(ServerType type){
        return servers.values().stream().anyMatch(server -> server.getServerType() == type);
    }
    
    public boolean isTargetServerOnline(String serverProxyName){
        return servers.values().stream().anyMatch(server -> server.getProxyName().equals(serverProxyName));
    }
    
    public int getTargetServerPlayers(String serverProxyName){
        return servers.values().stream().filter(server -> server.getProxyName().equals(serverProxyName)).mapToInt(Server::getOnlinePlayers).sum();
    }
    
    public EmptyServer getEmptyServer(){
        return emptyServer;
    }
    
}
