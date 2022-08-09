package com.podcrash.comissions.yandere.core.spigot.listener.bedwars;

import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.commands.shout.ShoutCommand;
import com.andrei1058.bedwars.configuration.Permissions;
import com.andrei1058.bedwars.support.papi.SupportPAPI;
import com.podcrash.comissions.yandere.core.common.data.loc.Loc;
import com.podcrash.comissions.yandere.core.common.data.logs.LogType;
import com.podcrash.comissions.yandere.core.common.data.user.props.Rank;
import com.podcrash.comissions.yandere.core.spigot.Main;
import com.podcrash.comissions.yandere.core.spigot.listener.MainEvents;
import com.podcrash.comissions.yandere.core.spigot.settings.Settings;
import com.podcrash.comissions.yandere.core.spigot.users.SpigotUser;
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

import static com.andrei1058.bedwars.BedWars.*;
import static com.andrei1058.bedwars.api.language.Language.getMsg;

public final class BWPlayerEvents extends MainEvents {
    
    public BWPlayerEvents(){
    }
    
    public void subPlayerQuitEvent(PlayerQuitEvent e){
    
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent e){
        try {
            final World world = e.getTo().getWorld();
            final UUID playerUUID = e.getPlayer().getUniqueId();
            final SpigotUser user = Main.getInstance().getPlayers().getPlayer(playerUUID);
            final Location loc = e.getTo();
            user.setLastLocation(new Loc(Settings.SERVER_NAME, world.getName(), loc.getX(), loc.getY(), loc.getZ()));
            Main.getInstance().getPlayers().savePlayer(user);
        } catch (NullPointerException ignored) {
        }
    }
    
    public void subPlayerJoinEvent(PlayerJoinEvent e){
        
    }
    
    @EventHandler
    public void subPlayerChatEvent(AsyncPlayerChatEvent event){
        
        Player p = event.getPlayer();
        String finalMessage = event.getMessage();
        
        /*for ( String bloqueada : SMain.getInstance( ).getLySettings( ).getDisallowedWords( ) ) {
            if ( finalMessage.toLowerCase( ).contains( bloqueada ) ) {
                p.sendMessage( Main.getApi( ).getUtils( ).prefix( ) + " &cNo puedes escribir la palabra: &4&l" + bloqueada + "&c." );
                p.playSound( p.getLocation( ) , XSound.ENTITY_BAT_HURT.parseSound( ) , 3 , ( float ) 0.2 );
                event.setCancelled( true );
                return;
            }
        }*/
        
        if (event.isCancelled()) return;
        SpigotUser user = Main.getInstance().getPlayers().getPlayer(p.getName());
        final String white_msg = p.hasPermission("yandere.chat.whitemessage") ? "&f" : "&7";
        if (p.hasPermission("yandere.chat.color")){
            finalMessage = Utils.format(finalMessage);
        }
        event.setCancelled(true);
        
        if (Arena.getArenaByPlayer(p) != null){
            boolean isDefault = user.getRank() == Rank.USUARIO;
            
            //final String clanTag = user.getClanRelation( ) != null ? (Main.getInstance().getClans( ).getClan( user.getClanRelation( ).getClanId( ) ).getTagFormatted( )) : "&cSin Clan";
            final String prefix = user.getRank().getTabPrefix();
            IArena a = Arena.getArenaByPlayer(p);
            ITeam t = a.getTeam(p);
            Arena.afkCheck.remove(p.getUniqueId());
            if (getAPI().getAFKUtil().isPlayerAFK(event.getPlayer())){
                Bukkit.getScheduler().runTask(plugin, () -> getAPI().getAFKUtil().setPlayerAFK(event.getPlayer(), false));
            }
            TextComponent level = Utils.hoverOverMessage(getLevelSupport().getLevel(p),
                    Arrays.asList(
                            "&eBedWars Level Info:",
                            "&7⪼ Nivel: &d" + getLevelSupport().getPlayerLevel(p),
                            "&7⪼ XP: &d" + getLevelSupport().getCurrentXpFormatted(p),
                            "&7⪼ XP Necesaria para subir: &d" + getLevelSupport().getRequiredXpFormatted(p),
                            getLevelSupport().getProgressBar(p)));
            TextComponent name = Utils.hoverOverMessage(white_msg + p.getName(),
                    Arrays.asList(
                            "&7Infomación del jugador:",
                            "&7⪼ Rango: " + getChatSupport().getPrefix(p),
                            "&7⪼ Monedas: &d" + getCoinsFormatted(user.getCoins()),
                            "&7⪼ Nivel: &d" + user.getLevel().getLevelName(),
                            "" + user.getLevel().getProgressBar()/* ,
                            "&7Clan: &d" + clanTag*/));
            TextComponent team = Utils.formatTC("");
            TextComponent rank = Utils.hoverOverMessageURL(isDefault ? "" : prefix,
                    Arrays.asList("&dYandere&5Ranks",
                            "",
                            "&7Este jugador tiene el rango " + prefix,
                            "",
                            "&7Puedes comprar más rangos en nuestra página web.",
                            "",
                            "&7Rangos disponibles: " + Rank.BRONCE.getTabPrefix() + " " + Rank.HIERRO.getTabPrefix() + " " + Rank.ORO.getTabPrefix() + " " + Rank.DIAMANTE.getTabPrefix(),
                            ""),
                    "https://store.yanderecraft.com");
            
            if (t != null){
                final String teamName = t.getColor().chat() + "「" + t.getDisplayName(Language.getPlayerLanguage(event.getPlayer())).toUpperCase() + "⏌ ";
                team = Utils.hoverOverMessage(teamName,
                        Arrays.asList(
                                "&7BedWars Game Info:",
                                "&7⪼ Equipo: " + t.getColor().chat() + t.getName(),
                                "&7⪼ Miembros: &d" + t.getMembers().size(),
                                "&7⪼ Estado: &d" + (t.isBedDestroyed() ? "&cELIMINADO" : "&aACTIVO")));
            }
            final TextComponent finalTeam = team;
            final String finalMessage3 = finalMessage;
            if (a.isSpectator(p)){
                event.getRecipients().clear();
                event.getRecipients().addAll(a.getSpectators());
                for ( Player player : event.getRecipients() ){
                    Bukkit.getServer().getScheduler().runTaskAsynchronously(Main.getInstance(), () -> player.spigot().sendMessage(
                            level,
                            rank,
                            Utils.formatTC("&7「ESPECTADOR⏌ "),
                            Utils.formatTC((isDefault ? "&7" : white_msg)),
                            name,
                            Utils.formatTC("&8&l⪼ " + (isDefault ? "&7" : white_msg) + finalMessage3)));
                }
                Main.getInstance().getLogs().createLog(LogType.CHAT, Utils.getServer(), "[MSG] Mensaje:" + finalMessage, p.getName());
            } else {
                if (a.getStatus() == GameState.waiting || a.getStatus() == GameState.starting){
                    event.getRecipients().clear();
                    event.getRecipients().addAll(a.getPlayers());
                    for ( Player player : event.getRecipients() ){
                        Bukkit.getServer().getScheduler().runTaskAsynchronously(Main.getInstance(), () -> player.spigot().sendMessage(
                                level,
                                finalTeam,
                                rank,
                                name,
                                Utils.formatTC("&8&l⪼ " + (isDefault ? "&7" : white_msg) + finalMessage3)));
                    }
                    Main.getInstance().getLogs().createLog(LogType.CHAT, Utils.getServer(), "[MSG] Mensaje:" + finalMessage, p.getName());
                    return;
                }
                if (finalMessage.startsWith("!") || finalMessage.startsWith("shout") || finalMessage.startsWith("SHOUT") || finalMessage.startsWith(getMsg(p, Messages.MEANING_SHOUT))){
                    if (!(p.hasPermission(Permissions.PERMISSION_SHOUT_COMMAND) || p.hasPermission(Permissions.PERMISSION_ALL))){
                        event.setCancelled(true);
                        p.sendMessage(getMsg(p, Messages.COMMAND_NOT_FOUND_OR_INSUFF_PERMS));
                        return;
                    }
                    if (ShoutCommand.isShoutCooldown(p)){
                        event.setCancelled(true);
                        p.sendMessage(getMsg(p, Messages.COMMAND_COOLDOWN).replace("{seconds}", String.valueOf(Math.round(ShoutCommand.getShoutCooldown(p)))));
                        return;
                    }
                    ShoutCommand.updateShout(p);
                    
                    event.getRecipients().clear();
                    event.getRecipients().addAll(a.getPlayers());
                    event.getRecipients().addAll(a.getSpectators());
                    
                    if (finalMessage.startsWith("!")) finalMessage = finalMessage.replaceFirst("!", "");
                    if (finalMessage.startsWith("shout")) finalMessage = finalMessage.replaceFirst("SHOUT", "");
                    if (finalMessage.startsWith("shout")) finalMessage = finalMessage.replaceFirst("shout", "");
                    if (finalMessage.startsWith(getMsg(p, Messages.MEANING_SHOUT)))
                        finalMessage = finalMessage.replaceFirst(getMsg(p, Messages.MEANING_SHOUT), "");
                    final String finalMessage1 = finalMessage;
                    for ( Player player : event.getRecipients() ){
                        Bukkit.getServer().getScheduler().runTaskAsynchronously(Main.getInstance(), () -> player.spigot().sendMessage(
                                level,
                                finalTeam,
                                rank,
                                Utils.formatTC("&7「G⏌ "),
                                name,
                                Utils.formatTC("&8&l⪼ " + (isDefault ? "&7" : white_msg) + finalMessage1)));
                    }
                    Main.getInstance().getLogs().createLog(LogType.CHAT, Utils.getServer(), "[MSG] Mensaje:" + finalMessage, p.getName());
                } else {
                    final String finalMessage2 = finalMessage;
                    event.getRecipients().clear();
                    assert t != null;
                    if (a.getMaxInTeam() == 1){
                        event.getRecipients().addAll(a.getPlayers());
                        event.getRecipients().addAll(a.getSpectators());
                    } else {
                        event.getRecipients().addAll(t.getMembers());
                    }
                    
                    event.setFormat(SupportPAPI.getSupportPAPI().replace(event.getPlayer(), getMsg(p, Messages.FORMATTING_CHAT_TEAM).replace("{vPrefix}", getChatSupport().getPrefix(p)).replace("{vSuffix}", getChatSupport().getSuffix(p))
                            .replace("{player}", p.getDisplayName()).replace("{team}", t.getColor().chat() + "「" + t.getDisplayName(Language.getPlayerLanguage(event.getPlayer())).toUpperCase() + "⏌")
                            .replace("{level}", getLevelSupport().getLevel(p))).replace("{finalMessage}", "%2$s"));
                    
                    for ( Player player : event.getRecipients() ){
                        Bukkit.getServer().getScheduler().runTaskAsynchronously(Main.getInstance(), () -> player.spigot().sendMessage(
                                level,
                                finalTeam,
                                rank,
                                name,
                                Utils.formatTC("&8&l⪼ " + (isDefault ? "&7" : white_msg) + finalMessage2)));
                    }
                    Main.getInstance().getLogs().createLog(LogType.CHAT, Utils.getServer(), "[MSG] Mensaje:" + finalMessage, p.getName());
                    
                }
            }
        }
        
    }
}
