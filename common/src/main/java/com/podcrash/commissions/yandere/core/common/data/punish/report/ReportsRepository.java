package com.podcrash.commissions.yandere.core.common.data.punish.report;

import com.mongodb.client.model.Filters;
import com.podcrash.commissions.yandere.core.common.data.punish.Punish;
import net.lymarket.lyapi.common.db.MongoDB;
import net.lymarket.lyapi.common.db.MongoDBClient;
import net.lymarket.lyapi.spigot.LyApi;
import org.bson.Document;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

public class ReportsRepository extends MongoDB<UUID, Punish> {
    
    private final HashMap<UUID, Report> reports = new HashMap<>();
    
    public ReportsRepository(MongoDBClient database, String databaseName){
        super(database, databaseName);
    }
    
    public Report getReport(UUID uuid){
        Report report = reports.getOrDefault(uuid, null);
        if (report == null){
            Document doc = database.findOneFast(TABLE_NAME, Filters.eq("UUID", uuid.toString()));
            if (doc == null) return null;
            report = LyApi.getGson().fromJson(doc.toJson(), Report.class);
            reports.put(report.getUUID(), report);
        }
        return report;
    }
    
    public LinkedList<Report> getReports(){
        return database.findMany(TABLE_NAME, Report.class);
    }
    
    public void createReport(Report report){
        database.insertOne(TABLE_NAME, report);
        reports.put(report.getUUID(), report);
    }
    
    public void unloadReport(UUID uuid){
        reports.remove(uuid);
    }
    
    public void deleteReport(Report report){
        database.deleteOne(TABLE_NAME, Filters.eq("UUID", report.getUUID().toString()));
        reports.remove(report.getUUID());
    }
    
    public void deleteReportByUUID(UUID uuid){
        database.deleteOne(TABLE_NAME, Filters.eq("UUID", uuid.toString()));
        reports.remove(uuid);
    }
    
    public String serialize(Report report){
        return LyApi.getGson().toJson(report);
    }
    
    public Report deserialize(String s){
        return LyApi.getGson().fromJson(s, Report.class);
    }
    
    public void fetchReports(){
        for ( Report report : getReports() ){
            reports.put(report.getUUID(), report);
        }
    }
}
