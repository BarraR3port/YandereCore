package com.podcrash.commissions.yandere.core.spigot.listener.bedwars.lobby;

import com.andrei1058.bedwars.proxy.BedWarsProxy;
import com.andrei1058.bedwars.proxy.arenamanager.ArenaManager;
import com.podcrash.commissions.yandere.core.common.data.cooldown.CoolDown;
import com.podcrash.commissions.yandere.core.common.data.cooldown.CoolDownType;
import com.podcrash.commissions.yandere.core.common.data.lobby.JoinBedWarsArenaType;
import com.podcrash.commissions.yandere.core.common.data.lobby.PlayerVisibility;
import com.podcrash.commissions.yandere.core.common.data.logs.LogType;
import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.common.data.user.props.Rank;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.cooldowns.LobbyCoolDown;
import com.podcrash.commissions.yandere.core.spigot.items.Items;
import com.podcrash.commissions.yandere.core.spigot.listener.lobby.LobbyPlayerEvents;
import com.podcrash.commissions.yandere.core.spigot.settings.Settings;
import net.lymarket.lyapi.spigot.utils.Utils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;
import java.util.Collections;

public final class LBWPlayerEvents extends LobbyPlayerEvents {
    
    public LBWPlayerEvents(){
    }
    
    public void subPlayerQuitEvent(PlayerQuitEvent e){
        
    }
    
    @Override
    public void subPlayerJoinEvent(PlayerJoinEvent e){
        Player p = e.getPlayer();
        try {
            p.teleport(Settings.SPAWN_LOCATION);
        } catch (NullPointerException | IllegalArgumentException ex) {
            p.teleport(p.getWorld().getSpawnLocation());
        }
        Items.setBedWarsLobbyItems(p);
        
        User user = Main.getInstance().getPlayers().getCachedPlayer(p.getUniqueId());
        boolean hasRank = user.getRank() != Rank.USUARIO;
        
        if (hasRank){
            p.setAllowFlight(true);
        }
        PlayerVisibility visibility = user.getPlayerVisibility();
        String joinMsg = hasRank ? Utils.format(" &8&l»" + user.getRank().getTabPrefix() + p.getName() + " &fse ha unido al servidor!") : "";
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            for ( Player targetPlayer : Bukkit.getOnlinePlayers() ){
                if (targetPlayer.getUniqueId().equals(p.getUniqueId())){
                    if (!joinMsg.equals("")){
                        targetPlayer.sendMessage(joinMsg);
                    }
                    continue;
                }
                User targetUser = Main.getInstance().getPlayers().getCachedPlayer(targetPlayer.getUniqueId());
                if (!joinMsg.equals("") && targetUser.getOptionOrDefault("announcements-join-others", true)){
                    targetPlayer.sendMessage(joinMsg);
                }
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
                        user.getLevel().getProgressBarFormatted()/* ,
                            "&7Clan: &c" + clanTag*/));
        TextComponent rank = Utils.hoverOverMessageURL(isDefault ? " " : prefix,
                Arrays.asList("&7「&eYandere &cRangos&7⏌",
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
        
        
        for ( Player player : Bukkit.getOnlinePlayers() ){
            player.spigot().sendMessage(msg);
        }
        
        Main.getInstance().getLogs().createLog(LogType.CHAT, Settings.PROXY_SERVER_NAME, message, p.getName());
        
        return true;
    }
    
    @Override
    public boolean subPlayerClicks(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if (Main.getInstance().getCoolDownManager().hasCoolDown(p.getUniqueId(), CoolDownType.ITEM_USE)){
            CoolDown coolDown = Main.getInstance().getCoolDownManager().getCoolDown(p.getUniqueId(), CoolDownType.ITEM_USE);
            p.spigot().sendMessage(Utils.hoverOverMessage(coolDown.getMessage(), Collections.singletonList("&7Tiempo restante: &e" + coolDown.getRemainingTime() + "s")));
            p.updateInventory();
            return true;
        }
        Main.getInstance().getCoolDownManager().removeCoolDown(p.getUniqueId(), CoolDownType.ITEM_USE);
        Action action = e.getAction();
        User user = Main.getInstance().getPlayers().getCachedPlayer(p.getUniqueId());
        JoinBedWarsArenaType currentJoinArenaType = user.getJoinBedWarsArenaType();
        if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)){
            if (currentJoinArenaType.equals(JoinBedWarsArenaType.RANDOM)){
                if (!ArenaManager.getInstance().joinRandomArena(p)){
                    p.sendMessage(Utils.format("&cNo se ha encontrado una partida de BedWars en modo &eAleatorio."));
                    p.teleport(Settings.SPAWN_LOCATION);
                }
            } else {
                if (!ArenaManager.getInstance().joinRandomFromGroup(p, currentJoinArenaType.getFormattedName())){
                    p.sendMessage(Utils.format("&cNo se ha encontrado una partida de BedWars en modo &e" + currentJoinArenaType.getFormattedName() + "."));
                    p.teleport(Settings.SPAWN_LOCATION);
                }
            }
            p.updateInventory();
            return true;
        }
    
        boolean next = !p.isSneaking();
    
        if (next){
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
        } else {
            user.prevJoinArenaType(Settings.SERVER_TYPE);
            Main.getInstance().getPlayers().savePlayer(user);
            switch(currentJoinArenaType){
                case SOLO:
                    p.getInventory().setItem(Items.LOBBY_JOIN_ARENA_BEDWARS_RANDOM.getSlot(), Items.LOBBY_JOIN_ARENA_BEDWARS_RANDOM.getItem());
                    break;
                case DUO:
                    p.getInventory().setItem(Items.LOBBY_JOIN_ARENA_BEDWARS_SOLO.getSlot(), Items.LOBBY_JOIN_ARENA_BEDWARS_SOLO.getItem());
                    break;
                case _3V3V3V3:
                    p.getInventory().setItem(Items.LOBBY_JOIN_ARENA_BEDWARS_DUO.getSlot(), Items.LOBBY_JOIN_ARENA_BEDWARS_DUO.getItem());
                    break;
                case _4V4V4V4:
                    p.getInventory().setItem(Items.LOBBY_JOIN_ARENA_BEDWARS_3V3V3V3.getSlot(), Items.LOBBY_JOIN_ARENA_BEDWARS_3V3V3V3.getItem());
                    break;
                default:
                    p.getInventory().setItem(Items.LOBBY_JOIN_ARENA_BEDWARS_4V4V4V4.getSlot(), Items.LOBBY_JOIN_ARENA_BEDWARS_4V4V4V4.getItem());
                    break;
            }
        }
    
        Main.getInstance().getCoolDownManager().addCoolDown(new LobbyCoolDown(p.getUniqueId(), 1));
        p.updateInventory();
        return true;
    }
    
    
}
