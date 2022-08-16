package com.podcrash.commissions.yandere.core.spigot.vanish;

import com.podcrash.commissions.yandere.core.spigot.items.Items;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public final class VanishManager {
    
    private final LinkedList<Player> vanished = new LinkedList<>();
    
    public LinkedList<Player> getVanished(){
        return vanished;
    }
    
    public boolean isVanished(Player p){
        return vanished.contains(p);
    }
    
    public void enable(Player p){
        
        for ( Player player : Bukkit.getOnlinePlayers() ){
            if (!player.hasPermission("yandere.vanish.see") && !player.hasPermission("yandere.vanish")){
                if (vanished.contains(player)){
                    p.showPlayer(player);
                } else {
                    player.hidePlayer(p);
                }
            } else {
                p.showPlayer(player);
            }
        }
        p.setAllowFlight(true);
        p.setFlying(true);
        p.setCanPickupItems(false);
        p.setFoodLevel(20);
        p.setHealth(20);
        p.setSaturation(5f);
        if (p.getInventory().contains(Items.VANISH_OFF)){
            p.getInventory().remove(Items.VANISH_OFF);
            p.getInventory().addItem(Items.VANISH_ON);
        }
        vanished.add(p);
        
    }
    
    public void disable(Player p){
        if (p.getGameMode().equals(GameMode.SURVIVAL) || p.getGameMode().equals(GameMode.ADVENTURE)){
            p.setAllowFlight(false);
            p.setFlying(false);
            p.setCanPickupItems(true);
        }
        if (p.getInventory().contains(Items.VANISH_ON)){
            p.getInventory().remove(Items.VANISH_ON);
            p.getInventory().addItem(Items.VANISH_OFF);
        }
        p.setHealth(20);
        p.setSaturation(5f);
        
        for ( Player people : Bukkit.getOnlinePlayers() ){
            people.showPlayer(p);
            /*if ( !p.hasPermission( "yandere.vanish" ) || !p.hasPermission( "yandere.staff" ) ) {
                for ( String players : Utils.getVanishedPlayers( ) ) {
                    try {
                        p.hidePlayer( Utils.getSpigot( ) , Bukkit.getPlayer( players ) );
                    } catch ( IllegalArgumentException | NullPointerException ignored ) {
                    }
                }
            }*/
        }
        vanished.remove(p);
    }
    
    
}
