package com.podcrash.comissions.yandere.core.common.data.user.props;

import java.util.Arrays;

public enum Rank {
    ADMIN("admin", "§c§l「Admin⏌", "§c§l「Admin⏌ §f", "§c§lAdmin"),
    DEV("dev", "§d「Dev⏌", "§d「Dev⏌ §f", "§d§lDeveloper"),
    MOD("mod", "§9「Mod⏌", "§9「Mod⏌ §f", "§9Mod"),
    AFILIADO("afiliado", "§4「Af⏌", "§4「Afiliado⏌ §f", "§9Afiliado"),
    DIAMANTE("diamante", "§b§l「Diamante⏌ ", "§b§l「Diamante⏌ §f", "§b§lDiamante"),
    ORO("oro", "§e「Oro⏌", "§e「Oro⏌ §f", "§eOro"),
    HIERRO("hierro", "§7「Hierro⏌", "§7「Hierro⏌ §7", "§7Hierro"),
    BRONCE("bronce", "§6「Bronce⏌", "§6「Bronce⏌ §7", "§6Bronce"),
    USUARIO("default", "§7", "§7", "§7Usuario");
    
    private final String lpName;
    private final String prefix;
    
    private final String lpScoreBoardName;
    private final String tabPrefix;
    
    Rank(String lpName, String prefix, String tabPrefix, String lpScoreBoardName){
        this.lpName = lpName;
        this.prefix = prefix;
        this.tabPrefix = tabPrefix;
        this.lpScoreBoardName = lpScoreBoardName;
    }
    
    
    public static Rank fromString(String rank){
        return Arrays.stream(Rank.values()).filter(r -> r.getLpName().equalsIgnoreCase(rank)).findFirst().orElse(USUARIO);
    }
    
    public String getLpName(){
        return this.lpName;
    }
    
    public String getPrefix(){
        return this.prefix;
    }
    
    public String getTabPrefix(){
        return this.tabPrefix;
    }
    
    public String getScoreBoardName(){
        return lpScoreBoardName;
    }
    
    
    public boolean isAdmin(){
        return this == ADMIN || this == DEV;
    }
    
    public boolean isMod(){
        return this == MOD || this == ADMIN || this == DEV;
    }
    
}
