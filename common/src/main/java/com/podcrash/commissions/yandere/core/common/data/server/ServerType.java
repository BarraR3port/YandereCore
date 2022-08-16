package com.podcrash.commissions.yandere.core.common.data.server;

public enum ServerType {
    LOBBY("LOBBY-", "Lobby"),
    REGISTRO("RG-", "Registro"),
    SKY_WARS("SW-", "SkyWars"),
    LOBBY_BED_WARS("LB-BW-", "Lobby BedWars"),
    BED_WARS("BW-", "BedWars"),
    TNT_TAG("TNT-TAG-", "TNT Tag"),
    PRACTICE("PR-", "Practice"),
    SURVIVAL("SG-", "Survival"),
    EMPTY("EMPTY", "Empty");
    
    private final String prefix;
    private final String name;
    
    ServerType(String prefix, String name){
        this.prefix = prefix;
        this.name = name;
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
}
