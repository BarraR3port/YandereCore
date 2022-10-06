package com.podcrash.commissions.yandere.core.common;

import com.podcrash.commissions.yandere.core.common.data.server.ServerType;
import com.podcrash.commissions.yandere.core.common.data.user.IPlayerRepository;
import com.podcrash.commissions.yandere.core.common.log.ILogRepository;
import com.podcrash.commissions.yandere.core.common.socket.ISocket;

import java.text.SimpleDateFormat;

public interface YandereApi<Config> {
    
    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    
    void error(String message);
    
    Config getConfig();
    
    Config getItems();
    
    String getVersion();
    
    IPlayerRepository getPlayers();
    
    ISocket getSocket();
    
    String getNMSVersion();
    
    String getProxyServerName();
    
    ServerType getServerType();
    
    ILogRepository getLogs();
    
}
