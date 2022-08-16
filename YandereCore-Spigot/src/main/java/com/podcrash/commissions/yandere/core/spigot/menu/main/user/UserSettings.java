package com.podcrash.commissions.yandere.core.spigot.menu.main.user;

import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.UpdatableMenu;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public class UserSettings extends UpdatableMenu {
    
    private final UUID target_user;
    
    public UserSettings(IPlayerMenuUtility playerMenuUtility, UUID target_user){
        super(playerMenuUtility);
        this.target_user = target_user;
    }
    
    @Override
    public String getMenuName(){
        return "Ajustes" + (getOwner().getUniqueId().equals(target_user) ? "" : " de " + getOwner().getName());
    }
    
    @Override
    public int getSlots(){
        return 0;
    }
    
    @Override
    public void setMenuItems(){
    
    }
    
    @Override
    public void handleMenu(InventoryClickEvent e){
    
    }
}
