package com.podcrash.commissions.yandere.core.spigot.sounds;

import com.cryptomorin.xseries.XSound;
import net.lymarket.lyapi.spigot.config.Config;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Sounds {
    
    public static XSound GOOD_SOUND;
    
    public static XSound BAD_SOUND;
    
    public static XSound SUPER_GOOD_SOUND;
    
    private static Config config;
    
    
    public static void init(Config config){
        Sounds.config = config;
        GOOD_SOUND = XSound.valueOf(config.getString("sounds.good-sound"));
        
        BAD_SOUND = XSound.valueOf(config.getString("sounds.bad-sound"));
        
        SUPER_GOOD_SOUND = XSound.valueOf(config.getString("sounds.super-good-sound"));
        
    }
    
    public static void play(Player player, String sound){
        XSound.valueOf(config.getString("sounds." + sound)).play(player);
    }
    
    public static void play(Location location, String sound){
        XSound.valueOf(config.getString("sounds." + sound)).play(location);
    }
}
