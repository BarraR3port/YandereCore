package com.podcrash.commissions.yandere.core.velocity.manager;

import com.mongodb.client.model.Filters;
import com.podcrash.commissions.yandere.core.common.data.server.ServerType;
import com.podcrash.commissions.yandere.core.common.data.user.IPlayerRepository;
import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.common.data.user.props.GainSource;
import com.podcrash.commissions.yandere.core.common.data.user.props.Rank;
import com.podcrash.commissions.yandere.core.common.error.UserNotFoundException;
import net.luckperms.api.LuckPermsProvider;
import net.lymarket.lyapi.common.Api;
import net.lymarket.lyapi.common.db.MongoDBClient;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerRepository extends IPlayerRepository {
    public PlayerRepository(MongoDBClient database, String tableName){
        super(database, tableName);
    }
    
    
    @Override
    public User getPlayer(String name){
        Document doc = database.findOneFast(TABLE_NAME, Filters.eq("name", name));
        if (doc == null) return null;
        User user = Api.getGson().fromJson(doc.toJson(), User.class);
        if (user == null){
            throw new UserNotFoundException(name);
        }
        list.put(user.getUUID(), user);
        return user;
    }
    
    @Override
    public User getCachedPlayer(String name){
        for ( User user : list.values() ){
            if (user.getName().startsWith(name) || user.getName().equalsIgnoreCase(name)){
                return user;
            }
        }
        
        Document doc = database.findOneFast(TABLE_NAME, Filters.eq("name", name));
        if (doc == null) throw new UserNotFoundException(name);
        User user = Api.getGson().fromJson(doc.toJson(), User.class);
        if (user == null){
            throw new UserNotFoundException(name);
        }
        list.put(user.getUUID(), user);
        return user;
    }
    
    @Override
    public User getPlayer(UUID uuid){
        Document doc = database.findOneFast(TABLE_NAME, Filters.eq("uuid", uuid.toString()));
        if (doc == null) return null;
        final User user = Api.getGson().fromJson(doc.toJson(), User.class);
        list.put(uuid, user);
        return user;
    }
    
    @Override
    public User getPlayer(UUID uuid, String name){
        Document docUUID = database.findOneFast(TABLE_NAME, Filters.eq("uuid", uuid.toString()));
        if (docUUID == null){
            Document docName = database.findOneFast(TABLE_NAME, Filters.eq("name", name));
            if (docName == null){
                return null;
            } else {
                User user = Api.getGson().fromJson(docName.toJson(), User.class);
                final UUID prevUUID = user.getUUID();
                user.setUUID(uuid);
                user.addDefaultProps();
                user.addDefaultOptions();
                user.addDefaultRewards();
                return savePlayer(user, prevUUID);
            }
        }
        User user = Api.getGson().fromJson(docUUID.toJson(), User.class);
        list.put(uuid, user);
        return user;
    }
    
    
    @Override
    public void createPlayer(String name, UUID uuid, String address){
        User user = new User(name, uuid);
        user.setAddress(address);
        final net.luckperms.api.model.user.User luckPermsUser = LuckPermsProvider.get().getUserManager().getUser(uuid);
        if (luckPermsUser != null){
            user.setRank(Rank.fromString(luckPermsUser.getPrimaryGroup()));
        }
        database.insertOne(TABLE_NAME, user);
        list.put(uuid, user);
    }
    
    @Override
    public User savePlayer(User user){
        database.replaceOneFast(TABLE_NAME, Filters.eq("uuid", user.getUUID().toString()), user);
        list.put(user.getUUID(), user);
        return user;
    }
    
    
    @Override
    public User savePlayer(User user, UUID prevUUID){
        database.replaceOneFast(TABLE_NAME, Filters.eq("uuid", prevUUID.toString()), user);
        list.put(user.getUUID(), user);
        return user;
    }
    
    
    @Override
    public void updatePlayer(UUID uuid){
        Document doc = database.findOneFast(TABLE_NAME, Filters.eq("uuid", uuid.toString()));
        if (doc == null) return;
        Api.getGson().fromJson(doc.toJson(), User.class);
    }
    
    @Override
    public User getUpdatedPlayer(UUID uuid){
        Document doc = database.findOneFast(TABLE_NAME, Filters.eq("uuid", uuid.toString()));
        final User user = Api.getGson().fromJson(doc.toJson(), User.class);
        list.put(uuid, user);
        return user;
    }
    
    @Override
    public void addProperty(User user, String key, String value){
        user.addProperty(key, value);
        savePlayer(user);
    }
    
    @Override
    public HashMap<String, String> getProperties(User user){
        return user.getProperties();
    }
    
    
    @Override
    public ArrayList<String> getPlayersName(String playerName){
        return database.findMany(TABLE_NAME, User.class).stream().map(User::getName).filter(name -> !name.equalsIgnoreCase(playerName)).collect(Collectors.toCollection(ArrayList::new));
    }
    
    @Override
    public ArrayList<String> getPlayersUUID(ArrayList<UUID> playersUUID){
        final ArrayList<String> players = new ArrayList<>();
        for ( UUID uuid : playersUUID ){
            players.add(getPlayer(uuid).getName());
        }
        return players;
    }
    
    @Override
    public UUID getUUIDByName(String name){
        for ( User user : list.values() ){
            if (user.getName().startsWith(name) || user.getName().equalsIgnoreCase(name)){
                return user.getUUID();
            }
        }
        return getPlayer(name).getUUID();
    }
    
    @Override
    public ArrayList<String> getPlayersName(){
        return database.findMany(TABLE_NAME, User.class).stream().map(User::getName).collect(Collectors.toCollection(ArrayList::new));
    }
    
    @Override
    public void addCoins(User player, long amount, GainSource source, ServerType serverSource){
    
    }
    
    @Override
    public void removeCoins(User player, long amount, GainSource source, ServerType serverSource){
    
    }
    
    @Override
    public void setCoins(User player, long amount, GainSource source, ServerType serverSource){
    
    }
    
    @Override
    public void setPlayerLevel(User player, int level, GainSource source, ServerType serverSource){
    
    }
    
    @Override
    public void addPlayerLevel(User player, int level, GainSource source, ServerType serverSource){
    
    }
    
    @Override
    public void removePlayerLevel(User player, int level, GainSource source, ServerType serverSource){
    
    }
    
    @Override
    public void setPlayerXp(User player, int xp, GainSource source, ServerType serverSource){
    
    }
    
    @Override
    public void addPlayerXp(User player, int xp, GainSource source, ServerType serverSource){
    
    }
    
    @Override
    public void removePlayerXp(User player, int xp, GainSource source, ServerType serverSource){
    
    }
    
    @Override
    public void setPlayerRank(User player, Rank rank){
    
    }
}
