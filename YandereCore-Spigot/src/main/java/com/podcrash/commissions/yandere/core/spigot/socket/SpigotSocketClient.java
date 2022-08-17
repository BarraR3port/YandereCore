package com.podcrash.commissions.yandere.core.spigot.socket;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.podcrash.commissions.yandere.core.common.data.server.ProxyStats;
import com.podcrash.commissions.yandere.core.common.socket.ISocket;
import com.podcrash.commissions.yandere.core.common.socket.ISocketClient;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.settings.Settings;
import com.podcrash.commissions.yandere.core.spigot.users.PlayersRepository;
import com.podcrash.commissions.yandere.core.spigot.users.SpigotUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.podcrash.commissions.yandere.core.spigot.Main.GSON;

public class SpigotSocketClient extends ISocket<SpigotUser> {
    private boolean reconnecting = false;
    private ProxySocket mainSocket;
    
    public SpigotSocketClient(PlayersRepository players){
        super(players);
        
    }
    
    public ISocketClient getSocket(){
        return mainSocket;
    }
    
    @Override
    public void sendJoinServer(UUID owner, String serverTarget){
        getPlayers().savePlayer(owner);
        final JsonObject js = new JsonObject();
        js.addProperty("type", "CONNECT_TO_SERVER");
        js.addProperty("current_server", Settings.SERVER_NAME);
        js.addProperty("server_target", serverTarget);
        js.addProperty("owner_uuid", owner.toString());
        js.addProperty("msg", "EMPTY");
        sendMessage(js);
    }
    
    @Override
    public void sendJoinServer(UUID owner, String serverTarget, String msg){
        getPlayers().savePlayer(owner);
        final JsonObject js = new JsonObject();
        js.addProperty("type", "CONNECT_TO_SERVER");
        js.addProperty("current_server", Settings.SERVER_NAME);
        js.addProperty("server_target", serverTarget);
        js.addProperty("owner_uuid", owner.toString());
        js.addProperty("msg", msg);
        sendMessage(js);
    }
    
    @Override
    public void sendMSGToPlayer(UUID target, String key){
        JsonObject js = new JsonObject();
        js.addProperty("type", "SEND_MSG_TO_PLAYER");
        js.addProperty("current_server", Settings.SERVER_NAME);
        js.addProperty("target_uuid", target.toString());
        js.addProperty("key", key);
        js.addProperty("has-replacements", false);
        sendMessage(js);
    }
    
    @Override
    public void sendMSGToPlayer(UUID target, String key, String word, String replacement){
        JsonObject js = new JsonObject();
        js.addProperty("type", "SEND_MSG_TO_PLAYER");
        js.addProperty("current_server", Settings.SERVER_NAME);
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
        js.addProperty("current_server", Settings.SERVER_NAME);
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
    public void sendUpdate(){
        JsonObject js = new JsonObject();
        js.addProperty("type", "UPDATE");
        js.addProperty("server_name", Settings.SERVER_NAME);
        js.addProperty("online_players", Bukkit.getOnlinePlayers().size());
        sendMessage(js);
    }
    
    @Override
    public void sendDisconnectInfoToProxy(){
        JsonObject js = new JsonObject();
        js.addProperty("type", "DISCONNECT");
        js.addProperty("server_name", Settings.SERVER_NAME);
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
    
    public SpigotSocketClient init() throws IOException{
        if (Settings.IS_SERVER_LINKED){
            if (mainSocket != null){
                mainSocket.socket.close();
            }
            Socket socket = new Socket(Settings.SOCKET_IP, Settings.SOCKET_PORT);
            mainSocket = new ProxySocket(socket, Settings.SERVER_NAME);
        }
        return this;
    }
    
    /**
     * Send arena data to the lobbies.
     */
    public void sendMessage(JsonObject message){
        if (message == null) return;
        if (Settings.IS_SERVER_LINKED){
            if (mainSocket == null){
                try {
                    Socket socket = new Socket(Settings.SOCKET_IP, Settings.SOCKET_PORT);
                    mainSocket = new ProxySocket(socket, Settings.SERVER_NAME);
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
    
    /**
     * Close active sockets.
     */
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
            
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
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
                                    Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                                        Main.getInstance().setProxyStats(GSON.fromJson(json.getAsJsonObject("stats"), ProxyStats.class));
                                    });
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
                                    continue;
                                }
                                
                                
                                /*case "INIT_CREATE_WORLD":{
                                    if (!json.has("server_name")) continue;
                                    if (!json.has("world_name")) continue;
                                    if (!json.has("owner_uuid")) continue;
                                    if (!json.has("world_uuid")) continue;
                                    if (!json.has("world_version")) continue;
                                    if (!json.has("world_server")) continue;
                                    if (!json.has("world_layer_material")) continue;
    
                                    String world_name = json.get("world_name").getAsString();
                                    UUID owner = UUID.fromString(json.get("owner_uuid").getAsString());
                                    UUID world_uuid = UUID.fromString(json.get("world_uuid").getAsString());
                                    String world_version = json.get("world_version").getAsString();
                                    String world_server = json.get("world_server").getAsString();
                                    String world_layer_material = json.get("world_layer_material").getAsString();
                                    Main.debug("Received INIT_CREATE_WORLD request: world_name:" + world_name + " world_uuid:" + world_uuid + " world_version:" + world_version + " world_server:" + world_server);
                                    if (Settings.SERVER_NAME.equals(world_server)){
                                        Bukkit.getScheduler().runTask(Main.getInstance(), ( ) -> {
                                            Main.debug("[World Creation] [Phase 1/2] Creating World: " + world_name);
                                            BWorld world = new BWorld(owner, world_name, world_server, world_version, world_uuid, world_layer_material);
                                            getWorlds().createCustomLayerWorld(world, world_layer_material);
                                            Main.debug("[World Creation] [Phase 2/2] Creating World: " + world_uuid);
                                            getWorlds().createWorld(world);
                                            final Player p = Bukkit.getPlayer(owner);
                                            final boolean teleport = p == null;
                                            getWorlds().addPlayerToTP(owner, world);
                                            Bukkit.getScheduler().runTask(Main.getInstance(), ( ) -> Main.getInstance().managePermissions(owner, world_uuid, false));
                                            
                                            json.remove("type");
                                            json.addProperty("type", "INIT_CREATE_WORLD_SUCCESS");
                                            json.addProperty("teleport", teleport);
                                            sendMessage(json);
                                            Main.debug("[World Creation] Sending message to Proxy: " + json);
                                        });
                                    } else {
                                        Main.debug("Received INIT_CREATE_WORLD but its meant for another server.\n " + json);
                                    }
                                    continue;
                                }
                                case "REMOVE_PLAYER_TO_TP_TO_WORLD":{
                                    if (!json.has("uuid")) continue;
                                    UUID owner = UUID.fromString(json.get("uuid").getAsString());
                                    try {
                                        getWorlds().removePlayerToTP(owner);
                                    } catch (Exception ignored) {
                                    }
                                    continue;
                                }
                                case "UPDATE_INV_POST_INIT_CREATE_WORLD_SUCCESS":{
                                    if (!json.has("server_name")) continue;
                                    if (!json.has("world_name")) continue;
                                    if (!json.has("owner_uuid")) continue;
                                    if (!json.has("world_uuid")) continue;
                                    if (!json.has("world_version")) continue;
                                    if (!json.has("world_server")) continue;
                                    if (!json.has("world_layer_material")) continue;
                                    if (!json.has("teleport")) continue;
                                    final UUID owner = UUID.fromString(json.get("owner_uuid").getAsString());
                                    final Player p = Bukkit.getPlayer(owner);
                                    if (p.getInventory().getHolder() instanceof IUpdatableMenu){
                                        ((IUpdatableMenu) p.getOpenInventory().getTopInventory().getHolder()).reOpen();
                                    }
                                    continue;
                                }
                                case "UPDATE_INV_WORLD_DELETE_SUCCESS":{
                                    if (!json.has("current_server")) continue;
                                    if (!json.has("server_target")) continue;
                                    if (!json.has("owner_uuid")) continue;
                                    if (!json.has("world_uuid")) continue;
                                    if (!json.has("same_server")) continue;
                                    if (!json.has("has_permission")) continue;
                                    final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                    try {
                                        final Player p = Bukkit.getPlayer(owner_uuid);
                                        
                                        if (p.getInventory().getHolder() instanceof AddPlayersToWorldMenuSelector){
                                            AddPlayersToWorldMenuSelector plotMenu = (AddPlayersToWorldMenuSelector) p.getInventory().getHolder();
                                            plotMenu.handleAccept();
                                        }
                                        
                                    } catch (NullPointerException ignored) {
                                    }
                                    continue;
                                }
                                case "JOIN_WORLD_REQUEST_PREV":{
                                    if (!json.has("server_target")) continue;
                                    if (!json.has("current_server")) continue;
                                    if (!json.has("owner_uuid")) continue;
                                    if (!json.has("world_uuid")) continue;
                                    if (!json.has("item_slot")) continue;
                                    final String server_target = json.get("server_target").getAsString();
                                    final String current_server = json.get("current_server").getAsString();
                                    final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                    final UUID world_uuid = UUID.fromString(json.get("world_uuid").getAsString());
                                    final BWorld world = getWorlds().getWorld(world_uuid);
                                    if (server_target.equals(current_server)){
                                        final Player p = Bukkit.getPlayer(owner_uuid);
                                        final World localWorld = Bukkit.getWorld(world.getUUID().toString());
                                        if (p != null && localWorld != null && (world.getOwner().equals(owner_uuid) || world.getMembers().contains(owner_uuid))){
                                            Main.debug("Teleporting " + p + " to " + localWorld.getName());
                                            Bukkit.getScheduler().runTask(Main.getInstance(), ( ) -> p.teleport(localWorld.getSpawnLocation()));
                                            getWorlds().addPlayerToWorldOnlineMembers(owner_uuid, world_uuid);
                                        } else {
                                            Main.debug("Teleporting " + owner_uuid + " to " + world.getUUID().toString());
                                        }
                                        sendMSGToPlayer(owner_uuid, "world.join", "world", world.split("-")[0]);
                                        continue;
                                    }
                                    
                                    json.remove("type");
                                    json.addProperty("type", "JOIN_WORLD_REQUEST_POST");
    
                                    if (world.getOwner().equals(owner_uuid) || world.getMembers().contains(owner_uuid) || getPlayers().getPlayer(owner_uuid).getRank().isAdmin()){
                                        getWorlds().addPlayerToTP(owner_uuid, world);
                                        json.addProperty("response", true);
                                    } else {
                                        json.addProperty("response", false);
                                    }
                                    sendMessage(json);
                                    continue;
                                }
                                case "JOIN_WORLD_REQUEST_POST_DENY":{
                                    if (!json.has("owner_uuid")) continue;
                                    if (!json.has("item_slot")) continue;
                                    final int item_slot = json.get("item_slot").getAsInt();
                                    final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                    try {
                                        final Player p = Bukkit.getPlayer(owner_uuid);
                                        if (p.getOpenInventory().getTopInventory().getHolder() instanceof WorldManagerMenu){
                                            WorldManagerMenu menu = (WorldManagerMenu) p.getOpenInventory().getTopInventory().getHolder();
                                            menu.checkSomething(p, item_slot, menu.getInventory().getItem(item_slot), "&cSin permisos", "&cNo tienes permisos", menu.getMenuUUID());
                                            sendMSGToPlayer(owner_uuid, "error.world.not-allowed-to-join-world");
                                        }
                                    } catch (Exception ignored) {
                                    }
                                    continue;
                                }
                                case "JOIN_PLOT_REQUEST_PREV":{
                                    if (!json.has("current_server")) continue;
                                    if (!json.has("server_target")) continue;
                                    if (!json.has("server_version")) continue;
                                    if (!json.has("owner_uuid")) continue;
                                    if (!json.has("plot_id")) continue;
                                    if (!json.has("item_slot")) continue;
                                    if (!json.has("plot_type")) continue;
                                    
                                    vs.getPlotManager().manageJoinPlot(json);
                                    continue;
                                }
                                case "JOIN_PLOT_REQUEST_POST_DENY":{
                                    if (!json.has("owner_uuid")) continue;
                                    if (!json.has("item_slot")) continue;
                                    final int item_slot = json.get("item_slot").getAsInt();
                                    final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                    try {
                                        final Player p = Bukkit.getPlayer(owner_uuid);
                                        if (p.getOpenInventory().getTopInventory().getHolder() instanceof WorldManagerMenu){
                                            WorldManagerMenu menu = (WorldManagerMenu) p.getOpenInventory().getTopInventory().getHolder();
                                            menu.checkSomething(p, item_slot, menu.getInventory().getItem(item_slot), "&cSin permisos", "&cNo tienes permisos", menu.getMenuUUID());
                                            sendMSGToPlayer(owner_uuid, "error.world.not-allowed-to-join-world");
                                        }
                                    } catch (Exception ignored) {
                                    }
                                    continue;
                                }
                                case "WORLD_DELETE_INIT":{
                                    if (!json.has("current_server")) continue;
                                    if (!json.has("server_target")) continue;
                                    if (!json.has("owner_uuid")) continue;
                                    if (!json.has("world_uuid")) continue;
                                    if (!json.has("same_server")) continue;
                                    if (!json.has("has_permission")) continue;
                                    final boolean has_permission = json.get("has_permission").getAsBoolean();
                                    final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                    final UUID world_uuid = UUID.fromString(json.get("world_uuid").getAsString());
                                    final BWorld world = getWorlds().getWorld(world_uuid);
                                    final String server_target = json.get("server_target").getAsString();
                                    json.remove("type");
                                    
                                    if (world.getOwner().equals(owner_uuid) || has_permission){
                                        Bukkit.getScheduler().runTask(Main.getInstance(), ( ) -> getWorlds().deleteWorldFromOutside(owner_uuid, world, server_target, json));
                                    } else {
                                        json.addProperty("type", "ERROR");
                                        json.addProperty("error", "WORLD_DELETE_FAILED");
                                        sendMessage(json);
                                        continue;
                                    }
                                    continue;
                                }
                                case "KICK_FROM_WORLD_PREV":{
                                    if (!json.has("server_target")) continue;
                                    if (!json.has("current_server")) continue;
                                    if (!json.has("world_uuid")) continue;
                                    if (!json.has("world_uuid")) continue;
                                    if (!json.has("owner_uuid")) continue;
                                    if (!json.has("target_uuid")) continue;
                                    final String server_target = json.get("server_target").getAsString();
                                    final String currentServer = json.get("current_server").getAsString();
                                    final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                    final UUID target_uuid = UUID.fromString(json.get("target_uuid").getAsString());
                                    if (server_target.equalsIgnoreCase(currentServer)){
                                        if (owner_uuid.equals(target_uuid)){
                                            sendMSGToPlayer(owner_uuid, "world.cant-kick-own");
                                            continue;
                                        }
                                    }
                                    
                                    json.remove("type");
                                    json.addProperty("type", "WORLD_KICK_SUCCESS");
                                    sendMessage(json);
                                    continue;
                                }
                                case "WORLD_KICK_SUCCESS":{
                                    if (!json.has("world_uuid")) continue;
                                    if (!json.has("world_uuid")) continue;
                                    if (!json.has("owner_uuid")) continue;
                                    if (!json.has("target_uuid")) continue;
                                    final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                    final UUID target_uuid = UUID.fromString(json.get("target_uuid").getAsString());
                                    final UUID world_uuid = UUID.fromString(json.get("world_uuid").getAsString());
                                    
                                    try {
                                        final HashMap<String, String> replace = new HashMap<>();
                                        replace.put("world", world_uuid.toString());
                                        replace.put("player", getPlayers().getPlayer(target_uuid).getName());
                                        sendMSGToPlayer(owner_uuid, "world.kick-success", replace);
                                    } catch (NullPointerException ignored) {
                                    }
                                    continue;
                                }
                                
                                
                                case "VISIT_REQUEST_PREV":{
                                    if (!json.has("current_server")) continue;
                                    if (!json.has("owner_uuid")) continue;
                                    if (!json.has("target_uuid")) continue;
                                    final String currentServer = json.get("current_server").getAsString();
                                    final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                    final UUID target_uuid = UUID.fromString(json.get("target_uuid").getAsString());
                                    
                                    final User targetUser = getPlayers().getPlayer(target_uuid);
                                    if (targetUser == null){
                                        sendMSGToPlayer(owner_uuid, "error.player-not-found");
                                        continue;
                                    }
                                    if (!targetUser.getOption("allow-visit-world-requests") && !targetUser.getOption("allow-visit-plot-requests")){
                                        sendMSGToPlayer(owner_uuid, "error.player.not-available-to-be-visited", "player", targetUser.getName());
                                        continue;
                                    }
                                    switch(Settings.SERVER_TYPE){
                                        case LOBBY:{
                                            sendMSGToPlayer(owner_uuid, "error.player.in-lobby", "player", targetUser.getName());
                                            continue;
                                        }
                                        case PLOT:{
                                            if (!targetUser.getOption("allow-visit-plot-requests")){
                                                sendMSGToPlayer(owner_uuid, "error.player.not-available-to-be-visited", "player", targetUser.getName());
                                                continue;
                                            } else {
                                                sendMSGToPlayer(owner_uuid, "visit.sent", "player", targetUser.getName());
                                                vs.getPlotManager().manageVisitJoinPlot(owner_uuid, targetUser, currentServer, Settings.SERVER_NAME);
                                            }
                                            continue;
                                        }
                                        case WORLDS:{
                                            if (!targetUser.getOption("allow-visit-world-requests")){
                                                sendMSGToPlayer(owner_uuid, "error.player.not-available-to-be-visited", "player", targetUser.getName());
                                                continue;
                                            } else {
                                                final Player targetPlayer = Bukkit.getPlayer(target_uuid);
                                                if (targetPlayer != null){
                                                    try {
                                                        final BWorld world = getWorlds().getWorld(UUID.fromString(targetPlayer.getWorld().getName()));
                                                        final String current_server = json.get("current_server").getAsString();
                                                        final UUID world_uuid = world.getUUID();
                                                        if (current_server.equals(Settings.SERVER_NAME)){
                                                            final Player p = Bukkit.getPlayer(owner_uuid);
                                                            final World localWorld = Bukkit.getWorld(world.getUUID().toString());
                                                            if (p != null && localWorld != null && (world.getOwner().equals(owner_uuid) || world.getMembers().contains(owner_uuid))){
                                                                Main.debug("Teleporting " + p + " to " + localWorld.getName());
                                                                Bukkit.getScheduler().runTask(Main.getInstance(), ( ) -> p.teleport(localWorld.getSpawnLocation()));
                                                                getWorlds().addPlayerToWorldOnlineMembers(owner_uuid, world_uuid);
                                                            } else {
                                                                Main.debug("Teleporting " + owner_uuid + " to " + world.getUUID().toString());
                                                            }
                                                            sendMSGToPlayer(owner_uuid, "world.join", "world", world.split("-")[0]);
                                                            continue;
                                                        }
    
                                                        json.remove("type");
                                                        json.addProperty("type", "JOIN_WORLD_REQUEST_POST");
                                                        json.addProperty("world_uuid", world_uuid.toString());
                                                        json.addProperty("server_target", Settings.SERVER_NAME);
                                                        json.addProperty("item_slot", 0);
    
                                                        if (world.getOwner().equals(owner_uuid) || world.getMembers().contains(owner_uuid) || getPlayers().getPlayer(owner_uuid).getRank().isAdmin()){
                                                            getWorlds().addPlayerToTP(owner_uuid, world);
                                                            json.addProperty("response", true);
                                                        } else {
                                                            sendMSGToPlayer(owner_uuid, "visit.sent", "player", targetUser.getName());
                                                            final WorldVisitRequest request = new WorldVisitRequest(owner_uuid, target_uuid, null, currentServer, Settings.SERVER_NAME);
                                                            getWorlds().manageVisitJoinWorld(request);
                                                            continue;
                                                        }
                                                        sendMessage(json);
                                                        continue;
    
                                                    } catch (IllegalArgumentException ignored) {
                                                    }
                                                }
    
                                                sendMSGToPlayer(owner_uuid, "visit.sent", "player", targetUser.getName());
                                                final WorldVisitRequest request = new WorldVisitRequest(owner_uuid, target_uuid, null, currentServer, Settings.SERVER_NAME);
                                                getWorlds().manageVisitJoinWorld(request);
                                            }
                                            continue;
                                        }
                                    }
                                    continue;
                                }
                                case "VISIT_REQUEST_DENY":{
                                    if (!json.has("current_server")) continue;
                                    if (!json.has("owner_uuid")) continue;
                                    if (!json.has("reason")) continue;
                                    final Player p = Bukkit.getPlayer(UUID.fromString(json.get("owner_uuid").getAsString()));
                                    if (p != null){
                                        final String reason = json.get("reason").getAsString();
                                        Bukkit.getScheduler().runTask(Main.getInstance(), ( ) -> Main.getLang().sendErrorMsg(p, reason));
                                    }
                                    continue;
                                }
    
                                case "JOIN_HOME_PREV":{
                                    if (!json.has("current_server")) continue;
                                    if (!json.has("server_target")) continue;
                                    if (!json.has("home_uuid")) continue;
                                    if (!json.has("owner_uuid")) continue;
                                    try {
                                        final String currentServer = json.get("current_server").getAsString();
                                        final String targetServer = json.get("server_target").getAsString();
                                        final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                        final SpigotHome home = getHomes().getHome(UUID.fromString(json.get("home_uuid").getAsString()));
                                        if (currentServer.equalsIgnoreCase(targetServer)){
                                            Bukkit.getScheduler().runTask(Main.getInstance(), ( ) -> {
                                                final Player player = Bukkit.getPlayer(owner_uuid);
                                                player.teleport(home.getBukkitLocation());
                                                Main.getLang().sendMsg(player, "home.tp-to-home", "home", home.getName());
                                            });
                                        } else {
                                            getHomes().addPlayerToTP(owner_uuid, home);
                                            json.remove("type");
                                            json.addProperty("type", "JOIN_HOME_POST");
                                            json.addProperty("tp", true);
                                            sendMessage(json);
                                        }
                                        continue;
                                    } catch (Exception e) {
                                        json.remove("type");
                                        json.addProperty("type", "JOIN_HOME_POST");
                                        json.addProperty("tp", false);
                                        sendMessage(json);
                                        continue;
                                    }
                                }
                                case "JOIN_WARP_PREV":{
                                    if (!json.has("current_server")) continue;
                                    if (!json.has("server_target")) continue;
                                    if (!json.has("warp_uuid")) continue;
                                    if (!json.has("owner_uuid")) continue;
                                    try {
                                        final String currentServer = json.get("current_server").getAsString();
                                        final String targetServer = json.get("server_target").getAsString();
                                        final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                        final SpigotWarp warp = getWarps().getWarp(UUID.fromString(json.get("warp_uuid").getAsString()));
                                        if (currentServer.equalsIgnoreCase(targetServer)){
                                            Bukkit.getScheduler().runTask(Main.getInstance(), ( ) -> {
                                                final Player player = Bukkit.getPlayer(owner_uuid);
                                                player.teleport(warp.getBukkitLocation());
                                                Main.getLang().sendMsg(player, "warps.tp-to-warp", "warp", warp.getType().getName());
                                            });
                                        } else {
                                            getWarps().addPlayerToTP(owner_uuid, warp);
                                            json.remove("type");
                                            json.addProperty("type", "JOIN_WARP_POST");
                                            json.addProperty("tp", true);
                                            sendMessage(json);
                                        }
                                        continue;
                                    } catch (Exception e) {
                                        json.remove("type");
                                        json.addProperty("type", "JOIN_WARP_POST");
                                        json.addProperty("tp", false);
                                        sendMessage(json);
                                        continue;
                                    }
                                }
                                case "ERROR":{
                                    if (!json.has("error")) continue;
                                    final String error = json.get("error").getAsString();
                                    switch(error){
                                        case "SERVER_NOT_ONLINE":{
                                            if (!json.has("owner_uuid")) continue;
                                            if (!json.has("server_target")) continue;
                                            final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                            final String server_target = json.get("server_target").getAsString();
                                            sendMSGToPlayer(owner_uuid, "error.server.not-online", "server", server_target);
                                        }
                                        case "WORLD_DELETE_FAILED":{
                                            if (!json.has("owner_uuid")) continue;
                                            if (!json.has("current_server")) continue;
                                            if (!json.has("server_target")) continue;
                                            if (!json.has("world_uuid")) continue;
                                            final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                            final String server_target = json.get("server_target").getAsString();
                                            final String world_uuid = json.get("world_uuid").getAsString();
                                            try {
                                                final HashMap<String, String> replace = new HashMap<>();
                                                replace.put("server", server_target);
                                                replace.put("world", world_uuid);
                                                sendMSGToPlayer(owner_uuid, "error.world.delete-failed", replace);
    
                                            } catch (NullPointerException ignored) {
                                            }
                                        }
                                    }
                                    continue;
                                }
                                case "SUCCESS_MSG":{
                                    if (!json.has("success_type")) continue;
                                    final String success_type = json.get("success_type").getAsString();
                                    if (success_type.equals("VISIT_REQUEST")){
                                        final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                        final String target_name = json.get("target_name").getAsString();
                                        Main.getLang().sendMsg(Bukkit.getPlayer(owner_uuid), "visit.sent", "player", target_name);
                                    }
                                    switch(success_type){
                                        case "VISIT_REQUEST":{
                                            if (!json.has("owner_uuid")) continue;
                                            if (!json.has("server_target")) continue;
                                            final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                            final String server_target = json.get("server_target").getAsString();
                                            sendMSGToPlayer(owner_uuid, "error.server.not-online", "server", server_target);
                                        }
                                        case "WORLD_DELETE_FAILED":{
                                            if (!json.has("owner_uuid")) continue;
                                            if (!json.has("current_server")) continue;
                                            if (!json.has("server_target")) continue;
                                            if (!json.has("world_uuid")) continue;
                                            final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                            final String server_target = json.get("server_target").getAsString();
                                            final String world_uuid = json.get("world_uuid").getAsString();
                                            try {
                                                final HashMap<String, String> replace = new HashMap<>();
                                                replace.put("server", server_target);
                                                replace.put("world", world_uuid);
                                                sendMSGToPlayer(owner_uuid, "error.world.delete-failed", replace);
    
                                            } catch (NullPointerException ignored) {
                                            }
                                        }
                                    }
                                }*/
                            }
                        } else {
                            reconnect("Server Closed Connection");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        
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
        
        public void reconnect(String msg){
            if (reconnecting) return;
            AtomicInteger reconnect_attempts = new AtomicInteger(1);
            if (!Main.getInstance().isEnabled()) return;
            disable("Reconnecting");
            reconnecting = true;
            Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), () -> {
                if (!reconnecting) return;
                int currentAttempt = reconnect_attempts.getAndIncrement();
                Main.debug("Reconnecting to Proxy Socket, attempt: " + currentAttempt + " of 10");
                if (currentAttempt > 10){
                    reconnecting = false;
                    Bukkit.shutdown();
                    return;
                }
                try {
                    init();
                    Main.debug("Reconnected to Proxy Socket");
                    sendUpdate();
                    reconnecting = false;
                } catch (IOException e) {
                    Main.debug("Failed to reconnect to " + name + " attempt " + currentAttempt);
                }
            }, 20, 150);
            
        }
        
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
