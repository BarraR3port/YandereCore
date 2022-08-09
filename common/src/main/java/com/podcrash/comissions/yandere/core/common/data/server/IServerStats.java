package com.podcrash.comissions.yandere.core.common.data.server;

import java.util.LinkedList;

public abstract class IServerStats {
    
    protected final LinkedList<Server> servers = new LinkedList<>();
    private final EmptyServer emptyServer = new EmptyServer();
    
    public void addServer(Server server){
        servers.add(server);
    }
    
    public void removeServer(String serverName){
        servers.removeIf(server -> server.getProxyName().equalsIgnoreCase(serverName));
    }
    
    public Server getRandomLobbyServer(){
        return servers.stream().filter(server -> server.getServerType() == ServerType.LOBBY).findAny().orElse(emptyServer);
    }
    
    public Server getRandomSkyWarsServer(){
        return servers.stream().filter(server -> server.getServerType() == ServerType.SKY_WARS).findAny().orElse(emptyServer);
    }
    
    public Server getRandomBedWarsServer(){
        return servers.stream().filter(server -> server.getServerType() == ServerType.LOBBY_BED_WARS).findAny().orElse(emptyServer);
    }
    
    public Server getRandomPracticeServer(){
        return servers.stream().filter(server -> server.getServerType() == ServerType.PRACTICE).findAny().orElse(emptyServer);
    }
    
    public Server getRandomTNTTagServer(){
        return servers.stream().filter(server -> server.getServerType() == ServerType.TNT_TAG).findAny().orElse(emptyServer);
    }
    
    public Server getRandomSurvivalGamesServer(){
        return servers.stream().filter(server -> server.getServerType() == ServerType.SURVIVAL).findAny().orElse(emptyServer);
    }
    
    public Server getRandomServer(){
        return servers.stream().findAny().orElse(emptyServer);
    }
    
    
}
