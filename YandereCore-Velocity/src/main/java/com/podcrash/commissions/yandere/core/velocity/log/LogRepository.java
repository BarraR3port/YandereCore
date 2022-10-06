package com.podcrash.commissions.yandere.core.velocity.log;

import com.podcrash.commissions.yandere.core.common.data.logs.Log;
import com.podcrash.commissions.yandere.core.common.data.logs.LogType;
import com.podcrash.commissions.yandere.core.common.log.ILogRepository;
import net.lymarket.lyapi.common.db.MongoDBClient;

import java.util.HashMap;

public class LogRepository extends ILogRepository {
    
    
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
    
    @Override
    public Log createLogWithProps(LogType type, String server, String msg, String p, HashMap<String, String> props){
        Log log = new Log(type, server, msg, p, props, new HashMap<>());
        database.insertOne(TABLE_NAME, log);
        list.put(log.getUuid(), log);
        return log;
    }
    
    @Override
    public Log createLogWithOptions(LogType type, String server, String msg, String p, HashMap<String, Boolean> options){
        Log log = new Log(type, server, msg, p, new HashMap<>(), options);
        database.insertOne(TABLE_NAME, log);
        list.put(log.getUuid(), log);
        return log;
    }
    
    @Override
    public Log createLog(LogType type, String server, String msg, String p, HashMap<String, String> props, HashMap<String, Boolean> options){
        Log log = new Log(type, server, msg, p, props, options);
        database.insertOne(TABLE_NAME, log);
        list.put(log.getUuid(), log);
        return log;
    }
}