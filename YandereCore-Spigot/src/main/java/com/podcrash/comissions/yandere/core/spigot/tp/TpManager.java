package com.podcrash.comissions.yandere.core.spigot.tp;

import com.podcrash.comissions.yandere.core.spigot.Main;
import com.podcrash.comissions.yandere.core.spigot.sounds.Sounds;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TpManager {
    
    public static void teleportToPlayer(final Player player, final String targetName){
        final Player target = Bukkit.getPlayer(targetName);
        if (!player.hasPermission("yandere.tp")){
            Sounds.play(player, "bad-sound");
            Utils.sendMessage(player, "&c[&4ERROR&c] &cNo tienes el siguiente permiso:&e yandere.tp");
            return;
        }
        if (target != null && target.getType().equals(EntityType.PLAYER) && target.isOnline()){
            final Location locpos = target.getLocation();
            Utils.sendMessage(player, "&aTeletransportanto hacia &e" + target.getName());
            if (player.teleport(locpos, PlayerTeleportEvent.TeleportCause.COMMAND)){
                Utils.sendMessage(target, player.getName() + " &ase ha teletransportado hacia tí.");
                Sounds.play(player, "tp-successfully");
                Sounds.play(target, "tp-successfully");
                player.closeInventory();
            } else {
                Utils.sendMessage(player, "&cHa ocurrido un error al teletransportarse hacia &e" + targetName);
                Sounds.BAD_SOUND.play(player);
                player.closeInventory();
            }
        } else {
            Main.getLang().sendErrorMsg(player, "player.not-online-general");
            Sounds.BAD_SOUND.play(player);
            player.closeInventory();
        }
    }
    
    public static void teleportPlayerToPlayer(final Player owner, final String originalName, final String targetName){
        final Player original = Bukkit.getPlayer(originalName);
        final Player target = Bukkit.getPlayer(targetName);
        if (!owner.hasPermission("yandere.tp.others")){
            Sounds.play(owner, "bad-sound");
            Utils.sendMessage(owner, "&c[&4ERROR&c] &cNo tienes el siguiente permiso:&e yandere.tp.others");
            return;
        }
        if ((original != null && target != null) && (original.getType().equals(EntityType.PLAYER) && original.isOnline() && target.getType().equals(EntityType.PLAYER) && target.isOnline())){
            final Location locpos = target.getLocation();
            Utils.sendMessage(owner, "&aTeleporting " + original.getName() + " to &e" + target.getName());
            Utils.sendMessage(original, "&aTeleporting to &e" + target.getName());
            if (original.teleport(locpos, PlayerTeleportEvent.TeleportCause.COMMAND)){
                Utils.sendMessage(target, original.getName() + " &ahas teleported to you.");
                Sounds.play(original, "tp-successfully");
                Sounds.play(target, "tp-successfully");
                Sounds.play(owner, "tp-successfully");
                owner.closeInventory();
            } else {
                Utils.sendMessage(owner, "&cFailed to teleport " + original.getName() + " to " + target.getName());
                Utils.sendMessage(original, "&cFailed to teleport to " + target.getName());
                Sounds.BAD_SOUND.play(owner);
                Sounds.BAD_SOUND.play(original);
                owner.closeInventory();
            }
            
        } else {
            Main.getLang().sendErrorMsg(owner, "player.not-online-general");
            Sounds.BAD_SOUND.play(owner);
            owner.closeInventory();
        }
    }
    
    public static void teleportAllToPlayer(Player original){
        if (!original.hasPermission("yandere.tp.all")){
            Sounds.play(original, "bad-sound");
            Utils.sendMessage(original, "&c[&4ERROR&c] &cNo tienes el siguiente permiso:&e yandere.tp.all");
            return;
        }
        Utils.sendMessage(original, "&aTeleporting all players to you.");
        final Location locpos = original.getLocation();
        for ( final Player target : Bukkit.getOnlinePlayers() ){
            if (target.teleport(locpos, PlayerTeleportEvent.TeleportCause.COMMAND)){
                Utils.sendMessage(target, "&aYou has being teleported to:" + original.getName());
                Sounds.play(target, "tp-successfully");
            }
        }
        Sounds.play(original, "tp-successfully");
    }
    
    
}
