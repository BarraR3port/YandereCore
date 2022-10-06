package com.podcrash.commissions.yandere.core.common.data.punish;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public enum PunishDateType {
    DAYS("Days"),
    HOURS("Hours"),
    MINUTES("Minutes"),
    SECONDS("Seconds");
    
    private final String name;
    
    PunishDateType(String name){
        this.name = name;
    }
    
    public static Date getDate(PunishDateType type, int amount){
        Calendar cal = Calendar.getInstance();
        switch(type){
            case DAYS:
                cal.add(Calendar.DATE, amount);
            case HOURS:
                cal.add(Calendar.HOUR, amount);
            case MINUTES:
                cal.add(Calendar.MINUTE, amount);
            case SECONDS:
                cal.add(Calendar.SECOND, amount);
        }
        return cal.getTime();
    }
    
    public static Date ConvertDate(int amount, String time){
        long now = new Date().getTime();
        switch(time){
            case "s":{
                return new Date(now + TimeUnit.SECONDS.toMillis(amount));
            }
            case "m":{
                return new Date(now + TimeUnit.MINUTES.toMillis(amount));
            }
            case "h":{
                return new Date(now + TimeUnit.HOURS.toMillis(amount));
            }
            case "d":{
                return new Date(now + TimeUnit.DAYS.toMillis(amount));
            }
            case "w":{
                return new Date(now + (TimeUnit.DAYS.toMillis(amount) * 7));
            }
            case "M":{
                return new Date(now + (TimeUnit.DAYS.toMillis(amount) * 31));
            }
            default:{
                return new Date(now + (TimeUnit.DAYS.toMillis(amount) * 365));
            }
        }
        
    }
    
    public String getName(){
        return name;
    }
}
