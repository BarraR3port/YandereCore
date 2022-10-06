package com.podcrash.commissions.yandere.core.velocity.config;

import com.podcrash.commissions.yandere.core.velocity.VMain;
import de.leonhard.storage.Config;

import java.util.Collections;

public interface VConfig {
    
    static void defaultConfig(){
        Config config = VMain.getInstance().getConfig();
        config.setDefault("web.url", "http://209.222.97.163:3000");
        config.setDefault("web.uuid", null);
        config.setDefault("web.pluginsToDelete", Collections.emptyList());
        config.setDefault("web.pluginsConfigured", false);
        config.setDefault("web.key", "ASDJ209KASDHKADJ2KASDJGHASDK4KAHSKKDHGHJA8JA20LASDHGKALSKVMXJNASDOJG");
        
        config.setDefault("global.debug", true);
        
        config.setDefault("db.host", "172.18.0.1");
        config.setDefault("db.enabled", true);
        config.setDefault("db.port", "27017");
        config.setDefault("db.user", "yandere");
        config.setDefault("db.password", "JF993KKLALLDJJ2KLALLGJASKJ29I9ASDK");
        config.setDefault("db.database", "yandere");
        config.setDefault("db.urli", "mongodb://yandere:JF993KKLALLDJJ2KLALLGJASKJ29I9ASDK@172.18.0.1:27017/");
        
        
    }
}

