package com.podcrash.commissions.yandere.core.support.common.version;

import com.podcrash.commissions.yandere.core.common.YandereApi;
import com.podcrash.commissions.yandere.core.common.data.server.ServerType;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class VersionSupport<U> {
    
    protected final JavaPlugin plugin;
    protected final YandereApi bbbApi;
    
    public VersionSupport(JavaPlugin plugin){
        this.plugin = plugin;
        this.bbbApi = (YandereApi) plugin;
        if (bbbApi.getServerType() == ServerType.LOBBY){
            /*case PLOT:{
                if (Bukkit.getPluginManager().getPlugin("PlotSquared") != null){
                    registerPlotEvents();
                }
                break;
            }
            case WORLDS:{
                registerWorldEvents();
                break;
            }*/
        }
        
        
    }
    
    public YandereApi getBbbApi(){
        return bbbApi;
    }
    
    public abstract void registerPlotEvents();
    
    public abstract void registerWorldEvents();
    
    public abstract void saveWorlds();
}
