package com.podcrash.commissions.yandere.core.common.data.reward;

public enum RewardType {
    COMMON("&7Común", "%7"),
    NORMAL("&eNormal", "%7"),
    RARE("&1Raro", "%7"),
    EPIC("&dÉpico", "%7"),
    LEGENDARY("&6Legendario", "%7");
    private final String name;
    private final String color;
    
    RewardType(String name, String color){
        this.name = name;
        this.color = color;
    }
    
    public String getName(){
        return name;
    }
    
    public String getColor(){
        return color;
    }
}
