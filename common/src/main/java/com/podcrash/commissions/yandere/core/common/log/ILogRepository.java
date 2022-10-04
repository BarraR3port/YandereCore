package com.podcrash.commissions.yandere.core.common.log;

import com.podcrash.commissions.yandere.core.common.data.logs.Log;
import com.podcrash.commissions.yandere.core.common.data.logs.LogType;
import net.lymarket.lyapi.common.db.MongoDB;
import net.lymarket.lyapi.common.db.MongoDBClient;
import net.lymarket.lyapi.spigot.LyApi;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.LinkedList;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

public abstract class ILogRepository extends MongoDB<UUID, Log> {
    
    private final int maxPerPage = 28;
    
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
        Document doc = database.findOneFast(TABLE_NAME, eq("uuid", uuid.toString()));
        if (doc == null) return null;
        return LyApi.getGson().fromJson(doc.toJson(), Log.class);
    }
    
    public Log getLastPlayerMessageLog(String name){
        LinkedList<Log> notReduced = new LinkedList<>(list.values());
        LinkedList<Log> reduced = notReduced.stream().filter(log -> log.getOwner().equals(name)).filter(log -> log.getType().equals(LogType.CHAT)).collect(Collectors.toCollection(LinkedList::new));
        return reduced.peekLast();
    }
    
    
    public LinkedList<Log> getLogs(){
        return database.findMany(TABLE_NAME, Log.class);
    }
    
    public LinkedList<Log> getLogsByPage(int page){
        return database.findManyPaginated(TABLE_NAME, page, maxPerPage + 1, Log.class);
    }
    
    public LinkedList<Log> getLogsByPageAndName(int page, String name){
        Bson equalComparison = eq("owner", name);
        return database.findManyPaginatedAndFilter(TABLE_NAME, equalComparison, page, maxPerPage + 1, Log.class);
    }
}
