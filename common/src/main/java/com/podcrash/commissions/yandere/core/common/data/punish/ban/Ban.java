package com.podcrash.commissions.yandere.core.common.data.punish.ban;

import com.podcrash.commissions.yandere.core.common.data.punish.Punish;
import com.podcrash.commissions.yandere.core.common.data.punish.PunishType;

import java.util.Date;

public class Ban extends Punish {
    
    public boolean Ip;
    public String address;
    protected Date expDate;
    //public boolean local;
    
    public Ban(String punished, String punisher, String reason, Date expDate, Boolean ipBanned, String server, String address){
        super(punished, punisher, reason, PunishType.BAN, server);
        this.expDate = expDate;
        this.Ip = ipBanned;
        this.address = address;
        //this.local = local;
    }
    
    public Date getExpDate(){
        return expDate;
    }
    
    public void setExpDate(Date expDate){
        this.expDate = expDate;
    }
    
    public boolean isPermanent(){
        return this.getExpDate().equals(this.parseDate("26-10-3000"));
    }
    
    public boolean isIpBanned(){
        return Ip;
    }
    
    public void setIpBanned(boolean value){
        this.Ip = value;
    }
    
    public String getAddress(){
        return address;
    }
}
