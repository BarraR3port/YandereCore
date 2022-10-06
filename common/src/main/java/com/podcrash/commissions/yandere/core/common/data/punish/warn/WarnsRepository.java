package com.podcrash.commissions.yandere.core.common.data.punish.warn;

import com.mongodb.client.model.Filters;
import com.podcrash.commissions.yandere.core.common.data.punish.Punish;
import net.lymarket.lyapi.common.db.MongoDB;
import net.lymarket.lyapi.common.db.MongoDBClient;
import net.lymarket.lyapi.spigot.LyApi;
import org.bson.Document;

import java.util.LinkedList;
import java.util.UUID;

public class WarnsRepository extends MongoDB<UUID, Punish> {
    
    public WarnsRepository(MongoDBClient database, String databaseName){
        super(database, databaseName);
    }
    
    public Warn getWarn(UUID uuid){
        Warn warn = (Warn) list.getOrDefault(uuid, null);
        if (warn == null){
            Document doc = database.findOneFast(TABLE_NAME, Filters.eq("UUID", uuid.toString()));
            if (doc == null) return null;
            warn = LyApi.getGson().fromJson(doc.toJson(), Warn.class);
            list.put(warn.getUUID(), warn);
        }
        return warn;
    }
    
    public LinkedList<Warn> getWarns(){
        return database.findMany(TABLE_NAME, Warn.class);
    }
    
    public void createWarn(Warn warn){
        database.insertOne(TABLE_NAME, warn);
        list.put(warn.getUUID(), warn);
    }
    
    public void unloadWarn(UUID uuid){
        list.remove(uuid);
    }
    
    public void deleteWarn(Warn warn){
        database.deleteOne(TABLE_NAME, Filters.eq("UUID", warn.getUUID().toString()));
        list.remove(warn.getUUID());
    }
    
    public void deleteWarnByUUID(UUID uuid){
        database.deleteOne(TABLE_NAME, Filters.eq("UUID", uuid.toString()));
        list.remove(uuid);
    }
    
    public String serialize(Warn warn){
        return LyApi.getGson().toJson(warn);
    }
    
    public Warn deserialize(String s){
        return LyApi.getGson().fromJson(s, Warn.class);
    }
    
    public void fetchWarns(){
        for ( Warn warn : getWarns() ){
            list.put(warn.getUUID(), warn);
        }
    }
}
