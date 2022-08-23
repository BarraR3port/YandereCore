package com.podcrash.commissions.yandere.core.spigot.listener.bedwars.lobby;

import com.andrei1058.bedwars.proxy.BedWarsProxy;
import com.andrei1058.bedwars.proxy.arenamanager.ArenaManager;
import com.cryptomorin.xseries.XMaterial;
import com.podcrash.commissions.yandere.core.common.data.cooldown.CoolDown;
import com.podcrash.commissions.yandere.core.common.data.cooldown.CoolDownType;
import com.podcrash.commissions.yandere.core.common.data.lobby.JoinBedWarsArenaType;
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
import com.podcrash.commissions.yandere.core.spigot.settings.Settings;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import net.lymarket.lyapi.spigot.utils.Utils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
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

/*import static com.andrei1058.bedwars.BedWars.getLevelSupport;*/

public final class LBWPlayerEvents extends MainEvents {
    
    public static LinkedList<UUID> builders = new LinkedList<>();
    
    public LBWPlayerEvents(){
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
        
        TextComponent level = Utils.hoverOverMessage(BedWarsProxy.getLevelManager().getLevel(p),
                Arrays.asList(
                        "&7「&eNivel de BedWars&7⏌",
                        "",
                        "&7► Nivel: &c" + BedWarsProxy.getLevelManager().getPlayerLevel(p),
                        "&7► XP: &c" + BedWarsProxy.getLevelManager().getCurrentXpFormatted(p),
                        "&7► XP Necesaria para subir: &c" + BedWarsProxy.getLevelManager().getRequiredXpFormatted(p),
                        BedWarsProxy.getLevelManager().getProgressBar(p)));
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
        if (NBTItem.hasTag(item, "lobby-player-join-arena")){
            Player p = e.getPlayer();
            if (Main.getInstance().getCoolDownManager().hasCoolDown(p.getUniqueId(), CoolDownType.ITEM_USE)){
                CoolDown coolDown = Main.getInstance().getCoolDownManager().getCoolDown(p.getUniqueId(), CoolDownType.ITEM_USE);
                p.sendMessage(Utils.format(coolDown.getMessage()));
                e.setCancelled(true);
                return;
            }
            Main.getInstance().getCoolDownManager().removeCoolDown(p.getUniqueId(), CoolDownType.ITEM_USE);
            Action action = e.getAction();
            User user = Main.getInstance().getPlayers().getLocalStoredPlayer(p.getUniqueId());
            JoinBedWarsArenaType currentJoinArenaType = user.getJoinBedWarsArenaType();
            if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)){
                if (currentJoinArenaType.equals(JoinBedWarsArenaType.RANDOM)){
                    if (!ArenaManager.getInstance().joinRandomArena(p)){
                        p.sendMessage(Utils.format("&cNo se ha encontrado una partida de BedWars en modo &eAleatorio."));
                        p.teleport(Settings.SPAWN_LOCATION);
                    }
                } else {
                    if (!ArenaManager.getInstance().joinRandomFromGroup(p, currentJoinArenaType.getBwName())){
                        p.sendMessage(Utils.format("&cNo se ha encontrado una partida de BedWars en modo &e" + currentJoinArenaType.getBwName() + "."));
                        p.teleport(Settings.SPAWN_LOCATION);
                    }
                }
                e.setCancelled(true);
                return;
            }
            
            user.nextJoinArenaType(Settings.SERVER_TYPE);
            Main.getInstance().getPlayers().savePlayer(user);
            switch(currentJoinArenaType){
                case SOLO:
                    p.getInventory().setItem(Items.LOBBY_JOIN_ARENA_BEDWARS_DUO.getSlot(), Items.LOBBY_JOIN_ARENA_BEDWARS_DUO.getItem());
                    break;
                case DUO:
                    p.getInventory().setItem(Items.LOBBY_JOIN_ARENA_BEDWARS_3V3V3V3.getSlot(), Items.LOBBY_JOIN_ARENA_BEDWARS_3V3V3V3.getItem());
                    break;
                case _3V3V3V3:
                    p.getInventory().setItem(Items.LOBBY_JOIN_ARENA_BEDWARS_4V4V4V4.getSlot(), Items.LOBBY_JOIN_ARENA_BEDWARS_4V4V4V4.getItem());
                    break;
                case _4V4V4V4:
                    p.getInventory().setItem(Items.LOBBY_JOIN_ARENA_BEDWARS_RANDOM.getSlot(), Items.LOBBY_JOIN_ARENA_BEDWARS_RANDOM.getItem());
                    break;
                default:
                    p.getInventory().setItem(Items.LOBBY_JOIN_ARENA_BEDWARS_SOLO.getSlot(), Items.LOBBY_JOIN_ARENA_BEDWARS_SOLO.getItem());
                    break;
            }
            Main.getInstance().getCoolDownManager().addCoolDown(new LobbyCoolDown(p.getUniqueId(), 3));
            p.updateInventory();
        }
    }
    
    
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
