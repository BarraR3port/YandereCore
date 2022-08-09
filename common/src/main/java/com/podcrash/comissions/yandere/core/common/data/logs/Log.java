package com.podcrash.comissions.yandere.core.common.data.logs;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class Log {
    
    private final HashMap<String, String> property = new HashMap<>();
    private final HashMap<String, Boolean> options = new HashMap<>();
    private final Date createDate;
    private final UUID uuid;
    private String server;
    private LogType type;
    private String msg;
    private String owner;
    
    public Log(LogType type, String server, String msg, String owner){
        this.type = type;
        this.msg = msg;
        this.owner = owner;
        this.server = server;
        this.createDate = new Date();
        this.uuid = UUID.randomUUID();
    }
    
    public Log(LogType type, String server, String msg){
        this.type = type;
        this.msg = msg;
        this.owner = "SYSTEM";
        this.server = server;
        this.createDate = new Date();
        this.uuid = UUID.randomUUID();
    }
    
    public LogType getType(){
        return type;
    }
    
    public void setType(LogType type){
        this.type = type;
    }
    
    public String getMsg(){
        return msg;
    }
    
    public void setMsg(String msg){
        this.msg = msg;
    }
    
    public String getOwner(){
        return owner;
    }
    
    public void setOwner(String owner){
        this.owner = owner;
    }
    
    public String getProperty(String key){
        return property.getOrDefault(key, null);
    }
    
    public void setProperty(String key, String value){
        this.property.put(key, value);
    }
    
    public HashMap<String, Boolean> getOptions(){
        return options;
    }
    
    public Boolean getOption(String key){
        return options.getOrDefault(key, false);
    }
    
    public void setOption(String key, Boolean value){
        this.options.put(key, value);
    }
    
    public Date getCreateDate(){
        return createDate;
    }
    
    public UUID getUuid(){
        return uuid;
    }
    
    public String getServer(){
        return server;
    }
    
    public void setServer(String server){
        this.server = server;
    }
}
