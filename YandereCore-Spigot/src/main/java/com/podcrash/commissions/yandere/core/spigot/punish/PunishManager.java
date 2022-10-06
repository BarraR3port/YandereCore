package com.podcrash.commissions.yandere.core.spigot.punish;

import com.podcrash.commissions.yandere.core.common.YandereApi;
import com.podcrash.commissions.yandere.core.common.data.logs.LogType;
import com.podcrash.commissions.yandere.core.common.data.punish.IPunishManager;
import com.podcrash.commissions.yandere.core.common.data.punish.PunishState;
import com.podcrash.commissions.yandere.core.common.data.punish.PunishType;
import com.podcrash.commissions.yandere.core.common.data.punish.ban.Ban;
import com.podcrash.commissions.yandere.core.common.data.punish.mute.Mute;
import com.podcrash.commissions.yandere.core.common.data.punish.report.Report;
import com.podcrash.commissions.yandere.core.common.data.punish.warn.Warn;
import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.settings.Settings;
import net.lymarket.lyapi.common.db.MongoDBClient;
import org.bukkit.entity.Player;

import java.util.*;

public class PunishManager extends IPunishManager {
    
    public PunishManager(MongoDBClient database){
        super(database);
    }
    
    public void banPlayer(User punished, User punisher, String reason, Date expDate, Boolean ip, String server){
        Ban ban = new Ban(punished.getName(), punisher.getName(), reason, expDate, ip, server, punished.getAddress());
        punished.addPunish(ban.getUUID(), PunishType.BAN);
        Main.getInstance().getPlayers().savePlayer(punished);
        this.getBans().createBan(ban);
        announceNewBan(ban);
    
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put(" &4• &7Baneador: &c", punisher.getName());
        data.put(" &4• &7Baneado: &c", punished.getName());
        data.put(" &4• &7Razón: &c", reason);
        data.put(" &4• &7IP: &c", ip ? "Sí" : "No");
        data.put(" &4• &7Expira el: &c", YandereApi.DATE_FORMAT.format(expDate));
        data.put(" &4• &7Creado el: &c", YandereApi.DATE_FORMAT.format(ban.getCreateDate()));
        data.put(" &4• &7ID: &c", ban.getUUID().toString().split("-")[0]);
    
        Main.getInstance().getLogs().createLogWithProps(LogType.PUNISHMENT, Settings.PROXY_SERVER_NAME, "&4Ha Baneado a: &7" + punished.getName(), punisher.getName(), data);
        Main.getInstance().getLogs().createLogWithProps(LogType.PUNISHMENT, Settings.PROXY_SERVER_NAME, "&4Fue Baneado por: &7" + punisher.getName(), punished.getName(), data);
    }
    
    public void permanentBan(User punished, User punisher, String reason, Boolean ip, String server){
        Ban ban = new Ban(punished.getName(), punisher.getName(), reason, PERMANENT_DATE, ip, server, punished.getAddress());
        punished.addPunish(ban.getUUID(), PunishType.BAN);
        Main.getInstance().getPlayers().savePlayer(punished);
        this.getBans().createBan(ban);
        announceNewBan(ban);
    
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put(" &4• &7Baneador: &c", punisher.getName());
        data.put(" &4• &7Baneado: &c", punished.getName());
        data.put(" &4• &7Razón: &c", reason);
        data.put(" &4• &7IP: &c", ip ? "Sí" : "No");
        data.put(" &4• &7Expira: &c", "Nunca");
        data.put(" &4• &7Creado el: &c", YandereApi.DATE_FORMAT.format(ban.getCreateDate()));
        data.put(" &4• &7ID: &c", ban.getUUID().toString().split("-")[0]);
    
        Main.getInstance().getLogs().createLogWithProps(LogType.PUNISHMENT, Settings.PROXY_SERVER_NAME, "&4Ha Baneado a: &7" + punished.getName(), punisher.getName(), data);
        Main.getInstance().getLogs().createLogWithProps(LogType.PUNISHMENT, Settings.PROXY_SERVER_NAME, "&4Fue Baneado por: &7" + punisher.getName(), punished.getName(), data);
    }
    
    public void unBanByUUID(Player owner, UUID uuid, User punished){
        Ban ban = this.getBans().updateBanState(uuid, PunishState.FORGIVEN);
        punished.removePunish(uuid);
        Main.getInstance().getPlayers().savePlayer(punished);
        announceNewUnBan(ban);
    
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put(" &4• &7Baneador: &c", owner.getName());
        data.put(" &4• &7DesBaneado: &c", punished.getName());
        data.put(" &4• &7Creado el: &c", YandereApi.DATE_FORMAT.format(ban.getCreateDate()));
        data.put(" &4• &7ID: &c", ban.getUUID().toString().split("-")[0]);
    
        Main.getInstance().getLogs().createLogWithProps(LogType.PUNISHMENT, Settings.PROXY_SERVER_NAME, "&4Ha DesBaneado a: &7" + punished.getName(), owner.getName(), data);
        Main.getInstance().getLogs().createLogWithProps(LogType.PUNISHMENT, Settings.PROXY_SERVER_NAME, "&4Fue DesBaneado por: &7" + owner.getName(), punished.getName(), data);
    }
    
    public void mutePlayer(User punished, User punisher, String reason, Date expDate, String server){
        Mute mute = new Mute(punished.getName(), punisher.getName(), reason, expDate, server);
        punished.addPunish(mute.getUUID(), PunishType.MUTE);
        Main.getInstance().getPlayers().savePlayer(punished);
        this.getMutes().createMute(mute);
        announceNewMute(mute);
    
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put(" &4• &7Muteador: &c", punisher.getName());
        data.put(" &4• &7Muteado: &c", punished.getName());
        data.put(" &4• &7Razón: &c", reason);
        data.put(" &4• &7Expira el: &c", YandereApi.DATE_FORMAT.format(expDate));
        data.put(" &4• &7Creado el: &c", YandereApi.DATE_FORMAT.format(mute.getCreateDate()));
        data.put(" &4• &7ID: &c", mute.getUUID().toString().split("-")[0]);
    
        Main.getInstance().getLogs().createLogWithProps(LogType.PUNISHMENT, Settings.PROXY_SERVER_NAME, "&4Ha Muteado a: &7" + punished.getName(), punisher.getName(), data);
        Main.getInstance().getLogs().createLogWithProps(LogType.PUNISHMENT, Settings.PROXY_SERVER_NAME, "&4Fue Muteado por: &7" + punisher.getName(), punished.getName(), data);
    
    }
    
    public void permanentMute(User punished, User punisher, String reason, String server){
        Mute mute = new Mute(punished.getName(), punisher.getName(), reason, this.PERMANENT_DATE, server);
        punished.addPunish(mute.getUUID(), PunishType.MUTE);
        Main.getInstance().getPlayers().savePlayer(punished);
        this.getMutes().createMute(mute);
        announceNewMute(mute);
    
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put(" &4• &7Muteador: &c", punisher.getName());
        data.put(" &4• &7Muteado: &c", punished.getName());
        data.put(" &4• &7Razón: &c", reason);
        data.put(" &4• &7Expira: &c", "Nunca");
        data.put(" &4• &7Creado el: &c", YandereApi.DATE_FORMAT.format(mute.getCreateDate()));
        data.put(" &4• &7ID: &c", mute.getUUID().toString().split("-")[0]);
    
        Main.getInstance().getLogs().createLogWithProps(LogType.PUNISHMENT, Settings.PROXY_SERVER_NAME, "&4Ha Muteado a: &7" + punished.getName(), punisher.getName(), data);
        Main.getInstance().getLogs().createLogWithProps(LogType.PUNISHMENT, Settings.PROXY_SERVER_NAME, "&4Fue Muteado por: &7" + punisher.getName(), punished.getName(), data);
    
    }
    
    public void createWarn(User punished, User punisher, String reason, String server){
        Warn warn = new Warn(punished.getName(), punisher.getName(), reason, server);
        punished.addPunish(warn.getUUID(), PunishType.WARN);
        this.getWarns().createWarn(warn);
        Main.getInstance().getPlayers().savePlayer(punished);
        announceNewWarn(warn);
    
        ArrayList<Warn> warns = new ArrayList<>();
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put(" &4• &7Avisador: &c", punisher.getName());
        data.put(" &4• &7Avisado: &c", punished.getName());
        data.put(" &4• &7Razón: &c", reason);
        data.put(" &4• &7Creado el: &c", YandereApi.DATE_FORMAT.format(warn.getCreateDate()));
        data.put(" &4• &7ID: &c", warn.getUUID().toString().split("-")[0]);
    
        Main.getInstance().getLogs().createLogWithProps(LogType.PUNISHMENT, Settings.PROXY_SERVER_NAME, "&4Ha Avisado a: &7" + punished.getName(), punisher.getName(), data);
        Main.getInstance().getLogs().createLogWithProps(LogType.PUNISHMENT, Settings.PROXY_SERVER_NAME, "&4Fue Avisado por: &7" + punisher.getName(), punished.getName(), data);
    
        HashMap<UUID, PunishType> punishments = punished.getPunishments();
        if (punishments != null){
            for ( UUID ids : punishments.keySet() ){
                if (punished.getPunishments().get(ids).equals(PunishType.WARN)){
                    warns.add(this.getWarns().getWarn(ids));
                }
            }
            warns.add(warn);
            if (warns.size() >= 3){
                permanentBan(punished, Main.getInstance().getPlayers().getPlayer(warn.getPunisher()), "Sobrepasó el limite de Warns (3)", false, warn.getServer());
            }
        }
    }
    
    public void createReport(User punished, User punisher, String reason, String server, String material){
        Report report = new Report(punished.getName(), punisher.getName(), reason, server);
        punished.addPunish(report.getUUID(), PunishType.REPORT);
        Main.getInstance().getPlayers().savePlayer(punished);
        this.getReports().createReport(report);
        announceNewReport(report);
    
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
    
        data.put("material", material);
        data.put(" &4• &7Reportador: &c", punisher.getName());
        data.put(" &4• &7Reportado: &c", punished.getName());
        data.put(" &4• &7Razón: &c", reason);
        data.put(" &4• &7Creado el: &c", YandereApi.DATE_FORMAT.format(report.getCreateDate()));
        data.put(" &4• &7ID: &c", report.getUUID().toString().split("-")[0]);
    
        Main.getInstance().getLogs().createLogWithProps(LogType.PUNISHMENT, Settings.PROXY_SERVER_NAME, "&4Ha Reportado a: &7" + punished.getName(), punisher.getName(), data);
        Main.getInstance().getLogs().createLogWithProps(LogType.PUNISHMENT, Settings.PROXY_SERVER_NAME, "&4Fue Reportado por: &7" + punisher.getName(), punished.getName(), data);
    
    }
    
    
    public void announceNewBan(Ban ban){
        Main.getInstance().getSocket().sendPunish(ban, "ban");
    }
    
    public void announceNewUnBan(Ban ban){
        Main.getInstance().getSocket().sendPunish(ban, "unban");
    }
    
    public void announceNewMute(Mute mute){
        Main.getInstance().getSocket().sendPunish(mute, "mute");
    }
    
    public void announceNewWarn(Warn warn){
        Main.getInstance().getSocket().sendPunish(warn, "warn");
    }
    
    public void announceNewReport(Report report){
        Main.getInstance().getSocket().sendPunish(report, "report");
    }
    
}
