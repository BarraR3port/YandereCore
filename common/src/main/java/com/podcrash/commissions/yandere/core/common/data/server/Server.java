package com.podcrash.commissions.yandere.core.common.data.server;

public class Server {
    
    private final String proxyName;
    private final int onlinePlayers;
    private final ServerType serverType;
    
    public Server(String proxyName, int onlinePlayers, ServerType serverType){
        this.proxyName = proxyName;
        this.onlinePlayers = onlinePlayers;
        this.serverType = serverType;
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
}
