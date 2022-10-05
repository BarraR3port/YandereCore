package com.podcrash.commissions.yandere.core.common.data.logs;

public enum LogType {
    UNKNOWN("Sin Filtro", "&4", "GLASS"),
    CHAT("Chat", "&7", "PAPER"),
    COMMAND("Comando", "&7", "COMMAND_BLOCK"),
    TP("Teletransportación (En Desarrollo)", "&3", "ENDER_PEARL"),
    PUNISHMENT("Castigo", "&c", "BARRIER"),
    CONNECT("Conección", "&a", "LIME_WOOL"),
    DISCONNECT("Desconección", "&c", "RED_WOOL");
    
    private final String name;
    private final String color;
    private final String material;
    
    LogType(String name, String color, String material){
        this.name = name;
        this.color = color;
        this.material = material;
    }
    
    public String getName(){
        return color + name;
    }
    
    public String getColor(){
        return color;
    }
    
    public String getMaterial(){
        return material;
    }
}
