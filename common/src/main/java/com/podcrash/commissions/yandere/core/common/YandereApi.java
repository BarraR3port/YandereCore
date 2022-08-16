package com.podcrash.commissions.yandere.core.common;

import com.podcrash.commissions.yandere.core.common.data.logs.ILogRepository;
import com.podcrash.commissions.yandere.core.common.data.server.ServerType;
import com.podcrash.commissions.yandere.core.common.db.IPlayerRepository;
import com.podcrash.commissions.yandere.core.common.socket.ISocket;
import net.lymarket.lyapi.spigot.config.Config;

public interface YandereApi<U> {
    
    void error(String message);
    
    Config getConfig();
    
    Config getItems();
    
    String getVersion();
    
    IPlayerRepository<U> getPlayers();
    
    ISocket<U> getSocket();
    
    String getNMSVersion();
    
    String getProxyServerName();
    
    ServerType getServerType();
    
    ILogRepository<U> getLogs();
    
}
