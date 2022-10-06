package com.podcrash.commissions.yandere.core.spigot.users;

import com.mongodb.client.model.Filters;
import com.podcrash.commissions.yandere.core.common.data.logs.LogType;
import com.podcrash.commissions.yandere.core.common.data.server.ChangeType;
import com.podcrash.commissions.yandere.core.common.data.server.ServerType;
import com.podcrash.commissions.yandere.core.common.data.user.IPlayerRepository;
import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.common.data.user.props.GainSource;
import com.podcrash.commissions.yandere.core.common.data.user.props.Rank;
import com.podcrash.commissions.yandere.core.common.error.UserNotFoundException;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.events.PlayerCoinsChangeEvent;
import com.podcrash.commissions.yandere.core.spigot.events.PlayerLevelChangeEvent;
import com.podcrash.commissions.yandere.core.spigot.events.PlayerRankChangeEvent;
import com.podcrash.commissions.yandere.core.spigot.events.PlayerXpChangeEvent;
import com.podcrash.commissions.yandere.core.spigot.settings.Settings;
import net.luckperms.api.LuckPermsProvider;
import net.lymarket.lyapi.common.db.MongoDBClient;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.stream.Collectors;

public final class PlayerRepository extends IPlayerRepository {
    
    public PlayerRepository(MongoDBClient database, String tableName){
        super(database, tableName);
    }
    
    @Override
    public User getPlayer(String name){
        Document doc = database.findOneFast(TABLE_NAME, Filters.eq("name", name));
        if (doc == null) throw new UserNotFoundException(name);
        User user = database.getGson().fromJson(doc.toJson(), User.class);
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
        User user = database.getGson().fromJson(doc.toJson(), User.class);
        if (user == null){
            throw new UserNotFoundException(name);
        }
        list.put(user.getUUID(), user);
        return user;
    }
    
    @Override
    public User getPlayer(UUID uuid){
        Document doc = database.findOneFast(TABLE_NAME, Filters.eq("uuid", uuid.toString()));
        if (doc == null) throw new UserNotFoundException(uuid.toString());
        final User user = database.getGson().fromJson(doc.toJson(), User.class);
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
                User user = database.getGson().fromJson(docName.toJson(), User.class);
                final UUID prevUUID = user.getUUID();
                user.setUUID(uuid);
                return savePlayer(user, prevUUID);
            }
        }
        User user = database.getGson().fromJson(docUUID.toJson(), User.class);
        list.put(uuid, user);
        return user;
    }
    
    
    @Override
    public void createPlayer(String name, UUID uuid, String address){
        SpigotUser user = new SpigotUser(name, uuid);
        user.setAddress(address);
        user.setOption("allow-pm", true);
        user.addProperty("lobby-player-visibility", "ALL");
        user.addProperty("lobby-sw-join-type", "RANDOM");
        user.addProperty("lobby-bw-join-type", "RANDOM");
        final net.luckperms.api.model.user.User luckPermsUser = LuckPermsProvider.get().getUserManager().getUser(uuid);
        if (luckPermsUser != null){
            user.setRank(Rank.fromString(luckPermsUser.getPrimaryGroup()));
        }
        database.insertOne(TABLE_NAME, user);
        list.put(uuid, user);
    }
    
    @Override
    public User savePlayer(User user){
        if (database.replaceOneFast(TABLE_NAME, Filters.eq("uuid", user.getUUID().toString()), user)){
            list.replace(user.getUUID(), user);
        } else {
            if (database.insertOne(TABLE_NAME, user)){
                list.put(user.getUUID(), user);
            } else {
                Bukkit.getLogger().severe("[YandereCore] Error while saving player " + user.getUUID() + " to database");
            }
        }
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
        database.getGson().fromJson(doc.toJson(), User.class);
    }
    
    @Override
    public User getUpdatedPlayer(UUID uuid){
        Document doc = database.findOneFast(TABLE_NAME, Filters.eq("uuid", uuid.toString()));
        final User user = database.getGson().fromJson(doc.toJson(), User.class);
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
            players.add(getCachedPlayer(uuid).getName());
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
        if (amount <= 0) return;
        PlayerCoinsChangeEvent event = new PlayerCoinsChangeEvent(Bukkit.getPlayer(player.getName()), amount, source, serverSource, ChangeType.ADD);
        if (event.isCancelled()) return;
        player.addCoins(amount);
        savePlayer(player);
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put(" &4• &7Cambio: &a+", amount + " &7 ⛃");
        data.put(" &4• &7Total: &c", player.getCoinsFormatted());
        data.put(" &4• &7Fuente: &a", source.getName());
        data.put(" &4• &7Servidor: &a", serverSource.getName());
        Main.getInstance().getLogs().createLogWithProps(LogType.MONEY_CHANGE, Settings.PROXY_SERVER_NAME, "&a+" + amount + " ⛃", player.getName(), data);
        try {
            Utils.playSound(Bukkit.getPlayer(player.getName()), "ORB_PICKUP");
            Utils.playActionBar(Bukkit.getPlayer(player.getName()), "&a+" + amount + " ⛃");
        } catch (NullPointerException ignored) {
        }
    }
    
    @Override
    public void removeCoins(User player, long amount, GainSource source, ServerType serverSource){
        if (amount <= 0) return;
        PlayerCoinsChangeEvent event = new PlayerCoinsChangeEvent(Bukkit.getPlayer(player.getName()), amount, source, serverSource, ChangeType.REMOVE);
        if (event.isCancelled()) return;
        if (player.getCoins() - amount <= 0){
            player.setCoins(0);
        } else {
            player.removeCoins(amount);
        }
        savePlayer(player);
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put(" &4• &7Cambio: &c-", amount + " &7 ⛃");
        data.put(" &4• &7Total: &c", player.getCoinsFormatted());
        data.put(" &4• &7Fuente: &a", source.getName());
        data.put(" &4• &7Servidor: &a", serverSource.getName());
        Main.getInstance().getLogs().createLogWithProps(LogType.MONEY_CHANGE, Settings.PROXY_SERVER_NAME, "&c-" + amount + " ⛃", player.getName(), data);
        try {
            Utils.playSound(Bukkit.getPlayer(player.getName()), "ORB_PICKUP");
            Utils.playActionBar(Bukkit.getPlayer(player.getName()), "&c-" + amount + " ⛃");
        } catch (NullPointerException ignored) {
        }
    }
    
    @Override
    public void setCoins(User player, long amount, GainSource source, ServerType serverSource){
        PlayerCoinsChangeEvent event = new PlayerCoinsChangeEvent(Bukkit.getPlayer(player.getName()), amount, source, serverSource, ChangeType.SET);
        if (event.isCancelled()) return;
        player.setCoins(Math.max(amount, 0));
        savePlayer(player);
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put(" &4• &7Total: &c", player.getCoinsFormatted());
        data.put(" &4• &7Fuente: &a", source.getName());
        data.put(" &4• &7Servidor: &a", serverSource.getName());
        Main.getInstance().getLogs().createLogWithProps(LogType.MONEY_CHANGE, Settings.PROXY_SERVER_NAME, "&8Ahora tienes &a" + amount + " ⛃", player.getName(), data);
        try {
            Utils.playSound(Bukkit.getPlayer(player.getName()), "ORB_PICKUP");
            Utils.playActionBar(Bukkit.getPlayer(player.getName()), "&8Ahora tienes &a" + amount + " ⛃");
        } catch (NullPointerException ignored) {
        }
    }
    
    @Override
    public void setPlayerLevel(User player, int level, GainSource source, ServerType serverSource){
        PlayerLevelChangeEvent event = new PlayerLevelChangeEvent(Bukkit.getPlayer(player.getName()), player.getLevel(), source, serverSource, ChangeType.SET);
        if (event.isCancelled()) return;
        player.getLevel().setLevel(level);
        savePlayer(player);
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put(" &4• &7Nivel: &a", String.valueOf(player.getLevel().getLevel()));
        data.put(" &4• &7Nivel Formateado: &a", player.getLevel().getLevelNameFormatted());
        data.put(" &4•", player.getLevel().getProgressBarFormatted());
        data.put(" &4• &7Fuente: &a", source.getName());
        data.put(" &4• &7Servidor: &a", serverSource.getName());
        Main.getInstance().getLogs().createLogWithProps(LogType.XP_CHANGE, Settings.PROXY_SERVER_NAME, "&aAhora eres nivel &c" + player.getLevel().getLevel(), player.getName(), data);
        try {
            Utils.playSound(Bukkit.getPlayer(player.getName()), "LEVEL_UP");
            Utils.playActionBar(Bukkit.getPlayer(player.getName()), "&aAhora eres nivel &c" + player.getLevel().getLevel());
        } catch (NullPointerException ignored) {
        }
    }
    
    @Override
    public void addPlayerLevel(User player, int level, GainSource source, ServerType serverSource){
        if (level <= 0) return;
        PlayerLevelChangeEvent event = new PlayerLevelChangeEvent(Bukkit.getPlayer(player.getName()), player.getLevel(), source, serverSource, ChangeType.ADD);
        if (event.isCancelled()) return;
        player.getLevel().addLevels(level);
        savePlayer(player);
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put(" &4• &7Cambio: &a+", String.valueOf(level));
        data.put(" &4• &7Nivel: &a", String.valueOf(player.getLevel().getLevel()));
        data.put(" &4• &7Nivel Formateado: &a", player.getLevel().getLevelNameFormatted());
        data.put(" &4•", player.getLevel().getProgressBarFormatted());
        data.put(" &4• &7Fuente: &a", source.getName());
        data.put(" &4• &7Servidor: &a", serverSource.getName());
        Main.getInstance().getLogs().createLogWithProps(LogType.XP_CHANGE, Settings.PROXY_SERVER_NAME, "&aSubiste al nivel &c" + player.getLevel().getLevel(), player.getName(), data);
        try {
            Utils.playSound(Bukkit.getPlayer(player.getName()), "LEVEL_UP");
            Utils.playActionBar(Bukkit.getPlayer(player.getName()), "&aSubiste al nivel &c" + player.getLevel().getLevel());
        } catch (NullPointerException ignored) {
        }
    }
    
    @Override
    public void removePlayerLevel(User player, int level, GainSource source, ServerType serverSource){
        PlayerLevelChangeEvent event = new PlayerLevelChangeEvent(Bukkit.getPlayer(player.getName()), player.getLevel(), source, serverSource, ChangeType.REMOVE);
        if (event.isCancelled()) return;
        player.getLevel().removeLevels(level);
        savePlayer(player);
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put(" &4• &7Cambio: &c-", String.valueOf(level));
        data.put(" &4• &7Nivel: &a", String.valueOf(player.getLevel().getLevel()));
        data.put(" &4• &7Nivel Formateado: &a", player.getLevel().getLevelNameFormatted());
        data.put(" &4•", player.getLevel().getProgressBarFormatted());
        data.put(" &4• &7Fuente: &a", source.getName());
        data.put(" &4• &7Servidor: &a", serverSource.getName());
        Main.getInstance().getLogs().createLogWithProps(LogType.XP_CHANGE, Settings.PROXY_SERVER_NAME, "&cBajaste al nivel &c" + player.getLevel().getLevel(), player.getName(), data);
        try {
            Utils.playSound(Bukkit.getPlayer(player.getName()), "LEVEL_UP");
            Utils.playActionBar(Bukkit.getPlayer(player.getName()), "&cBajaste al nivel &c" + player.getLevel().getLevel());
        } catch (NullPointerException ignored) {
        }
    }
    
    @Override
    public void setPlayerXp(User player, int xp, GainSource source, ServerType serverSource){
        PlayerXpChangeEvent event = new PlayerXpChangeEvent(Bukkit.getPlayer(player.getName()), xp, source, serverSource, ChangeType.SET);
        if (event.isCancelled()) return;
        player.getLevel().setXp(xp);
        savePlayer(player);
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put(" &4• &7Nivel: &a", String.valueOf(player.getLevel().getLevel()));
        data.put(" &4• &7Nivel Formateado: &a", player.getLevel().getLevelNameFormatted());
        data.put(" &4•", player.getLevel().getProgressBarFormatted());
        data.put(" &4• &7Fuente: &a", source.getName());
        data.put(" &4• &7Servidor: &a", serverSource.getName());
        Main.getInstance().getLogs().createLogWithProps(LogType.XP_CHANGE, Settings.PROXY_SERVER_NAME, "&aAhora tienes &c" + xp + " &5XP", player.getName(), data);
        try {
            Utils.playSound(Bukkit.getPlayer(player.getName()), "ORB_PICKUP");
            Utils.playActionBar(Bukkit.getPlayer(player.getName()), "&aAhora tienes &c" + xp + " &5XP");
        } catch (NullPointerException ignored) {
        }
    }
    
    @Override
    public void addPlayerXp(User player, int xp, GainSource source, ServerType serverSource){
        if (xp <= 0) return;
        PlayerXpChangeEvent event = new PlayerXpChangeEvent(Bukkit.getPlayer(player.getName()), xp, source, serverSource, ChangeType.SET);
        if (event.isCancelled()) return;
        player.getLevel().addXp(xp);
        savePlayer(player);
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put(" &4• &7Xp añadida: &a+", String.valueOf(xp));
        data.put(" &4• &7Nueva XP: &a", String.valueOf(player.getLevel().getCurrentXp()));
        data.put(" &4• &7Nivel: &a", String.valueOf(player.getLevel().getLevel()));
        data.put(" &4• &7Nivel Formateado: &a", player.getLevel().getLevelNameFormatted());
        data.put(" &4•", player.getLevel().getProgressBarFormatted());
        data.put(" &4• &7Fuente: &a", source.getName());
        data.put(" &4• &7Servidor: &a", serverSource.getName());
        Main.getInstance().getLogs().createLogWithProps(LogType.XP_CHANGE, Settings.PROXY_SERVER_NAME, "&a+" + xp + " XP", player.getName(), data);
        try {
            Utils.playSound(Bukkit.getPlayer(player.getName()), "ORB_PICKUP");
            Utils.playActionBar(Bukkit.getPlayer(player.getName()), "&a+" + xp + " XP");
        } catch (NullPointerException ignored) {
        }
    }
    
    @Override
    public void removePlayerXp(User player, int xp, GainSource source, ServerType serverSource){
        if (xp <= 0) return;
        PlayerXpChangeEvent event = new PlayerXpChangeEvent(Bukkit.getPlayer(player.getName()), xp, source, serverSource, ChangeType.REMOVE);
        if (event.isCancelled()) return;
        player.getLevel().removeXp(xp);
        savePlayer(player);
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put(" &4• &7Xp añadida: &c-", String.valueOf(xp));
        data.put(" &4• &7Nueva XP: &a", String.valueOf(player.getLevel().getCurrentXp()));
        data.put(" &4• &7Nivel: &a", String.valueOf(player.getLevel().getLevel()));
        data.put(" &4• &7Nivel Formateado: &a", player.getLevel().getLevelNameFormatted());
        data.put(" &4•", player.getLevel().getProgressBarFormatted());
        data.put(" &4• &7Fuente: &a", source.getName());
        data.put(" &4• &7Servidor: &a", serverSource.getName());
        Main.getInstance().getLogs().createLogWithProps(LogType.XP_CHANGE, Settings.PROXY_SERVER_NAME, "&c-" + xp + " XP", player.getName(), data);
        try {
            Utils.playSound(Bukkit.getPlayer(player.getName()), "ORB_PICKUP");
            Utils.playActionBar(Bukkit.getPlayer(player.getName()), "&c-" + xp + " XP");
        } catch (NullPointerException ignored) {
        }
        
    }
    
    @Override
    public void setPlayerRank(User player, Rank rank){
        PlayerRankChangeEvent event = new PlayerRankChangeEvent(Bukkit.getPlayer(player.getName()), rank, ChangeType.SET);
        if (event.isCancelled()) return;
        player.setRank(event.getRank());
        savePlayer(player);
        try {
            Utils.playSound(Bukkit.getPlayer(player.getName()), "LEVEL_UP");
            Utils.playActionBar(Bukkit.getPlayer(player.getName()), "&aSe te ha otorgado el rango: " + event.getRank().getTabPrefix());
        } catch (NullPointerException ignored) {
        }
    }
}
