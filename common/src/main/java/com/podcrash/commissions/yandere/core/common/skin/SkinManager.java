package com.podcrash.commissions.yandere.core.common.skin;

import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class SkinManager {
    
    private static final HashMap<String, String> skins = new HashMap<>();
    
    public SkinManager(){
    }
    
    public static String getSkin(String username){
        if (skins.containsKey(username)){
            return skins.get(username);
        }
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.mojang.com/users/profiles/minecraft/" + username)
                .addHeader("User-Agent", "OkHttp Bot")
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json; utf-8")
                .build();
        StringBuilder resultado = new StringBuilder();
        try (okhttp3.Response rs = httpClient.newCall(request).execute()) {
            if (!rs.isSuccessful()){
                throw new IOException("Unexpected code " + rs);
            }
            
            BufferedReader rd = new BufferedReader(new InputStreamReader(rs.body().byteStream()));
            String linea;
            while ((linea = rd.readLine()) != null) {
                resultado.append(linea);
            }
            rd.close();
            
            /*JsonElement jElement = new JsonParser( ).parse( resultado.toString( ) );*/
            Response res = (new GsonBuilder()).setDateFormat("MMM dd, yyyy HH:mm:ss a").serializeNulls().create().fromJson(resultado.toString(), Response.class);
            
            if (res.getName().equalsIgnoreCase(username)){
                StringBuilder skinResultado = new StringBuilder();
                Request skinRequest = new Request.Builder()
                        .url("https://sessionserver.mojang.com/session/minecraft/profile/" + res.getId())
                        .addHeader("User-Agent", "OkHttp Bot")
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json; utf-8")
                        .build();
                try (okhttp3.Response skinResponse = httpClient.newCall(skinRequest).execute()) {
                    if (!skinResponse.isSuccessful()){
                        throw new IOException("Unexpected code " + skinResponse);
                    }
                    
                    BufferedReader skinReader = new BufferedReader(new InputStreamReader(skinResponse.body().byteStream()));
                    String line;
                    while ((line = skinReader.readLine()) != null) {
                        skinResultado.append(line);
                    }
                    rd.close();
                    
                    /*JsonElement jElement = new JsonParser( ).parse( resultado.toString( ) );*/
                    SkinResponse skinRes = (new GsonBuilder()).setDateFormat("MMM dd, yyyy HH:mm:ss a").serializeNulls().create().fromJson(skinResultado.toString(), SkinResponse.class);
                    if (skinRes.getProperty().size() > 0){
                        final String skin = skinRes.getProperty().get(0).getValue();
                        skins.put(username, skin);
                        return skin;
                    } else {
                        return "";
                    }
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    
    private class Response {
        
        private final String id;
        
        private final String name;
        
        public Response(String name, String id){
            this.id = id;
            this.name = name;
        }
        
        public String getName(){
            return name;
        }
        
        public String getId(){
            return id;
        }
    }
    
    private class SkinResponse {
        
        private final String id;
        private final String name;
        private final ArrayList<SkinProperties> properties;
        
        public SkinResponse(String id, String name, ArrayList<SkinProperties> properties){
            this.id = id;
            this.name = name;
            this.properties = properties;
        }
        
        public String getName(){
            return name;
        }
        
        public String getId(){
            return id;
        }
        
        public ArrayList<SkinProperties> getProperty(){
            return properties;
        }
        
        
    }
    
    private class SkinProperties {
        
        private final String name;
        private final String value;
        
        public SkinProperties(String name, String value){
            this.name = name;
            this.value = value;
        }
        
        public String getName(){
            return name;
        }
        
        public String getValue(){
            return value;
        }
        
    }
}
