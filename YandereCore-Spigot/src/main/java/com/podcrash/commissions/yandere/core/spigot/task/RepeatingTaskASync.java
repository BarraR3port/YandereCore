package com.podcrash.commissions.yandere.core.spigot.task;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class RepeatingTaskASync implements Runnable {
    
    private final int taskId;
    
    public RepeatingTaskASync(JavaPlugin plugin, int delay){
        taskId = Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, this, delay);
    }
    
    public void cancel(){
        Bukkit.getScheduler().cancelTask(taskId);
    }
    
}