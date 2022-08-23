package com.podcrash.commissions.yandere.core.spigot.items.lobby;

import org.bukkit.inventory.ItemStack;

public final class LobbyItem {
    
    private ItemStack item;
    private int slot;
    
    public LobbyItem(ItemStack item, int slot){
        this.item = item;
        this.slot = slot;
    }
    
    public ItemStack getItem(){
        return item;
    }
    
    public void setItem(ItemStack item){
        this.item = item;
    }
    
    public int getSlot(){
        return slot;
    }
    
    public void setSlot(int slot){
        this.slot = slot;
    }
}
