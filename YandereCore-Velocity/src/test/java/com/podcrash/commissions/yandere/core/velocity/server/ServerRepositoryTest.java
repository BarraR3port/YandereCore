package com.podcrash.commissions.yandere.core.velocity.server;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.podcrash.commissions.yandere.core.common.data.server.repo.LoadedPlugin;
import com.podcrash.commissions.yandere.core.common.data.server.repo.OutPlugin;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

class ServerRepositoryTest {
    @Test
    public void test5(){
        //read all the files inside the resources folder and add them to a list
        List<String> files = new ArrayList<>();
        File folder = new File("src/test/resources");
        File[] listOfFiles = folder.listFiles();
        for ( int i = 0; i < listOfFiles.length; i++ ){
            if (listOfFiles[i].isFile()){
                files.add(listOfFiles[i].getName());
            }
        }
        
        //remove the files that are not .json
        files.removeIf(s -> !s.endsWith(".jar"));
        
        //create a new map to hold the data
        Map<String, String> map = new HashMap<>();
        
        //loop through the files and add them to the map
        for ( String file : files ){
            try {
                map.put(file, FileUtils.readFileToString(new File("src/test/resources/" + file), "UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Gson gson = (new GsonBuilder()).disableHtmlEscaping().setDateFormat("MMM dd, yyyy HH:mm:ss a").serializeNulls().create();
        //convert the map to a json string
        String json = gson.toJson(map);
        
        //write the json string to a file
        try {
            FileUtils.writeStringToFile(new File("src/test/resources/strings.json"), json, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    void checkForPluginsUpdates(){
        try {
            System.out.println("[UPDATE MACHINE] Initializing the ~Updating Machine~ ...");
            Gson gson = new GsonBuilder().serializeNulls().create();
            StringBuilder resultado = new StringBuilder();
            URL url = new URL("http://209.222.97.163:3000/api/lydark/server/checkPlugins");
            HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
            
            ArrayList<OutPlugin> plugins = new ArrayList<>();
            Iterator<File> it = FileUtils.iterateFiles(new File("src/test/resources/"), null, false);
            ArrayList<LoadedPlugin> loadedPlugins = new ArrayList<>(); // estos son los plugins que están en el server.
            while (it.hasNext()) {
                File file = it.next();
                String pl = file.getName();
                System.out.println("[UPDATE MACHINE] Checking plugin: " + pl);
                
                
                if (file.getName().endsWith(".jar")){
                    //System.out.println(file.getAbsolutePath()+"!/velocity-plugin.json");
                    
                    InputStream stream = new URLClassLoader(new URL[]{file.toURL()}).getResourceAsStream("velocity-plugin.json");
                    System.out.println(new URLClassLoader(new URL[]{file.toURL()}).getResource("velocity-plugin.json").getPath());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    String json = gson.toJson(builder.toString());
                    //System.out.printf("[UPDATE MACHINE] %s\n", json);
                    
                    /*System.out.println(loader.getName());
                    //System.out.println(loader.getResource("velocity-plugin.json"));
                    //System.out.println();
                    InputStream stream = loader.getResourceAsStream("velocity-plugin.json");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }*/
                    
                    //System.out.println(builder.toString());
                }
            
            
                /*if (pl.endsWith(".jar")){
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
                }*/
            }
            /*Gson gson = new GsonBuilder().serializeNulls().create();
            String json = gson.toJson(plugins);
            System.out.println("[UPDATE MACHINE] Sending the following plugins to the server: " + json);
            connexion.setRequestMethod("GET");
            connexion.setRequestProperty("Web-Api-Key", VMain.getInstance().getConfig().getString("web.key"));
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
                System.out.println("[UPDATE MACHINE] I have found " + pluginsSaved.size() + " plugins matches with the DB\n");
                System.out.println("[UPDATE MACHINE] Checking if there is something to update...");
                for ( LyPlugin plugin : pluginsSaved ){
                    for ( PluginContainer pluginBukkit : loadedPlugins ){
                        String bukkitPluginName = pluginBukkit.getDescription().getName().get();
                        if (plugin.getName().contains(bukkitPluginName) ||
                                bukkitPluginName.contains(plugin.getName()) ||
                                bukkitPluginName.equalsIgnoreCase(plugin.getName())){
                            if (!pluginBukkit.getDescription().getVersion().get().equals(plugin.getVersion())){
                                System.out.println("[UPDATE MACHINE] There is a new version for the plugin " + plugin.getName() + " (" + plugin.getVersion() + ")");
                                pluginsToUpdate.put(plugin, pluginBukkit);
                            }
                        }
                    }
                
                }
                if (pluginsToUpdate.size() > 0){
                    System.out.println("[UPDATE MACHINE] I have found " + pluginsToUpdate.size() + " plugins to update");
                    System.out.println("[UPDATE MACHINE] Starting the update process...");
                
                    pluginsToUpdate.forEach((plugin, pluginBukkit) -> {
                        *//*try {
                            
                            //updatePlugin(plugin, pluginBukkit);
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("[UPDATE MACHINE] An error has occurred when trying to update");
                        }*//*
                    });
                    System.out.println("[UPDATE MACHINE] Every Plugin has successfully updated!");
                    System.out.println("[UPDATE MACHINE] Restarting...");
                    //VMain.getInstance().getProxy().shutdown();
                
                } else {
                    System.out.println("[UPDATE MACHINE] You are up to date!");
                }
                *//*VMain.getInstance( ).getConfig( ).getBoolean( "web.pluginsConfigured" );*//*
            } else if (res.getType().equals("success")){
                System.out.println("[UPDATE MACHINE] You are up to date!");
            } else {
                System.out.println("[UPDATE MACHINE] There is something wrong with the server, please contact the AL TIO BARRAAAAAAAAAAAAAAAAAA");
            }*/
        } catch (IOException | NullPointerException |
                 ConcurrentModificationException /*| NoSuchAlgorithmException*/ err) {
            err.printStackTrace();
        }
        
        
    }
}