package com.podcrash.commissions.yandere.core.velocity.announcements;

import com.podcrash.commissions.yandere.core.velocity.VMain;
import com.podcrash.commissions.yandere.core.velocity.utils.Utils;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.scheduler.ScheduledTask;
import com.velocitypowered.api.scheduler.Scheduler;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class Announcement {
    
    private final int delay;
    private final TimeUnit timeType;
    private final boolean sound;
    private final LinkedList<String> messages;
    private final Scheduler.TaskBuilder taskBuilder;
    private ScheduledTask task;
    
    public Announcement(int delay, TimeUnit timeType, boolean sound, List<String> messages){
        this.delay = delay;
        this.timeType = timeType;
        this.sound = sound;
        this.messages = new LinkedList<>(messages);
        taskBuilder = VMain.getInstance().getProxy().getScheduler().buildTask(VMain.getInstance(), () -> {
            for ( Player p : VMain.getInstance().getProxy().getAllPlayers() ){
                for ( String msg : messages ){
                    p.sendMessage(Utils.format(msg));
                    if (sound){
                        p.playSound(Sound.sound(Key.key("minecraft:block.note_block.pling"), Sound.Source.MASTER, 1, 1));
                    }
                }
            }
        }).repeat(delay, timeType);
    }
    
    public int getDelay(){
        return delay;
    }
    
    public TimeUnit getTimeType(){
        return timeType;
    }
    
    public boolean isSound(){
        return sound;
    }
    
    public LinkedList<String> getMessages(){
        return messages;
    }
    
    public Announcement announce(){
        task = taskBuilder.schedule();
        return this;
    }
    
    public Announcement cancel(){
        if (task != null)
            task.cancel();
        return this;
    }
}
