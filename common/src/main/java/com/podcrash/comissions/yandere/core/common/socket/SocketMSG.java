package com.podcrash.comissions.yandere.core.common.socket;

import java.util.HashMap;
import java.util.UUID;

public interface SocketMSG {
    
    
    void sendJoinServer(UUID owner, String serverTarget);
    
    void sendJoinServer(UUID owner, String serverTarget, String msg);
    
    void sendMSGToPlayer(UUID target, String key);
    
    void sendMSGToPlayer(UUID target, String key, String word, String replacement);
    
    void sendMSGToPlayer(UUID target, String key, HashMap<String, String> replacementsMap);
    
    void sendUpdate();
    
    String encrypt(String json);
    
    String decrypt(String data);
}
