package com.podcrash.commissions.yandere.core.common.data.user.props;

import java.util.Date;
import java.util.UUID;

public abstract class Reward {
    
    private final UUID uuid;
    private final Date createDate;
    private String name;
    private Date expireDate;
    
    public Reward(String name, Date expireDate){
        this.name = name;
        this.uuid = UUID.randomUUID();
        this.createDate = new Date();
        this.expireDate = expireDate;
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public UUID getUuid(){
        return uuid;
    }
    
    public Date getCreateDate(){
        return createDate;
    }
    
    public Date getExpireDate(){
        return expireDate;
    }
    
    public void setExpireDate(Date expireDate){
        this.expireDate = expireDate;
    }
    
}
