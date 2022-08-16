package com.podcrash.commissions.yandere.core.spigot.users;

import com.podcrash.commissions.yandere.core.common.data.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

public class SpigotUser extends User {
    
    
    public SpigotUser(String name, UUID uuid){
        super(name, uuid);
    }
    
    public Location getBukkitLocation(){
        return new Location(Bukkit.getWorld(this.getLastLocation().getWorld()), this.getLastLocation().getX(), this.getLastLocation().getY(), this.getLastLocation().getZ());
    }
}
