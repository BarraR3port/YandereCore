package com.podcrash.commissions.yandere.core.spigot.listener.lobby;

import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.event.player.PlayerInteractEvent;

public class MainLobbyPlayerEvents extends LobbyPlayerEvents {
    
    public MainLobbyPlayerEvents(){
        super();
    }
    
    @Override
    public boolean subPlayerClicks(PlayerInteractEvent e, NBTItem item){
        return false;
    }
}

