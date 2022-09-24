package com.podcrash.commissions.yandere.core.common.data.server;

import net.lymarket.lyapi.common.db.MongoDB;
import net.lymarket.lyapi.common.db.MongoDBClient;

import java.util.UUID;

public abstract class IGlobalServerRepository extends MongoDB<UUID, GlobalServerSettings> {
    
    
    protected GlobalServerSettings globalServerSettings;
    
    public IGlobalServerRepository(MongoDBClient database, String TABLE_NAME){
        super(database, TABLE_NAME);
        create();
    }
    
    public abstract GlobalServerSettings getOrCreate();
    
    public abstract GlobalServerSettings create();
    
    public abstract GlobalServerSettings save();
    
    public abstract GlobalServerSettings fetch();
    
}
