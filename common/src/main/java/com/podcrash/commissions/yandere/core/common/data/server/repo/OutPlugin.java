package com.podcrash.commissions.yandere.core.common.data.server.repo;

public class OutPlugin {
    private final String name;
    private final String bukkitName;
    private final String hash;
    
    public OutPlugin(String name, String bukkitName, String hash){
        this.name = name;
        this.bukkitName = bukkitName;
        this.hash = hash;
    }
    
    public OutPlugin(String name){
        this.name = name;
        this.bukkitName = name;
        this.hash = null;
    }
    
    public String getName(){
        return name;
    }
    
    public String getBukkitName(){
        return bukkitName;
    }
    
    public String getHash(){
        return hash;
    }
}