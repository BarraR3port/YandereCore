package com.podcrash.commissions.yandere.core.velocity.utils;

import com.podcrash.commissions.yandere.core.common.data.server.ServerType;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

public class Utils {
    
    public static Component format(String s){
        return Component.text(ChatColor.translateAlternateColorCodes('&', s));
    }
    
    public static String getPlayerServerFormatted(Player player){
        final String serverName = player.getCurrentServer().get().getServerInfo().getName();
        ServerType type = ServerType.match(serverName);
        return type.getName();
    }
}
