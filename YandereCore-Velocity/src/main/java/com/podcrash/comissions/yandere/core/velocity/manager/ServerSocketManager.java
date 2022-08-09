package com.podcrash.comissions.yandere.core.velocity.manager;

import com.podcrash.comissions.yandere.core.velocity.socketmanager.ProxySocketServer;

import java.util.HashMap;
import java.util.Optional;

public class ServerSocketManager {
    
    private static ServerSocketManager instance;
    
    private final HashMap<String, ProxySocketServer> socketByServer = new HashMap<>();
    
    
    public ServerSocketManager(){
        instance = this;
        
    }
    
    public static ServerSocketManager getInstance(){
        return instance;
    }
    
    public static Optional<ProxySocketServer> getSocketByServer(String server){
        return Optional.ofNullable(getInstance().socketByServer.get(server));
    }
    
    public void registerServerSocket(String server, ProxySocketServer task){
        if (socketByServer.containsKey(server)){
            socketByServer.replace(server, task);
            return;
        }
        socketByServer.put(server, task);
    }
    
    public HashMap<String, ProxySocketServer> getSocketByServer(){
        return socketByServer;
    }
    
}
