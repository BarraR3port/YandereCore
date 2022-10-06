package com.podcrash.commissions.yandere.core.common.data.punish.warn;

import com.podcrash.commissions.yandere.core.common.data.punish.Punish;
import com.podcrash.commissions.yandere.core.common.data.punish.PunishType;

import java.util.Calendar;
import java.util.Date;

public class Warn extends Punish {
    
    protected Date expDate;
    
    public Warn(String punished, String punisher, String reason, String server){
        super(punished, punisher, reason, PunishType.WARN, server);
        this.expDate = ExpDate();
    }
    
    
    public Date getExpDate(){
        return expDate;
    }
    
    public void setExpDate(Date expDate){
        this.expDate = expDate;
    }
    
    private Date ExpDate(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, 60); //minus number would decrement the days
        return cal.getTime();
    }
}
