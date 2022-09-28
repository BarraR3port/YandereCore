package com.podcrash.commissions.yandere.core.spigot.practice;

import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.task.RepeatingTaskSync;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.podcrash.commissions.yandere.core.spigot.practice.PracticeArenaCreator.PracticeArenaCreatorState.*;


public final class PracticeArenaCreator {
    
    private final Player player;
    private final String arenaName;
    private final AtomicInteger preStartingTime = new AtomicInteger(0);
    private final AtomicInteger currentArenaCopied = new AtomicInteger(0);
    private final int waitingTime = 16;
    private final int lastZPositionSaved;
    private final LinkedHashMap<String, String> arenasToFix = new LinkedHashMap<>();
    private World world;
    private PracticeArenaCreatorState state = CREATING_WORLD;
    private int currentWaiting = 0;
    private boolean waiting;
    
    public PracticeArenaCreator(Player player, String arenaName){
        this.player = player;
        this.arenaName = arenaName;
        initCreation();
        lastZPositionSaved = Main.getInstance().getConfig().getInt("practice.arenas.lastZPositionSaved");
    }
    
    private void initCreation(){
        new RepeatingTaskSync(Main.getInstance(), 20, 20) {
            @Override
            public void run(){
                if (!player.isOnline()){
                    System.out.println("Player is not online, cancelling arena creation.");
                    cancel();
                    return;
                }
                if (waiting){
                    currentWaiting++;
                    if (currentWaiting >= waitingTime){
                        waiting = false;
                        currentWaiting = 0;
                        return;
                    }
                    return;
                }
                switch(state){
                    case CREATING_WORLD:{
                        world = Bukkit.getWorld("Arenas");
                        state = PRE_STARTING_COPY_OF_MAPS;
                    }
                    case PRE_STARTING_COPY_OF_MAPS:{
                        if (preStartingTime.get() <= 3){
                            player.sendMessage("Starting in " + (3 - preStartingTime.getAndIncrement()) + " seconds");
                        } else {
                            player.teleport(world.getSpawnLocation());
                            state = STARTING_COPY_OF_MAPS;
                        }
                        break;
                    }
                    case STARTING_COPY_OF_MAPS:{
                        int current = currentArenaCopied.getAndIncrement();
                        if (current == 0){
                            return;
                        }
                        if (current <= 10){
                            player.sendMessage("Copying arena " + arenaName + " #" + current + "...");
                            Location arenaCenterLocation = new Location(world, 0, 50, lastZPositionSaved + (400 * current));
                            player.setGameMode(org.bukkit.GameMode.CREATIVE);
                            player.teleport(arenaCenterLocation);
                            player.setFlying(true);
                            if (!player.performCommand("arena copypaste " + arenaName)){
                                player.sendMessage("Failed to copy arena " + arenaName + " #" + current + "...");
                                cancel();
                            }
                            waiting = true;
                            break;
                        } else if (current <= 20){
                            player.sendMessage("Copying arena " + arenaName + " #" + current + "...");
                            Location arenaCenterLocation = new Location(world, 0, 40, lastZPositionSaved + (400 * current));
                            player.setGameMode(org.bukkit.GameMode.CREATIVE);
                            player.teleport(arenaCenterLocation);
                            player.setFlying(true);
                            if (player.performCommand("arena copypaste " + arenaName)){
                                player.sendMessage("Arena " + arenaName + current + " has been copied!");
                                arenasToFix.put(arenaName + current, "build");
                            } else {
                                player.sendMessage("Failed to copy arena " + arenaName + " #" + current + "...");
                                cancel();
                            }
                            
                            waiting = true;
                            break;
                        } else if (current <= 25){
                            player.sendMessage("Copying arena " + arenaName + " #" + current + "...");
                            Location arenaCenterLocation = new Location(world, 0, 40, lastZPositionSaved + (400 * current));
                            player.setGameMode(org.bukkit.GameMode.CREATIVE);
                            player.teleport(arenaCenterLocation);
                            player.setFlying(true);
                            if (player.performCommand("arena copypaste " + arenaName)){
                                player.sendMessage("Arena " + arenaName + current + " has been copied!");
                                arenasToFix.put(arenaName + current, "ffa");
                            } else {
                                player.sendMessage("Failed to copy arena " + arenaName + " #" + current + "...");
                                cancel();
                            }
                            waiting = true;
                            break;
                        } else {
                            player.sendMessage("All arenas of " + arenaName + " has been copied and set up!");
                            state = FIXING_ARENAS;
                            break;
                        }
                        
                    }
                    case FIXING_ARENAS:{
                        for ( String arena : arenasToFix.keySet() ){
                            String type = arenasToFix.get(arena);
                            if (type.equalsIgnoreCase("build")){
                                player.sendMessage("Setting up the arena " + arena + " to be buildable...");
                                if (player.performCommand("arena build " + arena)){
                                    player.sendMessage("Arena " + arena + " has been set up!");
                                }
                            } else if (type.equalsIgnoreCase("ffa")){
                                player.sendMessage("Setting up the arena " + arena + " to be FFA...");
                                if (player.performCommand("arena ffa " + arena)){
                                    player.sendMessage("Arena " + arena + " has been set up!");
                                }
                            }
                        }
                        state = DONE;
                        break;
                    }
                    case DONE:{
                        Main.getInstance().getConfig().set("practice.arenas.lastZPositionSaved", lastZPositionSaved + (400 * 25));
                        Main.getInstance().getConfig().saveData();
                        this.cancel();
                        break;
                    }
                }
            }
        };
    }
    
    
    protected enum PracticeArenaCreatorState {
        CREATING_WORLD,
        PRE_STARTING_COPY_OF_MAPS,
        STARTING_COPY_OF_MAPS,
        FIXING_ARENAS,
        DONE
    }
    
}
