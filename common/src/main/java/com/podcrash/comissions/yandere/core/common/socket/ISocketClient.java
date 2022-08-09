package com.podcrash.comissions.yandere.core.common.socket;

import com.google.gson.JsonObject;

public interface ISocketClient {
    
    boolean sendMessage(JsonObject message);
    
    void reconnect(String msg);
}
