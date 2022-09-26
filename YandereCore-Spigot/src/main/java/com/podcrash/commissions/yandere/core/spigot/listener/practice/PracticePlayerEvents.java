package com.podcrash.commissions.yandere.core.spigot.listener.practice;

import com.podcrash.commissions.yandere.core.common.data.cooldown.CoolDown;
import com.podcrash.commissions.yandere.core.common.data.cooldown.CoolDownType;
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
import ga.strikepractice.StrikePractice;
import ga.strikepractice.api.StrikePracticeAPI;
import ga.strikepractice.stats.PlayerStats;
import net.lymarket.lyapi.spigot.LyApi;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import net.lymarket.lyapi.spigot.utils.Utils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;

public final class PracticePlayerEvents extends MainEvents {
    
    private final StrikePracticeAPI api;
    
    public PracticePlayerEvents(){
        api = StrikePractice.getAPI();
    }
    
    public void subPlayerQuitEvent(PlayerQuitEvent e){
        
    }
    
    @Override
    public boolean subPlayerChatEvent(AsyncPlayerChatEvent event){
        Player p = event.getPlayer();
        String message = event.getMessage();
    
        User user = Main.getInstance().getPlayers().getCachedPlayer(p.getUniqueId());
        boolean color = p.hasPermission("yandere.chat.color");
        event.setCancelled(true);
        PlayerStats stats = api.getPlayerStats(p);
        boolean isDefault = user.getRank() == Rank.USUARIO;
        final String prefix = user.getRank().getTabPrefix();
        final String white_msg = p.hasPermission("yandere.chat.whitemessage") ? "&f" : "&7";
        TextComponent level = Utils.hoverOverMessage("&7「" + stats.getGlobalElo() + "⏌",
                Arrays.asList(
                        "&7「&eEstadísticas del Jugador&7⏌",
                        "",
                        "&7► Asesinatos: &c" + stats.getKills(),
                        "&7► Muertes: &c" + stats.getDeaths(),
                        "&7► Elo: &c" + stats.getGlobalElo()));
        TextComponent name = Utils.hoverOverMessage(white_msg + p.getName(),
                Arrays.asList(
                        "&7「&eInformación del jugador&7⏌",
                        "",
                        "&7► Rango: " + prefix,
                        "&7► Monedas: &c" + user.getCoinsFormatted(),
                        "&7► Nivel: &c" + user.getLevel().getLevelName(),
                        "" + user.getLevel().getProgressBar()/* ,
                            "&7Clan: &c" + clanTag*/));
        TextComponent rank = Utils.hoverOverMessageURL(isDefault ? " " : prefix,
                Arrays.asList("&7「&eYandere &cRangos&7⏌",
                        "",
                        "&7Este jugador tiene el rango " + prefix,
                        "&7Puedes comprar más rangos en nuestra página web.",
                        "&7Rangos disponibles: " + Rank.BRONCE.getTabPrefix() + " " + Rank.HIERRO.getTabPrefix() + " " + Rank.ORO.getTabPrefix() + " " + Rank.DIAMANTE.getTabPrefix(),
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
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public synchronized void onPlayerInteractEvent(PlayerInteractEvent e){
        ItemStack item = e.getItem();
        if (item == null) return;
        if (item.getType() == Material.AIR) return;
        Player p = e.getPlayer();
        World w = p.getLocation().getWorld();
        if (!w.getName().equals("world")) return;
        if (e.getAction().equals(Action.PHYSICAL)) return;
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
        }
    }
    
    @Override
    public void onPlayerTeleport(PlayerTeleportEvent e){
    
    }
    
    public void subPlayerJoinEvent(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if (p.hasMetadata("NPC")) return;
        World w = p.getLocation().getWorld();
        try {
            if (w.getName().equals("world")){
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                    User user = Main.getInstance().getPlayers().getCachedPlayer(p.getUniqueId());
                    boolean hasRank = user.getRank() != Rank.USUARIO;
                    if (hasRank){
                        p.setAllowFlight(true);
                    }
                    String joinMsg = hasRank ? Utils.format(" &8&l»" + user.getRank().getTabPrefix() + p.getName() + " &fse ha unido al servidor!") : "";
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
                    }
                }, 4L);
            }
        } catch (UserNotFoundException ignored) {
        }
        
    }
    
}
