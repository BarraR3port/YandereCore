package com.podcrash.commissions.yandere.core.spigot.config;

import net.lymarket.lyapi.spigot.config.Config;
import net.lymarket.lyapi.spigot.config.ReloadableConfig;
import org.bukkit.plugin.java.JavaPlugin;

@ReloadableConfig
public class SoundsConfig extends Config {
    
    
    public SoundsConfig(JavaPlugin plugin, String name) {
        super(plugin, name);
    }
    
    
    public SoundsConfig(JavaPlugin plugin, String name, String resourcePath, String filePath) {
        super(plugin, name, resourcePath, filePath);
    }
}
