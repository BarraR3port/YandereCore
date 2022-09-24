package com.podcrash.commissions.yandere.core.common.data.server;


import java.util.UUID;

public class GlobalServerSettings {
    
    private final UUID uuid;
    private int maxBedWarsPlayers;
    private int maxSkyWarsPlayers;
    private int maxPracticePlayers;
    private int maxSurvivalGamesPlayers;
    private int maxTNTTagPlayers;
    private boolean bedWarsInDevelopment;
    private boolean skyWarsInDevelopment;
    private boolean practiceInDevelopment;
    private boolean survivalGamesInDevelopment;
    private boolean tntTagInDevelopment;
    
    public GlobalServerSettings(){
        this.uuid = UUID.randomUUID();
        this.maxBedWarsPlayers = 75;
        this.maxSkyWarsPlayers = 75;
        this.maxPracticePlayers = 100;
        this.maxSurvivalGamesPlayers = 50;
        this.maxTNTTagPlayers = 100;
        this.bedWarsInDevelopment = false;
        this.skyWarsInDevelopment = false;
        this.practiceInDevelopment = true;
        this.survivalGamesInDevelopment = true;
        this.tntTagInDevelopment = true;
        
    }
    
    public UUID getUuid(){
        return uuid;
    }
    
    public int getMaxBedWarsPlayers(){
        return maxBedWarsPlayers;
    }
    
    public void setMaxBedWarsPlayers(int maxBedWarsPlayers){
        this.maxBedWarsPlayers = maxBedWarsPlayers;
    }
    
    public int getMaxSkyWarsPlayers(){
        return maxSkyWarsPlayers;
    }
    
    public void setMaxSkyWarsPlayers(int maxSkyWarsPlayers){
        this.maxSkyWarsPlayers = maxSkyWarsPlayers;
    }
    
    public int getMaxPracticePlayers(){
        return maxPracticePlayers;
    }
    
    public void setMaxPracticePlayers(int maxPracticePlayers){
        this.maxPracticePlayers = maxPracticePlayers;
    }
    
    public int getMaxSurvivalGamesPlayers(){
        return maxSurvivalGamesPlayers;
    }
    
    public void setMaxSurvivalGamesPlayers(int maxSurvivalGamesPlayers){
        this.maxSurvivalGamesPlayers = maxSurvivalGamesPlayers;
    }
    
    public int getMaxTNTTagPlayers(){
        return maxTNTTagPlayers;
    }
    
    public void setMaxTNTTagPlayers(int maxTNTTagPlayers){
        this.maxTNTTagPlayers = maxTNTTagPlayers;
    }
    
    public boolean isBedWarsInDevelopment(){
        return bedWarsInDevelopment;
    }
    
    public void setBedWarsInDevelopment(boolean bedWarsInDevelopment){
        this.bedWarsInDevelopment = bedWarsInDevelopment;
    }
    
    public boolean isSkyWarsInDevelopment(){
        return skyWarsInDevelopment;
    }
    
    public void setSkyWarsInDevelopment(boolean skyWarsInDevelopment){
        this.skyWarsInDevelopment = skyWarsInDevelopment;
    }
    
    public boolean isPracticeInDevelopment(){
        return practiceInDevelopment;
    }
    
    public void setPracticeInDevelopment(boolean practiceInDevelopment){
        this.practiceInDevelopment = practiceInDevelopment;
    }
    
    public boolean isSurvivalGamesInDevelopment(){
        return survivalGamesInDevelopment;
    }
    
    public void setSurvivalGamesInDevelopment(boolean survivalGamesInDevelopment){
        this.survivalGamesInDevelopment = survivalGamesInDevelopment;
    }
    
    public boolean isTntTagInDevelopment(){
        return tntTagInDevelopment;
    }
    
    public void setTntTagInDevelopment(boolean tntTagInDevelopment){
        this.tntTagInDevelopment = tntTagInDevelopment;
    }
    
    public int getMax(ServerType serverType){
        switch(serverType){
            case BED_WARS:
            case LOBBY_BED_WARS:
                return maxBedWarsPlayers;
            case SKY_WARS:
                return maxSkyWarsPlayers;
            case PRACTICE:
                return maxPracticePlayers;
            case SURVIVAL:
                return maxSurvivalGamesPlayers;
            case TNT_TAG:
                return maxTNTTagPlayers;
            default:
                return 50;
        }
    }
}
