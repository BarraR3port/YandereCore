package com.podcrash.comissions.yandere.core.common.data.server;

import java.util.HashMap;

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
    
    public Server getRandomLobbyServer(){
        return servers.values().stream().filter(server -> server.getServerType() == ServerType.LOBBY).findAny().orElse(emptyServer);
    }
    
    public Server getRandomSkyWarsServer(){
        return servers.values().stream().filter(server -> server.getServerType() == ServerType.SKY_WARS).findAny().orElse(emptyServer);
    }
    
    public Server getRandomBedWarsServer(){
        return servers.values().stream().filter(server -> server.getServerType() == ServerType.LOBBY_BED_WARS).findAny().orElse(emptyServer);
    }
    
    public Server getRandomBedWarsLobbyServer(){
        return servers.values().stream().filter(server -> server.getServerType() == ServerType.LOBBY_BED_WARS).findAny().orElse(emptyServer);
    }
    
    public Server getRandomPracticeServer(){
        return servers.values().stream().filter(server -> server.getServerType() == ServerType.PRACTICE).findAny().orElse(emptyServer);
    }
    
    public Server getRandomTNTTagServer(){
        return servers.values().stream().filter(server -> server.getServerType() == ServerType.TNT_TAG).findAny().orElse(emptyServer);
    }
    
    public Server getRandomSurvivalGamesServer(){
        return servers.values().stream().filter(server -> server.getServerType() == ServerType.SURVIVAL).findAny().orElse(emptyServer);
    }
    
    public Server getRandomServer(){
        return servers.values().stream().findAny().orElse(emptyServer);
    }
    
    public int getSkyWarsPlayerSize(){
        return servers.values().stream().filter(server -> server.getServerType() == ServerType.SKY_WARS).mapToInt(Server::getOnlinePlayers).sum();
    }
    
    public int getBedWarsPlayerSize(){
        return servers.values().stream().filter(server -> server.getServerType() == ServerType.BED_WARS || server.getServerType() == ServerType.LOBBY_BED_WARS).mapToInt(Server::getOnlinePlayers).sum();
    }
    
    public int getPracticePlayerSize(){
        return servers.values().stream().filter(server -> server.getServerType() == ServerType.PRACTICE).mapToInt(Server::getOnlinePlayers).sum();
    }
    
    public int getTNTTagPlayerSize(){
        return servers.values().stream().filter(server -> server.getServerType() == ServerType.TNT_TAG).mapToInt(Server::getOnlinePlayers).sum();
    }
    
    public int getSurvivalPlayerSize(){
        return servers.values().stream().filter(server -> server.getServerType() == ServerType.SURVIVAL).mapToInt(Server::getOnlinePlayers).sum();
    }
    
    public int getAllPlayerSize(){
        return servers.values().stream().mapToInt(Server::getOnlinePlayers).sum();
    }
    
    public int getTargetServerSize(String serverProxyName){
        return servers.values().stream().filter(server -> server.getProxyName().equals(serverProxyName)).mapToInt(Server::getOnlinePlayers).sum();
    }
    
    public boolean isSkyWarsOnline(){
        return servers.values().stream().anyMatch(server -> server.getServerType() == ServerType.SKY_WARS);
    }
    
    public boolean isBedWarsOnline(){
        return servers.values().stream().anyMatch(server -> server.getServerType() == ServerType.BED_WARS || server.getServerType() == ServerType.LOBBY_BED_WARS);
    }
    
    public boolean isPracticeOnline(){
        return servers.values().stream().anyMatch(server -> server.getServerType() == ServerType.PRACTICE);
    }
    
    public boolean isTNTTagOnline(){
        return servers.values().stream().anyMatch(server -> server.getServerType() == ServerType.TNT_TAG);
    }
    
    public boolean isLobbyOnline(){
        return servers.values().stream().anyMatch(server -> server.getServerType() == ServerType.LOBBY);
    }
    
    public boolean isRegistroOnline(){
        return servers.values().stream().anyMatch(server -> server.getServerType() == ServerType.REGISTRO);
    }
    
    public boolean isSurvivalOnline(){
        return servers.values().stream().anyMatch(server -> server.getServerType() == ServerType.SURVIVAL);
    }
    
    public boolean isTargetServerOnline(String serverProxyName){
        return servers.values().stream().anyMatch(server -> server.getProxyName().equals(serverProxyName));
    }
    
}
