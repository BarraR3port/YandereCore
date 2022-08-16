package com.podcrash.comissions.yandere.core.spigot.listener.lobby;

import com.cryptomorin.xseries.XMaterial;
import com.podcrash.comissions.yandere.core.common.data.lobby.PlayerVisibility;
import com.podcrash.comissions.yandere.core.common.data.loc.Loc;
import com.podcrash.comissions.yandere.core.common.data.logs.LogType;
import com.podcrash.comissions.yandere.core.common.data.user.props.Rank;
import com.podcrash.comissions.yandere.core.common.error.UserNotFoundException;
import com.podcrash.comissions.yandere.core.spigot.Main;
import com.podcrash.comissions.yandere.core.spigot.items.Items;
import com.podcrash.comissions.yandere.core.spigot.listener.MainEvents;
import com.podcrash.comissions.yandere.core.spigot.settings.Settings;
import com.podcrash.comissions.yandere.core.spigot.users.SpigotUser;
import net.lymarket.lyapi.spigot.utils.Utils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;

import java.util.Arrays;
import java.util.UUID;

public final class LobbyPlayerEvents extends MainEvents {
    
    public LobbyPlayerEvents(){
    }
    
    public void subPlayerQuitEvent(PlayerQuitEvent e){
        
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent e){
        try {
            final World world = e.getTo().getWorld();
            final UUID playerUUID = e.getPlayer().getUniqueId();
            final SpigotUser user = Main.getInstance().getPlayers().getLocalStoredPlayer(playerUUID);
            final Location loc = e.getTo();
            user.setLastLocation(new Loc(Settings.SERVER_NAME, world.getName(), loc.getX(), loc.getY(), loc.getZ()));
            Main.getInstance().getPlayers().savePlayer(user);
        } catch (NullPointerException | UserNotFoundException ignored) {
        }
    }
    
    public void subPlayerJoinEvent(PlayerJoinEvent e){
        Player p = e.getPlayer();
        p.setFoodLevel(20);
        p.setFireTicks(0);
        p.setExp(0);
        p.setLevel(0);
        try {
            p.teleport(Settings.SPAWN_LOCATION);
        } catch (NullPointerException | IllegalArgumentException ex) {
            p.teleport(p.getWorld().getSpawnLocation());
        }
        Items.setLobbyItems(p);
        p.setHealth(20);
        p.setSaturation(20F);
        p.setGameMode(GameMode.ADVENTURE);
        
        SpigotUser user = Main.getInstance().getPlayers().getLocalStoredPlayer(p.getUniqueId());
        if (user.getRank() != Rank.USUARIO){
            p.setAllowFlight(true);
        }
        
        PlayerVisibility visibility = user.getPlayerVisibility();
        
        for ( Player targetPlayer : Bukkit.getOnlinePlayers() ){
            if (targetPlayer.getUniqueId().equals(p.getUniqueId())) continue;
            SpigotUser targetUser = Main.getInstance().getPlayers().getLocalStoredPlayer(targetPlayer.getUniqueId());
            PlayerVisibility targetVisibility = targetUser.getPlayerVisibility();
            Rank targetRank = targetUser.getRank();
            switch(visibility){
                case ALL:
                    p.showPlayer(targetPlayer);
                    break;
                case RANKS:
                    if (targetRank != Rank.USUARIO){
                        p.showPlayer(targetPlayer);
                    } else {
                        p.hidePlayer(targetPlayer);
                    }
                    break;
                default:
                    p.hidePlayer(targetPlayer);
                    break;
            }
            
            switch(targetVisibility){
                case ALL:{
                    targetPlayer.showPlayer(p);
                    break;
                }
                case RANKS:{
                    if (user.getRank() != Rank.USUARIO){
                        targetPlayer.showPlayer(p);
                    } else {
                        targetPlayer.hidePlayer(p);
                    }
                    break;
                }
                default:{
                    targetPlayer.hidePlayer(p);
                    break;
                }
            }
            
        }
        
        
    }
    
    @Override
    public void subPlayerChatEvent(AsyncPlayerChatEvent event){
        Player p = event.getPlayer();
        String message = event.getMessage();
        
        if (event.isCancelled()) return;
        
        SpigotUser user = Main.getInstance().getPlayers().getLocalStoredPlayer(p.getUniqueId());
        if (p.hasPermission("yandere.chat.color")){
            message = Utils.format(message);
        }
        
        event.setCancelled(true);
        
        final String finalMessage = message;
        
        boolean isDefault = user.getRank() == Rank.USUARIO;
        
        final String prefix = user.getRank().getTabPrefix();
        final String white_msg = p.hasPermission("yandere.chat.whitemessage") ? "&f" : "&7";
        TextComponent level = Utils.hoverOverMessage(user.getLevel().getLevelName(),
                Arrays.asList(
                        "&7「&eNivel General&7⏌",
                        "",
                        "&7► Nivel: &d" + user.getLevel().getLevelName(),
                        "&7► XP: &d" + user.getLevel().getFormattedCurrentXp(),
                        "&7► XP Necesario: &d" + user.getLevel().getFormattedRequiredXp(),
                        user.getLevel().getProgressBar()));
        TextComponent name = Utils.hoverOverMessage(white_msg + p.getName(),
                Arrays.asList(
                        "&7「&eInformación del jugador&7⏌",
                        "",
                        "&7► Rango: " + prefix,
                        "&7► Monedas: &d" + user.getCoinsFormatted(),
                        "&7► Nivel: &d" + user.getLevel().getLevelName(),
                        user.getLevel().getProgressBar()/* ,
                            "&7Clan: &d" + clanTag*/));
        TextComponent rank = Utils.hoverOverMessageURL(isDefault ? "" : prefix,
                Arrays.asList("&7「&eYandere &5Rangos&7⏌",
                        "",
                        "&7► Este jugador tiene el rango " + prefix,
                        "&7► Puedes comprar más rangos en nuestra página web.",
                        "&7► Rangos disponibles: " + Rank.BRONCE.getTabPrefix() + " " + Rank.HIERRO.getTabPrefix() + " " + Rank.ORO.getTabPrefix() + " " + Rank.DIAMANTE.getTabPrefix(),
                        ""),
                "https://store.yanderecraft.com");
        TextComponent msg = new TextComponent(level,
                rank,
                name,
                Utils.formatTC(" &8&l► " + (isDefault ? "&7" : white_msg) + finalMessage));
        
        Bukkit.getServer().getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            for ( Player player : Bukkit.getOnlinePlayers() ){
                player.spigot().sendMessage(msg);
            }
        });
        
        Main.getInstance().getLogs().createLog(LogType.CHAT, Utils.getServer(), finalMessage, p.getName());
        
        
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBreakBlocks(BlockBreakEvent e){
        e.setCancelled(true);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPlaceBlocks(BlockPlaceEvent e){
        e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent e){
        e.setCancelled(true);
        
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent e){
        e.setCancelled(true);
        
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerBedEnterEvent(PlayerBedEnterEvent e){
        e.setCancelled(true);
        
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerBucketFillEvent(PlayerBucketFillEvent e){
        e.setCancelled(true);
        
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent e){
        e.setCancelled(true);
        
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItemEvent(PlayerDropItemEvent e){
        e.setCancelled(true);
        
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent e){
        e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerPortalEvent(PlayerPortalEvent e){
        e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemDamageEvent(PlayerItemDamageEvent e){
        e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerShearEntityEvent(PlayerShearEntityEvent e){
        e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerFishEvent(PlayerFishEvent e){
        e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onExplosionPrimeEvent(ExplosionPrimeEvent e){
        e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onEntityExplodeEvent(EntityExplodeEvent e){
        e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onEntityTargetLivingEntityEvent(EntityTargetLivingEntityEvent e){
        e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e){
        e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByBlockEvent(EntityDamageByBlockEvent e){
        e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onEntityBreakDoorEvent(EntityBreakDoorEvent e){
        e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onEntityCreatePortalEvent(EntityCreatePortalEvent e){
        e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onEntitySpawnEvent(EntitySpawnEvent e){
        if (!(e.getEntity() instanceof Player))
            e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerRespawnEvent(PlayerRespawnEvent e){
        Items.setLobbyItems(e.getPlayer());
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerRespawnEvent(PlayerMoveEvent e){
        if (e.getPlayer().getLocation().getY() < 10){
            e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation());
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageEvent e){
        if (e.getEntityType().equals(EntityType.PLAYER)){
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerFoodLevelChange(FoodLevelChangeEvent e){
        e.setCancelled(true);
    }
    
    
    @EventHandler
    public void blockRedstoneEvent(BlockRedstoneEvent e){
        e.getBlock().setType(XMaterial.VOID_AIR.parseMaterial());
    }
    
    @EventHandler
    public void onTNTExplode(EntityExplodeEvent e){
        e.setCancelled(true);
    }
    
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent e){
        if (!Settings.DEBUG){
            if (e.getTo().getBlockY() <= -80){
                e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation());
            }
        }
    }
    
    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent e){
        e.setCancelled(true);
    }
    
    @EventHandler
    public void onWeatherChangeEvent(WeatherChangeEvent e){
        e.setCancelled(true);
    }
    
}
