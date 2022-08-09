package com.podcrash.comissions.yandere.core.velocity.manager;

import com.mongodb.client.model.Filters;
import com.podcrash.comissions.yandere.core.common.data.level.Level;
import com.podcrash.comissions.yandere.core.common.data.user.props.Rank;
import com.podcrash.comissions.yandere.core.common.db.IPlayerRepository;
import com.podcrash.comissions.yandere.core.common.error.UserNotFoundException;
import com.podcrash.comissions.yandere.core.velocity.user.VelocityUser;
import net.luckperms.api.LuckPermsProvider;
import net.lymarket.common.Api;
import net.lymarket.common.db.MongoDBClient;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayersRepository extends IPlayerRepository<VelocityUser> {
    public PlayersRepository(MongoDBClient database, String tableName){
        super(database, tableName);
    }
    
    
    @Override
    public VelocityUser getPlayer(String name){
        Document doc = database.findOneFast(TABLE_NAME, Filters.eq("name", name));
        if (doc == null) return null;
        VelocityUser user = Api.getGson().fromJson(doc.toJson(), VelocityUser.class);
        if (user == null){
            throw new UserNotFoundException(name);
        }
        list.put(user.getUUID(), user);
        return user;
    }
    
    @Override
    public VelocityUser getPlayer(UUID uuid){
        Document doc = database.findOneFast(TABLE_NAME, Filters.eq("uuid", uuid.toString()));
        if (doc == null) return null;
        final VelocityUser user = Api.getGson().fromJson(doc.toJson(), VelocityUser.class);
        list.put(uuid, user);
        return user;
    }
    
    @Override
    public VelocityUser getPlayer(UUID uuid, String name){
        Document docUUID = database.findOneFast(TABLE_NAME, Filters.eq("uuid", uuid.toString()));
        if (docUUID == null){
            Document docName = database.findOneFast(TABLE_NAME, Filters.eq("name", name));
            if (docName == null){
                return null;
            } else {
                VelocityUser user = Api.getGson().fromJson(docName.toJson(), VelocityUser.class);
                final UUID prevUUID = user.getUUID();
                user.setUUID(uuid);
                return savePlayer(user, prevUUID);
            }
        }
        VelocityUser user = Api.getGson().fromJson(docUUID.toJson(), VelocityUser.class);
        list.put(uuid, user);
        return user;
    }
    
    
    @Override
    public void createPlayer(String name, UUID uuid, String address){
        VelocityUser user = new VelocityUser(name, uuid);
        user.setAddress(address);
        user.setOption("allow-visit-plot-requests", true);
        user.setOption("allow-visit-world-requests", true);
        user.setOption("allow-pm", true);
        user.setOption("allow-friend-requests", true);
        user.setOption("changed-plots", false);
        final net.luckperms.api.model.user.User luckPermsUser = LuckPermsProvider.get().getUserManager().getUser(uuid);
        if (luckPermsUser != null){
            user.setRank(Rank.fromString(luckPermsUser.getPrimaryGroup()));
        }
        database.insertOne(TABLE_NAME, user);
        list.put(uuid, user);
    }
    
    @Override
    public VelocityUser savePlayer(VelocityUser user){
        database.replaceOneFast(TABLE_NAME, Filters.eq("uuid", user.getUUID().toString()), user);
        list.put(user.getUUID(), user);
        return user;
    }
    
    
    @Override
    public VelocityUser savePlayer(VelocityUser user, UUID prevUUID){
        database.replaceOneFast(TABLE_NAME, Filters.eq("uuid", prevUUID.toString()), user);
        list.put(user.getUUID(), user);
        return user;
    }
    
    
    @Override
    public void updatePlayer(UUID uuid){
        Document doc = database.findOneFast(TABLE_NAME, Filters.eq("uuid", uuid.toString()));
        if (doc == null) return;
        Api.getGson().fromJson(doc.toJson(), VelocityUser.class);
    }
    
    @Override
    public VelocityUser getUpdatedPlayer(UUID uuid){
        Document doc = database.findOneFast(TABLE_NAME, Filters.eq("uuid", uuid.toString()));
        final VelocityUser user = Api.getGson().fromJson(doc.toJson(), VelocityUser.class);
        list.put(uuid, user);
        return user;
    }
    
    @Override
    public void addProperty(VelocityUser user, String key, String value){
        user.addProperty(key, value);
        savePlayer(user);
    }
    
    @Override
    public HashMap<String, String> getProperties(VelocityUser user){
        return user.getProperties();
    }
    
    
    @Override
    public ArrayList<String> getPlayersName(String playerName){
        return database.findMany(TABLE_NAME, VelocityUser.class).stream().map(VelocityUser::getName).filter(name -> !name.equalsIgnoreCase(playerName)).collect(Collectors.toCollection(ArrayList::new));
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
    public ArrayList<String> getPlayersName(){
        return database.findMany(TABLE_NAME, VelocityUser.class).stream().map(VelocityUser::getName).collect(Collectors.toCollection(ArrayList::new));
    }
    
    @Override
    public void addCoins(VelocityUser player, int amount){
    
    }
    
    @Override
    public void removeCoins(VelocityUser player, int amount){
    
    }
    
    @Override
    public void setCoins(VelocityUser player, int amount){
    
    }
    
    @Override
    public void setPlayerLevel(VelocityUser player, int level){
    
    }
    
    @Override
    public void addPlayerLevel(VelocityUser player, int level){
    
    }
    
    @Override
    public void removePlayerLevel(VelocityUser player, int level){
    
    }
    
    @Override
    public void setPlayerXp(VelocityUser player, int xp, Level.GainSource source){
    
    }
    
    @Override
    public void addPlayerXp(VelocityUser player, int xp, Level.GainSource source){
    
    }
    
    @Override
    public void removePlayerXp(VelocityUser player, int xp, Level.GainSource source){
    
    }
    
    @Override
    public void setPlayerRank(VelocityUser player, Rank rank){
    
    }
}
