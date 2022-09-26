package com.podcrash.commissions.yandere.core.common.data.server.repo;

public class LoadedPlugin {
    private final String name;
    private final String bukkitName;
    private final String hash;
    private final String version;
    
    public LoadedPlugin(String name, String bukkitName, String hash, String version){
        this.name = name;
        this.bukkitName = bukkitName;
        this.hash = hash;
        this.version = version;
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
    
    public String getVersion(){
        return version;
    }
}