package com.podcrash.commissions.yandere.core.spigot.listener.practice;

import com.podcrash.commissions.yandere.core.common.data.loc.Loc;
import com.podcrash.commissions.yandere.core.common.data.logs.LogType;
import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.common.data.user.props.Rank;
import com.podcrash.commissions.yandere.core.common.error.UserNotFoundException;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.listener.MainEvents;
import com.podcrash.commissions.yandere.core.spigot.settings.Settings;
import ga.strikepractice.StrikePractice;
import ga.strikepractice.api.StrikePracticeAPI;
import ga.strikepractice.stats.PlayerStats;
import net.lymarket.lyapi.spigot.utils.Utils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Arrays;
import java.util.UUID;

public final class PracticePlayerEvents extends MainEvents {
    
    private final StrikePracticeAPI api;
    
    public PracticePlayerEvents(){
        api = StrikePractice.getAPI();
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
    
    }
    
    
    @Override
    public void subPlayerChatEvent(AsyncPlayerChatEvent event){
        Player p = event.getPlayer();
        String message = event.getMessage();
        
        if (event.isCancelled()) return;
    
        User user = Main.getInstance().getPlayers().getLocalStoredPlayer(p.getUniqueId());
        if (p.hasPermission("yandere.chat.color")){
            message = Utils.format(message);
        }
        event.setCancelled(true);
        
        PlayerStats stats = api.getPlayerStats(p);
        
        final String finalMessage = message;
        
        boolean isDefault = user.getRank() == Rank.USUARIO;
        
        final String prefix = user.getRank().getTabPrefix();
        final String white_msg = p.hasPermission("yandere.chat.whitemessage") ? "&f" : "&7";
        TextComponent level = Utils.hoverOverMessage("&7「" + stats.getGlobalElo() + "⏌",
                Arrays.asList(
                        "&7「&eEstadísticas del Jugador&7⏌",
                        "",
                        "&7► Asesinatos: &c" + stats.getKills(),
                        "&7► Muertes: &c" + stats.getDeaths(),
                        "&7► Elo: &c" + stats.getGlobalElo(),
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
                        "" + user.getLevel().getProgressBar()/* ,
                            "&7Clan: &c" + clanTag*/));
        TextComponent rank = Utils.hoverOverMessageURL(isDefault ? "" : prefix,
                Arrays.asList("&7「&eYandere &5Rangos&7⏌",
                        "",
                        "&7Este jugador tiene el rango " + prefix,
                        "&7Puedes comprar más rangos en nuestra página web.",
                        "&7Rangos disponibles: " + Rank.BRONCE.getTabPrefix() + " " + Rank.HIERRO.getTabPrefix() + " " + Rank.ORO.getTabPrefix() + " " + Rank.DIAMANTE.getTabPrefix(),
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
    
    
}
