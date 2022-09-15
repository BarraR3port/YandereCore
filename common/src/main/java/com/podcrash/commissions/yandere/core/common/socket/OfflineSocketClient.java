package com.podcrash.commissions.yandere.core.common.socket;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public final class OfflineSocketClient extends ISocket {
    public OfflineSocketClient(){
        super(null);
    }
    
    @Override
    public ISocketClient getSocket(){
        return null;
    }
    
    @Override
    public void sendCheckPluginUpdates(){
    
    }
    
    @Override
    public void sendDisconnectInfoToProxy(){
    
    }
    
    @Override
    public OfflineSocketClient init() throws IOException{
        return null;
    }
    
    @Override
    public void sendMessage(JsonObject message){
    
    }
    
    @Override
    public void disable(){
    
    }
    
    @Override
    public void sendJoinServer(UUID owner, String serverTarget){
    
    }
    
    @Override
    public void sendJoinServer(UUID owner, String serverTarget, String msg){
    
    }
    
    @Override
    public void sendMSGToPlayer(UUID target, String key){
    
    }
    
    @Override
    public void sendMSGToPlayer(UUID target, String key, String word, String replacement){
    
    }
    
    @Override
    public void sendMSGToPlayer(UUID target, String key, HashMap<String, String> replacementsMap){
    
    }
    
    @Override
    public void sendUpdate(){
    
    }
    
    @Override
    public String encrypt(String json){
        return null;
    }
    
    @Override
    public String decrypt(String data){
        return null;
    }
}
