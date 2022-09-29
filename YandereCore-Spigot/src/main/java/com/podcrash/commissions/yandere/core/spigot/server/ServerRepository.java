package com.podcrash.commissions.yandere.core.spigot.server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.model.Filters;
import com.podcrash.commissions.yandere.core.common.data.plugin.LyPlugin;
import com.podcrash.commissions.yandere.core.common.data.server.IServerRepository;
import com.podcrash.commissions.yandere.core.common.data.server.Server;
import com.podcrash.commissions.yandere.core.common.data.server.repo.LoadedPlugin;
import com.podcrash.commissions.yandere.core.common.data.server.repo.OutPlugin;
import com.podcrash.commissions.yandere.core.common.data.server.repo.Response;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.settings.Settings;
import net.lymarket.lyapi.common.Api;
import net.lymarket.lyapi.common.db.MongoDBClient;
import net.lymarket.lyapi.spigot.utils.Utils;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.commons.io.FileUtils;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Level;

public class ServerRepository extends IServerRepository {
    private final OkHttpClient httpClient = new OkHttpClient();
    private Server server;
    
    public ServerRepository(MongoDBClient database, String tableName){
        super(database, tableName);
        this.server = initializeServer();
    }
    
    @Override
    public Server initializeServer(){
        if (server == null){
            String serverID = Settings.WEB_UUID;
            if (serverID == null && Main.getInstance().getConfig().getConfigVersion() < 1.1){
                Server server = new Server(getUUIDFromPterodactyl(), Settings.PROXY_SERVER_NAME, Bukkit.getOnlinePlayers().size(), Settings.SERVER_TYPE);
                database.insertOne(TABLE_NAME, server);
                this.server = server;
                Main.getInstance().getConfig().set("web.uuid", server.getUuid().toString());
                Main.getInstance().saveConfig();
            } else {
                Document doc = database.findOneFast(TABLE_NAME, Filters.eq("uuid", serverID));
                if (doc != null){
                    server = Api.getGson().fromJson(doc.toJson(), Server.class);
                    Main.getInstance().getConfig().set("web.uuid", server.getUuid().toString());
                    Main.getInstance().saveConfig();
                } else {
                    Server server = new Server(Settings.PROXY_SERVER_NAME, Bukkit.getOnlinePlayers().size(), Settings.SERVER_TYPE);
                    database.insertOne(TABLE_NAME, server);
                    this.server = server;
                }
            }
        }
        
        return server;
    }
    
    @Override
    public Server getServer(){
        return server;
    }
    
    @Override
    public boolean isServerRegistered(){
        return false;
    }
    
    @Override
    public void checkForPluginsUpdates(){
        pluginFilesGarbageCollector();
        try {
            Main.getInstance().getLogger().info("[UPDATE MACHINE] Initializing the ~Updating Machine~ ...");
            
            StringBuilder resultado = new StringBuilder();
            //ArrayList<OutPlugin> plugins = Arrays.stream(Bukkit.getPluginManager().getPlugins()).map(plugin -> new OutPlugin(plugin.getName())).collect(Collectors.toCollection(ArrayList::new));
            ArrayList<OutPlugin> plugins = new ArrayList<>();
            Iterator<File> it = FileUtils.iterateFiles(new File(Main.getInstance().getDataFolder().getAbsolutePath().substring(0, Main.getInstance().getDataFolder().getAbsolutePath().length() - 11)), null, false);
            ArrayList<LoadedPlugin> loadedPlugins = new ArrayList<>(); // estos son los plugins que están en el server.
            while (it.hasNext()) {
                File file = it.next();
                String pl = file.getName();
                if (pl.endsWith(".jar")){
                    String pluginName = pl.replace(".jar", "").split("-")[0];
                    Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
                    if (plugin != null){
                        String hash = String.format(Locale.ROOT, "%032x", new BigInteger(1, MessageDigest.getInstance("MD5").digest(Files.readAllBytes(file.toPath()))));
                        plugins.add(new OutPlugin(plugin.getName(), plugin.getName(), hash));
                        loadedPlugins.add(new LoadedPlugin(plugin.getName(), plugin.getName(), hash, plugin.getDescription().getVersion()));
                    } else {
                        plugins.add(new OutPlugin(pluginName));
                    }
                }
            }
            String json = Api.getGson().toJson(plugins);
            Request request = new Request.Builder()
                    .url(Settings.WEB_URL + "/api/lydark/server/checkPlugins")
                    .addHeader("User-Agent", "OkHttp Bot")
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json; utf-8")
                    .addHeader("Web-Api-Key", Settings.WEB_KEY)
                    .addHeader("msg", json)
                    .build();
            
            try (okhttp3.Response rs = httpClient.newCall(request).execute()) {
                if (!rs.isSuccessful()){
                    Main.getInstance().getLogger().log(Level.SEVERE, "[UPDATE MACHINE] ERROR -> Failed to Check the plugins.");
                    Main.getInstance().getLogger().log(Level.SEVERE, "[UPDATE MACHINE] ERROR MSG: " + rs);
                    return;
                }
    
                BufferedReader rd = new BufferedReader(new InputStreamReader(rs.body().byteStream()));
                String linea;
                while ((linea = rd.readLine()) != null) {
                    resultado.append(linea);
                }
                rd.close();
    
                String response = resultado.toString();
    
                /*JsonElement jElement = new JsonParser( ).parse( resultado.toString( ) );*/
                Response res = Api.getGson().fromJson(response, Response.class);
                if (res.getType().equals("plugins")){
                    ArrayList<LyPlugin> pluginsFounded = res.getPlugins(); // estos son los plugins que están en el servidor y a la vez en el mongo
                    HashMap<LyPlugin, LoadedPlugin> pluginsToUpdate = new HashMap<>();
                    Main.getInstance().getLogger().warning("[UPDATE MACHINE] I have found " + pluginsFounded.size() + " plugins matches with the DB");
                    Main.getInstance().getLogger().warning("[UPDATE MACHINE] Checking if there is something to update...");
                    server.clearPlugins();
                    try {
                        for ( LyPlugin plugin : pluginsFounded ){
                            server.addPlugin(plugin.getUuid());
                            try {
                                for ( LoadedPlugin pluginLoaded : loadedPlugins ){
                                    if (plugin.getBukkitName().equalsIgnoreCase(pluginLoaded.getBukkitName())){
                                        System.out.println("Comparando " + plugin.getBukkitName() + " hash " + pluginLoaded.getBukkitName());
                                        System.out.println("---------- " + plugin.getHash() + " ------> " + pluginLoaded.getHash());
                                        if (!plugin.getHash().equals(pluginLoaded.getHash()) || !plugin.getVersion().equals(pluginLoaded.getVersion())){
                                            Main.getInstance().getLogger().log(Level.WARNING, "[UPDATE MACHINE] There is a new version of the plugin " + plugin.getBukkitName() + " (" + plugin.getVersion() + "#" + plugin.getHash());
                                            pluginsToUpdate.put(plugin, pluginLoaded);
                                        }
                                    }
                                }
                    
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    saveServer();
                    if (pluginsToUpdate.size() > 0){
                        Main.getInstance().getLogger().warning("[UPDATE MACHINE] I have found " + pluginsToUpdate.size() + " plugins to update");
                        Main.getInstance().getLogger().warning("[UPDATE MACHINE] Starting the update process...");
                        pluginsToUpdate.forEach(this::updatePlugin);
                        Main.getInstance().getLogger().warning("[UPDATE MACHINE] Every Plugin has successfully updated!");
                        Main.getInstance().getLogger().severe("[UPDATE MACHINE] Restarting.");
                        Main.getInstance().getLogger().severe("[UPDATE MACHINE] Restarting..");
                        Main.getInstance().getLogger().severe("[UPDATE MACHINE] Restarting...");
                        Main.getInstance().getLogger().severe("[UPDATE MACHINE] Restarting....");
                        Main.getInstance().getLogger().severe("[UPDATE MACHINE] Restarting.....");
                        for ( Player player : Bukkit.getOnlinePlayers() ){
                            player.kickPlayer(Utils.format("&cEl server se está reiniciando!"));
                        }
                        Bukkit.shutdown();
    
                    } else {
                        Main.getInstance().getLogger().info("[UPDATE MACHINE] You are up to date!");
                    }
                } else if (res.getType().equals("success")){
                    Main.getInstance().getLogger().info("[UPDATE MACHINE] You are up to date!");
                } else {
                    Main.getInstance().getLogger().severe("[UPDATE MACHINE] There is something wrong with the server, please contact the AL TIO BARRAAAAAAAAAAAAAAAAAA");
                }
            }
        } catch (IOException | NullPointerException | ConcurrentModificationException |
                 NoSuchAlgorithmException ignored) {
        }
        
    }
    
    public void pluginFilesGarbageCollector(){
        List<String> pluginsToDelete = Main.getInstance().getConfig().getStringList("web.pluginsToDelete");
        if (pluginsToDelete.size() > 0){
            Main.getInstance().getLogger().info("[UPDATE MACHINE] Found " + pluginsToDelete.size() + " old plugins files pending to be deleted!");
            for ( String path : pluginsToDelete ){
                String plugin = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
                Main.getInstance().getLogger().info("[UPDATE MACHINE] Deleting Plugin: " + plugin);
                try {
                    FileUtils.forceDelete(FileUtils.getFile(path));
                } catch (IOException ignored) {
                    if (path.contains("LyBedWars")){
                        try {
                            FileUtils.forceDelete(FileUtils.getFile(path));
                        } catch (IOException ignored1) {
                        }
                    }
                }
            }
            Main.getInstance().getConfig().set("web.pluginsToDelete", null);
            Main.getInstance().saveConfig();
        }
    }
    
    public void saveServer(){
        database.replaceOneFast(TABLE_NAME, Filters.eq("uuid", server.getUuid().toString()), server);
        Main.getInstance().getConfig().set("web.uuid", server.getUuid().toString());
        Main.getInstance().getConfig().set("web.plugins", server.getPluginsFormatted());
        Main.getInstance().saveConfig();
    }
    
    public void updatePlugin(LyPlugin plugin, LoadedPlugin outdatedPlugin){
        boolean isMainPlugin = plugin.getBukkitName().equals("YandereCore");
        Main.getInstance().getLogger().info("[UPDATE MACHINE] Updating " + plugin.getBukkitName() + "V:" + outdatedPlugin.getVersion() + " -> " + plugin.getVersion());
        Main.getInstance().getLogger().info("[UPDATE MACHINE] STEP 1/5 -> Downloading the Plugin");
        try {
            OkHttpClient httpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(Settings.WEB_URL + "/api/lydark/plugins/downloads/" + plugin.getUuid())
                    .addHeader("User-Agent", "OkHttp Bot")
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json; utf-8")
                    .addHeader("Web-Api-Key", Settings.WEB_KEY)
                    .build();
            
            try (okhttp3.Response rs = httpClient.newCall(request).execute()) {
                if (!rs.isSuccessful()){
                    Main.getInstance().getLogger().log(Level.SEVERE, "[UPDATE MACHINE] ERROR -> Failed to Download the plugin: " + plugin.getName() + " !");
                    Main.getInstance().getLogger().log(Level.SEVERE, "[UPDATE MACHINE] ERROR MSG: " + rs);
                    return;
                }
                if (!isMainPlugin){
                    Main.getInstance().getLogger().info("[UPDATE MACHINE] STEP 2/5 -> Disabling the Plugin " + outdatedPlugin.getBukkitName());
                    Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin(outdatedPlugin.getBukkitName()));
                } else {
                    Main.getInstance().getLogger().info("[UPDATE MACHINE] STEP 2/5 -> Main Plugin Update Found" + outdatedPlugin.getBukkitName());
                    Main.getInstance().getLogger().info("[UPDATE MACHINE] STEP 2/5 -> Waiting to update all the plugins to update this one.");
                }
    
                String path;
                String plugin_to_delete = outdatedPlugin.getName() + "-" + outdatedPlugin.getVersion() + ".jar";
                try {
                    path = Bukkit.getUpdateFolderFile().getAbsolutePath().substring(0, Bukkit.getUpdateFolderFile().getAbsolutePath().length() - 7) + "/" + new File(Bukkit.getPluginManager().getPlugin(outdatedPlugin.getName()).getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getName();
                } catch (Exception e) {
                    path = Bukkit.getUpdateFolderFile().getAbsolutePath().substring(0, Bukkit.getUpdateFolderFile().getAbsolutePath().length() - 7) + "/" + plugin_to_delete;
                }
                try {
                    if (!isMainPlugin){
                        Main.getInstance().getLogger().info("[UPDATE MACHINE] STEP 3/5 -> Deleting the file");
                        FileUtils.forceDelete(FileUtils.getFile(path));
                    } else {
                        throw new IOException();
                    }
                } catch (IOException e) {
                    Main.getInstance().getLogger().log(Level.SEVERE, "[UPDATE MACHINE] ERROR -> Couldn't delete the file with the normal way, trying with the panel api!");
        
                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "{\"root\":\"/plugins\",\"files\":[\"" + plugin_to_delete + "\"]}");
                    //RequestBody formBody = new FormBody.Builder().add("root", "/plugins").add( "files" , "["+plugin_to_delete+"]" ).build();
                    Request requestToDelete = new Request.Builder()
                            .url("https://control.yanderecraft.com/api/client/servers/beff672a/files/delete")
                            .addHeader("User-Agent", "OkHttp Bot")
                            .addHeader("Accept", "application/json")
                            .addHeader("Content-Type", "application/json; utf-8")
                            .addHeader("Authorization", "Bearer ptlc_MW0HtBp99Ln1khIgRevctUK50pOGPwBWo4mAZysUelS")
                            .post(body)
                            .build();
                    try (okhttp3.Response response = httpClient.newCall(requestToDelete).execute()) {
                        if (response.isSuccessful()){
                            Main.getInstance().getLogger().info("[UPDATE MACHINE] STEP 3/5 -> Successfully deleted the old plugin file: " + plugin_to_delete);
                        } else {
                            Main.getInstance().getLogger().log(Level.SEVERE, "[UPDATE MACHINE] ERROR -> Couldn't delete the file, don't worry, it will be deleted when the server restarts!");
                            ArrayList<String> pluginsToDelete = (ArrayList<String>) Main.getInstance().getConfig().getStringList("web.pluginsToDelete");
                            pluginsToDelete.add(path);
                            Main.getInstance().getConfig().set("web.pluginsToDelete", pluginsToDelete);
                            Main.getInstance().saveConfig();
                            FileUtils.forceDeleteOnExit(FileUtils.getFile(path));
                        }
                    }
                }
    
                String pathTarget = Bukkit.getUpdateFolderFile().getAbsolutePath().substring(0, Bukkit.getUpdateFolderFile().getAbsolutePath().length() - 7) + "/" + plugin.getName() + "-" + plugin.getVersion() + ".jar";
                try (InputStream in = rs.body().byteStream()) {
                    Main.getInstance().getLogger().info("[UPDATE MACHINE] STEP 4/5 -> Installing the Plugin");
                    Files.copy(in, Paths.get(pathTarget), StandardCopyOption.REPLACE_EXISTING);
                }
                Main.getInstance().getLogger().info("[UPDATE MACHINE] STEP 5/5 The plugin " + plugin.getName() + " has been updated to V: " + plugin.getVersion());
    
            }
            
        } catch (IOException e) {
            Main.getInstance().getLogger().log(Level.WARNING, "[UPDATE MACHINE] ERROR -> Couldn't download the plugin");
        }
        
    }
    
    public UUID getUUIDFromPterodactyl(){
        try {
            OkHttpClient httpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://control.yanderecraft.com/api/application/servers/external/" + Settings.PROXY_SERVER_NAME)
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json; utf-8")
                    .addHeader("Authorization", "Bearer ptlc_MW0HtBp99Ln1khIgRevctUK50pOGPwBWo4mAZysUelS")
                    .build();
            try (okhttp3.Response rs = httpClient.newCall(request).execute()) {
                if (!rs.isSuccessful()){
                    Main.getInstance().getLogger().log(Level.SEVERE, "[UPDATE MACHINE] ERROR -> Failed to get the UUID.");
                    Main.getInstance().getLogger().log(Level.SEVERE, "[UPDATE MACHINE] ERROR MSG: " + rs);
                    throw new IOException("Unexpected code " + rs);
                }
                StringBuilder resultado = new StringBuilder();
                BufferedReader rd = new BufferedReader(new InputStreamReader(rs.body().byteStream()));
                String linea;
                while ((linea = rd.readLine()) != null) {
                    resultado.append(linea);
                }
                rd.close();
                String response = resultado.toString();
                JsonObject jObject = new JsonParser().parse(response).getAsJsonObject();
                return UUID.fromString(jObject.get("attributes").getAsJsonObject().get("uuid").getAsString());
                
            }
        } catch (IOException | NullPointerException | ConcurrentModificationException ignored) {
        }
        return null;
    }
    
}
