package com.podcrash.commissions.yandere.core.common.data.punish.mute;

import com.mongodb.client.model.Filters;
import com.podcrash.commissions.yandere.core.common.data.punish.Punish;
import net.lymarket.lyapi.common.db.MongoDB;
import net.lymarket.lyapi.common.db.MongoDBClient;
import net.lymarket.lyapi.spigot.LyApi;
import org.bson.Document;

import java.util.LinkedList;
import java.util.UUID;

public class MutesRepository extends MongoDB<UUID, Punish> {
    
    public MutesRepository(MongoDBClient database, String databaseName){
        super(database, databaseName);
    }
    
    public Mute getMute(UUID uuid){
        Mute mute = (Mute) list.getOrDefault(uuid, null);
        if (mute == null){
            Document doc = database.findOneFast(TABLE_NAME, Filters.eq("UUID", uuid.toString()));
            if (doc == null) return null;
            mute = LyApi.getGson().fromJson(doc.toJson(), Mute.class);
            list.put(mute.getUUID(), mute);
        }
        return mute;
    }
    
    public LinkedList<Mute> getMutes(){
        return database.findMany(TABLE_NAME, Mute.class);
    }
    
    public void createMute(Mute mute){
        database.insertOne(TABLE_NAME, mute);
        list.put(mute.getUUID(), mute);
    }
    
    public void unloadMute(UUID uuid){
        list.remove(uuid);
    }
    
    public void deleteMute(Mute mute){
        database.deleteOne(TABLE_NAME, Filters.eq("UUID", mute.getUUID().toString()));
        list.remove(mute.getUUID());
    }
    
    public void deleteMuteByUUID(UUID uuid){
        database.deleteOne(TABLE_NAME, Filters.eq("UUID", uuid.toString()));
        list.remove(uuid);
    }
    
    public String serialize(Mute mute){
        return LyApi.getGson().toJson(mute);
    }
    
    public Mute deserialize(String s){
        return LyApi.getGson().fromJson(s, Mute.class);
    }
    
    public void fetchMutes(){
        for ( Mute mute : getMutes() ){
            list.put(mute.getUUID(), mute);
        }
    }
}
