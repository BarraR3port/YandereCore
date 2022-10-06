package com.podcrash.commissions.yandere.core.common.data.punish;

import com.podcrash.commissions.yandere.core.common.data.punish.ban.Ban;
import com.podcrash.commissions.yandere.core.common.data.punish.ban.BansRepository;
import com.podcrash.commissions.yandere.core.common.data.punish.mute.Mute;
import com.podcrash.commissions.yandere.core.common.data.punish.mute.MutesRepository;
import com.podcrash.commissions.yandere.core.common.data.punish.report.Report;
import com.podcrash.commissions.yandere.core.common.data.punish.report.ReportsRepository;
import com.podcrash.commissions.yandere.core.common.data.punish.warn.Warn;
import com.podcrash.commissions.yandere.core.common.data.punish.warn.WarnsRepository;
import net.lymarket.lyapi.common.db.MongoDBClient;
import net.lymarket.lyapi.spigot.LyApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public abstract class IPunishManager {
    
    public final Date PERMANENT_DATE = parseDate("29-10-3000");
    private final BansRepository bans;
    private final MutesRepository mutes;
    private final ReportsRepository reports;
    private final WarnsRepository warns;
    
    public IPunishManager(MongoDBClient database){
        this.bans = new BansRepository(database, "punish");
        this.mutes = new MutesRepository(database, "punish");
        this.reports = new ReportsRepository(database, "punish");
        this.warns = new WarnsRepository(database, "punish");
    }
    
    public BansRepository getBans(){
        return bans;
    }
    
    public MutesRepository getMutes(){
        return mutes;
    }
    
    public ReportsRepository getReports(){
        return reports;
    }
    
    public WarnsRepository getWarns(){
        return warns;
    }
    
    public Ban deserializeBan(String serializedBan){
        return LyApi.getGson().fromJson(serializedBan, Ban.class);
    }
    
    public Warn deserializeWarn(String serializedWarn){
        return LyApi.getGson().fromJson(serializedWarn, Warn.class);
    }
    
    public Mute deserializeMute(String serializedMute){
        return LyApi.getGson().fromJson(serializedMute, Mute.class);
    }
    
    public Report deserializeReport(String serializedReport){
        return LyApi.getGson().fromJson(serializedReport, Report.class);
    }
    
    private Date parseDate(String date){
        try {
            return new SimpleDateFormat("dd-MM-yyyy").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
    
}
