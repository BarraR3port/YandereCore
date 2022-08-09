package com.podcrash.comissions.yandere.core.spigot.listener.plugin;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.podcrash.comissions.yandere.core.spigot.Main;
import com.podcrash.comissions.yandere.core.spigot.settings.Settings;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class PluginMessage implements PluginMessageListener {
    
    
    public PluginMessage(){
    
    }
    
    @Override
    public void onPluginMessageReceived(String channel, Player p, byte[] msg){
        if (channel.equals("lymarket:bbb") || channel.equals("BungeeCord")){
            ByteArrayDataInput in = ByteStreams.newDataInput(msg);
            String subChannel = in.readUTF();
            switch(subChannel){
                case "GetServer":{
                    String servername = in.readUTF();
                    if (Settings.SERVER_NAME == null){
                        Main.getInstance().getConfig().set("global.proxy-server-name", servername);
                        Main.getInstance().getConfig().saveData();
                    }
                    Settings.SERVER_NAME = servername;
                    
                    
                }
                
            }
        }
    }
    
    
}
