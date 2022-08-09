package com.podcrash.comissions.yandere.core.spigot.socket;


import com.podcrash.comissions.yandere.core.spigot.users.PlayersRepository;
import com.podcrash.comissions.yandere.core.spigot.users.SpigotUser;
import net.lymarket.common.db.MongoDBClient;
import net.lymarket.lyapi.spigot.LyApi;
import org.junit.Test;

import java.util.UUID;

public class SpigotSocketClientTest {
    
    @Test
    public void test(){
        
        final MongoDBClient mongo = new MongoDBClient("mongodb://yandere:JF993KKLALLDJJ2KLALLGJASKJ29I9ASDK@localhost:27017", "yandere");
        PlayersRepository players = new PlayersRepository(mongo, "players");
        
        SpigotUser user = players.getPlayer(UUID.fromString("bc7d7eb8-cb64-4002-8ee7-6bd68e04d789"));
        
        System.out.println(LyApi.getGson().toJson(user));
    }
}