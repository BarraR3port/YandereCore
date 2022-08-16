package com.podcrash.commissions.yandere.core.common.data.level;


import com.podcrash.commissions.yandere.core.common.data.server.ChangeType;

import java.text.DecimalFormat;
import java.util.UUID;

public class Level {
    
    private final UUID uuid;
    private final OwnerType ownerType;
    private int level;
    private String levelName;
    private int currentXp;
    
    public Level(UUID uuid, int level, int currentXp, OwnerType ownerType){
        this.uuid = uuid;
        if (level <= 1) level = 1;
        if (currentXp < 0) currentXp = 0;
        
        this.level = level;
        this.currentXp = currentXp;
        this.levelName = getLevelNameFormatted();
        this.ownerType = ownerType;
        
    }
    
    public UUID getUuid(){
        return uuid;
    }
    
    public int getLevel(){
        return level;
    }
    
    public void setLevel(int level){
        this.level = level <= 0 ? 1 : level;
        levelName = getLevelNameFormatted();
        currentXp = 0;
        /*if ( ownerType == OwnerType.PLAYER ) {
            Bukkit.getPluginManager( ).callEvent( new PlayerLevelChangeEvent( Bukkit.getPlayer( this.uuid ) , this , ChangeType.SET ) );
        }*/
    }
    
    public String getLevelName(){
        return levelName;
    }
    
    public int getCurrentXp(){
        return currentXp;
    }
    
    public OwnerType getOwnerType(){
        return ownerType;
    }
    
    public void addXp(int amount){
        setTotalCurrentXp(getTotalCurrentXp() + amount, ChangeType.ADD);
    }
    
    public void removeXp(int amount){
        setTotalCurrentXp(getTotalCurrentXp() - amount, ChangeType.REMOVE);
    }
    
    public void setXp(int amount){
        setTotalCurrentXp(amount, ChangeType.SET);
    }
    
    private int getTotalCurrentXp(){
        int totalCurrentXp = 0;
        for ( int i = 1; i < level; i++ ){
            totalCurrentXp += getNextLevelXp(i);
        }
        return totalCurrentXp + currentXp;
    }
    
    public void setTotalCurrentXp(int amount, ChangeType changeType){
        int currentLevel = 1;
        while (amount >= getNextLevelXp(currentLevel)) {
            amount = amount - getNextLevelXp(currentLevel);
            currentLevel++;
        }
        /*if ( level != currentLevel && ownerType == OwnerType.PLAYER ) {
            Bukkit.getPluginManager( ).callEvent( new PlayerLevelChangeEvent( Bukkit.getPlayer( this.uuid ) , this , changeType ) );
        }*/
        
        level = currentLevel;
        currentXp = Math.max(amount, 0);
        levelName = getLevelNameFormatted();
    }
    
    public int getNextLevelXp(int level){
        switch(level){
            case 1:
                return 1000;
            case 2:
                return 2000;
            case 3:
                return 3000;
            case 4:
                return 3500;
            default:
                return 5000;
        }
    }
    
    public int getTotalNextLevelXp(){
        final int nextLevel = level + 1;
        int totalXpNextLevel = 0;
        for ( int i = 1; i < nextLevel; i++ ){
            totalXpNextLevel += getNextLevelXp(i);
        }
        return totalXpNextLevel;
    }
    
    public int getTotalNextLevelXp(int level){
        level++;
        int totalXpNextLevel = 0;
        for ( int i = 1; i < level; i++ ){
            totalXpNextLevel += getNextLevelXp(i);
        }
        return totalXpNextLevel;
    }
    
    public int getNextLevelXp(){
        final int level = this.level + 1;
        switch(level){
            case 1:
                return 1000;
            case 2:
                return 2000;
            case 3:
                return 3000;
            case 4:
                return 3500;
            default:
                return 5000;
        }
    }
    
    public String getProgressBar(){
        double l1 = ((getNextLevelXp() - currentXp) / (double) (getNextLevelXp())) * 10;
        int locked = (int) l1;
        int unlocked = 10 - locked;
        if (locked < 0 || unlocked < 0){
            locked = 10;
            unlocked = 0;
        }
        return "§8 [{progress}§8]" .replace("{progress}", "&d" + String.valueOf(new char[unlocked]).replace("\0", "■") + "&7" + String.valueOf(new char[locked]).replace("\0", "■"));
    }
    
    public String getFormattedRequiredXp(){
        final int nextLevelCost = getNextLevelXp();
        return nextLevelCost >= 1000000 ? nextLevelCost % 1000000 == 0 ? nextLevelCost / 1000000 + "M" : df((double) nextLevelCost / 1000000) + "M" : nextLevelCost >= 1000 ? nextLevelCost % 1000 == 0 ? nextLevelCost / 1000 + "k" : df((double) nextLevelCost / 1000) + "k" : String.valueOf(nextLevelCost);
    }
    
    public String getFormattedCurrentXp(){
        return currentXp >= 1000000 ? currentXp % 1000000 == 0 ? currentXp / 1000000 + "M" : df((double) currentXp / 1000000) + "M" : currentXp >= 1000 ? currentXp % 1000 == 0 ? currentXp / 1000 + "k" : df((double) currentXp / 1000) + "k" : String.valueOf(currentXp);
    }
    
    public String getProgressXP(){
        return (getCurrentXp() * 1000) / (getNextLevelXp(this.level) == 0 ? 1 : getNextLevelXp(this.level)) / 10 + "%";
    }
    
    public int getNextCost(int level){
        return (level != 1) ? (int) ((1250 * (Math.pow(level, 2)) + (6250 * level) - 7500) / 2) : 2500;
    }
    
    private String df(double d){
        return new DecimalFormat("#.#").format(d);
    }
    
    public void addLevels(int levels){
        level += levels;
        levelName = getLevelNameFormatted();
        currentXp = 0;
    }
    
    public void removeLevels(int levels){
        level -= levels;
        levelName = getLevelNameFormatted();
        currentXp = 0;
    }
    
    /**
     * Retorna los niveles en colores (Desde 400 en adelante quedan con el mismo color).
     */
    public String getLevelNameFormatted(){
        if (level >= 1 && level < 5){
            return "§8[" + level + "✩]";
        } else if (level >= 5 && level < 15){
            return "§7[" + level + "✩]";
        } else if (level >= 15 && level < 20){
            return "§a[" + level + "✩]";
        } else if (level >= 20 && level < 25){
            return "§2[" + level + "✩]";
        } else if (level >= 25 && level < 30){
            return "§e[" + level + "✩]";
        } else if (level >= 30 && level < 35){
            return "§6[" + level + "✩]";
        } else if (level >= 35 && level < 40){
            return "§6&l[" + level + "✩]";
        } else if (level >= 40 && level < 45){
            return "§b[" + level + "✩]";
        } else if (level >= 45 && level < 50){
            return "§b&l[" + level + "✩]";
        } else if (level >= 50 && level < 55){
            return "§1[" + level + "✩]";
        } else if (level >= 55 && level < 60){
            return "§1&l[" + level + "✩]";
        } else if (level >= 60 && level < 65){
            return "§2[" + level + "✩]";
        } else if (level >= 65 && level < 70){
            return "§2&l[" + level + "✩]";
        } else if (level >= 70 && level < 80){
            return "§c[" + level + "✩]";
        } else if (level >= 80 && level < 90){
            return "§c&l[" + level + "✩]";
        } else if (level >= 90 && level < 100){
            return "§4[" + level + "✩]";
        } else {
            return "§4&l[" + level + "✩]";
        }
    }
    
    public enum OwnerType {
        PLAYER, TEAM, CLAN, OTHER
    }
    
    public enum GainSource {
        PER_MINUTE, PER_TEAMMATE, GAME_WIN, GAME_LOSE, PER_KILL, PER_DEATH, PER_ASSISTANT, OTHER, COMMAND
    }
}
