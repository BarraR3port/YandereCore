package com.podcrash.commissions.yandere.core.spigot.socket;


import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.spigot.users.PlayerRepository;
import net.lymarket.lyapi.common.db.MongoDBClient;
import net.lymarket.lyapi.spigot.LyApi;
import org.junit.Test;

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
        int initial = 2;
        int amount = 10;
        int midValue = initial - amount;
        int finalValue = Math.max(midValue, 0);
    
        System.out.println(finalValue);
    
    }
}