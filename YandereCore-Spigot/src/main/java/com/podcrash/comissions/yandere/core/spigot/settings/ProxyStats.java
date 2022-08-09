package com.podcrash.comissions.yandere.core.spigot.settings;

import com.podcrash.comissions.yandere.core.common.data.server.IServerStats;
import com.podcrash.comissions.yandere.core.common.data.server.Server;
import com.podcrash.comissions.yandere.core.common.data.server.ServerType;

public class ProxyStats extends IServerStats {
    
    
    public int getSkyWarsPlayerSize(){
        return servers.stream().filter(server -> server.getServerType() == ServerType.SKY_WARS).mapToInt(Server::getOnlinePlayers).sum();
    }
    
    public int getBedWarsPlayerSize(){
        return servers.stream().filter(server -> server.getServerType() == ServerType.BED_WARS || server.getServerType() == ServerType.LOBBY_BED_WARS).mapToInt(Server::getOnlinePlayers).sum();
    }
    
    public int getPracticePlayerSize(){
        return servers.stream().filter(server -> server.getServerType() == ServerType.PRACTICE).mapToInt(Server::getOnlinePlayers).sum();
    }
    
    public int getTNTTagPlayerSize(){
        return servers.stream().filter(server -> server.getServerType() == ServerType.TNT_TAG).mapToInt(Server::getOnlinePlayers).sum();
    }
    
    public int getSurvivalPlayerSize(){
        return servers.stream().filter(server -> server.getServerType() == ServerType.SURVIVAL).mapToInt(Server::getOnlinePlayers).sum();
    }
    
    public int getAllPlayerSize(){
        return servers.stream().mapToInt(Server::getOnlinePlayers).sum();
    }
    
    public boolean isSkyWarsOnline(){
        return servers.stream().anyMatch(server -> server.getServerType() == ServerType.SKY_WARS);
    }
    
    public boolean isBedWarsOnline(){
        return servers.stream().anyMatch(server -> server.getServerType() == ServerType.BED_WARS || server.getServerType() == ServerType.LOBBY_BED_WARS);
    }
    
    public boolean isPracticeOnline(){
        return servers.stream().anyMatch(server -> server.getServerType() == ServerType.PRACTICE);
    }
    
    public boolean isTNTTagOnline(){
        return servers.stream().anyMatch(server -> server.getServerType() == ServerType.TNT_TAG);
    }
    
    public boolean isLobbyOnline(){
        return servers.stream().anyMatch(server -> server.getServerType() == ServerType.LOBBY);
    }
    
    public boolean isRegistroOnline(){
        return servers.stream().anyMatch(server -> server.getServerType() == ServerType.REGISTRO);
    }
    
    public boolean isSurvivalOnline(){
        return servers.stream().anyMatch(server -> server.getServerType() == ServerType.SURVIVAL);
    }
    
    
}
