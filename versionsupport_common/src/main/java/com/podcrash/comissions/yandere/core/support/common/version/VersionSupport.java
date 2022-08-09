package com.podcrash.comissions.yandere.core.support.common.version;

import com.podcrash.comissions.yandere.core.common.BBBApi;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class VersionSupport<U> {
    
    protected final JavaPlugin plugin;
    protected final BBBApi<U> bbbApi;
    
    public VersionSupport(JavaPlugin plugin){
        this.plugin = plugin;
        this.bbbApi = (BBBApi) plugin;
        switch(bbbApi.getServerType()){
            case LOBBY:{
                break;
            }
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
    
    public BBBApi<U> getBbbApi(){
        return bbbApi;
    }
    
    public abstract void registerPlotEvents();
    
    public abstract void registerWorldEvents();
    
    public abstract void saveWorlds();
}
