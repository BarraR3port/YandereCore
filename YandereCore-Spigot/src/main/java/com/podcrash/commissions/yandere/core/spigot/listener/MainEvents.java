package com.podcrash.commissions.yandere.core.spigot.listener;

import com.podcrash.commissions.yandere.core.common.data.cooldown.CoolDown;
import com.podcrash.commissions.yandere.core.common.data.cooldown.CoolDownType;
import com.podcrash.commissions.yandere.core.common.data.lobby.PlayerVisibility;
import com.podcrash.commissions.yandere.core.common.data.logs.Log;
import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.common.data.user.props.Rank;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.cooldowns.LobbyCoolDown;
import com.podcrash.commissions.yandere.core.spigot.cooldowns.MsgCoolDown;
import com.podcrash.commissions.yandere.core.spigot.items.Items;
import com.podcrash.commissions.yandere.core.spigot.menu.lobby.LobbyMenu;
import com.podcrash.commissions.yandere.core.spigot.menu.lobby.MultiLobbyMenu;
import net.lymarket.lyapi.spigot.LyApi;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.LinkedList;

public abstract class MainEvents implements Listener {
    
    private final LinkedList<String> swearList = new LinkedList<>();
    
    public MainEvents(){
        Collections.addAll(swearList,
                "puta", "maricon", "maricón", "puto", "pendejo",
                "pendeja", "culiao", "qlo", "kuliao", "weon", "scw", "aweonao",
                "aweona", "wna", "weona", "weonsito", "weonsita"
        );
        
    }
    
    public abstract void subPlayerQuitEvent(PlayerQuitEvent e);
    
    public abstract void subPlayerJoinEvent(PlayerJoinEvent e);
    
    public abstract boolean subPlayerChatEvent(AsyncPlayerChatEvent e);
    
    @EventHandler
    public abstract void onPlayerTeleport(PlayerTeleportEvent e);
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent e){
        Main.getInstance().getPlayers().getOrCreatePlayer(e.getName(), e.getUniqueId(), String.valueOf(e.getAddress()).replace("/", ""));
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerAsyncPlayerChatEvent(AsyncPlayerChatEvent e){
        
        if (!e.getPlayer().hasPermission("yandere.chat.swear.bypass")){
            for ( String word : e.getMessage().split(" ") ){
                if (swearList.contains(word.toLowerCase())){
                    Utils.sendMessage(e.getPlayer(), "&cHey! Cuida tu lenguaje!");
                    e.setCancelled(true);
                    return;
                }
            }
        }
        
        if (!e.getPlayer().hasPermission("yandere.chat.fast")){
            Log log = Main.getInstance().getLogs().getLastPlayerMessageLog(e.getPlayer().getName());
            if (log != null){
                if (Main.getInstance().getCoolDownManager().hasCoolDown(e.getPlayer().getUniqueId(), CoolDownType.MSG)){
                    CoolDown coolDown = Main.getInstance().getCoolDownManager().getCoolDown(e.getPlayer().getUniqueId(), CoolDownType.MSG);
                    e.getPlayer().sendMessage(Utils.format(coolDown.getMessage()));
                    e.setCancelled(true);
                    return;
                } else if (log.getMsg().equalsIgnoreCase(e.getMessage()) || log.getMsg().startsWith(e.getMessage()) || e.getMessage().startsWith(log.getMsg())){
                    e.getPlayer().sendMessage(Utils.format("&cNo puedes repetir el mismo mensaje!"));
                    e.setCancelled(true);
                    return;
                }
                Main.getInstance().getCoolDownManager().removeCoolDown(e.getPlayer().getUniqueId(), CoolDownType.MSG);
            }
        }
        
        if (subPlayerChatEvent(e)){
            if (!e.getPlayer().hasPermission("yandere.chat.fast")){
                Main.getInstance().getCoolDownManager().addCoolDown(new MsgCoolDown(e.getPlayer().getUniqueId(), 3));
            }
        } else {
            e.getPlayer().sendMessage(Utils.format("&cHa ocurrido un error, contactate con un admin si el problema persiste."));
            e.setCancelled(false);
        }
        
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
    
    @EventHandler
    public void onPlayerInteractInInventory(InventoryClickEvent e){
        ItemStack item = e.getCurrentItem();
        if (item == null) return;
        if (item.getType().equals(Material.AIR)) return;
        if (NBTItem.hasTag(item, "item-prop")){
            String prop = NBTItem.getTag(item, "item-prop");
            switch(prop){
                case "lobby-menu":
                case "no-move":
                case "multi-lobby-menu":
                    e.setCancelled(true);
            }
        }
        
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerClicks(PlayerInteractEvent e){
        ItemStack item = e.getItem();
        if (item == null) return;
        if (item.getType().equals(Material.AIR)) return;
        NBTItem nbtItem = new NBTItem(item);
        if (nbtItem.hasTag("item-prop")){
            String prop = nbtItem.getTag("item-prop");
            switch(prop){
                case "lobby-menu":
                case "no-move":
                case "multi-lobby-menu":
                    e.setCancelled(true);
            }
        }
        if (nbtItem.hasTag("lobby-item")){
            new LobbyMenu(LyApi.getPlayerMenuUtility(e.getPlayer())).open();
        } else if (nbtItem.hasTag("lobby-multi-lobby")){
            new MultiLobbyMenu(LyApi.getPlayerMenuUtility(e.getPlayer())).open();
        } else if (nbtItem.hasTag("command")){
            String command = nbtItem.getTag("command").replace("_", " ");
            e.getPlayer().performCommand(command);
        } else if (nbtItem.hasTag("lobby-player-visibility")){
            Player p = e.getPlayer();
            if (Main.getInstance().getCoolDownManager().hasCoolDown(p.getUniqueId(), CoolDownType.ITEM_USE)){
                CoolDown coolDown = Main.getInstance().getCoolDownManager().getCoolDown(p.getUniqueId(), CoolDownType.ITEM_USE);
                p.sendMessage(Utils.format(coolDown.getMessage()));
                e.setCancelled(true);
                return;
            }
            Main.getInstance().getCoolDownManager().removeCoolDown(p.getUniqueId(), CoolDownType.ITEM_USE);
            
            User user = Main.getInstance().getPlayers().getLocalStoredPlayer(p.getUniqueId());
            PlayerVisibility currentPlayerVisibility = user.getPlayerVisibility();
            user.nextPlayerVisibility();
            Main.getInstance().getPlayers().savePlayer(user);
            for ( Player player : Bukkit.getOnlinePlayers() ){
                if (player.getUniqueId().equals(p.getUniqueId())) continue;
                switch(currentPlayerVisibility){
                    case ALL:
                        final User spigotUser = Main.getInstance().getPlayers().getLocalStoredPlayer(player.getUniqueId());
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
                    p.getInventory().setItem(Items.LOBBY_PLAYER_VISIBILITY_RANKS.getSlot(), Items.LOBBY_PLAYER_VISIBILITY_RANKS.getItem());
                    break;
                case RANKS:
                    p.getInventory().setItem(Items.LOBBY_PLAYER_VISIBILITY_NONE.getSlot(), Items.LOBBY_PLAYER_VISIBILITY_NONE.getItem());
                    break;
                case NONE:
                    p.getInventory().setItem(Items.LOBBY_PLAYER_VISIBILITY_ALL.getSlot(), Items.LOBBY_PLAYER_VISIBILITY_ALL.getItem());
                    break;
            }
            Main.getInstance().getCoolDownManager().addCoolDown(new LobbyCoolDown(p.getUniqueId(), 3));
            p.updateInventory();
        }
    }
    
}
