package com.podcrash.commissions.yandere.core.spigot.menu.practice;

import com.cryptomorin.xseries.XMaterial;
import com.podcrash.commissions.yandere.core.common.data.practice.PracticeQueueType;
import ga.strikepractice.api.StrikePracticeAPI;
import ga.strikepractice.battlekit.BattleKit;
import ga.strikepractice.fights.botduel.BotDuel;
import ga.strikepractice.npc.CitizensNPC;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.Menu;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;


public class BotDifficultySelector extends Menu {
    
    private final StrikePracticeAPI api;
    private final BattleKit kit;
    private final PracticeQueueType type;
    private final BotDuel botDuel;
    private final boolean party;
    
    public BotDifficultySelector(IPlayerMenuUtility playerMenuUtility, StrikePracticeAPI api, BattleKit kit, PracticeQueueType type, BotDuel botDuel, boolean party){
        super(playerMenuUtility, true);
        this.api = api;
        this.kit = kit;
        this.type = type;
        this.botDuel = botDuel;
        this.party = party;
    }
    
    @Override
    public String getMenuName(){
        return "&4• &8Selecciona una Dificultad";
    }
    
    @Override
    public int getSlots(){
        return 27;
    }
    
    @Override
    public void setMenuItems(){
        
        inventory.setItem(10, new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
                .setHeadSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzE5NjEzMmJhOWRkMjI0MDMwZDc0N2RkYmNmNDI4Y2FjNjYyNGRmMzdmNjYxYzA2NjJmNGZlNjNjYWFhMjc1YiJ9fX0=")
                .setDisplayName("&a&lNovato")
                .addLoreLine("")
                .addLoreLine("&4• &7Dificultad: &aBaja")
                .addLoreLine("&4• &7Alcance/Reach: &92.7")
                .addLoreLine("")
                .addLoreLine("&7Click para seleccionar Dificultad")
                .addTag("type", "EASY")
                .build()
        );
        
        inventory.setItem(12, new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
                .setHeadSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjIzYTVkODc5MTViOWQ0MWIyZDM2OWMyYTdmZDI3MjM0NGZmNzM1ZjYwYjM1ZDY1ZTQyYWQ0M2ZiMmExZjljYyJ9fX0=")
                .setDisplayName("&6&lMedio")
                .addLoreLine("")
                .addLoreLine("&4• &7Dificultad: &6Media")
                .addLoreLine("&4• &7Alcance/Reach: &93")
                .addLoreLine("")
                .addLoreLine("&7Click para seleccionar Dificultad")
                .addTag("type", "NORMAL")
                .build()
        );
        
        inventory.setItem(14, new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
                .setHeadSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjU5ZGZkMjlhNWIzOGM0NzFkMGU3ZDY3MzgxNzIzZDNjMTkzNTQxMjcwOWQyMTE5NzE4MmRjMWU4ZjYwZWZlOCJ9fX0=")
                .setDisplayName("&e&lDifícil")
                .addLoreLine("")
                .addLoreLine("&4• &7Dificultad: &eDifícil")
                .addLoreLine("&4• &7Alcance/Reach: &93.2")
                .addLoreLine("")
                .addLoreLine("&7Click para seleccionar Dificultad")
                .addTag("type", "HARD")
                .build()
        );
        
        inventory.setItem(16, new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
                .setHeadSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTZjNWFmMjNiYzRiZmZmZjk3MjJlNmU3ZWNhNzQyOGI5ZGZjZWViMzdjYzE2YjU4ODJiNWFjYjZjN2RkODBlNyJ9fX0=")
                .setDisplayName("&4&lHacker")
                .addLoreLine("")
                .addLoreLine("&4• &7Dificultad: &4Imposible")
                .addLoreLine("&4• &7Alcance/Reach: &94.3")
                .addLoreLine("")
                .addLoreLine("&7Click para seleccionar Dificultad")
                .addTag("type", "HACKER")
                .build()
        );
        
        inventory.setItem(22, CLOSE_ITEM);
    }
    
    @Override
    public void handleMenu(InventoryClickEvent e){
        ItemStack item = e.getCurrentItem();
        if (NBTItem.hasTag(item, "type")){
            CitizensNPC.a botDifficulty = CitizensNPC.a.valueOf(NBTItem.getTag(item, "type"));
            botDuel.setDifficulty(botDifficulty);
            if (botDuel.canStart()){
                botDuel.start();
                return;
            }
            ga.strikepractice.arena.d.c(getOwner());
            
        } else if (NBTItem.hasTag(item, "ly-menu-close")){
            new ArenaSelector(playerMenuUtility, api, kit, type, botDuel, party).open();
        }
    }
}
