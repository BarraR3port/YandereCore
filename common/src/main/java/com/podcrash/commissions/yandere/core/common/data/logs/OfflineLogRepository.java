package com.podcrash.commissions.yandere.core.common.data.logs;

import com.podcrash.commissions.yandere.core.common.log.ILogRepository;

public final class OfflineLogRepository extends ILogRepository {
    
    
    public OfflineLogRepository(){
        super(null, "logs");
    }
    
    @Override
    public Log createLog(LogType type, String server, String msg, String p){
        return new Log(type, server, msg, p);
    }
    
    @Override
    public void trashFinder(){
    
    }
}
