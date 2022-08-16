package com.podcrash.comissions.yandere.core.spigot.config;

import net.lymarket.lyapi.spigot.config.Config;
import org.bukkit.plugin.java.JavaPlugin;

public final class YandereConfig extends Config {
    
    
    public YandereConfig(JavaPlugin plugin, String name){
        super(plugin, name);
    }
    
    
    public YandereConfig(JavaPlugin plugin, String name, String resourcePath, String filePath){
        super(plugin, name, resourcePath, filePath);
    }
}
