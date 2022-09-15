package com.podcrash.commissions.yandere.core.common.db;

import com.mongodb.client.model.Filters;
import com.podcrash.commissions.yandere.core.common.data.level.Level;
import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.common.data.user.props.Rank;
import net.lymarket.lyapi.common.db.MongoDB;
import net.lymarket.lyapi.common.db.MongoDBClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public abstract class IPlayerRepository extends MongoDB<UUID, User> {
    
    public IPlayerRepository(MongoDBClient database, String TABLE_NAME){
        super(database, TABLE_NAME);
    }
    
    @Override
    public void trashFinder(){
    }
    
    public User getCachedPlayer(UUID uuid) {
        if(list.containsKey(uuid)){
            return list.get(uuid);
        } else {
            User user = getPlayer(uuid);
            list.put(uuid, user);
            return user;
        }
    }
    
    public abstract User getCachedPlayer(String name);
    
    public abstract UUID getUUIDByName(String name);
    
    public abstract User getPlayer(String name);
    
    public abstract User getPlayer(UUID uuid);
    
    public abstract User getPlayer(UUID uuid, String name);
    
    public abstract void createPlayer(String name, UUID uuid, String address);
    
    public void getOrCreatePlayer(String name, UUID uuid, String address){
        User user = getPlayer(uuid, name);
        if (user == null){
            createPlayer(name, uuid, address);
        }
    }
    
    public void unloadPlayer(UUID uuid){
        User user = getCachedPlayer(uuid);
        database.replaceOneFast(TABLE_NAME, Filters.eq("uuid", uuid.toString()), user);
        list.remove(uuid);
    }
    
    public void deletePlayer(UUID uuid){
        database.deleteOne(TABLE_NAME, Filters.eq("uuid", uuid.toString()));
    }
    
    public HashMap<UUID, User> getPlayers(){
        return list;
    }
    
    public User savePlayer(UUID uuid){
        final User user = getCachedPlayer(uuid);
        database.replaceOneFast(TABLE_NAME, Filters.eq("uuid", uuid.toString()), user);
        return user;
    }
    
    public void unCachePlayer(UUID uuid){
        list.remove(uuid);
    }
    
    public abstract User savePlayer(User user);
    
    public abstract User savePlayer(User user, UUID prevUUID);
    
    public abstract void updatePlayer(UUID uuid);
    
    public abstract User getUpdatedPlayer(UUID uuid);
    
    public abstract void addProperty(User user, String key, String value);
    
    public abstract HashMap<String, String> getProperties(User user);
    
    public abstract ArrayList<String> getPlayersName(String playerName);
    
    public abstract ArrayList<String> getPlayersUUID(ArrayList<UUID> playersUUID);
    
    public abstract ArrayList<String> getPlayersName();
    
    public abstract void addCoins(User player, long amount);
    
    public abstract void removeCoins(User player, long amount);
    
    public abstract void setCoins(User player, long amount);
    
    public abstract void setPlayerLevel(User player, int level);
    
    public abstract void addPlayerLevel(User player, int level);
    
    public abstract void removePlayerLevel(User player, int level);
    
    public abstract void setPlayerXp(User player, int xp, Level.GainSource source);
    
    public abstract void addPlayerXp(User player, int xp, Level.GainSource source);
    
    public abstract void removePlayerXp(User player, int xp, Level.GainSource source);
    
    public abstract void setPlayerRank(User player, Rank rank);
}
