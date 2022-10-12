package com.podcrash.commissions.yandere.core.spigot.listener;

import com.podcrash.commissions.yandere.core.common.data.cooldown.CoolDown;
import com.podcrash.commissions.yandere.core.common.data.cooldown.CoolDownType;
import com.podcrash.commissions.yandere.core.common.data.logs.Log;
import com.podcrash.commissions.yandere.core.common.data.logs.LogType;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.cooldowns.MsgCoolDown;
import com.podcrash.commissions.yandere.core.spigot.settings.Settings;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
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
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
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
                    e.getPlayer().spigot().sendMessage(Utils.hoverOverMessage(coolDown.getMessage(), Collections.singletonList("&7Tiempo restante: &e" + coolDown.getRemainingTime() + "s")));
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
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e){
        e.setJoinMessage("");
        e.getPlayer().getInventory().setHeldItemSlot(0);
        e.getPlayer().updateInventory();
        Main.getInstance().getSocket().sendUpdate();
        subPlayerJoinEvent(e);
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerQuitEvent(PlayerQuitEvent e){
        e.setQuitMessage("");
        Main.getInstance().getPlayers().unloadPlayer(e.getPlayer().getUniqueId());
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> Main.getInstance().getSocket().sendUpdate(), 20L);
    }
    
    @EventHandler(ignoreCancelled = true)
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
    
    @EventHandler(ignoreCancelled = true)
    public synchronized void onPlayerInteractInInventory(InventoryDragEvent e){
        ItemStack item = e.getCursor();
        ItemStack itemOld = e.getOldCursor();
        if (item == null) return;
        if (itemOld == null) return;
        if (NBTItem.hasTag(item, "item-prop") || NBTItem.hasTag(itemOld, "item-prop")){
            String prop = NBTItem.getTag(item, "item-prop");
            switch(prop){
                case "lobby-menu":
                case "no-move":
                case "multi-lobby-menu":
                    e.setCancelled(true);
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerCommand(PlayerCommandPreprocessEvent e){
        Main.getInstance().getLogs().createLog(LogType.COMMAND, Settings.PROXY_SERVER_NAME, e.getMessage(), e.getPlayer().getName());
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onKick(PlayerKickEvent e){
        if (!e.getReason().equals("disconnect.spam"))
            return;
        e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerMoveItem(InventoryMoveItemEvent e){
        ItemStack item = e.getItem();
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
    
}
