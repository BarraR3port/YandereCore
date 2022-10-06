package com.podcrash.commissions.yandere.core.common.data.punish;

import com.podcrash.commissions.yandere.core.common.YandereApi;
import net.lymarket.lyapi.common.Api;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

public abstract class Punish {
    
    protected UUID UUID;
    protected String punished;
    protected String punisher;
    protected String reason;
    protected PunishType punishType;
    protected Date createDate;
    protected PunishState state;
    protected String server;
    
    public Punish(String punished, String punisher, String reason, PunishType punishType, String server){
        this.UUID = java.util.UUID.randomUUID();
        this.punished = punished;
        this.punisher = punisher;
        this.reason = reason;
        this.punishType = punishType;
        this.createDate = new Date();
        this.state = PunishState.ACTIVE;
        this.server = server;
    }
    
    public UUID getUUID(){
        return UUID;
    }
    
    public String getPunished(){
        return punished;
    }
    
    public void setPunished(String punished){
        this.punished = punished;
    }
    
    public String getPunisher(){
        return punisher;
    }
    
    public void setPunisher(String punisher){
        this.punisher = punisher;
    }
    
    public String getReason(){
        return reason;
    }
    
    public void setReason(String reason){
        this.reason = reason;
    }
    
    public PunishType getPunishType(){
        return punishType;
    }
    
    public void setPunishType(PunishType punishType){
        this.punishType = punishType;
    }
    
    public Date getCreateDate(){
        return createDate;
    }
    
    public void setCreateDate(Date createDate){
        this.createDate = createDate;
    }
    
    
    public PunishState getState(){
        return state;
    }
    
    public void setState(PunishState state){
        this.state = state;
    }
    
    public String getServer(){
        return server;
    }
    
    protected Date parseDate(String date){
        try {
            return YandereApi.DATE_FORMAT.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
    
    public String serialize(){
        return Api.getGson().toJson(this);
    }
    
    @Override
    public String toString(){
        return serialize();
    }
}

