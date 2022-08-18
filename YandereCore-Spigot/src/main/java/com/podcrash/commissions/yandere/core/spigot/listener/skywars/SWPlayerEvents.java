package com.podcrash.commissions.yandere.core.spigot.listener.skywars;

import com.cryptomorin.xseries.XMaterial;
import com.podcrash.commissions.yandere.core.common.data.lobby.PlayerVisibility;
import com.podcrash.commissions.yandere.core.common.data.logs.LogType;
import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.common.data.user.props.Rank;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.items.Items;
import com.podcrash.commissions.yandere.core.spigot.listener.MainEvents;
import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.enums.State;
import io.github.Leonardo0013YT.UltraSkyWars.game.UltraGame;
import io.github.Leonardo0013YT.UltraSkyWars.game.UltraRankedGame;
import io.github.Leonardo0013YT.UltraSkyWars.game.UltraTeamGame;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.objects.Level;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.team.Team;
import io.github.Leonardo0013YT.ranks.ranks.EloRank;
import net.lymarket.lyapi.spigot.utils.Utils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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

public final class SWPlayerEvents extends MainEvents {
    
    private final UltraSkyWars plugin;
    
    public SWPlayerEvents(){
        this.plugin = UltraSkyWars.get();
    }
    
    public void subPlayerQuitEvent(PlayerQuitEvent e){
        
    }
    
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void subPlayerChatEvent(AsyncPlayerChatEvent event){
        Player p = event.getPlayer();
        String message = event.getMessage();
        User user = Main.getInstance().getPlayers().getPlayer(p.getUniqueId());
        if (p.hasPermission("yandere.chat.color")){
            message = Utils.format(message);
        }
        
        event.setCancelled(true);
        event.getRecipients().clear();
        String finalMessage = message;
        boolean isDefault = user.getRank() == Rank.USUARIO;
        
        final String prefix = user.getRank().getTabPrefix();
        final String white_msg = p.hasPermission("yandere.chat.whitemessage") ? "&f" : "&7";
        
        finalMessage = finalMessage.replaceAll("%", "%%");
        final SWPlayer sw = this.plugin.getDb().getSWPlayer(p);
        final Level playerLevel = this.plugin.getLvl().getLevel(p);
        TextComponent level = sw.isShowLevel() ? Utils.hoverOverMessage("&e「" + playerLevel.getPrefix() + "&e⏌ ",
                Arrays.asList(
                        "&7「&eNivel de SkyWars&7⏌",
                        "",
                        "&7Nivel: &c" + playerLevel.getLevel(),
                        "&7XP: &c" + playerLevel.getXp(),
                        "&7XP Necesario: &c" + playerLevel.getLevelUp())) : Utils.formatTC("");
        TextComponent name = Utils.hoverOverMessage(white_msg + p.getName() + " ",
                Arrays.asList(
                        "&7「&eInformación del jugador&7⏌",
                        "",
                        "&7► Rango: " + prefix,
                        "&7► Monedas: &c" + user.getCoinsFormatted(),
                        "&7► Nivel: &c" + user.getLevel().getLevelName(),
                        "" + user.getLevel().getProgressBar()/* ,
                            "&7Clan: &c" + clanTag*/));
        TextComponent rank = Utils.hoverOverMessageURL(isDefault ? "" : prefix,
                Arrays.asList(
                        "&7「&eYandere &5Rangos&7⏌",
                        "",
                        "&7► Este jugador tiene el rango " + prefix,
                        "&7► Puedes comprar más rangos en nuestra página web.",
                        "&7► Rangos disponibles: " + Rank.BRONCE.getTabPrefix() + " " + Rank.HIERRO.getTabPrefix() + " " + Rank.ORO.getTabPrefix() + " " + Rank.DIAMANTE.getTabPrefix(),
                        ""),
                "https://store.yanderecraft.com");
        
        Game game = this.plugin.getGm().getGameByPlayer(p);
        TextComponent msg = new TextComponent();
        if (game == null){
            if (this.plugin.getCm().isMainLobby()){
                World w = p.getLocation().getWorld();
                if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                    event.getRecipients().addAll(w.getPlayers());
                    msg = new TextComponent(level, rank, name, Utils.formatTC(" &8&l► " + (isDefault ? "&7" : white_msg) + finalMessage));
                }
            }
        } else {
            if (game.getSpectators().contains(p)){
                event.getRecipients().addAll(game.getSpectators());
                msg = new TextComponent(Utils.formatTC("&7「ESPECTADOR⏌ "), level, rank, name, Utils.formatTC(" &8&l► " + (isDefault ? "&7" : white_msg) + finalMessage));
            } else if (this.plugin.getCm().isChatSystem()){
                if (!game.isState(State.WAITING) && !game.isState(State.STARTING)){
                    if (game.isState(State.PREGAME) || game.isState(State.GAME) || game.isState(State.FINISH) || game.isState(State.RESTARTING)){
                        if (game instanceof UltraGame){
                            event.getRecipients().addAll(game.getCached());
                            msg = new TextComponent(level, rank, name, Utils.formatTC(" &8&l► " + (isDefault ? "&7" : white_msg) + finalMessage));
                            
                        } else if (game instanceof UltraTeamGame){
                            Team team = game.getTeamPlayer(p);
                            TextComponent teamString = Utils.formatTC("");
                            if (team == null) return;
                            String teamName = this.plugin.getLang().get(p, "team").replaceAll("<#>", String.valueOf(team.getId() + 1));
                            teamString = Utils.hoverOverMessage(teamName,
                                    Arrays.asList(
                                            "&7「&eInformación del Equipo&7⏌",
                                            "",
                                            "&7► Equipo: " + teamName,
                                            "&7► Miembros: &c" + team.getTeamSize(),
                                            "&7► Kills: &c" + team.getKills()));
                            if (message.startsWith("!")){
                                event.getRecipients().addAll(game.getCached());
                                msg = new TextComponent(Utils.formatTC("&7「GLOBAL⏌ "), level, rank, teamString, name, Utils.formatTC(" &8&l► " + (isDefault ? "&7" : white_msg) + finalMessage));
                                
                            } else {
                                event.getRecipients().addAll(team.getMembers());
                                msg = new TextComponent(level, rank, teamString, name, Utils.formatTC(" &8&l► " + (isDefault ? "&7" : white_msg) + finalMessage));
                                
                            }
                        } else {
                            event.getRecipients().addAll(game.getCached());
                            EloRank eloRank = this.plugin.getIjm().getEloRank().getErm().getEloRank(p);
                            TextComponent elo = Utils.hoverOverMessage(eloRank.getColor() + eloRank.getCorto(),
                                    Arrays.asList(
                                            "&7「&eInformación de Rankeds&7⏌",
                                            "",
                                            "&7► Elo: " + eloRank.getColor() + eloRank.getName(),
                                            "&7► Máximo Elo: " + eloRank.getMax(),
                                            "&7► Mínimo Elo: &c" + eloRank.getMin()));
                            msg = new TextComponent(level, rank, elo, name, Utils.formatTC(" &8&l► " + (isDefault ? "&7" : white_msg) + finalMessage));
                        }
                    }
                    
                } else {
                    event.getRecipients().addAll(game.getCached());
                    
                    if (game instanceof UltraRankedGame){
                        EloRank eloRank = this.plugin.getIjm().getEloRank().getErm().getEloRank(p);
                        TextComponent elo = Utils.hoverOverMessage(eloRank.getColor() + eloRank.getCorto(),
                                Arrays.asList(
                                        "&7「&eInformación de Rankeds&7⏌",
                                        "",
                                        "&7► Elo: " + eloRank.getColor() + eloRank.getName(),
                                        "&7► Máximo Elo: " + eloRank.getMax(),
                                        "&7► Mínimo Elo: &c" + eloRank.getMin()));
                        msg = new TextComponent(Utils.formatTC("&7「ESPECTADOR⏌ "), level, rank, elo, name, Utils.formatTC(" &8&l► " + (isDefault ? "&7" : white_msg) + finalMessage));
                    } else {
                        msg = new TextComponent(level, rank, name, Utils.formatTC(" &8&l► " + (isDefault ? "&7" : white_msg) + finalMessage));
                    }
                }
            }
        }
        
        final TextComponent finalText = msg;
        Bukkit.getServer().getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            for ( Player player : event.getRecipients() ){
                player.spigot().sendMessage(finalText);
            }
        });
        Main.getInstance().getLogs().createLog(LogType.CHAT, Utils.getServer(), finalMessage, p.getName());
    }
    
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent e){
        Player p = e.getPlayer();
        if (this.plugin.getCm().isMainLobby()){
            World w = p.getLocation().getWorld();
            if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                p.setFoodLevel(20);
                p.setFireTicks(0);
                p.setExp(0);
                p.setLevel(0);
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
                            if (targetRank != Rank.USUARIO){
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
        }
    }
    
    public void subPlayerJoinEvent(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if (this.plugin.getCm().isMainLobby()){
            World w = p.getLocation().getWorld();
            if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                p.setFoodLevel(20);
                p.setFireTicks(0);
                p.setExp(0);
                p.setLevel(0);
                Items.setLobbyItems(p);
                p.setHealth(20);
                p.setSaturation(20F);
                p.setGameMode(GameMode.ADVENTURE);
    
                User user = Main.getInstance().getPlayers().getLocalStoredPlayer(p.getUniqueId());
                if (user.getRank() != Rank.USUARIO){
                    p.setAllowFlight(true);
                    
                }
                
                PlayerVisibility visibility = user.getPlayerVisibility();
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
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
        
                }, 1L);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBreakBlocks(BlockBreakEvent e){
        Player p = e.getPlayer();
        if (this.plugin.getCm().isMainLobby()){
            World w = p.getLocation().getWorld();
            if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPlaceBlocks(BlockPlaceEvent e){
        Player p = e.getPlayer();
        if (this.plugin.getCm().isMainLobby()){
            World w = p.getLocation().getWorld();
            if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerBedEnterEvent(PlayerBedEnterEvent e){
        Player p = e.getPlayer();
        if (this.plugin.getCm().isMainLobby()){
            World w = p.getLocation().getWorld();
            if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                e.setCancelled(true);
            }
        }
        
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerBucketFillEvent(PlayerBucketFillEvent e){
        Player p = e.getPlayer();
        if (this.plugin.getCm().isMainLobby()){
            World w = p.getLocation().getWorld();
            if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent e){
        Player p = e.getPlayer();
        if (this.plugin.getCm().isMainLobby()){
            World w = p.getLocation().getWorld();
            if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                e.setCancelled(true);
            }
        }
        
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItemEvent(PlayerDropItemEvent e){
        Player p = e.getPlayer();
        if (this.plugin.getCm().isMainLobby()){
            World w = p.getLocation().getWorld();
            if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                e.setCancelled(true);
            }
        }
        
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent e){
        Player p = e.getPlayer();
        if (this.plugin.getCm().isMainLobby()){
            World w = p.getLocation().getWorld();
            if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerPortalEvent(PlayerPortalEvent e){
        Player p = e.getPlayer();
        if (this.plugin.getCm().isMainLobby()){
            World w = p.getLocation().getWorld();
            if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemDamageEvent(PlayerItemDamageEvent e){
        Player p = e.getPlayer();
        if (this.plugin.getCm().isMainLobby()){
            World w = p.getLocation().getWorld();
            if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerShearEntityEvent(PlayerShearEntityEvent e){
        Player p = e.getPlayer();
        if (this.plugin.getCm().isMainLobby()){
            World w = p.getLocation().getWorld();
            if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerFishEvent(PlayerFishEvent e){
        Player p = e.getPlayer();
        if (this.plugin.getCm().isMainLobby()){
            World w = p.getLocation().getWorld();
            if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onExplosionPrimeEvent(ExplosionPrimeEvent e){
        if (this.plugin.getCm().isMainLobby()){
            World w = e.getEntity().getWorld();
            if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onEntityExplodeEvent(EntityExplodeEvent e){
        if (this.plugin.getCm().isMainLobby()){
            World w = e.getLocation().getWorld();
            if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onEntityTargetLivingEntityEvent(EntityTargetLivingEntityEvent e){
        if (this.plugin.getCm().isMainLobby()){
            World w = e.getEntity().getWorld();
            if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e){
        if (this.plugin.getCm().isMainLobby()){
            World w = e.getDamager().getWorld();
            if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByBlockEvent(EntityDamageByBlockEvent e){
        if (this.plugin.getCm().isMainLobby()){
            World w = e.getDamager().getWorld();
            if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onEntityBreakDoorEvent(EntityBreakDoorEvent e){
        if (this.plugin.getCm().isMainLobby()){
            World w = e.getEntity().getWorld();
            if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onEntityCreatePortalEvent(EntityCreatePortalEvent e){
        if (this.plugin.getCm().isMainLobby()){
            World w = e.getEntity().getWorld();
            if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onEntitySpawnEvent(EntitySpawnEvent e){
        if (!(e.getEntity() instanceof Player)){
            if (this.plugin.getCm().isMainLobby()){
                World w = e.getEntity().getWorld();
                if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                    e.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerRespawnEvent(PlayerRespawnEvent e){
        Player p = e.getPlayer();
        if (this.plugin.getCm().isMainLobby()){
            World w = p.getWorld();
            if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                Items.setLobbyItems(e.getPlayer());
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageEvent e){
        if (e.getEntityType().equals(EntityType.PLAYER)){
            if (this.plugin.getCm().isMainLobby()){
                World w = e.getEntity().getWorld();
                if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                    e.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerFoodLevelChange(FoodLevelChangeEvent e){
        if (e.getEntityType().equals(EntityType.PLAYER)){
            if (this.plugin.getCm().isMainLobby()){
                World w = e.getEntity().getWorld();
                if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                    e.setCancelled(true);
                }
            }
        }
    }
    
    
    @EventHandler
    public void blockRedstoneEvent(BlockRedstoneEvent e){
        e.getBlock().setType(XMaterial.VOID_AIR.parseMaterial());
    }
    
    @EventHandler
    public void onTNTExplode(EntityExplodeEvent e){
        if (e.getEntityType().equals(EntityType.PLAYER)){
            if (this.plugin.getCm().isMainLobby()){
                World w = e.getEntity().getWorld();
                if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                    e.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent e){
        if (this.plugin.getCm().isMainLobby()){
            World w = e.getPlayer().getWorld();
            if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                e.setCancelled(true);
            }
        }
        
    }
    
    @EventHandler
    public void onWeatherChangeEvent(WeatherChangeEvent e){
        if (this.plugin.getCm().isMainLobby()){
            World w = e.getWorld();
            if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                e.setCancelled(true);
            }
        }
        
    }
    
}
