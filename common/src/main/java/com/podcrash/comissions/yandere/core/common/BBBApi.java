package com.podcrash.comissions.yandere.core.common;

import com.podcrash.comissions.yandere.core.common.data.logs.ILogRepository;
import com.podcrash.comissions.yandere.core.common.data.server.ServerType;
import com.podcrash.comissions.yandere.core.common.db.IPlayerRepository;
import com.podcrash.comissions.yandere.core.common.socket.ISocket;
import net.lymarket.lyapi.spigot.config.Config;

public interface BBBApi<U> {
    
    void error(String message);
    
    Config getConfig();
    
    Config getItems();
    
    String getVersion();
    
    IPlayerRepository<U> getPlayers();
    
    ISocket<U> getSocket();
    
    String getNMSVersion();
    
    String getProxyServerName();
    
    void debug(String message);
    
    ServerType getServerType();
    
    ILogRepository<U> getLogs();
    
}
