package com.podcrash.commissions.yandere.core.spigot.server;

import com.mongodb.client.model.Filters;
import com.podcrash.commissions.yandere.core.common.data.plugin.LyPlugin;
import com.podcrash.commissions.yandere.core.common.data.server.IServerRepository;
import com.podcrash.commissions.yandere.core.common.data.server.Server;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.settings.Settings;
import net.lymarket.lyapi.common.Api;
import net.lymarket.lyapi.common.db.MongoDBClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.io.FileUtils;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class ServerRepository extends IServerRepository {
    private Server server;
    
    public ServerRepository(MongoDBClient database, String tableName){
        super(database, tableName);
        this.server = initializeServer();
    }
    
    @Override
    public Server initializeServer(){
        if (server == null){
            String serverID = Settings.WEB_UUID;
            if (serverID == null){
                Server server = new Server(Settings.PROXY_SERVER_NAME, Bukkit.getOnlinePlayers().size(), Settings.SERVER_TYPE);
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
        /*if ( server == null ) {
            Server server = new Server( ServerType.getTypeFromServerName( Main.getInstance( ).getUtils( ).getServer( ) ) , Main.getInstance( ).getUtils( ).getServer( ) , )
            database.insertOne( TABLE_NAME , server );
            
        }*/
        
        return false;
    }
    
    @Override
    public void checkForPluginsUpdates(){
        pluginFilesGarbageCollector();
        try {
            Main.getInstance().getLogger().info("[YandereUpdates] Initializing the ~Updating Machine~ ...");
            
            StringBuilder resultado = new StringBuilder();
            ArrayList<OutPlugin> plugins = Arrays.stream(Bukkit.getPluginManager().getPlugins()).map(plugin -> new OutPlugin(plugin.getName())).collect(Collectors.toCollection(ArrayList::new));
            
            Iterator<File> it = FileUtils.iterateFiles(new File(Main.getInstance().getDataFolder().getAbsolutePath().substring(0, Main.getInstance().getDataFolder().getAbsolutePath().length() - 11)), null, false);
            while (it.hasNext()) {
                String pl = it.next().getName();
                if (pl.endsWith(".jar")){
                    plugins.add(new OutPlugin(pl.replace(".jar", "")));
                }
            }
            
            String json = Api.getGson().toJson(plugins);
            
            OkHttpClient httpClient = new OkHttpClient();
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
                    Main.getInstance().getLogger().log(Level.SEVERE, "[YandereUpdates] ERROR -> Failed to Check the plugins.");
                    Main.getInstance().getLogger().log(Level.SEVERE, "[YandereUpdates] ERROR MSG: " + rs);
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
                    ArrayList<LyPlugin> pluginsSaved = res.getPlugins(); // estos son los plugins que están en el servidor y a la vez en el mongo
                    ArrayList<Plugin> loadedPlugins = new ArrayList<>(Arrays.asList(Bukkit.getPluginManager().getPlugins())); // estos son los plugins que están en el server.
                    ArrayList<OutdatedPlugin> outdatedPlugins = loadedPlugins.stream().map(plugin -> new OutdatedPlugin(plugin.getName(), plugin.getDescription().getVersion())).collect(Collectors.toCollection(ArrayList::new));
                    Iterator<File> it2 = FileUtils.iterateFiles(new File(Main.getInstance().getDataFolder().getAbsolutePath().substring(0, Main.getInstance().getDataFolder().getAbsolutePath().length() - 11)), null, false);
                    while (it2.hasNext()) {
                        String pl = it2.next().getName();
                        if (pl.endsWith(".jar")){
                            pluginsSaved.forEach(plugin -> {
                                if (pl.contains(plugin.getName())){
                                    outdatedPlugins.add(new OutdatedPlugin(plugin.getName(), plugin.getVersion()));
                                    Main.getInstance().getLogger().info("[YandereUpdates] Found the plugin " + plugin.getName() + " V=" + plugin.getVersion() + " which matches with the DB, and locally stored as: " + pl);
                                }
                            });
                        }
                    }
                    
                    HashMap<LyPlugin, OutdatedPlugin> pluginsToUpdate = new HashMap<>();
                    Main.getInstance().getLogger().warning("[YandereUpdates] I have found " + pluginsSaved.size() + " plugins matches with the DB");
                    Main.getInstance().getLogger().warning("[YandereUpdates] Checking if there is something to update...");
                    for ( LyPlugin plugin : pluginsSaved ){
                        server.addPlugin(plugin.getUuid());
                        for ( OutdatedPlugin pluginBukkit : outdatedPlugins ){
                            if (plugin.getName().contains(pluginBukkit.getName()) ||
                                    plugin.getBukkitName().contains(pluginBukkit.getName()) ||
                                    plugin.getName().equalsIgnoreCase(pluginBukkit.getName())){
                                if (!plugin.getVersion().equals(pluginBukkit.getVersion())){
                                    Main.getInstance().getLogger().log(Level.WARNING, "[YandereUpdates] There is a new version for the plugin " + plugin.getName() + " (" + plugin.getVersion() + ")");
                                    pluginsToUpdate.put(plugin, pluginBukkit);
                                }
                            }
                        }
                        
                    }
                    saveServer();
                    if (pluginsToUpdate.size() > 0){
                        Main.getInstance().getLogger().warning("[YandereUpdates] I have found " + pluginsToUpdate.size() + " plugins to update");
                        Main.getInstance().getLogger().warning("[YandereUpdates] Starting the update process...");
                        pluginsToUpdate.forEach(this::updatePlugin);
                        Main.getInstance().getLogger().warning("[YandereUpdates] Every Plugin has successfully updated!");
                        Main.getInstance().getLogger().severe("[YandereUpdates] Restarting.");
                        Main.getInstance().getLogger().severe("[YandereUpdates] Restarting..");
                        Main.getInstance().getLogger().severe("[YandereUpdates] Restarting...");
                        Main.getInstance().getLogger().severe("[YandereUpdates] Restarting....");
                        Main.getInstance().getLogger().severe("[YandereUpdates] Restarting.....");
                        Bukkit.shutdown();
                        
                    } else {
                        Main.getInstance().getLogger().info("[YandereUpdates] You are up to date!");
                    }
                } else if (res.getType().equals("success")){
                    Main.getInstance().getLogger().info("[YandereUpdates] You are up to date!");
                } else {
                    Main.getInstance().getLogger().severe("[YandereUpdates] There is something wrong with the server, please contact the AL TIO BARRAAAAAAAAAAAAAAAAAA");
                }
            }
        } catch (IOException | NullPointerException | ConcurrentModificationException ignored) {
        }
        
    }
    
    public void pluginFilesGarbageCollector(){
        List<String> pluginsToDelete = Main.getInstance().getConfig().getStringList("web.pluginsToDelete");
        if (pluginsToDelete.size() > 0){
            Main.getInstance().getLogger().info("[YandereUpdates] Found " + pluginsToDelete.size() + " old plugins files pending to be deleted!");
            for ( String path : pluginsToDelete ){
                String plugin = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
                Main.getInstance().getLogger().info("[YandereUpdates] Deleting Plugin: " + plugin);
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
        } else {
            Main.getInstance().getConfig().set("web.pluginsToDelete", new ArrayList<String>());
            Main.getInstance().saveConfig();
        }
    }
    
    public void saveServer() {
        database.replaceOneFast(TABLE_NAME, Filters.eq("uuid", server.getUuid().toString()), server);
        Main.getInstance().getConfig().set("web.uuid", server.getUuid().toString());
        Main.getInstance().getConfig().set("web.plugins", server.getPluginsFormatted());
        Main.getInstance().saveConfig();
    }
    
    public void updatePlugin(LyPlugin plugin, OutdatedPlugin outdatedPlugin){
        Main.getInstance().getLogger().info("[YandereUpdates] Updating " + plugin.getName() + " to -> V: " + plugin.getVersion() + " ...");
        Main.getInstance().getLogger().info("[YandereUpdates] STEP 1/5 -> Downloading the Plugin");
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
                    Main.getInstance().getLogger().log(Level.SEVERE, "[YandereUpdates] ERROR -> Failed to Download the plugin: " + plugin.getName() + " !");
                    Main.getInstance().getLogger().log(Level.SEVERE, "[YandereUpdates] ERROR MSG: " + rs);
                    return;
                }
                
                String pathTarget = Bukkit.getUpdateFolderFile().getAbsolutePath().substring(0, Bukkit.getUpdateFolderFile().getAbsolutePath().length() - 7) + "/" + plugin.getName() + "-" + plugin.getVersion() + ".jar";
                try (InputStream in = rs.body().byteStream()) {
                    Main.getInstance().getLogger().info("[YandereUpdates] STEP 2/5 -> Installing the Plugin");
                    Files.copy(in, Paths.get(pathTarget), StandardCopyOption.REPLACE_EXISTING);
                }
                String path;
                try {
                    path = Bukkit.getUpdateFolderFile().getAbsolutePath().substring(0, Bukkit.getUpdateFolderFile().getAbsolutePath().length() - 7) + "/" + new File(Bukkit.getPluginManager().getPlugin(outdatedPlugin.getName()).getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getName();
                } catch (Exception e) {
                    path = Bukkit.getUpdateFolderFile().getAbsolutePath().substring(0, Bukkit.getUpdateFolderFile().getAbsolutePath().length() - 7) + "/" + outdatedPlugin.getName() + "-" + outdatedPlugin.getVersion() + ".jar";
                }
                Main.getInstance().getLogger().info("[YandereUpdates] STEP 4/5 -> Disabling the plugin");
                try {
                    Bukkit.getPluginManager().getPlugin(plugin.getName()).getPluginLoader().disablePlugin(Bukkit.getPluginManager().getPlugin(plugin.getName()));
                } catch (NullPointerException ignored) {
                }
                Main.getInstance().getLogger().info("[YandereUpdates] STEP 5/5 -> Deleting the file");
                try {
                    FileUtils.forceDelete(FileUtils.getFile(path));
                } catch (IOException e) {
                    Main.getInstance().getLogger().log(Level.SEVERE, "[YandereUpdates] ERROR -> Couldn't delete the file, don't worry, it will be deleted when the server restarts!");
                    ArrayList<String> pluginsToDelete = (ArrayList<String>) Main.getInstance().getConfig().getStringList("web.pluginsToDelete");
                    if (outdatedPlugin.getName().contains("bedwars-plugin") || outdatedPlugin.getName().contains("LyBedwars")){
                        pluginsToDelete.add(Bukkit.getUpdateFolderFile().getAbsolutePath().substring(0, Bukkit.getUpdateFolderFile().getAbsolutePath().length() - 7) + "/bedwars-plugin-" + outdatedPlugin.getVersion() + ".jar");
                        pluginsToDelete.add(Bukkit.getUpdateFolderFile().getAbsolutePath().substring(0, Bukkit.getUpdateFolderFile().getAbsolutePath().length() - 7) + "/bedwars-plugin-" + outdatedPlugin.getVersion() + ".jara");
                        pluginsToDelete.add(Bukkit.getUpdateFolderFile().getAbsolutePath().substring(0, Bukkit.getUpdateFolderFile().getAbsolutePath().length() - 7) + "/bedwars-plugin-" + outdatedPlugin.getVersion() + ".jarA");
                    }
                    pluginsToDelete.add(path);
                    Main.getInstance().getConfig().set("web.pluginsToDelete", pluginsToDelete);
                    Main.getInstance().saveConfig();
                }
                //FileUtils.forceDelete( file );
                Main.getInstance().getLogger().info("[YandereUpdates] The plugin " + plugin.getName() + " has been updated to V: " + plugin.getVersion());
                /*} catch ( Exception e ) {
                    Main.getInstance( ).getLogger( ).log( Level.SEVERE , "[YandereUpdates] ERROR -> Failed to find the old plugin file path!" );
                    Main.getInstance( ).getLogger( ).log( Level.SEVERE , "[YandereUpdates] STEP 1/2 --> Moving the file to the /update folder " );
                    FileUtils.moveFile( new File( pathTarget ) , Bukkit.getUpdateFolderFile( ));
                    Main.getInstance( ).getLogger( ).log( Level.SEVERE , "[YandereUpdates] STEP 2/2 SUCCESS --> File Moved " );
                    
                }*/
            }
            
        } catch (IOException e) {
            Main.getInstance().getLogger().log(Level.WARNING, "[YandereUpdates] ERROR -> Couldn't download the plugin");
        }
        
    }
    
    private static class OutPlugin {
        private final String name;
        private final String bukkitName;
        
        public OutPlugin(String name, String bukkitName){
            this.name = name;
            this.bukkitName = bukkitName;
        }
        
        public OutPlugin(String name){
            this.name = name;
            this.bukkitName = name;
        }
        
        public String getName(){
            return name;
        }
        
        public String getBukkitName(){
            return bukkitName;
        }
    }
    
    private static class OutdatedPlugin {
        private final String name;
        private final String version;
        
        public OutdatedPlugin(String name, String version){
            this.name = name;
            this.version = version;
        }
        
        public String getName(){
            return name;
        }
        
        public String getVersion(){
            return version;
        }
    }
    
    
    private static class Response {
        private final String type;
        private final ArrayList<LyPlugin> registeredPlugins;
        
        public Response(String type, ArrayList<LyPlugin> registeredPlugins){
            this.type = type;
            this.registeredPlugins = registeredPlugins;
        }
        
        public String getType(){
            return type;
        }
        
        public ArrayList<LyPlugin> getPlugins(){
            return registeredPlugins;
        }
    }
    
}
