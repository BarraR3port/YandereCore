package com.podcrash.comissions.yandere.core.velocity.socketmanager;

import com.google.gson.*;
import com.podcrash.comissions.yandere.core.common.data.server.IServerStats;
import com.podcrash.comissions.yandere.core.common.data.server.Server;
import com.podcrash.comissions.yandere.core.common.data.server.ServerType;
import com.podcrash.comissions.yandere.core.velocity.VMain;
import com.podcrash.comissions.yandere.core.velocity.manager.ServerSocketManager;
import com.podcrash.comissions.yandere.core.velocity.utils.Utils;
import com.velocitypowered.api.proxy.ConnectionRequestBuilder;
import com.velocitypowered.api.proxy.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ProxySocketServer implements Runnable {
    
    private final Socket socket;
    private final Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
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
            socket.close();
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
    
    public void sendProxyServerStats(IServerStats stats){
        final JsonObject js = new JsonObject();
        js.addProperty("type", "UPDATE_SERVER_STATS");
        js.addProperty("stats", gson.toJson(stats));
        sendMessage(js);
    }
    
    
    public void sendMessage(JsonObject message){
        if (socket == null){
            return;
        }
        if (!socket.isConnected()){
            return;
        }
        if (out == null){
            return;
        }
        if (in == null){
            return;
        }
        if (out.checkError()){
            return;
        }
        final String type = message.get("type").getAsString();
        if (type != null && !type.equals("MSG_RECEIVED")){
            VMain.debug("Sending message to " + (server == null ? "Unknown" : server.getProxyName()) + ": \n" + gson.toJson(message));
        }
        out.println(encrypt(gson.toJson(message)));
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
                    
                    /*TODO Fix thiss ASAPPPPPPPPPPPP*/
                    /*TODO Fix thiss ASAPPPPPPPPPPPP*/
                    /*TODO Fix thiss ASAPPPPPPPPPPPP*/
                    /*TODO Fix thiss ASAPPPPPPPPPPPP*/
                    /*TODO Fix thiss ASAPPPPPPPPPPPP*/
                    /*TODO Fix thiss ASAPPPPPPPPPPPP*/
                    if (type.equals("UPDATE")){
                        if (!json.has("server_type")) continue;
                        final String server_name = json.get("server_name").getAsString();
                        final ServerType serverType = ServerType.match(server_name);
                        final int online_players = json.get("online_players").getAsInt();
                        server = new Server(server_name, online_players, serverType);
                        ServerSocketManager.getInstance().registerServerSocket(server_name, this);
                        VMain.getInstance().getServerManager().addServer(server);
                        continue;
                    }
                    /*TODO Fix thiss ASAPPPPPPPPPPPP*/
                    /*TODO Fix thiss ASAPPPPPPPPPPPP*/
                    /*TODO Fix thiss ASAPPPPPPPPPPPP*/
                    /*TODO Fix thiss ASAPPPPPPPPPPPP*/
                    /*TODO Fix thiss ASAPPPPPPPPPPPP*/
                    /*TODO Fix thiss ASAPPPPPPPPPPPP*/
                    /*TODO Fix thiss ASAPPPPPPPPPPPP*/
                    
                    
                    VMain.debug("Received message from " + (server == null ? "Unknown" : server.getProxyName()) + ": \n" + gson.toJson(json));
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
                            case "CONNECT_TO_SERVER" -> {
                                if (!json.has("server_target")) continue;
                                if (!json.has("current_server")) continue;
                                if (!json.has("owner_uuid")) continue;
                                if (!json.has("msg")) continue;
                                final String serverName = json.get("server_target").getAsString();
                                final String currentServer = json.get("current_server").getAsString();
                                final UUID owner_uuid = UUID.fromString(json.get("owner_uuid").getAsString());
                                final String msg = json.get("msg").getAsString();
                                if (serverName.equals("EMPTY")) continue;
                                if (VMain.getInstance().getProxy().getPlayer(owner_uuid).isPresent()){
                                    final Player p = VMain.getInstance().getProxy().getPlayer(owner_uuid).get();
                                    if (currentServer.equalsIgnoreCase(serverName)){
                                        p.sendMessage(Utils.format("&cYa estás conectado en el server " + serverName));
                                        return;
                                    }
                                    VMain.getInstance().getProxy().getServer(serverName).ifPresent(server -> {
                                        try {
                                            ConnectionRequestBuilder.Result result = p.createConnectionRequest(server).connect().get(2, TimeUnit.SECONDS);
                                            if (!result.getStatus().equals(ConnectionRequestBuilder.Status.SUCCESS)){
                                                p.sendMessage(Utils.format("&cError al conectar, probablemente tu versión no es compatible con la de este server."));
                                            }
                                        } catch (InterruptedException | ExecutionException | TimeoutException ignored) {
                                        }
                                    });
                                    if (!msg.equalsIgnoreCase("EMPTY")){
                                        p.sendMessage(Utils.format(msg.replace("%player%", p.getUsername())));
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

