package com.podcrash.comissions.yandere.core.common.data.user.props;

import java.util.Arrays;

public enum Rank {
    ADMIN("admin", "&c&l「Admin⏌", "&c&l「Admin⏌ ", "&c&lAdmin"),
    DEV("dev", "&d&l「Dev⏌", "&d&l「Dev⏌ ", "&d&lDeveloper"),
    MOD("mod", "&9「Mod⏌", "&9&l「Mod⏌ ", "&9&lMod"),
    AFILIADO("afiliado", "&4「Af⏌", "&4&l「Afiliado⏌ ", "&9&lAfiliado"),
    DIAMANTE("diamante", "&b「Diamante⏌", "&b&l「Diamante⏌ ", "&b&lDiamante"),
    ORO("oro", "&e「Oro⏌", "&e&l「Oro⏌ ", "&e&lOro"),
    HIERRO("hierro", "&7「Hierro⏌", "&7&l「Hierro⏌ ", "&7&lHierro"),
    BRONCE("bronce", "&6「Bronce⏌", "&6&l「Bronce⏌ ", "&6&Bronce"),
    USUARIO("default", "&7", "&7", "&7Usuario");
    
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
