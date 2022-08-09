package com.podcrash.comissions.yandere.core.spigot.log;

import com.podcrash.comissions.yandere.core.common.data.logs.ILogRepository;
import com.podcrash.comissions.yandere.core.common.data.logs.Log;
import com.podcrash.comissions.yandere.core.common.data.logs.LogType;
import net.lymarket.common.db.MongoDBClient;

public class LogRepository extends ILogRepository<Log> {
    
    
    public LogRepository(MongoDBClient database, String tableName){
        super(database, tableName);
    }
    
    @Override
    public void trashFinder(){
    }
    
    @Override
    public Log createLog(LogType type, String server, String msg, String p){
        Log log = new Log(type, server, msg, p);
        database.insertOne(TABLE_NAME, log);
        return log;
    }
}