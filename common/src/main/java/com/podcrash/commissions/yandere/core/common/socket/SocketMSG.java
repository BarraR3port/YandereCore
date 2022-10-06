package com.podcrash.commissions.yandere.core.common.socket;

import com.podcrash.commissions.yandere.core.common.data.punish.Punish;

import java.util.HashMap;
import java.util.UUID;

public interface SocketMSG {
    
    
    void sendJoinServer(UUID owner, String serverTarget);
    
    void sendJoinServer(UUID owner, String serverTarget, String msg);
    
    void sendMSGToPlayer(UUID target, String key);
    
    void sendCheckPluginUpdates();
    
    void sendCheckGlobalServerStatsFetchData();
    
    void sendDisconnectInfoToProxy();
    
    void sendMSGToPlayer(UUID target, String key, String word, String replacement);
    
    void sendMSGToPlayer(UUID target, String key, HashMap<String, String> replacementsMap);
    
    void sendPunish(Punish punish, String punishType);
    
    void sendUpdate();
    
    String encrypt(String json);
    
    String decrypt(String data);
}
