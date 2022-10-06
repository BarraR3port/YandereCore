package com.podcrash.commissions.yandere.core.velocity.punish;

import com.podcrash.commissions.yandere.core.common.data.punish.IPunishManager;
import com.podcrash.commissions.yandere.core.common.data.punish.ban.Ban;
import com.podcrash.commissions.yandere.core.common.data.punish.mute.Mute;
import com.podcrash.commissions.yandere.core.common.data.punish.report.Report;
import com.podcrash.commissions.yandere.core.common.data.punish.warn.Warn;
import com.podcrash.commissions.yandere.core.velocity.VMain;
import com.podcrash.commissions.yandere.core.velocity.announcements.VAnnouncer;
import com.podcrash.commissions.yandere.core.velocity.utils.Utils;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.event.HoverEvent;
import net.lymarket.lyapi.common.db.MongoDBClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PunishManager extends IPunishManager {
    
    private final VAnnouncer announcements = new VAnnouncer();
    private final ProxyServer proxyServer;
    
    public PunishManager(ProxyServer proxyServer, MongoDBClient database){
        super(database);
        this.proxyServer = proxyServer;
    }
    
    public void banPlayer(Ban ban){
        VMain.getInstance().getProxy().getScheduler().buildTask(VMain.getInstance(), () -> {
            this.getBans().fetchBans();
            for ( Player p : proxyServer.getAllPlayers() ){
                if (p.hasPermission("yandere.staff.punishments.announcements")){
                    for ( String s : announcements.banPlayer(ban) ){
                        p.sendMessage(Utils.format(s));
                    }
                }
            }
            
            VMain.getInstance().getPlayers().getCachedPlayer(ban.getPunished());
            
            if (proxyServer.getPlayer(ban.getPunished()).isPresent()){
                Player player = proxyServer.getPlayer(ban.getPunished()).get();
                player.disconnect(announcements.banPlayerKickMessage(ban));
            }
        }).schedule();
    }
    
    public void unBanPlayer(Ban ban, String server){
        VMain.getInstance().getProxy().getScheduler().buildTask(VMain.getInstance(), () -> {
            this.getBans().fetchBans();
            for ( Player p : proxyServer.getAllPlayers() ){
                if (p.hasPermission("yandere.staff.punishments.announcements")){
                    for ( String s : announcements.unBanPlayer(ban.getPunished(), ban.getPunisher(), server) ){
                        p.sendMessage(Utils.format(s));
                    }
                }
            }
        }).schedule();
    }
    
    
    public void warnPlayer(Warn warn){
        VMain.getInstance().getProxy().getScheduler().buildTask(VMain.getInstance(), () -> {
            this.getWarns().fetchWarns();
            for ( Player p : proxyServer.getAllPlayers() ){
                if (p.hasPermission("yandere.staff.punishments.announcements")){
                    for ( String s : announcements.warnPlayer(warn) ){
                        p.sendMessage(Utils.format(s));
                    }
                }
            }
        }).schedule();
        
    }
    
    public void reportPlayer(Report report){
        VMain.getInstance().getProxy().getScheduler().buildTask(VMain.getInstance(), () -> {
            this.getReports().fetchReports();
            for ( Player p : proxyServer.getAllPlayers() ){
                if (p.hasPermission("yandere.staff.punishments.announcements")){
                    for ( String s : announcements.reportPlayer(report) ){
                        p.sendMessage(Utils.format(s));
                    }
                }
            }
        }).schedule();
    }
    
    
    public void mutePlayer(Mute mute){
        VMain.getInstance().getProxy().getScheduler().buildTask(VMain.getInstance(), () -> {
            this.getMutes().fetchMutes();
            for ( Player p : proxyServer.getAllPlayers() ){
                if (p.hasPermission("yandere.staff.punishments.announcements")){
                    for ( String s : announcements.mutePlayer(mute) ){
                        p.sendMessage(Utils.format(s));
                    }
                }
            }
        }).schedule();
    }
    
    public void sendStaffChat(Player staff, String msg, String server){
        VMain.getInstance().getProxy().getScheduler().buildTask(VMain.getInstance(), () -> {
            /*LuckPerms api = LuckPermsProvider.get( );
            String prefixFrom = api.getPlayerAdapter( Player.class ).getUser( staff ).getCachedData( ).getMetaData( ).getPrefix( );*/
            String pattern = "dd/MM/yy HH:mm";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("es", "MX"));
            String date = simpleDateFormat.format(new Date());
            for ( Player player : proxyServer.getAllPlayers() ){
                if (player.hasPermission("yandere.staff.staffchat")){
                    player.sendMessage(Utils.format(" &7「&dYandere&5MD&7⏌ " +
                            Utils.format( /*prefixFrom + */staff.getUsername()).hoverEvent(HoverEvent.showText(Utils.format( /*prefixFrom + */staff.getUsername() + "\n" +
                                    "&7Server: " + server + "\n" +
                                    "&7Fecha y hora: &d" + date + "\n"))) +
                            " &8&l⪼ " + (staff.hasPermission("yandere.whitemessage") ? "&f" : "&7") + msg));
                }
            }
        }).schedule();
    }
    
}
