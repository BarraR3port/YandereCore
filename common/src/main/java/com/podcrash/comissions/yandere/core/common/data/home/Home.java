package com.podcrash.comissions.yandere.core.common.data.home;

import com.podcrash.comissions.yandere.core.common.data.loc.Loc;

import java.util.UUID;

public abstract class Home {
    private final UUID owner;
    private final UUID uuid;
    private String name;
    private Loc location;
    private String version;
    
    public Home(UUID owner, String name, Loc location, String version){
        this.owner = owner;
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.location = location;
        this.version = version;
    }
    
    public Home(String uuid, UUID owner, String name, Loc location, String version){
        this.owner = owner;
        this.name = name;
        this.location = location;
        this.uuid = UUID.fromString(uuid);
        this.version = version;
    }
    
    public UUID getOwner(){
        return owner;
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String homeName){
        this.name = homeName;
    }
    
    public Loc getLocation(){
        return location;
    }
    
    public void setLocation(Loc homeLoc){
        this.location = homeLoc;
    }
    
    public UUID getUUID(){
        return uuid;
    }
    
    public String getVersion(){
        return version;
    }
    
    public void setVersion(String version){
        this.version = version;
    }
}
