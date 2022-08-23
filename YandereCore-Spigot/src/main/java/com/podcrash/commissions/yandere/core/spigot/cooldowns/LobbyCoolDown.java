package com.podcrash.commissions.yandere.core.spigot.cooldowns;

import com.podcrash.commissions.yandere.core.common.data.cooldown.CoolDown;
import com.podcrash.commissions.yandere.core.common.data.cooldown.CoolDownType;

import java.util.UUID;

public class LobbyCoolDown extends CoolDown {
    
    public LobbyCoolDown(UUID player, int duration){
        super(player, duration, CoolDownType.ITEM_USE);
    }
    
    @Override
    public String getMessage(){
        return "&cTienes que esperar " + getRemainingTime() + " s para volver a usar este comando.";
    }
    
}
