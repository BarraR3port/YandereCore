package com.podcrash.commissions.yandere.core.common.data.punish.report;

import com.podcrash.commissions.yandere.core.common.data.punish.Punish;
import com.podcrash.commissions.yandere.core.common.data.punish.PunishType;

public class Report extends Punish {
    
    
    public Report(String punished, String punisher, String reason, String server){
        super(punished, punisher, reason, PunishType.REPORT, server);
    }
    
    
}
