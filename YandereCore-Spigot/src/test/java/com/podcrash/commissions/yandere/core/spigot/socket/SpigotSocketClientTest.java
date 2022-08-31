package com.podcrash.commissions.yandere.core.spigot.socket;


import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.spigot.users.PlayerRepository;
import net.lymarket.lyapi.common.db.MongoDBClient;
import net.lymarket.lyapi.spigot.LyApi;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

public class SpigotSocketClientTest {
    
    
    public void test(){
        
        final MongoDBClient mongo = new MongoDBClient("mongodb://yandere:JF993KKLALLDJJ2KLALLGJASKJ29I9ASDK@localhost:27017/yandere", "yandere");
        PlayerRepository players = new PlayerRepository(mongo, "players");
        
        User user = players.getPlayer(UUID.fromString("bc7d7eb8-cb64-4002-8ee7-6bd68e04d789"));
        
        System.out.println(LyApi.getGson().toJson(user));
    }
    
    
    public void test2(){
        final MongoDBClient mongo = new MongoDBClient("mongodb://yandere:JF993KKLALLDJJ2KLALLGJASKJ29I9ASDK@localhost:27017/yandere", "yandere");
        PlayerRepository players = new PlayerRepository(mongo, "players");
        
        User user = players.getPlayer(UUID.fromString("bc7d7eb8-cb64-4002-8ee7-6bd68e04d789"));
        //user.addCoins(300);
        //players.savePlayer(user);
        System.out.println(user.getCoins());
        System.out.println(user.getCoinsFormatted());
    }
    
    @Test
    public void test3(){
        String textToTranslate = "&c&l&Hola cómo estás=?";
        ArrayList<String> toRemove = new ArrayList<>();
        for ( int i = 0; i < textToTranslate.length(); i++ ){
            if (textToTranslate.charAt(i) == '&' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(textToTranslate.charAt(i + 1)) > -1){
                toRemove.add(String.valueOf(textToTranslate.subSequence(i, i + 2)));
            }
        }
        for ( String s : toRemove ){
            textToTranslate = textToTranslate.replace(s, "");
        }
    
        System.out.println(textToTranslate);
    
    }
}