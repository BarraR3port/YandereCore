package com.podcrash.commissions.yandere.core.common.log;

import com.mongodb.client.model.Filters;
import com.podcrash.commissions.yandere.core.common.data.logs.Log;
import com.podcrash.commissions.yandere.core.common.data.logs.LogType;
import net.lymarket.lyapi.common.db.MongoDB;
import net.lymarket.lyapi.common.db.MongoDBClient;
import net.lymarket.lyapi.spigot.LyApi;
import org.bson.Document;

import java.util.LinkedList;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class ILogRepository extends MongoDB<UUID, Log> {
    
    public ILogRepository(MongoDBClient database, String TABLE_NAME){
        super(database, TABLE_NAME);
    }
    
    public abstract Log createLog(LogType type, String server, String msg, String p);
    
    public Log createLog(LogType type, String server, String msg){
        Log log = new Log(type, server, msg);
        database.insertOne(TABLE_NAME, log);
        list.put(log.getUuid(), log);
        return log;
    }
    
    public Log getLog(UUID uuid){
        if (list.containsKey(uuid)){
            return list.get(uuid);
        }
        Document doc = database.findOneFast(TABLE_NAME, Filters.eq("uuid", uuid.toString()));
        if (doc == null) return null;
        return LyApi.getGson().fromJson(doc.toJson(), Log.class);
    }
    
    public Log getLastPlayerMessageLog(String name){
        LinkedList<Log> notReduced = new LinkedList<>(list.values());
        LinkedList<Log> reduced = notReduced.stream().filter(log -> log.getOwner().equals(name)).filter(log -> log.getType().equals(LogType.CHAT)).collect(Collectors.toCollection(LinkedList::new));
        return reduced.peekLast();
    }
    
    
}
