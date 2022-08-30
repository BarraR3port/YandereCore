package com.podcrash.commissions.yandere.core.common.data.user.props;

import java.util.Arrays;

public enum Rank {
    ADMIN("admin", "§c§l「Admin⏌ §f", "§c§l「Admin⏌ §f", "§c§lAdmin"),
    DEV("dev", "§d「Dev⏌ §f", "§d「Dev⏌ §f", "§d§lDeveloper"),
    MOD("mod", "§9「Mod⏌ §f", "§9「Mod⏌ §f", "§9Mod"),
    AFILIADO("afiliado", "§4「Af⏌ §f", "§4「Afiliado⏌ §f", "§4Afiliado"),
    DIAMANTE("diamante", "§b§l「D⏌ §f", "§b§l「Diamante⏌ §f", "§b§lDiamante"),
    ORO("oro", "§e「O⏌ §f", "§e「Oro⏌ §f", "§eOro"),
    HIERRO("hierro", "§7「H⏌ §7", "§7「Hierro⏌ §7", "§7Hierro"),
    BRONCE("bronce", "§6「B⏌ §7", "§6「Bronce⏌ §7", "§6Bronce"),
    USUARIO("default", "§7", "§7", "§7Usuario");
    
    private final String lpName;
    private final String tagNamePrefix;
    private final String tabPrefix;
    private final String lpScoreBoardName;
    
    Rank(String lpName, String tagNamePrefix, String tabPrefix, String lpScoreBoardName){
        this.lpName = lpName;
        this.tagNamePrefix = tagNamePrefix;
        this.tabPrefix = tabPrefix;
        this.lpScoreBoardName = lpScoreBoardName;
    }
    
    
    public static Rank fromString(String rank){
        return Arrays.stream(Rank.values()).filter(r -> r.getLpName().equalsIgnoreCase(rank)).findFirst().orElse(USUARIO);
    }
    
    public String getLpName(){
        return this.lpName;
    }
    
    public String getTagNamePrefix(){
        return this.tagNamePrefix;
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
