package com.podcrash.commissions.yandere.core.common.socket;

import com.google.gson.JsonObject;
import com.podcrash.commissions.yandere.core.common.db.IPlayerRepository;

import java.io.IOException;

public abstract class ISocket implements SocketMSG {
    
    private final IPlayerRepository players;
    
    public ISocket(IPlayerRepository players){
        this.players = players;
    }
    
    protected IPlayerRepository getPlayers(){
        return players;
    }
    
    public abstract ISocketClient getSocket();
    
    public abstract void sendDisconnectInfoToProxy();
    
    public abstract ISocket init() throws IOException;
    
    public abstract void sendMessage(JsonObject message);
    
    public abstract void disable();
}
