package com.podcrash.commissions.yandere.core.common.data.punish.mute;

import com.podcrash.commissions.yandere.core.common.data.punish.Punish;
import com.podcrash.commissions.yandere.core.common.data.punish.PunishType;

import java.util.Date;

public class Mute extends Punish {
    
    protected Date expDate;
    
    public Mute(String punished, String punisher, String reason, Date expDate, String server){
        super(punished, punisher, reason, PunishType.MUTE, server);
        this.expDate = expDate;
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
    
}
