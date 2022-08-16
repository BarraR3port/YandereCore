package com.podcrash.commissions.yandere.core.common.data.response;

import java.util.UUID;

public final class WorldVisitResponse implements Response {
    
    private final UUID owner_uuid;
    private final UUID target_uuid;
    private final String targetName;
    private final String target_server;
    private final String initial_server;
    private UUID world_uuid;
    private boolean response;
    
    
    public WorldVisitResponse(UUID owner_uuid, UUID world_uuid, UUID target_uuid, String targetName, String target_server, String initial_server, boolean response){
        this.owner_uuid = owner_uuid;
        this.world_uuid = world_uuid;
        this.targetName = targetName;
        this.target_uuid = target_uuid;
        this.target_server = target_server;
        this.response = response;
        this.initial_server = initial_server;
    }
    
    public WorldVisitResponse(boolean response){
        this.owner_uuid = null;
        this.world_uuid = null;
        this.target_uuid = null;
        this.target_server = null;
        this.targetName = null;
        this.initial_server = null;
        this.response = response;
    }
    
    public String getTargetName(){
        return targetName;
    }
    
    public String getInitial_server(){
        return initial_server;
    }
    
    public UUID getOwner_uuid(){
        return owner_uuid;
    }
    
    public UUID getWorld_uuid(){
        return world_uuid;
    }
    
    public void setWorld_uuid(UUID world_uuid){
        this.world_uuid = world_uuid;
    }
    
    public UUID getTarget_uuid(){
        return target_uuid;
    }
    
    public boolean getResponse(){
        return response;
    }
    
    public void setResponse(boolean response){
        this.response = response;
    }
    
    public String getTarget_server(){
        return target_server;
    }
}
