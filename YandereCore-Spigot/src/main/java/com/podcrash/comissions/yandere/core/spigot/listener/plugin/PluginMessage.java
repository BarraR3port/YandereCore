package com.podcrash.comissions.yandere.core.spigot.listener.plugin;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.podcrash.comissions.yandere.core.common.data.server.ServerType;
import com.podcrash.comissions.yandere.core.spigot.Main;
import com.podcrash.comissions.yandere.core.spigot.settings.Settings;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class PluginMessage implements PluginMessageListener {
    
    
    public PluginMessage(){
    
    }
    
    @Override
    public void onPluginMessageReceived(String channel, Player p, byte[] msg){
        if (channel.equals("podcrash:yandere") || channel.equals("BungeeCord")){
            ByteArrayDataInput in = ByteStreams.newDataInput(msg);
            String subChannel = in.readUTF();
            if ("YourServerName" .equals(subChannel)){
                String servername = in.readUTF();
                Settings.SERVER_NAME = servername;
                Settings.SERVER_TYPE = ServerType.match(servername);
                Main.getInstance().getConfig().set("global.proxy-server-name", servername);
                Main.getInstance().getConfig().set("global.server-type", Settings.SERVER_TYPE.toString());
                Main.getInstance().getConfig().saveData();
                Main.getInstance().getServer().shutdown();
                
            }
        }
    }
    
    
}
