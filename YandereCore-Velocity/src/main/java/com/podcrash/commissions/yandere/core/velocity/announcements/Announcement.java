package com.podcrash.commissions.yandere.core.velocity.announcements;

import com.podcrash.commissions.yandere.core.velocity.VMain;
import com.podcrash.commissions.yandere.core.velocity.utils.Utils;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;

import java.util.LinkedList;
import java.util.List;

public final class Announcement {
    private final boolean sound;
    private final LinkedList<String> messages;
    
    public Announcement(boolean sound, List<String> messages){
        this.sound = sound;
        this.messages = new LinkedList<>(messages);
    }
    
    public boolean isSound(){
        return sound;
    }
    
    public LinkedList<String> getMessages(){
        return messages;
    }
    
    public void startAnnouncing(){
        for ( Player p : VMain.getInstance().getProxy().getAllPlayers() ){
            for ( String msg : messages ){
                p.sendMessage(Utils.format(msg));
                if (sound){
                    p.playSound(Sound.sound(Key.key("minecraft:block.note_block.pling"), Sound.Source.MASTER, 1, 1));
                }
            }
        }
    }
}
