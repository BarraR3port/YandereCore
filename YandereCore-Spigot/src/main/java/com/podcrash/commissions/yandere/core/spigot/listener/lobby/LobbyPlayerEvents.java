package com.podcrash.commissions.yandere.core.spigot.listener.lobby;

import com.cryptomorin.xseries.XMaterial;
import com.podcrash.commissions.yandere.core.common.data.cooldown.CoolDown;
import com.podcrash.commissions.yandere.core.common.data.cooldown.CoolDownType;
import com.podcrash.commissions.yandere.core.common.data.lobby.PlayerVisibility;
import com.podcrash.commissions.yandere.core.common.data.loc.Loc;
import com.podcrash.commissions.yandere.core.common.data.logs.LogType;
import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.common.data.user.props.Rank;
import com.podcrash.commissions.yandere.core.common.error.UserNotFoundException;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.cooldowns.LobbyCoolDown;
import com.podcrash.commissions.yandere.core.spigot.items.Items;
import com.podcrash.commissions.yandere.core.spigot.listener.MainEvents;
import com.podcrash.commissions.yandere.core.spigot.menu.lobby.LobbyMenu;
import com.podcrash.commissions.yandere.core.spigot.menu.lobby.MultiLobbyMenu;
import com.podcrash.commissions.yandere.core.spigot.settings.Settings;
import net.lymarket.lyapi.spigot.LyApi;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import net.lymarket.lyapi.spigot.utils.Utils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
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
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.UUID;

public abstract class LobbyPlayerEvents extends MainEvents {
    
    public static LinkedList<UUID> builders = new LinkedList<>();
    
    public LobbyPlayerEvents(){
    }
    
    public void subPlayerQuitEvent(PlayerQuitEvent e){
        
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent e){
        try {
            final World world = e.getTo().getWorld();
            final UUID playerUUID = e.getPlayer().getUniqueId();
            final User user = Main.getInstance().getPlayers().getLocalStoredPlayer(playerUUID);
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
    
        User user = Main.getInstance().getPlayers().getLocalStoredPlayer(p.getUniqueId());
        if (user.getRank() != Rank.USUARIO){
            p.setAllowFlight(true);
        }
        PlayerVisibility visibility = user.getPlayerVisibility();
        for ( Player targetPlayer : Bukkit.getOnlinePlayers() ){
            if (targetPlayer.getUniqueId().equals(p.getUniqueId())) continue;
            User targetUser = Main.getInstance().getPlayers().getLocalStoredPlayer(targetPlayer.getUniqueId());
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
    public boolean subPlayerChatEvent(AsyncPlayerChatEvent event){
        Player p = event.getPlayer();
        String message = event.getMessage();
        
        User user = Main.getInstance().getPlayers().getLocalStoredPlayer(p.getUniqueId());
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
                        "&7► Nivel: &c" + user.getLevel().getLevelName(),
                        "&7► XP: &c" + user.getLevel().getFormattedCurrentXp(),
                        "&7► XP Necesario: &c" + user.getLevel().getFormattedRequiredXp(),
                        user.getLevel().getProgressBar()));
        TextComponent name = Utils.hoverOverMessage(white_msg + p.getName(),
                Arrays.asList(
                        "&7「&eInformación del jugador&7⏌",
                        "",
                        "&7► Rango: " + prefix,
                        "&7► Monedas: &c" + user.getCoinsFormatted(),
                        "&7► Nivel: &c" + user.getLevel().getLevelName(),
                        user.getLevel().getProgressBar()/* ,
                            "&7Clan: &c" + clanTag*/));
        TextComponent rank = Utils.hoverOverMessageURL(isDefault ? " " : prefix,
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
    
        Main.getInstance().getLogs().createLog(LogType.CHAT, Settings.SERVER_NAME, finalMessage, p.getName());
    
        return true;
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
        Player p = e.getPlayer();
        if (nbtItem.hasTag("lobby-item")){
            new LobbyMenu(LyApi.getPlayerMenuUtility(e.getPlayer())).open();
        } else if (nbtItem.hasTag("lobby-multi-lobby")){
            new MultiLobbyMenu(LyApi.getPlayerMenuUtility(e.getPlayer())).open();
        } else if (nbtItem.hasTag("command")){
            String command = nbtItem.getTag("command").replace("_", " ");
            e.getPlayer().performCommand(command);
        } else if (nbtItem.hasTag("lobby-player-visibility")){
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
        } else if (nbtItem.hasTag("lobby-player-join-arena")){
            e.setCancelled(subPlayerClicks(e, nbtItem));
        }
    }
    
    public abstract boolean subPlayerClicks(PlayerInteractEvent e, NBTItem item);
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBreakBlocks(BlockBreakEvent e){
        if (builders.contains(e.getPlayer().getUniqueId())) return;
        e.setCancelled(true);
        
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPlaceBlocks(BlockPlaceEvent e){
        if (builders.contains(e.getPlayer().getUniqueId())) return;
        e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent e){
        if (builders.contains(e.getPlayer().getUniqueId())) return;
        e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent e){
        if (builders.contains(e.getPlayer().getUniqueId())) return;
        e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerBedEnterEvent(PlayerBedEnterEvent e){
        if (builders.contains(e.getPlayer().getUniqueId())) return;
        e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerBucketFillEvent(PlayerBucketFillEvent e){
        if (builders.contains(e.getPlayer().getUniqueId())) return;
        e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent e){
        if (builders.contains(e.getPlayer().getUniqueId())) return;
        e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItemEvent(PlayerDropItemEvent e){
        if (builders.contains(e.getPlayer().getUniqueId())) return;
        e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent e){
        if (builders.contains(e.getPlayer().getUniqueId())) return;
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
        if (e.getEntity() instanceof Player) return;
        if (e.getEntity() instanceof ArmorStand) return;
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
        if (e.getEntity() instanceof Player) return;
        if (e.getEntity() instanceof ArmorStand) return;
        e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerRespawnEvent(PlayerRespawnEvent e){
        Items.setLobbyItems(e.getPlayer());
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerRespawnEvent(PlayerMoveEvent e){
        if (e.getPlayer().getLocation().getY() < -4){
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
    
    
    @EventHandler(ignoreCancelled = true)
    public void onWeatherChangeEvent(WeatherChangeEvent e){
        if (e.toWeatherState()){
            e.setCancelled(true);
        }
    }
    
}
