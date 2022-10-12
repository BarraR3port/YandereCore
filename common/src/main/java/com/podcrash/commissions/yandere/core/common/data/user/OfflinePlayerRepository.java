package com.podcrash.commissions.yandere.core.common.data.user;

import com.podcrash.commissions.yandere.core.common.data.level.Level;
import com.podcrash.commissions.yandere.core.common.data.server.ServerType;
import com.podcrash.commissions.yandere.core.common.data.user.props.GainSource;
import com.podcrash.commissions.yandere.core.common.data.user.props.Rank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public final class OfflinePlayerRepository extends IPlayerRepository {
    
    public OfflinePlayerRepository(){
        super(null, "players");
    }
    
    
    @Override
    public User getCachedPlayer(String name){
        for ( User user : list.values() ){
            if (user.getName().startsWith(name) || user.getName().equalsIgnoreCase(name)){
                return user;
            }
        }
        return null;
    }
    
    @Override
    public User getCachedPlayer(UUID uuid){
        return list.get(uuid);
    }
    
    @Override
    public UUID getUUIDByName(String name){
        for ( User user : list.values() ){
            if (user.getName().startsWith(name) || user.getName().equalsIgnoreCase(name)){
                return user.getUUID();
            }
        }
        return null;
    }
    
    @Override
    public User getPlayer(String name){
        return getCachedPlayer(name);
    }
    
    @Override
    public User getCachedPlayerOrCreate(String name, UUID uuid, String address){
        return null;
    }
    
    @Override
    public User getPlayer(UUID uuid){
        return getCachedPlayer(uuid);
    }
    
    @Override
    public User getPlayer(UUID uuid, String name){
        return getCachedPlayer(uuid);
    }
    
    @Override
    public OfflineUser createPlayer(String name, UUID uuid, String address){
        OfflineUser user = new OfflineUser(name, uuid);
        user.setAddress(address);
        list.put(uuid, user);
        return user;
    }
    
    @Override
    public User savePlayer(User user){
        return list.replace(user.getUUID(), user);
    }
    
    @Override
    public User savePlayer(User user, UUID prevUUID){
        list.remove(prevUUID);
        return list.put(user.getUUID(), user);
    }
    
    @Override
    public void updatePlayer(UUID uuid){
    
    }
    
    @Override
    public User getUpdatedPlayer(UUID uuid){
        return list.get(uuid);
    }
    
    @Override
    public void addProperty(User user, String key, String value){
        user.addProperty(key, value);
        list.replace(user.getUUID(), user);
    }
    
    @Override
    public HashMap<String, String> getProperties(User user){
        return getCachedPlayer(user.getUUID()).getProperties();
    }
    
    @Override
    public ArrayList<String> getPlayersName(String playerName){
        return list.values().stream().filter(user -> user.getName().startsWith(playerName) || user.getName().equalsIgnoreCase(playerName)).map(User::getName).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    @Override
    public ArrayList<String> getPlayersUUID(ArrayList<UUID> playersUUID){
        return list.values().stream().filter(user -> playersUUID.contains(user.getUUID())).map(User::getName).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    @Override
    public ArrayList<String> getPlayersName(){
        return list.values().stream().map(User::getName).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    @Override
    public void addCoins(User player, long amount, GainSource source, ServerType serverSource){
        player.addCoins(amount);
        list.replace(player.getUUID(), player);
    }
    
    @Override
    public void removeCoins(User player, long amount, GainSource source, ServerType serverSource){
        player.removeCoins(amount);
        list.replace(player.getUUID(), player);
    }
    
    @Override
    public void setCoins(User player, long amount, GainSource source, ServerType serverSource){
        player.setCoins(amount);
        list.replace(player.getUUID(), player);
    }
    
    @Override
    public void setPlayerLevel(User player, int level, GainSource source, ServerType serverSource){
        player.setLevel(new Level(player.getUUID(), level, 0, Level.OwnerType.PLAYER));
        list.replace(player.getUUID(), player);
    }
    
    @Override
    public void addPlayerLevel(User player, int level, GainSource source, ServerType serverSource){
        player.getLevel().addLevels(level);
        list.replace(player.getUUID(), player);
    }
    
    @Override
    public void removePlayerLevel(User player, int level, GainSource source, ServerType serverSource){
        player.getLevel().removeLevels(level);
        list.replace(player.getUUID(), player);
    }
    
    @Override
    public void setPlayerXp(User player, int xp, GainSource source, ServerType serverSource){
        player.getLevel().setXp(xp);
        list.replace(player.getUUID(), player);
    }
    
    @Override
    public void addPlayerXp(User player, int xp, GainSource source, ServerType serverSource){
        player.getLevel().addXp(xp);
        list.replace(player.getUUID(), player);
    }
    
    @Override
    public void removePlayerXp(User player, int xp, GainSource source, ServerType serverSource){
        player.getLevel().removeXp(xp);
        list.replace(player.getUUID(), player);
    }
    
    @Override
    public void setPlayerRank(User player, Rank rank){
        player.setRank(rank);
        list.replace(player.getUUID(), player);
    }
}
