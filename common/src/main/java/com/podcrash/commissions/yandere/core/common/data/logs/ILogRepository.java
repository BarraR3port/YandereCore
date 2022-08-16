package com.podcrash.commissions.yandere.core.common.data.logs;

import com.mongodb.client.model.Filters;
import net.lymarket.common.db.MongoDB;
import net.lymarket.common.db.MongoDBClient;
import net.lymarket.lyapi.spigot.LyApi;
import org.bson.Document;

import java.util.UUID;

public abstract class ILogRepository<U> extends MongoDB<UUID, U> {
    
    public ILogRepository(MongoDBClient database, String TABLE_NAME){
        super(database, TABLE_NAME);
    }
    
    public abstract Log createLog(LogType type, String server, String msg, String p);
    
    public Log createLog(LogType type, String server, String msg){
        Log log = new Log(type, server, msg);
        database.insertOne(TABLE_NAME, log);
        return log;
    }
    
    public Log getLog(UUID uuid){
        Document doc = database.findOneFast(TABLE_NAME, Filters.eq("uuid", uuid.toString()));
        if (doc == null) return null;
        return LyApi.getGson().fromJson(doc.toJson(), Log.class);
    }
    
}
