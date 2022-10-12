package com.podcrash.commissions.yandere.core.spigot.config;

import net.lymarket.lyapi.spigot.config.Config;
import net.lymarket.lyapi.spigot.config.ReloadableConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

@ReloadableConfig
public final class YandereConfig extends Config {
    
    
    public YandereConfig(JavaPlugin plugin, String name){
        super(plugin, name);
    
        if (getConfigVersion() != 1.2){
            set("config-version", 1.2);
            set("commands-hidden", Arrays.asList("?", "about", "plugins", "pl", "ver", "version", "icanhasbukkit", "worldedit", "fawe", "fawecancel", "velocity", "anvil", ";", "we", "brush", "vis", "visual", "visualize"));
            set("commands-hidden-starts-with", Arrays.asList("/fastasyncworldedit", "//", "/bukkit", "/worldedit", "/luckperms", "/placeholderapi", "/jumppads", "/holographicdisplays"));
            saveData();
        }
    }
    
    
    public YandereConfig(JavaPlugin plugin, String name, String resourcePath, String filePath){
        super(plugin, name, resourcePath, filePath);
    }
}
