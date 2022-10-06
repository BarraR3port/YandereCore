package com.podcrash.commissions.yandere.core.common.log;

import com.mongodb.MongoTimeoutException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.podcrash.commissions.yandere.core.common.data.DBOrderType;
import com.podcrash.commissions.yandere.core.common.data.logs.Log;
import com.podcrash.commissions.yandere.core.common.data.logs.LogType;
import net.lymarket.lyapi.common.db.MongoDB;
import net.lymarket.lyapi.common.db.MongoDBClient;
import net.lymarket.lyapi.spigot.LyApi;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public abstract class ILogRepository extends MongoDB<UUID, Log> {
    
    
    public ILogRepository(MongoDBClient database, String TABLE_NAME){
        super(database, TABLE_NAME);
    }
    
    public abstract Log createLog(LogType type, String server, String msg, String p);
    
    public abstract Log createLogWithProps(LogType type, String server, String msg, String p, HashMap<String, String> props);
    
    public abstract Log createLogWithOptions(LogType type, String server, String msg, String p, HashMap<String, Boolean> options);
    
    public abstract Log createLog(LogType type, String server, String msg, String p, HashMap<String, String> props, HashMap<String, Boolean> options);
    
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
    
    public LinkedList<Log> getLogsByType(LogType type){
        LinkedList<Log> list = new LinkedList<>();
        Bson mongoFilter;
        if (type != LogType.UNKNOWN){
            mongoFilter = and(eq("type", type.toString()));
        } else {
            mongoFilter = new Document();
        }
        try {
            MongoCollection<Document> collection = database.getDatabase().getCollection(TABLE_NAME);
            FindIterable<Document> documents = collection.find(mongoFilter);
            MongoCursor<Document> cursor = documents.cursor();
            while (cursor.hasNext()) {
                Log current = database.getGson().fromJson(cursor.next().toJson(), Log.class);
                list.add(current);
            }
        } catch (MongoTimeoutException TimeOut) {
            TimeOut.printStackTrace();
        }
        return list;
    }
    
    public LinkedList<Log> getLogsByPage(int page, int maxPerPage, LogType filter, DBOrderType orderType){
        Bson mongoFilter;
        if (filter != LogType.UNKNOWN){
            mongoFilter = and(eq("type", filter.toString()));
        } else {
            mongoFilter = new Document();
        }
        LinkedList<Log> list = new LinkedList<>();
        Bson sort = new Document(orderType.isDate() ? "createDate" : "owner", orderType.getOrder());
        try {
            MongoCollection<Document> collection = this.database.getDatabase().getCollection(TABLE_NAME);
            FindIterable<Document> documents = collection.find(mongoFilter).skip(page).limit(maxPerPage + 1).sort(sort);
            MongoCursor<Document> cursor = documents.cursor();
            while (cursor.hasNext()) {
                Log current = this.database.getGson().fromJson(cursor.next().toJson(), Log.class);
                list.add(current);
            }
        } catch (MongoTimeoutException ex) {
            ex.printStackTrace();
        }
        return list;
    }
    
    public LinkedList<Log> getLogsByPageAndName(int page, String name, int maxPerPage, LogType filter, DBOrderType orderType){
        Bson mongoFilter;
        if (filter != LogType.UNKNOWN){
            mongoFilter = and(eq("owner", name), eq("type", filter.toString()));
        } else {
            mongoFilter = eq("owner", name);
        }
        LinkedList<Log> list = new LinkedList<>();
        Bson sort = new Document(orderType.isDate() ? "createDate" : "owner", orderType.getOrder());
        try {
            MongoCollection<Document> collection = this.database.getDatabase().getCollection(TABLE_NAME);
            FindIterable<Document> documents = collection.find(mongoFilter).skip(page).limit(maxPerPage + 1).sort(sort);
            MongoCursor<Document> cursor = documents.cursor();
            while (cursor.hasNext()) {
                Log current = this.database.getGson().fromJson(cursor.next().toJson(), Log.class);
                list.add(current);
            }
        } catch (MongoTimeoutException ex) {
            ex.printStackTrace();
        }
        return list;
    }
    
    public int getLogsByPageAndNameSize(int page, int maxPerPage, String name){
        try {
            MongoCollection<Document> collection = database.getDatabase().getCollection(TABLE_NAME);
            Bson filter = eq("owner", name);
            FindIterable<Document> documents = collection.find(filter).skip(page).limit(maxPerPage + 1);
            MongoCursor<Document> cursor = documents.cursor();
            int size = 0;
            while (cursor.hasNext()) {
                cursor.next();
                size++;
            }
            return size;
        } catch (MongoTimeoutException TimeOut) {
            TimeOut.printStackTrace();
        }
        return 0;
    }
}
