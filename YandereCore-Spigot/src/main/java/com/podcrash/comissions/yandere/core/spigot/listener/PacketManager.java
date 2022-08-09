package com.podcrash.comissions.yandere.core.spigot.listener;

import org.bukkit.plugin.java.JavaPlugin;

public final class PacketManager {
    
    
    public PacketManager(JavaPlugin plugin){
        
        /*ProtocolManager manager = ProtocolLibrary.getProtocolManager( );
        manager.addPacketListener( new PacketAdapter( plugin , ListenerPriority.NORMAL ,  PacketType.Play.Client.TAB_COMPLETE ) {
            
            *//**
         * @param event
         *//*
            @Override
            public void onPacketReceiving( PacketEvent event ){
                PacketContainer packet = event.getPacket( );
                System.out.println(packet );
                Player p = event.getPlayer( );
                String text = packet.getStrings().read( 0 );
                boolean assumeCommand = packet.getBooleans().read( 0 );
                *//*Position position = ;*//*
                
                p.sendMessage( "text: " + text  + " AssumeCommand: " + assumeCommand  );
                
                PacketContainer packetToSend = manager.createPacket( PacketType.Play.Server.TAB_COMPLETE );
                String[] s = new String[ ] { "test", "test2" };
                packetToSend.getStringArrays().write( 0 , s );
                try {
                    manager.sendServerPacket( p, packetToSend );
                } catch ( InvocationTargetException e ) {
                    throw new RuntimeException( e );
                }
                
            }
        } );*/
        
    }
}
