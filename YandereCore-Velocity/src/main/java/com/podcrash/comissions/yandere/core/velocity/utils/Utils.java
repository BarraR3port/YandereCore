package com.podcrash.comissions.yandere.core.velocity.utils;

import net.kyori.adventure.text.Component;

public class Utils {
    
    public static Component format(String s){
        return Component.text(ChatColor.translateAlternateColorCodes('&', s));
    }
    
}
