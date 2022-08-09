package com.podcrash.comissions.yandere.core.spigot.users;

import com.mongodb.client.model.Filters;
import com.podcrash.comissions.yandere.core.common.data.level.Level;
import com.podcrash.comissions.yandere.core.common.data.server.ChangeType;
import com.podcrash.comissions.yandere.core.common.data.user.props.Rank;
import com.podcrash.comissions.yandere.core.common.db.IPlayerRepository;
import com.podcrash.comissions.yandere.core.common.error.UserNotFoundException;
import com.podcrash.comissions.yandere.core.spigot.events.PlayerCoinsChangeEvent;
import com.podcrash.comissions.yandere.core.spigot.events.PlayerLevelChangeEvent;
import com.podcrash.comissions.yandere.core.spigot.events.PlayerRankChangeEvent;
import com.podcrash.comissions.yandere.core.spigot.events.PlayerXpChangeEvent;
import net.luckperms.api.LuckPermsProvider;
import net.lymarket.common.Api;
import net.lymarket.common.db.MongoDBClient;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

public final class PlayersRepository extends IPlayerRepository<SpigotUser> {
    
    
    public PlayersRepository(MongoDBClient database, String tableName){
        super(database, tableName);
    }
    
    @Override
    public SpigotUser getPlayer(String name){
        Document doc = database.findOneFast(TABLE_NAME, Filters.eq("name", name));
        if (doc == null) throw new UserNotFoundException(name);
        SpigotUser user = Api.getGson().fromJson(doc.toJson(), SpigotUser.class);
        if (user == null){
            throw new UserNotFoundException(name);
        }
        list.put(user.getUUID(), user);
        return user;
    }
    
    @Override
    public SpigotUser getPlayer(UUID uuid){
        Document doc = database.findOneFast(TABLE_NAME, Filters.eq("uuid", uuid.toString()));
        if (doc == null) throw new UserNotFoundException(uuid.toString());
        final SpigotUser user = Api.getGson().fromJson(doc.toJson(), SpigotUser.class);
        list.put(uuid, user);
        return user;
    }
    
    @Override
    public SpigotUser getPlayer(UUID uuid, String name){
        Document docUUID = database.findOneFast(TABLE_NAME, Filters.eq("uuid", uuid.toString()));
        if (docUUID == null){
            Document docName = database.findOneFast(TABLE_NAME, Filters.eq("name", name));
            if (docName == null){
                return null;
            } else {
                SpigotUser user = Api.getGson().fromJson(docName.toJson(), SpigotUser.class);
                final UUID prevUUID = user.getUUID();
                user.setUUID(uuid);
                return savePlayer(user, prevUUID);
            }
        }
        SpigotUser user = Api.getGson().fromJson(docUUID.toJson(), SpigotUser.class);
        list.put(uuid, user);
        return user;
    }
    
    
    @Override
    public void createPlayer(String name, UUID uuid, String address){
        SpigotUser user = new SpigotUser(name, uuid);
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
    public SpigotUser savePlayer(SpigotUser user){
        database.replaceOneFast(TABLE_NAME, Filters.eq("uuid", user.getUUID().toString()), user);
        list.put(user.getUUID(), user);
        return user;
    }
    
    
    @Override
    public SpigotUser savePlayer(SpigotUser user, UUID prevUUID){
        database.replaceOneFast(TABLE_NAME, Filters.eq("uuid", prevUUID.toString()), user);
        list.put(user.getUUID(), user);
        return user;
    }
    
    
    @Override
    public void updatePlayer(UUID uuid){
        Document doc = database.findOneFast(TABLE_NAME, Filters.eq("uuid", uuid.toString()));
        if (doc == null) return;
        Api.getGson().fromJson(doc.toJson(), SpigotUser.class);
    }
    
    @Override
    public SpigotUser getUpdatedPlayer(UUID uuid){
        Document doc = database.findOneFast(TABLE_NAME, Filters.eq("uuid", uuid.toString()));
        final SpigotUser user = Api.getGson().fromJson(doc.toJson(), SpigotUser.class);
        list.put(uuid, user);
        return user;
    }
    
    @Override
    public void addProperty(SpigotUser user, String key, String value){
        user.addProperty(key, value);
        savePlayer(user);
    }
    
    @Override
    public HashMap<String, String> getProperties(SpigotUser user){
        return user.getProperties();
    }
    
    @Override
    public ArrayList<String> getPlayersName(String playerName){
        return database.findMany(TABLE_NAME, SpigotUser.class).stream().map(SpigotUser::getName).filter(name -> !name.equalsIgnoreCase(playerName)).collect(Collectors.toCollection(ArrayList::new));
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
        return database.findMany(TABLE_NAME, SpigotUser.class).stream().map(SpigotUser::getName).collect(Collectors.toCollection(ArrayList::new));
    }
    
    
    @Override
    public void addCoins(SpigotUser player, int amount){
        PlayerCoinsChangeEvent event = new PlayerCoinsChangeEvent(Bukkit.getPlayer(player.getName()), amount, Level.GainSource.COMMAND, ChangeType.ADD);
        if (event.isCancelled()) return;
        player.addCoins(amount);
        savePlayer(player);
        Utils.playSound(Bukkit.getPlayer(player.getName()), "ORB_PICKUP");
        Utils.playActionBar(Bukkit.getPlayer(player.getName()), "&a+" + amount + " &5Coins" + " &e⛃");
    }
    
    @Override
    public void removeCoins(SpigotUser player, int amount){
        PlayerCoinsChangeEvent event = new PlayerCoinsChangeEvent(Bukkit.getPlayer(player.getName()), amount, Level.GainSource.COMMAND, ChangeType.REMOVE);
        if (event.isCancelled()) return;
        if (player.getCoins() - amount <= 0){
            player.setCoins(0);
        } else {
            player.removeCoins(amount);
        }
        savePlayer(player);
        Utils.playSound(Bukkit.getPlayer(player.getName()), "ORB_PICKUP");
        Utils.playActionBar(Bukkit.getPlayer(player.getName()), "&c-" + amount + " &5Coins" + " &e⛃");
    }
    
    @Override
    public void setCoins(SpigotUser player, int amount){
        PlayerCoinsChangeEvent event = new PlayerCoinsChangeEvent(Bukkit.getPlayer(player.getName()), amount, Level.GainSource.COMMAND, ChangeType.SET);
        if (event.isCancelled()) return;
        player.setCoins(Math.max(amount, 0));
        savePlayer(player);
        Utils.playSound(Bukkit.getPlayer(player.getName()), "ORB_PICKUP");
        Utils.playActionBar(Bukkit.getPlayer(player.getName()), "&8Ahora tienes &a" + amount + " &5Coins" + " &e⛃");
    }
    
    @Override
    public void setPlayerLevel(SpigotUser player, int level){
        PlayerLevelChangeEvent event = new PlayerLevelChangeEvent(Bukkit.getPlayer(player.getName()), player.getLevel(), ChangeType.SET);
        if (event.isCancelled()) return;
        player.getLevel().setLevel(level);
        savePlayer(player);
        Utils.playSound(Bukkit.getPlayer(player.getName()), "LEVEL_UP");
        Utils.playActionBar(Bukkit.getPlayer(player.getName()), "&aAhora eres nivel &d" + player.getLevel().getLevel());
    }
    
    @Override
    public void addPlayerLevel(SpigotUser player, int level){
        PlayerLevelChangeEvent event = new PlayerLevelChangeEvent(Bukkit.getPlayer(player.getName()), player.getLevel(), ChangeType.ADD);
        if (event.isCancelled()) return;
        player.getLevel().addLevels(level);
        savePlayer(player);
        Utils.playSound(Bukkit.getPlayer(player.getName()), "LEVEL_UP");
        Utils.playActionBar(Bukkit.getPlayer(player.getName()), "&aSubiste al nivel &d" + player.getLevel().getLevel());
    }
    
    @Override
    public void removePlayerLevel(SpigotUser player, int level){
        PlayerLevelChangeEvent event = new PlayerLevelChangeEvent(Bukkit.getPlayer(player.getName()), player.getLevel(), ChangeType.REMOVE);
        if (event.isCancelled()) return;
        player.getLevel().removeLevels(level);
        savePlayer(player);
        Utils.playSound(Bukkit.getPlayer(player.getName()), "LEVEL_UP");
        Utils.playActionBar(Bukkit.getPlayer(player.getName()), "&cBajaste al nivel &d" + player.getLevel().getLevel());
    }
    
    @Override
    public void setPlayerXp(SpigotUser player, int xp, Level.GainSource source){
        PlayerXpChangeEvent event = new PlayerXpChangeEvent(Bukkit.getPlayer(player.getName()), xp, source, ChangeType.SET);
        if (event.isCancelled()) return;
        player.getLevel().setXp(xp);
        savePlayer(player);
        Utils.playSound(Bukkit.getPlayer(player.getName()), "ORB_PICKUP");
        Utils.playActionBar(Bukkit.getPlayer(player.getName()), "&aAhora tienes &d" + xp + " &5XP");
    }
    
    @Override
    public void addPlayerXp(SpigotUser player, int xp, Level.GainSource source){
        PlayerXpChangeEvent event = new PlayerXpChangeEvent(Bukkit.getPlayer(player.getName()), xp, source, ChangeType.SET);
        if (event.isCancelled()) return;
        player.getLevel().addXp(xp);
        savePlayer(player);
        Utils.playSound(Bukkit.getPlayer(player.getName()), "ORB_PICKUP");
        Utils.playActionBar(Bukkit.getPlayer(player.getName()), "&a+&d" + xp + " &5XP");
    }
    
    @Override
    public void removePlayerXp(SpigotUser player, int xp, Level.GainSource source){
        PlayerXpChangeEvent event = new PlayerXpChangeEvent(Bukkit.getPlayer(player.getName()), xp, source, ChangeType.REMOVE);
        if (event.isCancelled()) return;
        player.getLevel().removeXp(xp);
        savePlayer(player);
        Utils.playSound(Bukkit.getPlayer(player.getName()), "ORB_PICKUP");
        Utils.playActionBar(Bukkit.getPlayer(player.getName()), "&c-&d" + xp + " &5XP");
    }
    
    @Override
    public void setPlayerRank(SpigotUser player, Rank rank){
        PlayerRankChangeEvent event = new PlayerRankChangeEvent(Bukkit.getPlayer(player.getName()), rank, ChangeType.SET);
        if (event.isCancelled()) return;
        player.setRank(event.getRank());
        savePlayer(player);
        Utils.playSound(Bukkit.getPlayer(player.getName()), "LEVEL_UP");
        Utils.playActionBar(Bukkit.getPlayer(player.getName()), "&aSe te ha otorgado el rango: " + event.getRank().getTabPrefix());
    }
}
