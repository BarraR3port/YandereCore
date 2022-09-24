package com.podcrash.commissions.yandere.core.velocity.announcements;

import com.podcrash.commissions.yandere.core.velocity.VMain;
import de.leonhard.storage.Config;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public final class AnnouncementManager {
    
    private final LinkedList<Announcement> announcements = new LinkedList<>();
    
    public AnnouncementManager(){
    }
    
    public AnnouncementManager init(){
        Config config = VMain.getConfig();
        for ( String key : config.getSection("announcement").keySet() ){
            Announcement announcement = new Announcement(
                    config.getInt("announcements." + key + ".delay"),
                    config.getEnum("announcements." + key + ".timeType", TimeUnit.class),
                    config.getBoolean("announcements." + key + ".sound"),
                    config.getStringList("announcements." + key + ".messages")
            );
            announcement.announce();
            announcements.add(announcement);
        }
        return this;
    }
    
    public void stopAnnouncements(){
        for ( Announcement announcement : announcements ){
            announcement.cancel();
        }
    }
}
