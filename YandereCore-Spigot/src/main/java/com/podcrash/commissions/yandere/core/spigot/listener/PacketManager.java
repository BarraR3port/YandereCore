package com.podcrash.commissions.yandere.core.spigot.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.podcrash.commissions.yandere.core.spigot.Main;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class PacketManager {
    
    
    public PacketManager(Main plugin){
        
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        manager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.TAB_COMPLETE) {
            
            @Override
            public void onPacketReceiving(PacketEvent event){
            
            }
            
            @Override
            public void onPacketSending(PacketEvent event){
                Player p = event.getPlayer();
                if (!p.hasPermission("yandere.staff.commands")){
                    PacketContainer packet = event.getPacket();
                    String[] commands = packet.getStringArrays().read(0);
                    ArrayList<String> newCommands = new ArrayList<>(Arrays.asList(commands));
                    List<String> commandsHidden = plugin.getConfig().getStringList("commands-hidden");
                    List<String> commandsHiddenStartsWith = plugin.getConfig().getStringList("commands-hidden-starts-with");
                    for ( String command : commands ){
                        boolean remove = false;
                        for ( String hiddenCommand : commandsHiddenStartsWith ){
                            if (command.startsWith(hiddenCommand)){
                                newCommands.remove(command);
                                remove = true;
                                break;
                            }
                        }
                        if (!remove){
                            if (command.startsWith("/")){
                                command = command.substring(1);
                            }
                            if (commandsHidden.contains(command)){
                                newCommands.remove("/" + command);
                            }
                        }
                    }
                    packet.getStringArrays().write(0, newCommands.toArray(new String[0]));
                }
            }
        });
        manager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.TAB_COMPLETE) {
            @Override
            public void onPacketReceiving(PacketEvent event){
                Player p = event.getPlayer();
                if (!p.hasPermission("yandere.staff.commands")){
                    PacketContainer packet = event.getPacket();
                    String text = packet.getStrings().read(0).substring(1);
                    List<String> commandsHidden = plugin.getConfig().getStringList("commands-hidden");
                    if (commandsHidden.contains(text)){
                        event.setCancelled(true);
                        return;
                    }
                    for ( String s : commandsHidden ){
                        if (text.startsWith(s)){
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
            
            @Override
            public void onPacketSending(PacketEvent event){
            }
        });
        
    }
}
