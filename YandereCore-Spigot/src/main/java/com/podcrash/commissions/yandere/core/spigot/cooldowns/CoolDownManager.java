package com.podcrash.commissions.yandere.core.spigot.cooldowns;

import com.podcrash.commissions.yandere.core.common.data.cooldown.CoolDown;
import com.podcrash.commissions.yandere.core.common.data.cooldown.CoolDownType;

import java.util.LinkedList;
import java.util.UUID;

public class CoolDownManager {
    
    private final LinkedList<CoolDown> cooldowns = new LinkedList<>();
    
    public void addCoolDown(CoolDown coolDown){
        cooldowns.add(coolDown);
    }
    
    public boolean hasCoolDown(UUID player, CoolDownType type){
        return cooldowns.stream().anyMatch(coolDown -> coolDown.getType().equals(type) && coolDown.getPlayer().equals(player) && coolDown.isCoolDown());
    }
    
    public void removeCoolDown(UUID player, CoolDownType type){
        cooldowns.removeIf(coolDown -> coolDown.getType().equals(type) && coolDown.getPlayer().equals(player));
    }
    
    public CoolDown getCoolDown(UUID player, CoolDownType type){
        return cooldowns.stream().filter(coolDown -> coolDown.getType().equals(type) && coolDown.getPlayer().equals(player)).findFirst().orElse(null);
    }
    
}
