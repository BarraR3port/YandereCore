package com.podcrash.commissions.yandere.core.common.data.punish.ban;

import com.podcrash.commissions.yandere.core.common.data.punish.Punish;
import com.podcrash.commissions.yandere.core.common.data.punish.PunishState;
import net.lymarket.lyapi.common.db.MongoDB;
import net.lymarket.lyapi.common.db.MongoDBClient;
import net.lymarket.lyapi.spigot.LyApi;
import org.bson.Document;

import java.util.LinkedList;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class BansRepository extends MongoDB<UUID, Punish> {
    
    
    public BansRepository(MongoDBClient database, String databaseName){
        super(database, databaseName);
    }
    
    public Ban getBan(UUID uuid){
        Ban ban = (Ban) list.getOrDefault(uuid, null);
        if (ban == null){
            Document doc = database.findOneFast(TABLE_NAME, eq("UUID", uuid.toString()));
            if (doc == null) return null;
            ban = LyApi.getGson().fromJson(doc.toJson(), Ban.class);
            list.put(ban.getUUID(), ban);
        }
        return ban;
    }
    
    public LinkedList<Ban> getBans(){
        return database.findMany(TABLE_NAME, Ban.class);
    }
    
    public LinkedList<Ban> getBansByIp(String ip){
        return database.findManyFiltered(TABLE_NAME, and(
                eq("Ip", true),
                eq("address", ip),
                eq("state", PunishState.ACTIVE.name())
        ), Ban.class);
    }
    
    public LinkedList<Ban> getBansByName(String name){
        return database.findManyFiltered(TABLE_NAME, and(
                eq("punished", name),
                eq("state", PunishState.ACTIVE.name())
        ), Ban.class);
    }
    
    public void createBan(Ban ban){
        database.insertOne(TABLE_NAME, ban);
        list.put(ban.getUUID(), ban);
    }
    
    public Ban updateBanState(UUID uuid, PunishState state){
        Ban ban = getBan(uuid);
        ban.setState(state);
        database.replaceOneFast(TABLE_NAME, eq("UUID", uuid.toString()), ban);
        list.put(uuid, ban);
        return ban;
    }
    
    
    public void unloadBan(UUID uuid){
        list.remove(uuid);
    }
    
    public void deleteBan(Ban ban){
        database.deleteOne(TABLE_NAME, eq("UUID", ban.getUUID().toString()));
        list.remove(ban.getUUID());
    }
    
    public void deleteBanByUUID(UUID uuid){
        database.deleteOne(TABLE_NAME, eq("UUID", uuid.toString()));
        list.remove(uuid);
    }
    
    public String serialize(Ban ban){
        return LyApi.getGson().toJson(ban);
    }
    
    public Ban deserialize(String s){
        return LyApi.getGson().fromJson(s, Ban.class);
    }
    
    public void fetchBans(){
        for ( Ban ban : getBans() ){
            list.put(ban.getUUID(), ban);
        }
    }
    
    public LinkedList<String> getBannedPlayers(){
        return database.findManyFiltered(TABLE_NAME,
                        eq("punishState", PunishState.ACTIVE.name()
                        ), Ban.class)
                .stream()
                .map(Punish::getPunished)
                .collect(Collectors.toCollection(LinkedList::new));
        
    }
}
