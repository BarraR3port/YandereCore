package com.podcrash.commissions.yandere.core.common.data.server;

import net.lymarket.lyapi.common.db.MongoDB;
import net.lymarket.lyapi.common.db.MongoDBClient;

import java.util.UUID;

public abstract class IServerRepository extends MongoDB<UUID, Server> {
    
    public IServerRepository(MongoDBClient database, String TABLE_NAME){
        super(database, TABLE_NAME);
    }
    
    public abstract Server initializeServer();
    
    public abstract Server getServer();
    
    public abstract boolean isServerRegistered();
    
    public abstract void checkForPluginsUpdates();
    
}
