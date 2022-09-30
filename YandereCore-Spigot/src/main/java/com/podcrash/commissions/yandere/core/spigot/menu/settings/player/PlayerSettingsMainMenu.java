package com.podcrash.commissions.yandere.core.spigot.menu.settings.player;

import com.podcrash.commissions.yandere.core.spigot.Main;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.UpdatableMenu;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerSettingsMainMenu extends UpdatableMenu {
    
    
    private final int[] decorateSlots = {
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 17,
            18, 26,
            27, 35,
            36, 37, 38, 39, 40, 41, 42, 43, 44,
            45, 46, 47, 48, 50, 51, 52, 53};
    
    private final UUID targetUUID;
    
    public PlayerSettingsMainMenu(IPlayerMenuUtility playerMenuUtility){
        super(playerMenuUtility);
        this.targetUUID = getOwner().getUniqueId();
        this.setDebug(true);
        
    }
    
    public PlayerSettingsMainMenu(IPlayerMenuUtility playerMenuUtility, UUID target){
        super(playerMenuUtility);
        this.targetUUID = target;
        this.setDebug(true);
    }
    
    @Override
    public String getMenuName(){
        return "&4• &8" + (targetUUID.equals(getOwner().getUniqueId()) ? "Ajustes" : "Ajustes de " + Main.getInstance().getPlayers().getCachedPlayer(targetUUID).getName());
    }
    
    @Override
    public int getSlots(){
        return 54;
    }
    
    @Override
    public void setMenuItems(){
        /*for ( int i : decorateSlots ){
            inventory.setItem(i, null);
        }*/
        int[] separateSlot = {12, 21, 30};
        ItemStack fillerMidItem = new ItemBuilder(FILLER_GLASS.clone()).setDyeColor(14).build();
        for ( int i : separateSlot ){
            inventory.setItem(i, fillerMidItem);
        }
        inventory.setItem(49, CLOSE_ITEM);
    }
    
    @Override
    public void handleMenu(InventoryClickEvent e){
    
    }
}
