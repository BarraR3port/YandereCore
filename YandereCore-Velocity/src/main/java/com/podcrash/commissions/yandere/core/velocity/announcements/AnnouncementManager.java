package com.podcrash.commissions.yandere.core.velocity.announcements;

import com.podcrash.commissions.yandere.core.velocity.VMain;
import com.velocitypowered.api.scheduler.ScheduledTask;
import com.velocitypowered.api.scheduler.TaskStatus;
import de.leonhard.storage.Config;

import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public final class AnnouncementManager {
    
    private final LinkedList<Announcement> announcements = new LinkedList<>();
    private int delay;
    private ScheduledTask task;
    
    public AnnouncementManager(){
    }
    
    public AnnouncementManager init(){
        Config config = VMain.getInstance().getConfig();
        delay = config.getInt("announcements.delay");
        for ( String key : config.getSection("announcements.list").singleLayerKeySet() ){
            Announcement announcement = new Announcement(
                    config.getBoolean("announcements.list." + key + ".sound"),
                    config.getStringList("announcements.list." + key + ".messages"));
            announcements.add(announcement);
        }
        startAnnouncements();
        return this;
    }
    
    public void stopAnnouncements(){
        if (task != null && (!task.status().equals(TaskStatus.FINISHED) || !task.status().equals(TaskStatus.CANCELLED)))
            task.cancel();
    }
    
    public void startAnnouncements(){
        stopAnnouncements();
        task = VMain.getInstance().getProxy().getScheduler().buildTask(VMain.getInstance(), () -> {
            Announcement announcement = announcements.get(ThreadLocalRandom.current().nextInt(0, announcements.size()));
            if (announcement != null){
                announcement.startAnnouncing();
            }
        }).repeat(delay, TimeUnit.MINUTES).schedule();
    }
}
