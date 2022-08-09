package com.podcrash.comissions.yandere.core.common.socket;

import com.podcrash.comissions.yandere.core.common.db.IPlayerRepository;

public abstract class ISocket<U> implements SocketMSG {
    
    private final IPlayerRepository<U> players;
    
    public ISocket(IPlayerRepository<U> players){
        this.players = players;
    }
    
    protected IPlayerRepository<U> getPlayers(){
        return players;
    }
    
    public abstract ISocketClient getSocket();
    
    public abstract void sendDisconnectInfoToProxy();
}
