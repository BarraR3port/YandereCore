package com.podcrash.commissions.yandere.core.spigot.socket;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.podcrash.commissions.yandere.core.common.data.server.ProxyStats;
import com.podcrash.commissions.yandere.core.common.data.user.IPlayerRepository;
import com.podcrash.commissions.yandere.core.common.socket.ISocket;
import com.podcrash.commissions.yandere.core.common.socket.ISocketClient;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.party.PartiesManager;
import com.podcrash.commissions.yandere.core.spigot.settings.Settings;
import com.podcrash.commissions.yandere.core.spigot.task.RepeatingTaskSync;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.podcrash.commissions.yandere.core.spigot.Main.GSON;

public class SpigotSocketClient extends ISocket {
    private boolean reconnecting = false;
    private ProxySocket mainSocket;
    
    public SpigotSocketClient(IPlayerRepository players){
        super(players);
        
    }
    
    public ISocketClient getSocket(){
        return mainSocket;
    }
    
    @Override
    public void sendJoinServer(UUID owner, String serverTarget){
        if (serverTarget.equals(Settings.PROXY_SERVER_NAME)){
            Bukkit.getPlayer(owner).sendMessage(Utils.format("&cYa estás conectado en este server."));
            return;
        }
        getPlayers().savePlayer(owner);
        final JsonObject js = new JsonObject();
        js.addProperty("type", "CONNECT_TO_SERVER");
        js.addProperty("current_server", Settings.PROXY_SERVER_NAME);
        js.addProperty("server_target", serverTarget);
        js.addProperty("owner_uuid", owner.toString());
        js.addProperty("msg", "EMPTY");
        if (PartiesManager.isInParty(owner)){
            if (PartiesManager.isPartyLeader(owner)){
                List<Player> players = PartiesManager.getPlayersParty(owner);
                players.forEach(player -> getPlayers().savePlayer(player.getUniqueId()));
                String uuids = players.stream().map(Player::getUniqueId).map(UUID::toString).collect(Collectors.joining(";"));
                js.addProperty("party_members", uuids);
            }
        }
        sendMessage(js);
    }
    
    @Override
    public void sendJoinServer(UUID owner, String serverTarget, String msg){
        if (serverTarget.equals(Settings.PROXY_SERVER_NAME)){
            Bukkit.getPlayer(owner).sendMessage(Utils.format("&cYa estás conectado en este server."));
            return;
        }
        getPlayers().savePlayer(owner);
        final JsonObject js = new JsonObject();
        js.addProperty("type", "CONNECT_TO_SERVER");
        js.addProperty("current_server", Settings.PROXY_SERVER_NAME);
        js.addProperty("server_target", serverTarget);
        js.addProperty("owner_uuid", owner.toString());
        js.addProperty("msg", msg);
        if (PartiesManager.isInParty(owner)){
            if (PartiesManager.isPartyLeader(owner)){
                List<Player> players = PartiesManager.getPlayersParty(Bukkit.getPlayer(owner));
                players.forEach(player -> getPlayers().savePlayer(player.getUniqueId()));
                String uuids = players.stream().map(Player::getUniqueId).map(UUID::toString).collect(Collectors.joining(";"));
                js.addProperty("party_members", uuids);
            }
        }
        sendMessage(js);
    }
    
    @Override
    public void sendMSGToPlayer(UUID target, String key){
        JsonObject js = new JsonObject();
        js.addProperty("type", "SEND_MSG_TO_PLAYER");
        js.addProperty("current_server", Settings.PROXY_SERVER_NAME);
        js.addProperty("target_uuid", target.toString());
        js.addProperty("key", key);
        js.addProperty("has-replacements", false);
        sendMessage(js);
    }
    
    @Override
    public void sendMSGToPlayer(UUID target, String key, String word, String replacement){
        JsonObject js = new JsonObject();
        js.addProperty("type", "SEND_MSG_TO_PLAYER");
        js.addProperty("current_server", Settings.PROXY_SERVER_NAME);
        js.addProperty("target_uuid", target.toString());
        js.addProperty("key", key);
        js.addProperty("has-replacements", true);
        JsonObject replacements = new JsonObject();
        replacements.addProperty(word, replacement);
        js.add("replacements", replacements);
        sendMessage(js);
    }
    
    @Override
    public void sendMSGToPlayer(UUID target, String key, HashMap<String, String> replacementsMap){
        JsonObject js = new JsonObject();
        js.addProperty("type", "SEND_MSG_TO_PLAYER");
        js.addProperty("current_server", Settings.PROXY_SERVER_NAME);
        js.addProperty("target_uuid", target.toString());
        js.addProperty("key", key);
        js.addProperty("has-replacements", true);
        JsonObject replacements = new JsonObject();
        for ( String word : replacementsMap.keySet() ){
            replacements.addProperty(word, replacementsMap.get(word));
        }
        js.add("replacements", replacements);
        sendMessage(js);
    }
    
    @Override
    public void sendCheckPluginUpdates(){
        JsonObject js = new JsonObject();
        js.addProperty("type", "CHECK_PLUGIN_UPDATES");
        js.addProperty("current_server", Settings.PROXY_SERVER_NAME);
        sendMessage(js);
    }
    
    @Override
    public void sendCheckGlobalServerStatsFetchData(){
        JsonObject js = new JsonObject();
        js.addProperty("type", "GLOBAL_SERVER_FETCH");
        js.addProperty("current_server", Settings.PROXY_SERVER_NAME);
        sendMessage(js);
    }
    
    @Override
    public void sendUpdate(){
        JsonObject js = new JsonObject();
        js.addProperty("type", "UPDATE");
        js.addProperty("server_name", Settings.PROXY_SERVER_NAME);
        js.addProperty("online_players", Bukkit.getOnlinePlayers().size());
        sendMessage(js);
    }
    
    @Override
    public void sendDisconnectInfoToProxy(){
        JsonObject js = new JsonObject();
        js.addProperty("type", "DISCONNECT");
        js.addProperty("server_name", Settings.PROXY_SERVER_NAME);
        sendMessage(js);
    }
    
    @Override
    public String encrypt(String msg){
        msg = msg.replace("\\n", "");
        msg = msg.replace("\n", "");
        msg = msg.replace("\\\"", "\"");
        msg = msg.replace("\"{", "{");
        msg = msg.replace("}\"", "}");
        return Base64.getEncoder().encodeToString(msg.getBytes());
    }
    
    @Override
    public String decrypt(String data){
        return new String(Base64.getDecoder().decode(data.getBytes()));
    }
    
    @Override
    public SpigotSocketClient init() throws IOException{
        if (Settings.IS_SERVER_LINKED){
            if (mainSocket != null){
                mainSocket.socket.close();
            }
            Socket socket = new Socket(Settings.SOCKET_IP, Settings.SOCKET_PORT);
            mainSocket = new ProxySocket(socket, Settings.PROXY_SERVER_NAME);
        }
        return this;
    }
    
    @Override
    public void sendMessage(JsonObject message){
        if (message == null) return;
        if (Settings.IS_SERVER_LINKED){
            if (mainSocket == null){
                try {
                    Socket socket = new Socket(Settings.SOCKET_IP, Settings.SOCKET_PORT);
                    mainSocket = new ProxySocket(socket, Settings.PROXY_SERVER_NAME);
                    mainSocket.sendMessage(message);
                    reconnecting = false;
                } catch (IOException err) {
                    err.printStackTrace();
                }
            } else {
                mainSocket.sendMessage(message);
            }
        }
    }
    
    @Override
    public void disable(){
        mainSocket.disable("Close active sockets");
    }
    
    private class ProxySocket implements ISocketClient {
        private final Socket socket;
        private final String name;
        private PrintWriter out;
        private Scanner in;
        private boolean compute = true;
    
        private ProxySocket(Socket socket, String name){
            this.name = name;
            this.socket = socket;
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException ignored) {
                out = null;
                return;
            }
            try {
                in = new Scanner(socket.getInputStream());
            } catch (IOException ignored) {
                return;
            }
        
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new BukkitRunnable() {
                @Override
                public void run(){
                    while (compute) {
                        try {
                            if (in.hasNext()){
                                final String encrypted = in.nextLine();
                                String decryptedMessage = decrypt(encrypted);
                                if (decryptedMessage == null || decryptedMessage.isEmpty()){
                                    Main.debug("Received bad data from: " + socket.getInetAddress().toString() + "\nMsg: " + encrypted);
                                    continue;
                                }
                                final JsonObject json;
                                try {
                                    json = new JsonParser().parse(decryptedMessage).getAsJsonObject();
                                } catch (JsonSyntaxException e) {
                                    Main.debug("Received bad data from: " + socket.getInetAddress().toString());
                                    continue;
                                }
                                if (json == null) continue;
                                if (!json.has("type")) continue;
                                final String type = json.get("type").getAsString().toUpperCase();
                                if (!type.equals("UPDATE_SERVER_STATS") && !type.equals("MSG_RECEIVED")){
                                    Main.debug("Received message from " + name + ": \n" + GSON.toJson(json));
                                }
                                switch(type){
                                    case "MSG_RECEIVED":{
                                        Main.debug("Received message from " + name + ": \n" + GSON.toJson(json));
                                        continue;
                                    }
                                    case "UPDATE_SERVER_STATS":{
                                        if (!json.has("stats")) continue;
                                        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> Main.getInstance().setProxyStats(GSON.fromJson(json.getAsJsonObject("stats"), ProxyStats.class)));
                                        continue;
                                    }
                                    case "CHECK_PLUGIN_UPDATES":
                                    case "CHECK_PLUGIN_UPDATES_POST":{
                                        Utils.sendMessage(Bukkit.getConsoleSender(), "&c&lYandere &7- Checking for plugin updates externally...");
                                        Bukkit.getScheduler().runTask(Main.getInstance(), () -> Main.getInstance().getServerRepository().checkForPluginsUpdates());
                                        continue;
                                    }
                                    case "GLOBAL_SERVER_FETCH_POST":{
                                        Utils.sendMessage(Bukkit.getConsoleSender(), "&c&lYandere &7- Checking for Server Settings updates externally...");
                                        Bukkit.getScheduler().runTask(Main.getInstance(), () -> Main.getInstance().getGlobalServerSettings().fetch());
                                        continue;
                                    }
                                    case "SEND_MSG_TO_PLAYER_POST":{
                                        if (!json.has("target_uuid")) continue;
                                        if (!json.has("current_server")) continue;
                                        if (!json.has("key")) continue;
                                        final UUID target_uuid = UUID.fromString(json.get("target_uuid").getAsString());
                                        final String key = json.get("key").getAsString();
                                        final boolean hasReplacements = json.get("has-replacements").getAsBoolean();
    
                                        try {
                                            final Player player = Bukkit.getPlayer(target_uuid);
                                            if (player == null) continue;
                                            if (hasReplacements){
                                                final JsonObject replacements = json.get("replacements").getAsJsonObject();
                                                final HashMap<String, String> replace = new HashMap<>();
    
                                                for ( Map.Entry<String, JsonElement> entry : replacements.entrySet() ){
                                                    replace.put(entry.getKey(), entry.getValue().getAsString());
                                                }
                                                player.sendMessage(Main.getLang().getMSG(key, replace));
                                            } else {
                                                player.sendMessage(Main.getLang().getMSG(key));
                                            }
        
                                        } catch (NullPointerException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            } else {
                                reconnect("Server Closed Connection");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
    
                }
            
            });
        }
    
        @Override
        public boolean sendMessage(JsonObject message){
            if (socket == null){
                reconnect("Socket is null");
                return false;
            }
            if (!socket.isConnected()){
                reconnect("Socket is not connected");
                return false;
            }
            if (out == null){
                reconnect("Output stream is null");
                return false;
            }
            if (in == null){
                reconnect("Input stream is null");
                return false;
            }
            if (out.checkError()){
                reconnect("Output stream has error");
                return false;
            }
            if (Settings.DEVELOPMENT_MODE){
                Main.debug("Sending message to " + name + ": \n" + GSON.toJson(message));
                Main.debug("Sending Encrypted Message to " + name + ": \n" + encrypt(GSON.toJson(message)));
            } else {
                Main.debug("Sending message to " + name + ": \n" + GSON.toJson(message));
            }
            final UUID msgUUID = UUID.randomUUID();
            message.addProperty("socket-msg-uuid", msgUUID.toString());
            out.println(encrypt(GSON.toJson(message)));
        
            return true;
        }
    
        @Override
        public void reconnect(String msg){
            if (reconnecting) return;
            AtomicInteger reconnect_attempts = new AtomicInteger(1);
            if (!Main.getInstance().isEnabled()) return;
            disable("Reconnecting");
            reconnecting = true;
            new RepeatingTaskSync(Main.getInstance(), 20, 200) {
                @Override
                public void run(){
                    if (!reconnecting) return;
                    int currentAttempt = reconnect_attempts.getAndIncrement();
                    Main.debug("Reconnecting to Proxy Socket, attempt: " + currentAttempt + " of 10");
                    if (currentAttempt > 10){
                        reconnecting = false;
                        cancel(); // cancels itself
                        Bukkit.shutdown();
                        return;
                    }
                    try {
                        init();
                        Main.debug("Reconnected to Proxy Socket");
                        sendUpdate();
                        reconnecting = false;
                        cancel(); // cancels itself
                    } catch (IOException e) {
                        Main.debug("Failed to reconnect to " + name + " attempt " + currentAttempt + " attempting again in 10 seconds");
                    }
                }
            };
        
        }
    
        @Override
        public void disable(String reason){
            compute = false;
            Main.debug("Disabling socket: " + socket.toString());
            Main.debug("Disabling socket Reason: " + reason);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            in = null;
            out = null;
        }
    }
}
