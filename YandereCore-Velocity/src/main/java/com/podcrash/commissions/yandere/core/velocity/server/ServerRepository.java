package com.podcrash.commissions.yandere.core.velocity.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.podcrash.commissions.yandere.core.common.data.plugin.LyPlugin;
import com.podcrash.commissions.yandere.core.common.data.server.IServerRepository;
import com.podcrash.commissions.yandere.core.common.data.server.Server;
import com.podcrash.commissions.yandere.core.common.data.server.repo.OutPlugin;
import com.podcrash.commissions.yandere.core.common.data.server.repo.Response;
import com.podcrash.commissions.yandere.core.velocity.VMain;
import com.velocitypowered.api.plugin.PluginContainer;
import net.lymarket.lyapi.common.db.MongoDBClient;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ServerRepository extends IServerRepository {
    
    public ServerRepository(MongoDBClient database, String tableName){
        super(database, tableName);
    }
    
    @Override
    public Server initializeServer(){
        return null;
    }
    
    @Override
    public Server getServer(){
        return null;
    }
    
    @Override
    public boolean isServerRegistered(){
        return false;
    }
    
    @Override
    public void checkForPluginsUpdates(){
        try {
            pluginFilesGarbageCollector();
            VMain.getInstance().getLogger().info("[UPDATE MACHINE] Initializing the ~Updating Machine~ ...");
            
            StringBuilder resultado = new StringBuilder();
            URL url = new URL(VMain.getConfig().get("web.url") + "/api/lydark/server/checkPlugins");
            HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
            
            ArrayList<OutPlugin> plugins = VMain.getInstance().getProxy().getPluginManager().getPlugins().stream().map(plugin -> new OutPlugin(plugin.getDescription().getName().get())).collect(Collectors.toCollection(ArrayList::new));
            
            Gson gson = new GsonBuilder().serializeNulls().create();
            String json = gson.toJson(plugins);
            VMain.getInstance().getLogger().info("[UPDATE MACHINE] Sending the following plugins to the server: " + json);
            connexion.setRequestMethod("GET");
            connexion.setRequestProperty("Web-Api-Key", VMain.getConfig().getString("web.key"));
            connexion.setDoOutput(true);
            connexion.setRequestProperty("msg", json);
            
            BufferedReader rd = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
            String linea;
            while ((linea = rd.readLine()) != null) {
                resultado.append(linea);
            }
            rd.close();
            
            String response = resultado.toString();
            
            Response res = gson.fromJson(response, Response.class);
            
            ArrayList<PluginContainer> loadedPlugins = new ArrayList<>(VMain.getInstance().getProxy().getPluginManager().getPlugins());
            
            if (res.getType().equals("plugins")){
                ArrayList<LyPlugin> pluginsSaved = res.getPlugins(); // estos son los plugins que están en el servidor y a la vez en el mongo
                HashMap<LyPlugin, PluginContainer> pluginsToUpdate = new HashMap<>();
                VMain.getInstance().getLogger().info("[UPDATE MACHINE] I have found " + pluginsSaved.size() + " plugins matches with the DB\n");
                VMain.getInstance().getLogger().info("[UPDATE MACHINE] Checking if there is something to update...");
                for ( LyPlugin plugin : pluginsSaved ){
                    for ( PluginContainer pluginBukkit : loadedPlugins ){
                        String bukkitPluginName = pluginBukkit.getDescription().getName().get();
                        if (plugin.getName().contains(bukkitPluginName) ||
                                bukkitPluginName.contains(plugin.getName()) ||
                                bukkitPluginName.equalsIgnoreCase(plugin.getName())){
                            if (!pluginBukkit.getDescription().getVersion().get().equals(plugin.getVersion())){
                                VMain.getInstance().getLogger().warn("[UPDATE MACHINE] There is a new version for the plugin " + plugin.getName() + " (" + plugin.getVersion() + ")");
                                pluginsToUpdate.put(plugin, pluginBukkit);
                            }
                        }
                    }
                    
                }
                if (pluginsToUpdate.size() > 0){
                    VMain.getInstance().getLogger().warn("[UPDATE MACHINE] I have found " + pluginsToUpdate.size() + " plugins to update");
                    VMain.getInstance().getLogger().info("[UPDATE MACHINE] Starting the update process...");
    
                    pluginsToUpdate.forEach((plugin, pluginBukkit) -> {
                        try {
                            updatePlugin(plugin, pluginBukkit);
                        } catch (IOException e) {
                            e.printStackTrace();
                            VMain.getInstance().getLogger().warn("[UPDATE MACHINE] An error has occurred when trying to update");
                        }
                    });
                    VMain.getInstance().getLogger().info("[UPDATE MACHINE] Every Plugin has successfully updated!");
                    VMain.getInstance().getLogger().warn("[UPDATE MACHINE] Restarting...");
                    VMain.getInstance().getProxy().shutdown();
    
                } else {
                    VMain.getInstance().getLogger().info("[UPDATE MACHINE] You are up to date!");
                }
                /*VMain.getInstance( ).getConfig( ).getBoolean( "web.pluginsConfigured" );*/
            } else if (res.getType().equals("success")){
                VMain.getInstance().getLogger().info("[UPDATE MACHINE] You are up to date!");
            } else {
                VMain.getInstance().getLogger().warn("[UPDATE MACHINE] There is something wrong with the server, please contact the AL TIO BARRAAAAAAAAAAAAAAAAAA");
            }
        } catch (IOException | NullPointerException | ConcurrentModificationException ignored) {
            ignored.printStackTrace();
        }
    
    }
    
    /*public void checkForPluginsUpdates2(){
        try {
            pluginFilesGarbageCollector();
            VMain.getInstance().getLogger().info("[UPDATE MACHINE] Initializing the ~Updating Machine~ ...");
            
            StringBuilder resultado = new StringBuilder();
            URL url = new URL(VMain.getConfig().get("web.url") + "/api/lydark/server/checkPlugins");
            HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
            ArrayList<OutPlugin> pluginsold = VMain.getInstance().getProxy().getPluginManager().getPlugins().stream().map(plugin -> new OutPlugin(plugin.getDescription().getName().get())).collect(Collectors.toCollection(ArrayList::new));
            
            ArrayList<OutPlugin> plugins = new ArrayList<>();
            String absolutePath = VMain.getInstance().getPath().toAbsolutePath().toString();
            Iterator<File> it = FileUtils.iterateFiles(new File(absolutePath.substring(0, absolutePath.length() - 7)), null, false);
            ArrayList<LoadedPlugin> loadedPlugins = new ArrayList<>(); // estos son los plugins que están en el server.
            while (it.hasNext()) {
                File file = it.next();
                String pl = file.getName();
                
                
                if (file.getName().endsWith(".jar")){
                    InputStream stream = new URLClassLoader(new URL[]{file.toURL()}).getResourceAsStream("module.json");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    
                    System.out.println(builder.toString());
                }
                
                
                if (pl.endsWith(".jar")){
                    String pluginName = pl.replace(".jar", "").split("-")[0];
                    PluginContainer plugin = VMain.getInstance().getProxy().getPluginManager().getPlugin(pluginName).orElse(null);
                    plugin.getDescription().get
                    if (plugin != null){
                        String hash = String.format(Locale.ROOT, "%032x", new BigInteger(1, MessageDigest.getInstance("MD5").digest(Files.readAllBytes(file.toPath()))));
                        plugins.add(new OutPlugin(plugin.name(), plugin.id(), hash));
                        loadedPlugins.add(new LoadedPlugin(plugin.name(), plugin.id(), hash, plugin.version()));
                    } else {
                        plugins.add(new OutPlugin(pluginName));
                    }
                }
            }
            Gson gson = new GsonBuilder().serializeNulls().create();
            String json = gson.toJson(plugins);
            VMain.getInstance().getLogger().info("[UPDATE MACHINE] Sending the following plugins to the server: " + json);
            connexion.setRequestMethod("GET");
            connexion.setRequestProperty("Web-Api-Key", VMain.getConfig().getString("web.key"));
            connexion.setDoOutput(true);
            connexion.setRequestProperty("msg", json);
            
            BufferedReader rd = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
            String linea;
            while ((linea = rd.readLine()) != null) {
                resultado.append(linea);
            }
            rd.close();
            
            String response = resultado.toString();
            
            Response res = gson.fromJson(response, Response.class);
            
            ArrayList<PluginContainer> loadedPlugins = new ArrayList<>(VMain.getInstance().getProxy().getPluginManager().getPlugins());
            
            if (res.getType().equals("plugins")){
                ArrayList<LyPlugin> pluginsSaved = res.getPlugins(); // estos son los plugins que están en el servidor y a la vez en el mongo
                HashMap<LyPlugin, PluginContainer> pluginsToUpdate = new HashMap<>();
                VMain.getInstance().getLogger().info("[UPDATE MACHINE] I have found " + pluginsSaved.size() + " plugins matches with the DB\n");
                VMain.getInstance().getLogger().info("[UPDATE MACHINE] Checking if there is something to update...");
                for ( LyPlugin plugin : pluginsSaved ){
                    for ( PluginContainer pluginBukkit : loadedPlugins ){
                        String bukkitPluginName = pluginBukkit.getDescription().getName().get();
                        if (plugin.getName().contains(bukkitPluginName) ||
                                bukkitPluginName.contains(plugin.getName()) ||
                                bukkitPluginName.equalsIgnoreCase(plugin.getName())){
                            if (!pluginBukkit.getDescription().getVersion().get().equals(plugin.getVersion())){
                                VMain.getInstance().getLogger().warn("[UPDATE MACHINE] There is a new version for the plugin " + plugin.getName() + " (" + plugin.getVersion() + ")");
                                pluginsToUpdate.put(plugin, pluginBukkit);
                            }
                        }
                    }
                    
                }
                if (pluginsToUpdate.size() > 0){
                    VMain.getInstance().getLogger().warn("[UPDATE MACHINE] I have found " + pluginsToUpdate.size() + " plugins to update");
                    VMain.getInstance().getLogger().info("[UPDATE MACHINE] Starting the update process...");
                    
                    pluginsToUpdate.forEach((plugin, pluginBukkit) -> {
                        try {
                            updatePlugin(plugin, pluginBukkit);
                        } catch (IOException e) {
                            e.printStackTrace();
                            VMain.getInstance().getLogger().warn("[UPDATE MACHINE] An error has occurred when trying to update");
                        }
                    });
                    VMain.getInstance().getLogger().info("[UPDATE MACHINE] Every Plugin has successfully updated!");
                    VMain.getInstance().getLogger().warn("[UPDATE MACHINE] Restarting...");
                    VMain.getInstance().getProxy().shutdown();
                    
                } else {
                    VMain.getInstance().getLogger().info("[UPDATE MACHINE] You are up to date!");
                }
                *//*VMain.getInstance( ).getConfig( ).getBoolean( "web.pluginsConfigured" );*//*
            } else if (res.getType().equals("success")){
                VMain.getInstance().getLogger().info("[UPDATE MACHINE] You are up to date!");
            } else {
                VMain.getInstance().getLogger().warn("[UPDATE MACHINE] There is something wrong with the server, please contact the AL TIO BARRAAAAAAAAAAAAAAAAAA");
            }
        } catch (IOException | NullPointerException | ConcurrentModificationException | NoSuchAlgorithmException err) {
            err.printStackTrace();
        }
        
    }*/
    
    public void pluginFilesGarbageCollector(){
        List<String> pluginsToDelete = VMain.getConfig().getStringList("web.pluginsToDelete");
        if (pluginsToDelete.size() > 0){
            VMain.getInstance().getLogger().info("[UPDATE MACHINE] Found " + pluginsToDelete.size() + " old plugins files pending to be deleted!");
            for ( String path : pluginsToDelete ){
                String plugin = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
                VMain.getInstance().getLogger().info("[UPDATE MACHINE] Deleting Plugin: " + plugin);
                try {
                    FileUtils.forceDeleteOnExit(FileUtils.getFile(path));
                } catch (IOException e) {
                    VMain.getInstance().getLogger().severe("[UPDATE MACHINE] ERROR The plugin" + plugin + " couldn't be deleted!");
                }
            }
            VMain.getConfig().set("web.pluginsToDelete", null);
        }
    }
    
    
    public void updatePlugin(LyPlugin plugin, PluginContainer outdatedPlugin) throws IOException{
        VMain.getInstance().getLogger().info("[UPDATE MACHINE] Updating " + plugin.getName() + " to --> V: " + plugin.getVersion() + " ...");
    
        VMain.getInstance().getLogger().info("[UPDATE MACHINE] STEP 1/5 -> Downloading the Plugin");
        URL url = new URL(VMain.getConfig().get("web.url") + "/api/lydark/plugins/downloads/" + plugin.getUuid());
        HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
        connexion.setRequestMethod("GET");
        connexion.setRequestProperty("Web-Api-Key", VMain.getConfig().getString("web.key"));
        connexion.setDoOutput(true);
        
        Path pluginsPath = Paths.get("plugins");
        String pathTarget = new File(pluginsPath.toAbsolutePath() + "/" + plugin.getName() + "-" + plugin.getVersion() + ".jar").getAbsolutePath();
        try (InputStream in = connexion.getInputStream()) {
            VMain.getInstance().getLogger().info("[UPDATE MACHINE] STEP 2/5 -> Installing the Plugin");
            Files.copy(in, Paths.get(pathTarget), StandardCopyOption.REPLACE_EXISTING);
        }
        VMain.getInstance().getLogger().info("[UPDATE MACHINE] STEP 3/5 -> Finding the old plugin file path");
        String path = new File(pluginsPath.toAbsolutePath() + "\\" + outdatedPlugin.getDescription().getName().get() + "-" + outdatedPlugin.getDescription().getVersion().get() + ".jar").getAbsolutePath();
    
        VMain.getInstance().getLogger().info("[UPDATE MACHINE] STEP 4/5 -> Deleting the file");
        try {
            String plugin_to_delete = outdatedPlugin.getDescription().getName().get() + "-" + outdatedPlugin.getDescription().getVersion().get() + ".jar";
            
            OkHttpClient httpClient = new OkHttpClient();
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "{\"root\":\"/plugins\",\"files\":[\"" + plugin_to_delete + "\"]}");
            //RequestBody formBody = new FormBody.Builder().add("root", "/plugins").add( "files" , "["+plugin_to_delete+"]" ).build();
            Request request = new Request.Builder()
                    .url("https://control.yanderecraft.com/api/client/servers/beff672a/files/delete")
                    .addHeader("User-Agent", "OkHttp Bot")
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json; utf-8")
                    .addHeader("Authorization", "Bearer ptlc_MW0HtBp99Ln1khIgRevctUK50pOGPwBWo4mAZysUelS")
                    .post(body)
                    .build();
            
            try (okhttp3.Response response = httpClient.newCall(request).execute()) {
                
                if (!response.isSuccessful()){
                    VMain.getInstance().getLogger().severe("[UPDATE MACHINE] STEP 5/5 -> Failed to delete the old plugin file: " + response);
                    VMain.getInstance().getLogger().severe("[UPDATE MACHINE] STEP 5/5 -> Failed to delete the old plugin file: " + plugin_to_delete);
    
                }
                VMain.getInstance().getLogger().info("[UPDATE MACHINE] STEP 5/5 -> Successfully deleted the old plugin file: " + plugin_to_delete);
                
                // Get response body
            }
            
        } catch (IOException e) {
            VMain.getInstance().getLogger().warn("[UPDATE MACHINE] ERROR -> Couldn't delete the file, don't worry, it will be deleted when the server restarts!");
            List<String> pluginsToDelete = VMain.getConfig().getStringList("web.pluginsToDelete");
            pluginsToDelete.add(path);
            VMain.getConfig().set("web.pluginsToDelete", pluginsToDelete);
            e.printStackTrace();
        }
        //FileUtils.forceDelete( file );
        VMain.getInstance().getLogger().info("[UPDATE MACHINE] The plugin " + plugin.getName() + " has been updated to V: " + plugin.getVersion());
        
        
    }
    
    
}