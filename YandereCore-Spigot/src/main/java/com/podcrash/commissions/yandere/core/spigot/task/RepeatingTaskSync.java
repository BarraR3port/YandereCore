package com.podcrash.commissions.yandere.core.spigot.task;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class RepeatingTaskSync implements Runnable {
    
    private final int taskId;
    
    public RepeatingTaskSync(JavaPlugin plugin, int delay, int period){
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, delay, period);
    }
    
    public void cancel(){
        Bukkit.getScheduler().cancelTask(taskId);
    }
    
    public int getTaskId(){
        return taskId;
    }
}