package com.podcrash.comissions.yandere.core.common.data.msg;


import java.util.Base64;
import java.util.UUID;


public abstract class Msg {
    private final UUID owner;
    private final String version;
    private String msg;
    
    public Msg(UUID owner, String msg, String version){
        this.msg = encrypt(msg);
        this.owner = owner;
        this.version = version;
    }
    
    public String getMsg(){
        return decrypt(msg);
    }
    
    public void setMsg(String msg){
        this.msg = msg;
    }
    
    public UUID getOwner(){
        return owner;
    }
    
    
    public String getVersion(){
        return version;
    }
    
    public String encrypt(String msg){
        return Base64.getEncoder().encodeToString(msg.getBytes());
    }
    
    public String decrypt(String data){
        return new String(Base64.getDecoder().decode(data.getBytes()));
    }
}
