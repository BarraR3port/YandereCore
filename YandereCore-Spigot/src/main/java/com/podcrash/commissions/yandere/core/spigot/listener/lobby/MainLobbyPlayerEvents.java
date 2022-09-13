package com.podcrash.commissions.yandere.core.spigot.listener.lobby;

import com.podcrash.commissions.yandere.core.common.data.lobby.PlayerVisibility;
import com.podcrash.commissions.yandere.core.common.data.logs.LogType;
import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.common.data.user.props.Rank;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.items.Items;
import com.podcrash.commissions.yandere.core.spigot.settings.Settings;
import net.lymarket.lyapi.spigot.utils.Utils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Arrays;

public class MainLobbyPlayerEvents extends LobbyPlayerEvents {
    
    public MainLobbyPlayerEvents(){
        super();
    }
    
    @Override
    public boolean subPlayerChatEvent(AsyncPlayerChatEvent event){
        Player p = event.getPlayer();
        String message = event.getMessage();
        
        User user = Main.getInstance().getPlayers().getCachedPlayer(p.getUniqueId());
        boolean color = p.hasPermission("yandere.chat.color");
        
        event.setCancelled(true);
        
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
                Utils.formatTC(" &8&l► " + (isDefault ? "&7" : white_msg)), (color ? Utils.formatTC(message) : Utils.stripColorsToTextComponent(message)));
        
        Bukkit.getServer().getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            for ( Player player : Bukkit.getOnlinePlayers() ){
                player.spigot().sendMessage(msg);
            }
        });
        
        Main.getInstance().getLogs().createLog(LogType.CHAT, Settings.PROXY_SERVER_NAME, message, p.getName());
        
        return true;
    }
    
    @Override
    public boolean subPlayerClicks(PlayerInteractEvent e){
        return false;
    }
    
    public void subPlayerJoinEvent(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        try {
            p.teleport(Settings.SPAWN_LOCATION);
        } catch(NullPointerException | IllegalArgumentException ex) {
            p.teleport(p.getWorld().getSpawnLocation());
        }
        Items.setLobbyItems(p);
        User user = Main.getInstance().getPlayers().getCachedPlayer(p.getUniqueId());
        boolean hasRank = user.getRank() != Rank.USUARIO;
        String joinMsg = "";
        if(hasRank){
            p.setAllowFlight(true);
            joinMsg = " &8&l»" + user.getRank().getTabPrefix() + p.getName() + " &fse ha unido al servidor!";
        }
        PlayerVisibility visibility = user.getPlayerVisibility();
        for ( Player targetPlayer : Bukkit.getOnlinePlayers() ){
            if(targetPlayer.getUniqueId().equals(p.getUniqueId())){
                p.sendMessage(Utils.format(joinMsg));
                continue;
            }
            User targetUser = Main.getInstance().getPlayers().getCachedPlayer(targetPlayer.getUniqueId());
            if(hasRank && targetUser.getOption("announcements-join-others")){
                targetPlayer.sendMessage(Utils.format(joinMsg));
            }
            PlayerVisibility targetVisibility = targetUser.getPlayerVisibility();
            Rank targetRank = targetUser.getRank();
            switch(visibility) {
                case ALL:
                    p.showPlayer(targetPlayer);
                    break;
                case RANKS:
                    if(targetRank != Rank.USUARIO){
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
}

