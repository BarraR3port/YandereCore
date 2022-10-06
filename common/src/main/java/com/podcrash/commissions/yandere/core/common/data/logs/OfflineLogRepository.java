package com.podcrash.commissions.yandere.core.common.data.logs;

import com.podcrash.commissions.yandere.core.common.log.ILogRepository;

import java.util.HashMap;

public final class OfflineLogRepository extends ILogRepository {
    
    
    public OfflineLogRepository(){
        super(null, "logs");
    }
    
    @Override
    public Log createLog(LogType type, String server, String msg, String p){
        return new Log(type, server, msg, p);
    }
    
    @Override
    public Log createLogWithProps(LogType type, String server, String msg, String p, HashMap<String, String> props){
        return null;
    }
    
    @Override
    public Log createLogWithOptions(LogType type, String server, String msg, String p, HashMap<String, Boolean> options){
        return null;
    }
    
    @Override
    public Log createLog(LogType type, String server, String msg, String p, HashMap<String, String> props, HashMap<String, Boolean> options){
        return null;
    }
    
    @Override
    public void trashFinder(){
    
    }
}
