package com.podcrash.commissions.yandere.core.spigot.cooldowns;

import com.podcrash.commissions.yandere.core.common.data.cooldown.CoolDown;
import com.podcrash.commissions.yandere.core.common.data.cooldown.CoolDownType;

import java.util.UUID;

public class MsgCoolDown extends CoolDown {
    
    public MsgCoolDown(UUID player, int duration){
        super(player, duration, CoolDownType.MSG);
    }
    
    @Override
    public String getMessage(){
        return "&cEspera, estás hablando muy rápido!";
    }
    
}