package com.podcrash.comissions.yandere.core.common.data.user;

import com.podcrash.comissions.yandere.core.common.data.level.Level;
import com.podcrash.comissions.yandere.core.common.data.lobby.PlayerVisibility;
import com.podcrash.comissions.yandere.core.common.data.loc.Loc;
import com.podcrash.comissions.yandere.core.common.data.user.props.Rank;
import com.podcrash.comissions.yandere.core.common.data.user.props.Reward;
import com.podcrash.comissions.yandere.core.common.data.user.props.Stats;
import com.podcrash.comissions.yandere.core.common.skin.SkinManager;
import net.lymarket.common.Api;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class User {
    private final Date createDate = new Date();
    private final LinkedHashMap<String, Boolean> options = new LinkedHashMap<>();
    private final LinkedHashMap<String, String> properties = new LinkedHashMap<>();
    private final LinkedList<Reward> rewards = new LinkedList<>();
    private Rank rank;
    private long coins;
    private Level level;
    private UUID uuid;
    private Stats stats;
    private String name;
    private String address;
    private String skin;
    private Loc lastLocation;
    
    public User(String name, UUID uuid){
        this.name = name;
        this.uuid = uuid;
        stats = new Stats();
        skin = SkinManager.getSkin(name);
        rank = Rank.USUARIO;
        this.level = new Level(uuid, 1, 0, Level.OwnerType.PLAYER);
        this.coins = 0;
        addDefaultProps();
        addDefaultOptions();
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public Date getCreateDate(){
        return createDate;
    }
    
    public @NotNull UUID getUUID(){
        return uuid;
    }
    
    public void setUUID(UUID uuid){
        this.uuid = uuid;
    }
    
    @Override
    public String toString(){
        return Api.getGson().toJson(this);
    }
    
    public boolean getOption(String key){
        return options.getOrDefault(key, false);
    }
    
    public void removeOption(String key){
        options.remove(key);
    }
    
    public void setOption(String key, boolean value){
        options.put(key, value);
    }
    
    public String getProperty(String key){
        return properties.get(key);
    }
    
    public void removeProperty(String key){
        properties.remove(key);
    }
    
    public void addProperty(String key, String value){
        properties.put(key, value);
    }
    
    public String serialize(){
        return Api.getGson().toJson(this);
    }
    
    public User deserialize(String s){
        return Api.getGson().fromJson(s, User.class);
    }
    
    public HashMap<String, String> getProperties(){
        return properties;
    }
    
    public Date getDateCreate(){
        return this.createDate;
    }
    
    public String getAddress(){
        return address;
    }
    
    public void setAddress(String address){
        this.address = address;
    }
    
    public Stats getStats(){
        return stats;
    }
    
    public void setStats(Stats stats){
        this.stats = stats;
    }
    
    public String getSkin(){
        return skin;
    }
    
    public void updateSkin(){
        this.skin = SkinManager.getSkin(this.name);
    }
    
    public Rank getRank(){
        return rank;
    }
    
    public void setRank(Rank rank){
        this.rank = rank;
    }
    
    public Loc getLastLocation(){
        return this.lastLocation;
    }
    
    public void setLastLocation(Loc loc){
        this.lastLocation = loc;
    }
    
    public Level getLevel(){
        return level;
    }
    
    public void setLevel(Level level){
        this.level = level;
    }
    
    public LinkedList<Reward> getRewards(){
        return rewards;
    }
    
    public void addReward(Reward reward){
        rewards.add(reward);
    }
    
    public long getCoins(){
        return coins;
    }
    
    public void setCoins(long coins){
        this.coins = coins;
    }
    
    public void addCoins(long coins){
        this.coins += coins;
    }
    
    public void removeCoins(long coins){
        this.coins -= coins;
    }
    
    private void addDefaultOptions(){
        //options.put("lobby_")
    }
    
    private void addDefaultProps(){
        properties.put("lobby_player_visibility", PlayerVisibility.ALL.toString());
    }
    
    public void nextPlayerVisibility(){
        PlayerVisibility visibility = PlayerVisibility.valueOf(properties.get("lobby_player_visibility"));
        if (visibility == PlayerVisibility.ALL){
            properties.put("lobby_player_visibility", PlayerVisibility.FRIENDS.toString());
        } else if (visibility == PlayerVisibility.FRIENDS){
            properties.put("lobby_player_visibility", PlayerVisibility.IMPORTANT.toString());
        } else if (visibility == PlayerVisibility.IMPORTANT){
            properties.put("lobby_player_visibility", PlayerVisibility.NONE.toString());
        } else if (visibility == PlayerVisibility.NONE){
            properties.put("lobby_player_visibility", PlayerVisibility.ALL.toString());
        }
    }
    
    public void prevPlayerVisibility(){
        PlayerVisibility visibility = PlayerVisibility.valueOf(properties.get("lobby_player_visibility"));
        if (visibility == PlayerVisibility.ALL){
            properties.put("lobby_player_visibility", PlayerVisibility.NONE.toString());
        } else if (visibility == PlayerVisibility.FRIENDS){
            properties.put("lobby_player_visibility", PlayerVisibility.ALL.toString());
        } else if (visibility == PlayerVisibility.IMPORTANT){
            properties.put("lobby_player_visibility", PlayerVisibility.FRIENDS.toString());
        } else if (visibility == PlayerVisibility.NONE){
            properties.put("lobby_player_visibility", PlayerVisibility.IMPORTANT.toString());
        }
    }
}
