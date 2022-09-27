package com.podcrash.commissions.yandere.core.velocity.socketmanager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.podcrash.commissions.yandere.core.common.data.server.Server;
import com.podcrash.commissions.yandere.core.common.data.server.ServerType;
import com.podcrash.commissions.yandere.core.velocity.VMain;
import com.podcrash.commissions.yandere.core.velocity.manager.ServerSocketManager;
import com.podcrash.commissions.yandere.core.velocity.utils.Utils;
import com.velocitypowered.api.proxy.ConnectionRequestBuilder;
import com.velocitypowered.api.proxy.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static com.podcrash.commissions.yandere.core.velocity.VMain.GSON;

public class ProxySocketServer implements Runnable {
    
    private final Socket socket;
    private Scanner in;
    private PrintWriter out;
    private Server server;
    
    public ProxySocketServer(Socket socket){
        this.socket = socket;
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void closeConnections(){
        try {
            if (!socket.isClosed()){
                socket.close();
            }
            
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
    
    public void sendProxyServerStats(){
        final JsonObject js = new JsonObject();
        js.addProperty("type", "UPDATE_SERVER_STATS");
        js.addProperty("stats", GSON.toJson(VMain.getInstance().getServerManager()));
        sendMessage(js);
    }
    
    
    public void sendMessage(JsonObject message){
        if (socket == null) return;
        if (!socket.isConnected()) return;
        if (out == null) return;
        if (in == null) return;
        if (out.checkError()) return;
        final String type = message.get("type").getAsString();
        if (type != null && !type.equals("MSG_RECEIVED") && !type.equals("UPDATE_SERVER_STATS")){
            VMain.debug("Sending message to " + (server == null ? "Unknown" : server.getProxyName()) + ": \n" + GSON.toJson(message));
        }
        out.println(encrypt(GSON.toJson(message)));
    }
    
    @Override
    public void run(){
        while (ServerSocketTask.compute && socket.isConnected()) {
            try {
                if (in.hasNext()){
                    final String encrypted = in.nextLine();
                    String decryptedMessage = decrypt(encrypted);
                    if (decryptedMessage == null || decryptedMessage.isEmpty()){
                        VMain.debug("Received bad data from: " + socket.getInetAddress().toString() + "\nMsg: " + encrypted);
                        continue;
                    }
                    final JsonObject json;
                    try {
                        
                        JsonElement jse = new JsonParser().parse(decryptedMessage);
                        if (jse.isJsonNull() || !jse.isJsonObject()){
                            VMain.debug("Received bad data from: " + socket.getInetAddress().toString() + "\nMsg: " + decryptedMessage);
                            continue;
                        }
                        json = jse.getAsJsonObject();
                    } catch (JsonSyntaxException e) {
                        VMain.debug("Received bad data from: " + socket.getInetAddress().toString() + "\nMsg: " + decryptedMessage);
                        e.printStackTrace();
                        continue;
                    }
                    
                    if (!json.has("type")) continue;
                    if (!json.has("socket-msg-uuid")) continue;
                    String type = json.get("type").getAsString();
                    
                    if (type.equals("UPDATE")){
                        if (!json.has("server_name")) continue;
                        final String server_name = json.get("server_name").getAsString();
                        final ServerType serverType = ServerType.match(server_name);
                        final int online_players = json.get("online_players").getAsInt();
                        ServerSocketTask.otherTasks.add(VMain.getInstance().getProxy().getScheduler().buildTask(VMain.getInstance(), () -> {
                                    server = new Server(server_name, online_players, serverType);
                                    ServerSocketManager.getInstance().registerServerSocket(server_name, this);
                                    VMain.getInstance().getServerManager().addServer(server);
                                }
                        ).schedule());
                        continue;
                    }
                    
                    VMain.debug("Received message from " + (server == null ? "Unknown" : server.getProxyName()) + ": \n" + GSON.toJson(json));
                    final UUID msgUUID = UUID.fromString(json.get("socket-msg-uuid").getAsString());
                    final JsonObject msg_received = new JsonObject();
                    msg_received.addProperty("type", "MSG_RECEIVED");
                    msg_received.addProperty("socket-msg-uuid", msgUUID.toString());
                    sendMessage(msg_received);
                    try {
                        switch(type){
                            case "DISCONNECT" -> {
                                if (!json.has("server_name")) continue;
                                if (json.get("server_name").getAsString() == null) continue;
                                final String server_name = json.get("server_name").getAsString();
                                VMain.getInstance().getServerManager().removeServer(server_name);
                                if (VMain.getInstance().getProxy().getServer(server_name).isPresent()){
                                    VMain.getInstance().getProxy().getServer(server_name).get().getPlayersConnected().forEach(player -> player.createConnectionRequest(VMain.getInstance().getProxy().getServer(VMain.getInstance().getServerManager().getRandomServerByType(ServerType.LOBBY).getProxyName()).get()).connect());
                                }
                            }
                            case "SEND_MSG_TO_PLAYER" -> {
                                if (!json.has("target_uuid")) continue;
                                if (!json.has("current_server")) continue;
                                if (!json.has("key")) continue;
                                final UUID target_uuid = UUID.fromString(json.get("target_uuid").getAsString());
    
                                json.remove("type");
                                json.addProperty("type", "SEND_MSG_TO_PLAYER_POST");
                                VMain.getInstance().getProxy().getPlayer(target_uuid).flatMap(p -> p.getCurrentServer().flatMap(server -> ServerSocketManager.getSocketByServer(server.getServerInfo().getName()))).ifPresent(socket -> socket.sendMessage(json));
                            }
                            case "CHECK_PLUGIN_UPDATES" -> {
                                json.remove("type");
                                json.addProperty("type", "CHECK_PLUGIN_UPDATES_POST");
                                VMain.getInstance().getProxy().getAllServers().forEach(server -> ServerSocketManager.getSocketByServer(server.getServerInfo().getName()).ifPresent(socket -> socket.sendMessage(json)));
    
                                //socketManager.getSocketServers().forEach(socketServer -> socketServer.sendMessage(json));
                            }
                            case "GLOBAL_SERVER_FETCH" -> {
                                json.remove("type");
                                json.addProperty("type", "GLOBAL_SERVER_FETCH_POST");
                                VMain.getInstance().getProxy().getAllServers().forEach(server -> ServerSocketManager.getSocketByServer(server.getServerInfo().getName()).ifPresent(socket -> socket.sendMessage(json)));
    
                                //socketManager.getSocketServers().forEach(socketServer -> socketServer.sendMessage(json));
                            }
                            case "CONNECT_TO_SERVER" -> {
                                if (!json.has("server_target")) continue;
                                if (!json.has("current_server")) continue;
                                if (!json.has("owner_uuid")) continue;
                                if (!json.has("msg")) continue;
                                final String serverName = json.get("server_target").getAsString();
                                final String currentServer = json.get("current_server").getAsString();
                                final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                final String msg = json.get("msg").getAsString();
                                List<UUID> party_members = new ArrayList<>();
                                if (json.has("party_members")){
                                    party_members = Arrays.stream(json.get("party_members").getAsString().split(";")).map(UUID::fromString).collect(Collectors.toList());
                                } else {
                                    party_members.add(owner_uuid);
                                }
                                for ( UUID party_member_uuid : party_members ){
                                    if (VMain.getInstance().getProxy().getPlayer(party_member_uuid).isPresent()){
                                        final Player p = VMain.getInstance().getProxy().getPlayer(party_member_uuid).get();
                                        if (currentServer.equalsIgnoreCase(serverName)){
                                            p.sendMessage(Utils.format("&cYa estás conectado en el server " + serverName));
                                            return;
                                        }
                                        if (serverName.equals("EMPTY")){
                                            p.sendMessage(Utils.format("&cEste server está cerrado."));
                                            continue;
                                        }
    
                                        VMain.getInstance().getProxy().getServer(serverName).ifPresent(server -> {
                                            try {
                                                ConnectionRequestBuilder.Result result = p.createConnectionRequest(server).connect().get(5, TimeUnit.SECONDS);
                                                if (!result.getStatus().equals(ConnectionRequestBuilder.Status.SUCCESS)){
                                                    p.sendMessage(Utils.format("&cError al conectar, espera unos segundos."));
                                                }
                                            } catch (InterruptedException | ExecutionException |
                                                     TimeoutException ignored) {
                                                p.sendMessage(Utils.format("&cError al conectar, espera unos segundos."));
                                            }
                                        });
                                        if (!msg.equalsIgnoreCase("EMPTY")){
                                            p.sendMessage(Utils.format(msg.replace("%player%", p.getUsername())));
                                        }
                                    }
                                }
    
                            }
                            case "ERROR" -> {
                                if (!json.has("error")) continue;
                                final String error = json.get("error").getAsString();
                                VMain.getInstance().getLogger().severe(error);
                                switch(error){
                                    case "WORLD_DELETE_FAILED":{
                                        if (!json.has("owner_uuid")) continue;
                                        if (!json.has("current_server")) continue;
                                        if (!json.has("server_target")) continue;
                                        if (!json.has("world_uuid")) continue;
                                        final String current_server = json.get("current_server").getAsString();
    
                                        ServerSocketManager.getSocketByServer(current_server).ifPresent(socket -> socket.sendMessage(json));
    
                                    }
                                    case "SERVER_NOT_ONLINE":{
                                        if (!json.has("owner_uuid")) continue;
                                        if (!json.has("current_server")) continue;
                                        if (!json.has("server_target")) continue;
                                        if (!json.has("world_uuid")) continue;
                                        final String current_server = json.get("current_server").getAsString();
                                        ServerSocketManager.getSocketByServer(current_server).ifPresent(socket -> socket.sendMessage(json));
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        socket.close();
                        VMain.getInstance().getLogger().info("Socket closed: " + socket);
                        VMain.getInstance().getServerManager().removeServer(server.getProxyName());
                        
                    } catch (IOException | NullPointerException e) {
                        e.printStackTrace();
                    }
                    
                    Thread.currentThread().interrupt();
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            socket.close();
            VMain.getInstance().getLogger().info("Socket closed: " + socket);
            if (server != null)
                VMain.getInstance().getServerManager().removeServer(server.getProxyName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread.currentThread().interrupt();
    }
    
    
    public PrintWriter getOut(){
        return out;
    }
    
    public String encrypt(String msg){
        msg = msg.replace("\\n", "");
        msg = msg.replace("\n", "");
        msg = msg.replace("\\\"", "\"");
        msg = msg.replace("\"{", "{");
        msg = msg.replace("}\"", "}");
        return Base64.getEncoder().encodeToString(msg.getBytes());
    }
    
    public String decrypt(String data){
        return new String(Base64.getDecoder().decode(data.getBytes()));
    }
}

