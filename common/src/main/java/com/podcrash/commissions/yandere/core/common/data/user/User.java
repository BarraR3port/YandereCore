package com.podcrash.commissions.yandere.core.common.data.user;

import com.podcrash.commissions.yandere.core.common.data.level.Level;
import com.podcrash.commissions.yandere.core.common.data.lobby.JoinBedWarsArenaType;
import com.podcrash.commissions.yandere.core.common.data.lobby.JoinSkyWarsArenaType;
import com.podcrash.commissions.yandere.core.common.data.lobby.PlayerVisibility;
import com.podcrash.commissions.yandere.core.common.data.loc.Loc;
import com.podcrash.commissions.yandere.core.common.data.reward.BetaTester;
import com.podcrash.commissions.yandere.core.common.data.server.ServerType;
import com.podcrash.commissions.yandere.core.common.data.user.props.Rank;
import com.podcrash.commissions.yandere.core.common.data.user.props.Reward;
import com.podcrash.commissions.yandere.core.common.data.user.props.Stats;
import com.podcrash.commissions.yandere.core.common.skin.SkinManager;
import net.lymarket.lyapi.common.Api;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.*;

public class User {
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
    private String suffix = "";
    
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
        addDefaultRewards();
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
    
    public boolean getOptionOrDefault(String key, boolean def){
        return options.getOrDefault(key, def);
    }
    
    public void removeOption(String key){
        options.remove(key);
    }
    
    public void setOption(String key, boolean value){
        options.put(key, value);
    }
    
    public String getProperty(String key){
        return properties.getOrDefault(key, "");
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
    
    public void addDefaultOptions(){
        options.put("announcements-streams", true);
        options.put("announcements-general", true);
        options.put("announcements-join", true);
        options.put("announcements-join-others", true);
        //options.put("lobby_")
    }
    
    public void addDefaultProps(){
        properties.put("lobby-player-visibility", PlayerVisibility.ALL.toString());
        properties.put("lobby-sw-join-type", JoinSkyWarsArenaType.RANDOM.toString());
        properties.put("lobby-bw-join-type", JoinBedWarsArenaType.RANDOM.toString());
    }
    
    public void addDefaultRewards(){
        if (new Date().before(new Date("10/10/2022"))){
            rewards.add(new BetaTester());
        }
    }
    
    public void nextPlayerVisibility(){
        PlayerVisibility visibility = PlayerVisibility.valueOf(properties.get("lobby-player-visibility"));
        switch(visibility){
            case ALL:{
                properties.put("lobby-player-visibility", PlayerVisibility.RANKS.toString());
                break;
            }
            case RANKS:{
                properties.put("lobby-player-visibility", PlayerVisibility.NONE.toString());
                break;
            }
            case NONE:{
                properties.put("lobby-player-visibility", PlayerVisibility.ALL.toString());
                break;
            }
        }
    }
    
    public void nextJoinArenaType(ServerType serverType){
        switch(serverType){
            case SKY_WARS:{
                JoinSkyWarsArenaType type = JoinSkyWarsArenaType.valueOf(properties.get("lobby-sw-join-type")).getNext();
                properties.replace("lobby-sw-join-type", type.toString());
                break;
            }
            case LOBBY_BED_WARS:
            case BED_WARS:{
                JoinBedWarsArenaType type = JoinBedWarsArenaType.valueOf(properties.get("lobby-bw-join-type")).getNext();
                properties.replace("lobby-bw-join-type", type.toString());
                break;
            }
            
        }
    }
    
    public PlayerVisibility getPlayerVisibility(){
        return PlayerVisibility.valueOf(properties.get("lobby-player-visibility"));
    }
    
    public JoinSkyWarsArenaType getJoinSkyWarsArenaType(){
        return JoinSkyWarsArenaType.valueOf(properties.get("lobby-sw-join-type"));
    }
    
    public JoinBedWarsArenaType getJoinBedWarsArenaType(){
        return JoinBedWarsArenaType.valueOf(properties.get("lobby-bw-join-type"));
    }
    
    public String getCoinsFormatted(){
        if (coins >= 1000000f){
            DecimalFormat df = new DecimalFormat("#.#");
            return Utils.format(df.format(coins / 1000000f) + "&eM⛃");
        }
        if (coins >= 10000f){
            DecimalFormat df = new DecimalFormat("#.#");
            return Utils.format(df.format(coins / 1000f) + "&eK⛃");
        }
        return Utils.format(coins + "&e⛂");
    }
    
    public String getCoinsSemiFormatted(){
        return Utils.format(new DecimalFormat("###,###,###").format(coins) + "&e⛃");
    }
    
    public String getSuffix(){
        return suffix;
    }
    
    public void setSuffix(String suffix){
        this.suffix = suffix;
    }
}
