package com.podcrash.commissions.yandere.core.common.db;

import com.mongodb.client.model.Filters;
import com.podcrash.commissions.yandere.core.common.data.level.Level;
import com.podcrash.commissions.yandere.core.common.data.user.props.Rank;
import net.lymarket.common.db.MongoDB;
import net.lymarket.common.db.MongoDBClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

public abstract class IPlayerRepository<U> extends MongoDB<UUID, U> {
    
    protected final LinkedHashMap<UUID, U> list = new LinkedHashMap();
    
    public IPlayerRepository(MongoDBClient database, String TABLE_NAME){
        super(database, TABLE_NAME);
    }
    
    @Override
    public void trashFinder(){
    }
    
    public U getLocalStoredPlayer(UUID uuid){
        if (list.containsKey(uuid)){
            return list.get(uuid);
        } else {
            U user = getPlayer(uuid);
            list.put(uuid, user);
            return user;
        }
    }
    
    public abstract U getPlayer(String name);
    
    public abstract U getPlayer(UUID uuid);
    
    public abstract U getPlayer(UUID uuid, String name);
    
    public abstract void createPlayer(String name, UUID uuid, String address);
    
    public void getOrCreatePlayer(String name, UUID uuid, String address){
        U user = getPlayer(uuid, name);
        if (user == null){
            createPlayer(name, uuid, address);
        }
    }
    
    public void unloadPlayer(UUID uuid){
        U user = getLocalStoredPlayer(uuid);
        database.replaceOneFast(TABLE_NAME, Filters.eq("uuid", uuid.toString()), user);
        list.remove(uuid);
        
    }
    
    public void deletePlayer(UUID uuid){
        database.deleteOne(TABLE_NAME, Filters.eq("uuid", uuid.toString()));
    }
    
    public HashMap<UUID, U> getPlayers(){
        return list;
    }
    
    public U savePlayer(UUID uuid){
        final U user = getPlayer(uuid);
        database.replaceOneFast(TABLE_NAME, Filters.eq("uuid", uuid.toString()), user);
        return user;
    }
    
    public abstract U savePlayer(U user);
    
    public abstract U savePlayer(U user, UUID prevUUID);
    
    public abstract void updatePlayer(UUID uuid);
    
    public abstract U getUpdatedPlayer(UUID uuid);
    
    public abstract void addProperty(U user, String key, String value);
    
    public abstract HashMap<String, String> getProperties(U user);
    
    public abstract ArrayList<String> getPlayersName(String playerName);
    
    public abstract ArrayList<String> getPlayersUUID(ArrayList<UUID> playersUUID);
    
    public abstract ArrayList<String> getPlayersName();
    
    public abstract void addCoins(U player, int amount);
    
    public abstract void removeCoins(U player, int amount);
    
    public abstract void setCoins(U player, int amount);
    
    public abstract void setPlayerLevel(U player, int level);
    
    public abstract void addPlayerLevel(U player, int level);
    
    public abstract void removePlayerLevel(U player, int level);
    
    public abstract void setPlayerXp(U player, int xp, Level.GainSource source);
    
    public abstract void addPlayerXp(U player, int xp, Level.GainSource source);
    
    public abstract void removePlayerXp(U player, int xp, Level.GainSource source);
    
    public abstract void setPlayerRank(U player, Rank rank);
}
