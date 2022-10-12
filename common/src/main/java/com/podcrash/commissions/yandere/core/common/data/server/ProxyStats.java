package com.podcrash.commissions.yandere.core.common.data.server;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public final class ProxyStats {
    private final HashMap<String, Server> servers = new HashMap<>();
    
    private final EmptyServer emptyServer = new EmptyServer();
    
    public static String getDefaultServerCapacity(){
        int maxPlayers = 1000;
        int currentPlayers = 0;
        double l1 = ((maxPlayers - currentPlayers) / (double) (maxPlayers)) * 10;
        int locked = (int) l1;
        int unlocked = 10 - locked;
        if (locked < 0 || unlocked < 0){
            locked = 10;
            unlocked = 0;
        }
        String progress = "&8 [{progress}&8]".replace("{progress}", "&c" + String.valueOf(new char[unlocked]).replace("\0", "■") + "&7" + String.valueOf(new char[locked]).replace("\0", "■"));
        double percentage = ((double) currentPlayers / (double) maxPlayers) * 100;
        String formattedPercentage = new DecimalFormat("####.#").format(percentage);
        return progress + " &e" + formattedPercentage + "%";
    }
    
    public HashMap<String, Server> getServers(){
        return servers;
    }
    
    public void addServer(Server server){
        servers.put(server.getProxyName(), server);
    }
    
    public void removeServer(String serverName){
        servers.remove(serverName);
    }
    
    public Server getRandomServerByType(ServerType type){
        List<Server> list = servers.values().stream().filter(server -> server.getServerType() == type).collect(Collectors.toList());
        Collections.shuffle(list);
        return list.stream().findFirst().orElse(emptyServer);
    }
    
    public Server getRandomServer(){
        return servers.values().stream().findAny().orElse(emptyServer);
    }
    
    public int getServerSizeByType(ServerType type){
        return servers.values().stream().filter(server -> server.getServerType() == type).mapToInt(Server::getOnlinePlayers).sum();
    }
    
    public int getAllPlayerSize(){
        return servers.values().stream().mapToInt(Server::getOnlinePlayers).sum();
    }
    
    public int getTargetServerSize(String serverProxyName){
        return servers.values().stream().filter(server -> server.getProxyName().equals(serverProxyName)).mapToInt(Server::getOnlinePlayers).sum();
    }
    
    public boolean isServerByTypeOnline(ServerType type){
        return servers.values().stream().anyMatch(server -> server.getServerType() == type);
    }
    
    public boolean isTargetServerOnline(String serverProxyName){
        return servers.values().stream().anyMatch(server -> server.getProxyName().equals(serverProxyName));
    }
    
    public int getTargetServerPlayers(String serverProxyName){
        return servers.values().stream().filter(server -> server.getProxyName().equals(serverProxyName)).mapToInt(Server::getOnlinePlayers).sum();
    }
    
    public EmptyServer getEmptyServer(){
        return emptyServer;
    }
    
    public String getTargetServerCapacity(ServerType type, GlobalServerSettings settings){
        int serversSize = (int) servers.values().stream().filter(server -> server.getServerType() == type).count();
        if (serversSize == 0) return null;
        int maxPlayers = settings.getMax(type) * serversSize;
        int currentPlayers = servers.values().stream().filter(server -> server.getServerType().equals(type)).mapToInt(Server::getOnlinePlayers).sum();
        double l1 = ((maxPlayers - currentPlayers) / (double) (maxPlayers)) * 10;
        int locked = (int) l1;
        int unlocked = 10 - locked;
        if (locked < 0 || unlocked < 0){
            locked = 10;
            unlocked = 0;
        }
        String progress = "&8 [{progress}&8]".replace("{progress}", "&c" + String.valueOf(new char[unlocked]).replace("\0", "■") + "&7" + String.valueOf(new char[locked]).replace("\0", "■"));
        double percentage = ((double) currentPlayers / (double) maxPlayers) * 100;
        String formattedPercentage = new DecimalFormat("####.#").format(percentage);
        return progress + " &e" + formattedPercentage + "%";
    }
    
    public int getCurrentTargetServerCapacity(ServerType type){
        int serversSize = (int) servers.values().stream().filter(server -> server.getServerType() == type).count();
        if (serversSize == 0) return 0;
        return servers.values().stream().filter(server -> server.getServerType().equals(type)).mapToInt(Server::getOnlinePlayers).sum();
    }
    
    public String getCurrentTargetServerCapacityFormatted(ServerType type, GlobalServerSettings settings){
        int current = getCurrentTargetServerCapacity(type);
        int serversSize = (int) servers.values().stream().filter(server -> server.getServerType() == type).count();
        if (serversSize == 0) return current + "/" + 75;
        int maxPlayers = settings.getMax(type) * serversSize;
        return getCurrentTargetServerCapacity(type) + "/" + maxPlayers;
    }
    
}
