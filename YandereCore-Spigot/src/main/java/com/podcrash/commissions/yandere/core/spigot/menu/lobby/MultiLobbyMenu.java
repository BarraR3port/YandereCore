package com.podcrash.commissions.yandere.core.spigot.menu.lobby;

import com.podcrash.commissions.yandere.core.common.data.server.ServerType;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.UpdatableMenu;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MultiLobbyMenu extends UpdatableMenu {
    
    private final ServerType serverType;
    
    public MultiLobbyMenu(IPlayerMenuUtility playerMenuUtility, ServerType serverType){
        super(playerMenuUtility);
        this.serverType = serverType;
    }
    
    @Override
    public String getMenuName(){
        switch(serverType){
            case SKY_WARS:
                return "Lobbys de &eSky Wars";
            case BED_WARS:
                return "Lobbys de &eBed Wars";
            case PRACTICE:
                return "Lobbys de &ePractice";
            case TNT_TAG:
                return "Lobbys de &eTNT Tag";
            case SURVIVAL:
                return "Lobbys de &eSurvival";
            case LOBBY:
            default:
                return "Lobbys";
        }
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
