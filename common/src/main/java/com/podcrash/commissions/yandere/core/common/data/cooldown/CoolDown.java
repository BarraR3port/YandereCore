package com.podcrash.commissions.yandere.core.common.data.cooldown;

import java.util.UUID;

public abstract class CoolDown {
    private final long startTime;
    private final UUID player;
    private final int duration;
    private final CoolDownType type;
    
    public CoolDown(UUID player, int duration, CoolDownType type){
        this.startTime = System.currentTimeMillis();
        this.player = player;
        this.duration = duration;
        this.type = type;
    }
    
    public long getStartTime(){
        return startTime;
    }
    
    public UUID getPlayer(){
        return player;
    }
    
    public int getDuration(){
        return duration;
    }
    
    public abstract String getMessage();
    
    public boolean isCoolDown(){
        return ((startTime / 1000) + duration) - (System.currentTimeMillis() / 1000) > 0;
    }
    
    public String getRemainingTime(){
        return ((startTime / 1000) + duration) - (System.currentTimeMillis() / 1000) + "";
    }
    
    public CoolDownType getType(){
        return type;
    }
}

