package com.podcrash.commissions.yandere.core.common.data.logs;

public enum LogType {
    PUNISHMENT("Castigo", "&c"),
    COMMAND("Comando", "&7"),
    CONNECT("Conección", "&a"),
    DISCONNECT("Desconección", "&c"),
    CHAT("Chat", "&7"),
    TP("Teleportación", "&3"),
    UNKNOWN("Desconocido", "&4");
    
    private final String name;
    private final String color;
    
    LogType(String name, String color){
        this.name = name;
        this.color = color;
    }
    
    public String getName(){
        return color + name;
    }
    
    public String getColor(){
        return color;
    }
}
