package com.podcrash.commissions.yandere.core.common.data.server.repo;

import com.podcrash.commissions.yandere.core.common.data.plugin.LyPlugin;

import java.util.ArrayList;

public class Response {
    private final String type;
    private final ArrayList<LyPlugin> registeredPlugins;
    
    public Response(String type, ArrayList<LyPlugin> registeredPlugins){
        this.type = type;
        this.registeredPlugins = registeredPlugins;
    }
    
    public String getType(){
        return type;
    }
    
    public ArrayList<LyPlugin> getPlugins(){
        return registeredPlugins;
    }
}