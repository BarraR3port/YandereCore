package com.podcrash.comissions.yandere.core.spigot.listener;

import com.podcrash.comissions.yandere.core.common.data.lobby.PlayerVisibility;
import com.podcrash.comissions.yandere.core.common.data.user.props.Rank;
import com.podcrash.comissions.yandere.core.spigot.Main;
import com.podcrash.comissions.yandere.core.spigot.items.Items;
import com.podcrash.comissions.yandere.core.spigot.menu.lobby.LobbyMenu;
import com.podcrash.comissions.yandere.core.spigot.users.SpigotUser;
import net.lymarket.lyapi.spigot.LyApi;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public abstract class MainEvents implements Listener {
    
    public MainEvents(){
    }
    
    public abstract void subPlayerQuitEvent(PlayerQuitEvent e);
    
    public abstract void subPlayerJoinEvent(PlayerJoinEvent e);
    
    public abstract void subPlayerChatEvent(AsyncPlayerChatEvent e);
    
    @EventHandler
    public abstract void onPlayerTeleport(PlayerTeleportEvent e);
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent e){
        Main.getInstance().getPlayers().getOrCreatePlayer(e.getName(), e.getUniqueId(), String.valueOf(e.getAddress()).replace("/", ""));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerAsyncPlayerChatEvent(AsyncPlayerChatEvent e){
        subPlayerChatEvent(e);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent e){
        e.setJoinMessage("");
        Main.getInstance().getSocket().sendUpdate();
        subPlayerJoinEvent(e);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuitEvent(PlayerQuitEvent e){
        e.setQuitMessage("");
        Main.getInstance().getPlayers().unloadPlayer(e.getPlayer().getUniqueId());
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> Main.getInstance().getSocket().sendUpdate(), 40L);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerClicks(PlayerInteractEvent e){
        ItemStack item = e.getItem();
        if (item == null){
            return;
        }
        if (NBTItem.hasTag(item, "lobby-item")){
            new LobbyMenu(LyApi.getPlayerMenuUtility(e.getPlayer())).open();
        }
        if (NBTItem.hasTag(item, "lobby-player-visibility")){
            Player p = e.getPlayer();
            SpigotUser user = Main.getInstance().getPlayers().getLocalStoredPlayer(p.getUniqueId());
            PlayerVisibility currentPlayerVisibility = user.getPlayerVisibility();
            user.nextPlayerVisibility();
            Main.getInstance().getPlayers().savePlayer(user);
            for ( Player player : Bukkit.getOnlinePlayers() ){
                if (player.getUniqueId().equals(p.getUniqueId())) continue;
                switch(currentPlayerVisibility){
                    case ALL:
                        final SpigotUser spigotUser = Main.getInstance().getPlayers().getLocalStoredPlayer(player.getUniqueId());
                        if (spigotUser.getRank() == Rank.USUARIO){
                            p.hidePlayer(player);
                        }
                        break;
                    case RANKS:
                        p.hidePlayer(player);
                        break;
                    case NONE:
                        p.showPlayer(player);
                        break;
                }
            }
            switch(currentPlayerVisibility){
                case ALL:
                    p.getInventory().setItem(8, Items.LOBBY_PLAYER_VISIBILITY_RANKS.clone());
                    break;
                case RANKS:
                    p.getInventory().setItem(8, Items.LOBBY_PLAYER_VISIBILITY_NONE.clone());
                    break;
                case NONE:
                    p.getInventory().setItem(8, Items.LOBBY_PLAYER_VISIBILITY_ALL.clone());
                    break;
            }
            
            p.updateInventory();
        }
        
        
    }
    
}
