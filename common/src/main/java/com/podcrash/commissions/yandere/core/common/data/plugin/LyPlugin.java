package com.podcrash.commissions.yandere.core.common.data.plugin;

import java.util.UUID;

public class LyPlugin {
    
    private final UUID uuid;
    private final String name;
    private final String bukkitName;
    private final String version;
    private final String fileExtension;
    private final String fileName;
    private final String hash;
    
    public LyPlugin(String name, String bukkitName, String version, String fileExtension, String hash){
        this.name = name;
        this.bukkitName = bukkitName;
        this.version = version;
        this.uuid = UUID.randomUUID();
        this.fileExtension = fileExtension;
        this.fileName = name + "-" + version + fileExtension;
        this.hash = hash;
    }
    
    public UUID getUuid(){
        return uuid;
    }
    
    public String getName(){
        return name;
    }
    
    public String getVersion(){
        return version;
    }
    
    public String getFileExtension(){
        return fileExtension;
    }
    
    public String getFileName(){
        return fileName;
    }
    
    public String getBukkitName(){
        return bukkitName;
    }
    
    public String getHash(){
        return hash;
    }
}
