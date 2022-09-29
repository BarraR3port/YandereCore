package com.podcrash.commissions.yandere.core.spigot.listener.skywars;

import com.cryptomorin.xseries.XMaterial;
import com.podcrash.commissions.yandere.core.common.data.cooldown.CoolDown;
import com.podcrash.commissions.yandere.core.common.data.cooldown.CoolDownType;
import com.podcrash.commissions.yandere.core.common.data.lobby.JoinSkyWarsArenaType;
import com.podcrash.commissions.yandere.core.common.data.lobby.PlayerVisibility;
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
import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.enums.State;
import io.github.Leonardo0013YT.UltraSkyWars.game.UltraGame;
import io.github.Leonardo0013YT.UltraSkyWars.game.UltraRankedGame;
import io.github.Leonardo0013YT.UltraSkyWars.game.UltraTeamGame;
import io.github.Leonardo0013YT.UltraSkyWars.modules.ranks.ranks.EloRank;
import io.github.Leonardo0013YT.UltraSkyWars.objects.Level;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.team.Team;
import net.lymarket.lyapi.spigot.LyApi;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import net.lymarket.lyapi.spigot.utils.Utils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
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

import java.util.*;

public final class SWPlayerEvents extends MainEvents {
    
    public static LinkedList<UUID> builders = new LinkedList<>();
    private final UltraSkyWars plugin;
    
    public SWPlayerEvents(){
        this.plugin = UltraSkyWars.get();
    }
    
    public void subPlayerQuitEvent(PlayerQuitEvent e){
        
    }
    
    
    public boolean subPlayerChatEvent(AsyncPlayerChatEvent event){
        Player p = event.getPlayer();
        String message = event.getMessage();
        User user = Main.getInstance().getPlayers().getPlayer(p.getUniqueId());
        boolean color = p.hasPermission("yandere.chat.color");
        
        event.setCancelled(true);
        event.getRecipients().clear();
        String finalMessage = message;
        boolean isDefault = user.getRank() == Rank.USUARIO;
        
        final String prefix = user.getRank().getTabPrefix();
        final String white_msg = p.hasPermission("yandere.chat.whitemessage") ? "&f" : "&7";
        
        finalMessage = finalMessage.replaceAll("%", "%%");
        final SWPlayer sw = this.plugin.getDb().getSWPlayer(p);
        final Level playerLevel = this.plugin.getLvl().getLevel(p);
        
        TextComponent level = sw.isShowLevel() ? Utils.hoverOverMessage("&7[" + playerLevel.getPrefix() + "&7] ",
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
                        "" + user.getLevel().getProgressBarFormatted()/* ,
                            "&7Clan: &c" + clanTag*/));
        TextComponent rank = Utils.hoverOverMessageURL(isDefault ? " " : prefix,
                Arrays.asList(
                        "&7「&eYandere &cRangos&7⏌",
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
                    msg = new TextComponent(level, rank, name, Utils.formatTC(" &8&l► " + (isDefault ? "&7" : white_msg)), (color ? Utils.formatTC(finalMessage) : Utils.stripColorsToTextComponent(finalMessage)));
                }
            }
        } else {
            if (game.getSpectators().contains(p)){
                event.getRecipients().addAll(game.getSpectators());
                msg = new TextComponent(Utils.formatTC("&7「ESPECTADOR⏌ "), level, rank, name, Utils.formatTC(" &8&l► " + (isDefault ? "&7" : white_msg)), (color ? Utils.formatTC(finalMessage) : Utils.stripColorsToTextComponent(finalMessage)));
            } else if (this.plugin.getCm().isChatSystem()){
                if (!game.isState(State.WAITING) && !game.isState(State.STARTING)){
                    if (game.isState(State.PREGAME) || game.isState(State.GAME) || game.isState(State.FINISH) || game.isState(State.RESTARTING)){
                        if (game instanceof UltraGame){
                            event.getRecipients().addAll(game.getCached());
                            msg = new TextComponent(level, rank, name, Utils.formatTC(" &8&l► " + (isDefault ? "&7" : white_msg)), (color ? Utils.formatTC(finalMessage) : Utils.stripColorsToTextComponent(finalMessage)));
                            
                        } else if (game instanceof UltraTeamGame){
                            Team team = game.getTeamPlayer(p);
                            TextComponent teamString = Utils.formatTC("");
                            if (team == null) return false;
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
                                msg = new TextComponent(Utils.formatTC("&7「GLOBAL⏌ "), level, rank, teamString, name, Utils.formatTC(" &8&l► " + (isDefault ? "&7" : white_msg)), (color ? Utils.formatTC(finalMessage) : Utils.stripColorsToTextComponent(finalMessage)));
                                
                            } else {
                                event.getRecipients().addAll(team.getMembers());
                                msg = new TextComponent(level, rank, teamString, name, Utils.formatTC(" &8&l► " + (isDefault ? "&7" : white_msg)), (color ? Utils.formatTC(finalMessage) : Utils.stripColorsToTextComponent(finalMessage)));
                                
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
                            msg = new TextComponent(level, rank, elo, name, Utils.formatTC(" &8&l► " + (isDefault ? "&7" : white_msg)), (color ? Utils.formatTC(finalMessage) : Utils.stripColorsToTextComponent(finalMessage)));
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
                        msg = new TextComponent(Utils.formatTC("&7「ESPECTADOR⏌ "), level, rank, elo, name, Utils.formatTC(" &8&l► " + (isDefault ? "&7" : white_msg)), (color ? Utils.formatTC(finalMessage) : Utils.stripColorsToTextComponent(finalMessage)));
                    } else {
                        msg = new TextComponent(level, rank, name, Utils.formatTC(" &8&l► " + (isDefault ? "&7" : white_msg)), (color ? Utils.formatTC(finalMessage) : Utils.stripColorsToTextComponent(finalMessage)));
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
        Main.getInstance().getLogs().createLog(LogType.CHAT, Settings.PROXY_SERVER_NAME, finalMessage, p.getName());
        return true;
    }
    
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTeleport(PlayerTeleportEvent e){
        Player p = e.getPlayer();
        e.setCancelled(false);
        if (p.hasMetadata("NPC")) return;
        try {
            if (this.plugin.getCm().isMainLobby()){
                World w = p.getLocation().getWorld();
                if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                    e.setCancelled(false);
                    Items.setSkyWarsLobbyItems(p);
                    User user = Main.getInstance().getPlayers().getCachedPlayer(p.getUniqueId());
                    if (user.getRank() != Rank.USUARIO){
                        p.setAllowFlight(true);
                    }
                    PlayerVisibility visibility = user.getPlayerVisibility();
    
                    for ( Player targetPlayer : w.getPlayers() ){
                        if (targetPlayer.getUniqueId().equals(p.getUniqueId())) continue;
                        User targetUser = Main.getInstance().getPlayers().getCachedPlayer(targetPlayer.getUniqueId());
                        PlayerVisibility targetVisibility = targetUser.getPlayerVisibility();
                        Rank targetRank = targetUser.getRank();
                        switch(visibility){
                            case ALL:
                                if (targetPlayer.getWorld().equals(p.getWorld())){
                                    p.showPlayer(targetPlayer);
                                }
                                break;
                            case RANKS:
                                if (targetRank != Rank.USUARIO){
                                    if (targetPlayer.getWorld().equals(p.getWorld())){
                                        p.showPlayer(targetPlayer);
                                    }
                                } else {
                                    if (targetPlayer.getWorld().equals(p.getWorld())){
                                        p.hidePlayer(targetPlayer);
                                    }
                                }
                                break;
                            default:
                                if (targetPlayer.getWorld().equals(p.getWorld())){
                                    p.hidePlayer(targetPlayer);
                                }
                                break;
                        }
                        
                        switch(targetVisibility){
                            case ALL:{
                                if (targetPlayer.getWorld().equals(p.getWorld()))
                                    targetPlayer.showPlayer(p);
                                break;
                            }
                            case RANKS:{
                                if (targetRank != Rank.USUARIO){
                                    
                                    if (targetPlayer.getWorld().equals(p.getWorld()))
                                        targetPlayer.showPlayer(p);
                                } else {
                                    if (targetPlayer.getWorld().equals(p.getWorld())){
                                        targetPlayer.hidePlayer(p);
                                    }
                                }
                                break;
                            }
                            default:{
                                if (targetPlayer.getWorld().equals(p.getWorld())){
                                    targetPlayer.hidePlayer(p);
                                }
                                break;
                            }
                        }
                        
                    }
                }
            }
        } catch (UserNotFoundException ignored) {
        }
    }
    
    public void subPlayerJoinEvent(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if (p.hasMetadata("NPC")) return;
        if (this.plugin.getCm().isMainLobby()){
            World w = p.getLocation().getWorld();
            try {
                if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                    Items.setSkyWarsLobbyItems(p);
                    User user = Main.getInstance().getPlayers().getCachedPlayer(p.getUniqueId());
                    boolean hasRank = user.getRank() != Rank.USUARIO;
    
                    if (hasRank){
                        p.setAllowFlight(true);
                    }
                    PlayerVisibility visibility = user.getPlayerVisibility();
                    String joinMsg = hasRank ? Utils.format(" &8&l»" + user.getRank().getTabPrefix() + p.getName() + " &fse ha unido al servidor!") : "";
                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                        for ( Player targetPlayer : w.getPlayers() ){
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
                                    if (targetPlayer.getWorld().equals(p.getWorld())){
                                        p.showPlayer(targetPlayer);
                                    }
                                    break;
                                case RANKS:
                                    if (targetRank != Rank.USUARIO){
                                        if (targetPlayer.getWorld().equals(p.getWorld())){
                                            p.showPlayer(targetPlayer);
                                        }
                                    } else {
                                        if (targetPlayer.getWorld().equals(p.getWorld())){
                                            p.hidePlayer(targetPlayer);
                                        }
                                    }
                                    break;
                                default:
                                    if (targetPlayer.getWorld().equals(p.getWorld())){
                                        p.hidePlayer(targetPlayer);
                                    }
                                    break;
                            }
                            
                            switch(targetVisibility){
                                case ALL:{
                                    if (targetPlayer.getWorld().equals(p.getWorld())){
                                        targetPlayer.showPlayer(p);
                                    }
                                    break;
                                }
                                case RANKS:{
                                    if (user.getRank() != Rank.USUARIO){
                                        if (targetPlayer.getWorld().equals(p.getWorld())){
                                            targetPlayer.showPlayer(p);
                                        }
                                    } else {
                                        if (targetPlayer.getWorld().equals(p.getWorld())){
                                            targetPlayer.hidePlayer(p);
                                        }
                                    }
                                    break;
                                }
                                default:{
                                    if (targetPlayer.getWorld().equals(p.getWorld())){
                                        targetPlayer.hidePlayer(p);
                                    }
                                    break;
                                }
                            }
                            
                        }
                        
                    }, 1L);
                }
            } catch (UserNotFoundException ignored) {
            }
        }
    }
    
    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent e){
        Player p = e.getPlayer();
        for ( Player targetPlayer : Bukkit.getOnlinePlayers() ){
            if (p.equals(targetPlayer)) continue;
            if (p.getWorld().equals(targetPlayer.getWorld())){
                p.showPlayer(targetPlayer);
                targetPlayer.showPlayer(p);
            } else {
                p.hidePlayer(targetPlayer);
                targetPlayer.hidePlayer(p);
            }
        }
        if (this.plugin.getCm().isMainLobby()){
            World w = p.getLocation().getWorld();
            try {
                if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                    Items.setSkyWarsLobbyItems(p);
                    User user = Main.getInstance().getPlayers().getCachedPlayer(p.getUniqueId());
                    if (user.getRank() != Rank.USUARIO){
                        p.setAllowFlight(true);
                    }
                    
                    PlayerVisibility visibility = user.getPlayerVisibility();
                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                        for ( Player targetPlayer : w.getPlayers() ){
                            if (targetPlayer.getUniqueId().equals(p.getUniqueId())) continue;
                            User targetUser = Main.getInstance().getPlayers().getCachedPlayer(targetPlayer.getUniqueId());
                            PlayerVisibility targetVisibility = targetUser.getPlayerVisibility();
                            Rank targetRank = targetUser.getRank();
                            switch(visibility){
                                case ALL:
                                    if (targetPlayer.getWorld().equals(p.getWorld())){
                                        p.showPlayer(targetPlayer);
                                    }
                                    break;
                                case RANKS:
                                    if (targetRank != Rank.USUARIO){
                                        if (targetPlayer.getWorld().equals(p.getWorld())){
                                            p.showPlayer(targetPlayer);
                                        }
                                    } else {
                                        if (targetPlayer.getWorld().equals(p.getWorld())){
                                            p.hidePlayer(targetPlayer);
                                        }
                                    }
                                    break;
                                default:
                                    if (targetPlayer.getWorld().equals(p.getWorld())){
                                        p.hidePlayer(targetPlayer);
                                    }
                                    break;
                            }
                            
                            switch(targetVisibility){
                                case ALL:{
                                    if (targetPlayer.getWorld().equals(p.getWorld())){
                                        targetPlayer.showPlayer(p);
                                    }
                                    break;
                                }
                                case RANKS:{
                                    if (user.getRank() != Rank.USUARIO){
                                        if (targetPlayer.getWorld().equals(p.getWorld())){
                                            targetPlayer.showPlayer(p);
                                        }
                                    } else {
                                        if (targetPlayer.getWorld().equals(p.getWorld())){
                                            targetPlayer.hidePlayer(p);
                                        }
                                    }
                                    break;
                                }
                                default:{
                                    if (targetPlayer.getWorld().equals(p.getWorld())){
                                        targetPlayer.hidePlayer(p);
                                    }
                                    break;
                                }
                            }
                            
                        }
                        
                    }, 1L);
                }
            } catch (UserNotFoundException ignored) {
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public synchronized void onPlayerInteractEvent(PlayerInteractEvent e){
        ItemStack item = e.getItem();
        if (item == null) return;
        if (item.getType() == Material.AIR) return;
        Player p = e.getPlayer();
        if (this.plugin.getCm().isMainLobby()){
            World w = p.getLocation().getWorld();
            if (!w.getName().equals(this.plugin.getCm().getLobbyWorld()))
                return;
            if (e.getAction().equals(Action.PHYSICAL)) return;
            boolean rightClick = e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK;
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
                if (Main.getInstance().getCoolDownManager().hasCoolDown(p.getUniqueId(), CoolDownType.ITEM_USE)){
                    CoolDown coolDown = Main.getInstance().getCoolDownManager().getCoolDown(p.getUniqueId(), CoolDownType.ITEM_USE);
                    p.spigot().sendMessage(Utils.hoverOverMessage(coolDown.getMessage(), Collections.singletonList("&7Tiempo restante: &e" + coolDown.getRemainingTime() + "s")));
                    e.setCancelled(true);
                    return;
                }
                Main.getInstance().getCoolDownManager().removeCoolDown(p.getUniqueId(), CoolDownType.ITEM_USE);
                
                User user = Main.getInstance().getPlayers().getCachedPlayer(p.getUniqueId());
                PlayerVisibility currentPlayerVisibility = user.getPlayerVisibility();
                user.nextPlayerVisibility();
                Main.getInstance().getPlayers().savePlayer(user);
                for ( Player player : w.getPlayers() ){
                    if (player.getUniqueId().equals(p.getUniqueId())) continue;
                    switch(currentPlayerVisibility){
                        case ALL:
                            final User spigotUser = Main.getInstance().getPlayers().getCachedPlayer(player.getUniqueId());
                            if (spigotUser.getRank() == Rank.USUARIO){
                                if (player.getWorld().equals(p.getWorld())){
                                    p.hidePlayer(player);
                                }
                            }
                            break;
                        case RANKS:
                            if (player.getWorld().equals(p.getWorld())){
                                p.hidePlayer(player);
                            }
                            break;
                        case NONE:
                            if (player.getWorld().equals(p.getWorld())){
                                p.showPlayer(player);
                            }
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
                Main.getInstance().getCoolDownManager().addCoolDown(new LobbyCoolDown(p.getUniqueId(), 1));
                p.updateInventory();
            } else if (nbtItem.hasTag("lobby-player-join-arena")){
                if (!rightClick && Main.getInstance().getCoolDownManager().hasCoolDown(p.getUniqueId(), CoolDownType.ITEM_USE)){
                    CoolDown coolDown = Main.getInstance().getCoolDownManager().getCoolDown(p.getUniqueId(), CoolDownType.ITEM_USE);
                    p.spigot().sendMessage(Utils.hoverOverMessage(coolDown.getMessage(), Collections.singletonList("&7Tiempo restante: &e" + coolDown.getRemainingTime() + "s")));
                    e.setCancelled(true);
                    return;
                }
                Main.getInstance().getCoolDownManager().removeCoolDown(p.getUniqueId(), CoolDownType.ITEM_USE);
                
                User user = Main.getInstance().getPlayers().getCachedPlayer(p.getUniqueId());
                JoinSkyWarsArenaType currentJoinArenaType = user.getJoinSkyWarsArenaType();
                
                if (rightClick){
                    if (!this.plugin.getGm().addRandomGame(p, currentJoinArenaType.name())){
                        p.sendMessage(Utils.format("&cNo se ha encontrado una partida de SkyWars en modo &e" + currentJoinArenaType.name().toLowerCase() + "&c."));
                    }
                    e.setCancelled(true);
                    return;
                }
                user.nextJoinArenaType(Settings.SERVER_TYPE);
                Main.getInstance().getPlayers().savePlayer(user);
                switch(currentJoinArenaType){
                    case SOLO:
                        p.getInventory().setItem(Items.LOBBY_JOIN_ARENA_SKYWARS_TEAM.getSlot(), Items.LOBBY_JOIN_ARENA_SKYWARS_TEAM.getItem());
                        break;
                    case TEAM:
                        p.getInventory().setItem(Items.LOBBY_JOIN_ARENA_SKYWARS_RANKED.getSlot(), Items.LOBBY_JOIN_ARENA_SKYWARS_RANKED.getItem());
                        break;
                    case RANKED:
                        p.getInventory().setItem(Items.LOBBY_JOIN_ARENA_SKYWARS_RANDOM.getSlot(), Items.LOBBY_JOIN_ARENA_SKYWARS_RANDOM.getItem());
                        break;
                    default:
                        p.getInventory().setItem(Items.LOBBY_JOIN_ARENA_SKYWARS_SOLO.getSlot(), Items.LOBBY_JOIN_ARENA_SKYWARS_SOLO.getItem());
                        break;
                }
                Main.getInstance().getCoolDownManager().addCoolDown(new LobbyCoolDown(p.getUniqueId(), 3));
                p.updateInventory();
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBreakBlocks(BlockBreakEvent e){
        Player p = e.getPlayer();
        if (this.plugin.getCm().isMainLobby()){
            if (builders.contains(p.getUniqueId())) return;
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
            if (builders.contains(p.getUniqueId())) return;
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
            if (builders.contains(p.getUniqueId())) return;
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
            if (builders.contains(p.getUniqueId())) return;
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
            if (builders.contains(p.getUniqueId())) return;
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
            if (builders.contains(p.getUniqueId())) return;
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
            if (builders.contains(p.getUniqueId())) return;
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
            if (builders.contains(p.getUniqueId())) return;
            World w = p.getLocation().getWorld();
            if (w.getName().equals(this.plugin.getCm().getLobbyWorld()))
                e.setCancelled(true);
            User user = Main.getInstance().getPlayers().getCachedPlayer(p.getUniqueId());
            JoinSkyWarsArenaType currentJoinArenaType = user.getJoinSkyWarsArenaType();
            switch(currentJoinArenaType){
                case SOLO:{
                    LinkedHashMap<Integer, Game> games = new LinkedHashMap<>(this.plugin.getGm().getGames());
                    Game soloGame = games.values().stream().filter(game -> game.getGameType().equals("SOLO")).findAny().orElse(null);
                    if (soloGame == null){
                        p.sendMessage(Utils.format("&cNo se ha encontrado una partida de SkyWars en modo &eSolitario."));
                        p.teleport(this.plugin.getMainLobby());
                        return;
                    }
                    this.plugin.getGm().addPlayerGame(p, soloGame.getId());
                    break;
                }
                case TEAM:{
                    if (!this.plugin.getGm().addRandomGame(p, "TEAM")){
                        p.sendMessage(Utils.format("&cNo se ha encontrado una partida de SkyWars en modo &eEquipo."));
                        p.teleport(this.plugin.getMainLobby());
                    }
                    break;
                }
                case RANKED:{
                    if (!this.plugin.getGm().addRandomGame(p, "RANKED")){
                        p.sendMessage(Utils.format("&cNo se ha encontrado una partida de SkyWars en modo &eRanked."));
                        p.teleport(this.plugin.getMainLobby());
                    }
                    break;
                }
                case RANDOM:{
                    Map<Integer, Game> games = this.plugin.getGm().getGames();
                    Game game = games.get(new Random().nextInt(games.size()));
                    if (game == null){
                        p.sendMessage(Utils.format("&cNo se ha encontrado una partida de SkyWars."));
                        p.teleport(this.plugin.getMainLobby());
                        return;
                    }
                    this.plugin.getGm().removePlayerAllGame(p);
                    this.plugin.getGm().addPlayerGame(p, game.getId());
                    break;
                }
            }
            
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemDamageEvent(PlayerItemDamageEvent e){
        Player p = e.getPlayer();
        if (this.plugin.getCm().isMainLobby()){
            if (builders.contains(p.getUniqueId())) return;
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
            if (builders.contains(p.getUniqueId())) return;
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
            if (builders.contains(p.getUniqueId())) return;
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
            if (builders.contains(e.getEntity().getUniqueId())) return;
            World w = e.getLocation().getWorld();
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
            if (e.getDamager() == null) return;
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
        if (e.getEntity() instanceof Player) return;
        if (e.getEntity() instanceof ArmorStand) return;
        
        if (this.plugin.getCm().isMainLobby()){
            World w = e.getEntity().getWorld();
            if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                e.setCancelled(true);
            }
        }
        
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerRespawnEvent(PlayerRespawnEvent e){
        Player p = e.getPlayer();
        if (this.plugin.getCm().isMainLobby()){
            World w = p.getWorld();
            if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                Items.setSkyWarsLobbyItems(e.getPlayer());
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageEvent e){
        if (e.getEntity() instanceof Player) return;
        if (e.getEntity() instanceof ArmorStand) return;
        
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
    public void onWeatherChangeEvent(WeatherChangeEvent e){
        if (this.plugin.getCm().isMainLobby()){
            World w = e.getWorld();
            if (w.getName().equals(this.plugin.getCm().getLobbyWorld())){
                if (e.toWeatherState()){
                    e.setCancelled(true);
                }
            }
        }
        
    }
    
}
