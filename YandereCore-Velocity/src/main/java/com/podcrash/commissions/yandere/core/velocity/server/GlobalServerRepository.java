package com.podcrash.commissions.yandere.core.velocity.server;

import com.mongodb.client.model.Filters;
import com.podcrash.commissions.yandere.core.common.data.server.GlobalServerSettings;
import com.podcrash.commissions.yandere.core.common.data.server.IGlobalServerRepository;
import com.podcrash.commissions.yandere.core.common.error.GlobalServerNotFoundException;
import com.podcrash.commissions.yandere.core.velocity.VMain;
import net.lymarket.lyapi.common.db.MongoDBClient;
import org.bson.Document;

public class GlobalServerRepository extends IGlobalServerRepository {
    
    public GlobalServerRepository(MongoDBClient database, String tableName){
        super(database, tableName);
    }
    
    @Override
    public GlobalServerSettings getOrCreate(){
        if (globalServerSettings == null){
            return create();
        }
        return globalServerSettings;
    }
    
    @Override
    public GlobalServerSettings create(){
        GlobalServerSettings settings = fetch();
        if (settings == null){
            settings = new GlobalServerSettings();
            database.insertOne(TABLE_NAME, settings);
        }
        return settings;
    }
    
    @Override
    public GlobalServerSettings save(){
        if (database.replaceOneFast(TABLE_NAME, Filters.eq("uuid", globalServerSettings.getUuid()), globalServerSettings)){
            return globalServerSettings;
        } else {
            if (database.insertOne(TABLE_NAME, globalServerSettings)){
                return globalServerSettings;
            } else {
                VMain.debug("[Error] While saving Global Server Settings to database.");
            }
        }
        return null;
    }
    
    @Override
    public GlobalServerSettings fetch(){
        Document doc = database.findOneFast(TABLE_NAME, Filters.empty());
        if (doc == null) throw new GlobalServerNotFoundException();
        globalServerSettings = database.getGson().fromJson(doc.toJson(), GlobalServerSettings.class);
        return globalServerSettings;
    }
    
}
