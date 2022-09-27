package com.podcrash.commissions.yandere.core.common.data.practice;

public enum PracticeQueueType {
    UNRANKED("&3Unranked"),
    RANKED("&aRanked"),
    PREMIUM("&5Premium"),
    BOT("&4Bot"),
    EDIT_KIT("&2Editar Kits"),
    PARTY_UNRANKED("&3Unranked Party"),
    PARTY_RANKED("&aRanked Party");
    
    private final String displayName;
    
    PracticeQueueType(String displayName){
        this.displayName = displayName;
    }
    
    public String getDisplayName(){
        return displayName;
    }
}
