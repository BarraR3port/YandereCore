package com.podcrash.commissions.yandere.core.common.data.server;

public enum ServerType {
    LOBBY("LOBBY-", "Lobby", 50),
    REGISTRO("RG-", "Registro", 20),
    SKY_WARS("SW-", "SkyWars", 75),
    LOBBY_BED_WARS("LB-BW-", "Lobby BedWars", 50),
    BED_WARS("BW-", "BedWars", 75),
    TNT_TAG("TNT-TAG-", "TNT Tag", 100),
    PRACTICE("PR-", "Practice", 50),
    SURVIVAL("SG-", "Survival", 50),
    EMPTY("EMPTY", "Empty", 0);
    
    private final String prefix;
    private final String name;
    private final int maxPlayers;
    
    ServerType(String prefix, String name, int maxPlayers){
        this.prefix = prefix;
        this.name = name;
        this.maxPlayers = maxPlayers;
    }
    
    public static ServerType match(String serverName){
        for ( ServerType type : values() ){
            if (serverName.startsWith(type.prefix) || serverName.equalsIgnoreCase(type.prefix)){
                return type;
            }
        }
        return EMPTY;
    }
    
    public static boolean matchServerType(String serverName, ServerType type){
        return match(serverName).equals(type);
    }
    
    public String getPrefix(){
        return prefix;
    }
    
    public String getName(){
        return name;
    }
    
    public int getMaxPlayers(){
        return maxPlayers;
    }
}
