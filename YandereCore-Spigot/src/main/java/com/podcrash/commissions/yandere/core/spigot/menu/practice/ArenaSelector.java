package com.podcrash.commissions.yandere.core.spigot.menu.practice;

import com.podcrash.commissions.yandere.core.common.data.practice.PracticeQueueType;
import ga.strikepractice.api.StrikePracticeAPI;
import ga.strikepractice.arena.Arena;
import ga.strikepractice.arena.d;
import ga.strikepractice.battlekit.BattleKit;
import ga.strikepractice.fights.botduel.BotDuel;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.Menu;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;


public class ArenaSelector extends Menu {
    
    private final int[] slots = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43};
    private final StrikePracticeAPI api;
    private final BattleKit kit;
    private final PracticeQueueType type;
    private final BotDuel botDuel;
    
    public ArenaSelector(IPlayerMenuUtility playerMenuUtility, StrikePracticeAPI api, BattleKit kit, PracticeQueueType type, BotDuel botDuel){
        super(playerMenuUtility, true);
        this.api = api;
        this.kit = kit;
        this.type = type;
        this.botDuel = botDuel;
    }
    
    @Override
    public String getMenuName(){
        return "&4• &8Selecciona una Arena.";
    }
    
    @Override
    public int getSlots(){
        return 54;
    }
    
    @Override
    public void setMenuItems(){
        List<d> arenas = kit.isBuild() ? d.a(getOwner(), kit) : d.c(getOwner(), kit);
        List<String> arenasAdded = new ArrayList<>();
        List<String> arenasAddedDisplay = new ArrayList<>();
        for ( int i = 0; i < arenas.size() && i < slots.length; i++ ){
            d arena = arenas.get(i);
            if (arenasAdded.contains(arena.getName())) continue;
            if (arenasAddedDisplay.contains(arena.getDisplayName())) continue;
            
            inventory.setItem(slots[i], new ItemBuilder(arena.getIcon().clone())
                    .setDisplayName(arena.getDisplayName())
                    .addLoreLine("")
                    .addLoreLine("&7Click para seleccionar arena.")
                    .addTag("arena-name", arena.getName())
                    .build()
            );
            arenasAdded.add(arena.getName());
            arenasAddedDisplay.add(arena.getDisplayName());
        }
        inventory.setItem(49, CLOSE_ITEM);
    }
    
    @Override
    public void handleMenu(InventoryClickEvent e){
        ItemStack item = e.getCurrentItem();
        if (NBTItem.hasTag(item, "arena-name")){
            Arena arena = api.getArena(NBTItem.getTag(item, "arena-name"));
            if (arena == null) return;
            botDuel.setArena(arena);
            new BotDifficultySelector(playerMenuUtility, api, kit, type, botDuel).open();
        } else if (NBTItem.hasTag(item, "ly-menu-close")){
            new PracticeQueueMenu(playerMenuUtility, type).open();
        }
    }
}
