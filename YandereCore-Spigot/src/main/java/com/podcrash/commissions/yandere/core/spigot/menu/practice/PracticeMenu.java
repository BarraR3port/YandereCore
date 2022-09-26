package com.podcrash.commissions.yandere.core.spigot.menu.practice;

import com.cryptomorin.xseries.XMaterial;
import com.podcrash.commissions.yandere.core.common.data.practice.PracticeQueueType;
import me.clip.placeholderapi.PlaceholderAPI;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.Menu;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PracticeMenu extends Menu {
    
    public PracticeMenu(IPlayerMenuUtility playerMenuUtility){
        super(playerMenuUtility);
    }
    
    @Override
    public String getMenuName(){
        return "&4• &8Selecciona una cola";
    }
    
    @Override
    public int getSlots(){
        return 36;
    }
    
    @Override
    public void setMenuItems(){
        
        inventory.setItem(11, new ItemBuilder(Material.STONE_SWORD)
                .setDisplayName("&3&lUnranked Queue")
                .addLoreLine("")
                .addLoreLine(" &a⋆ &fEn cola: &c" + PlaceholderAPI.setPlaceholders(this.getOwner(), "%strikepractice_in_unranked_queue%"))
                .addLoreLine(" &a⋆ &fJugando: &c" + PlaceholderAPI.setPlaceholders(this.getOwner(), "%strikepractice_in_fight%"))
                .addLoreLine("")
                .addLoreLine(" &a⋆ &fHaz click para abrir la lista de colas")
                .addTag("queue-type", PracticeQueueType.UNRANKED.name())
                .build());
        
        inventory.setItem(13, new ItemBuilder(Material.EMERALD)
                .setDisplayName("&a&lEstadísticas")
                .addLoreLine("")
                .addLoreLine("&7Haz click para ver tus estadísticas.")
                .addTag("command", "leader")
                .build());
        
        inventory.setItem(15, new ItemBuilder(Material.GOLD_SWORD)
                .setDisplayName("&5&lPremium Queue")
                .addLoreLine("")
                .addLoreLine(" &a⋆ &fEn cola: &c" + PlaceholderAPI.setPlaceholders(this.getOwner(), "%strikepractice_in_ranked_queue%"))
                .addLoreLine(" &a⋆ &fJugando: &c" + PlaceholderAPI.setPlaceholders(this.getOwner(), "%strikepractice_in_fight%"))
                .addLoreLine(" &a⋆ &fRankeds Premium Restantes: &c" + PlaceholderAPI.setPlaceholders(this.getOwner(), "%strikepractice_premiummatches%"))
                .addLoreLine("")
                .addLoreLine("&7Haz click para abrir la lista de colas")
                .addTag("queue-type", PracticeQueueType.PREMIUM.name())
                .build());
        
        
        inventory.setItem(20, new ItemBuilder(Material.DIAMOND_SWORD)
                .setDisplayName("&a&lRanked Queue")
                .addLoreLine("")
                .addLoreLine(" &a⋆ &fEn cola: &c" + PlaceholderAPI.setPlaceholders(this.getOwner(), "%strikepractice_in_ranked_queue%"))
                .addLoreLine(" &a⋆ &fJugando: &c" + PlaceholderAPI.setPlaceholders(this.getOwner(), "%strikepractice_in_fight%"))
                .addLoreLine(" &a⋆ &fRankeds Restantes: &c" + PlaceholderAPI.setPlaceholders(this.getOwner(), "%strikepractice_rankeds_left%"))
                .addLoreLine("")
                .addLoreLine("&7Haz click para abrir la lista de colas")
                .addTag("queue-type", PracticeQueueType.RANKED.name())
                .build());
        
        inventory.setItem(22, new ItemBuilder(Material.BOOK)
                .setDisplayName("&a&lEditor de Kits")
                .addLoreLine("")
                .addLoreLine("&7Haz click para editar tus kits.")
                .addTag("queue-type", PracticeQueueType.EDIT_KIT.name())
                .build());
        
        inventory.setItem(24, new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
                .setHeadSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODkwYjFjZDBjYjEwZGNjM2U5OWJmNDEwNGIxMDM2MGM5Mjc5ZmEwYTJhYTdiZGVkMTQ4MzM1OWIwNDc0ZTExZSJ9fX0=")
                .setDisplayName("&a&lBots")
                .addLoreLine("")
                .addLoreLine("&fPon a prueba tus habilidades y entrena")
                .addLoreLine("&fcontra bots de distintos niveles.")
                .addLoreLine("")
                .addLoreLine("&7Haz click para abrir la lista de colas")
                .addTag("queue-type", PracticeQueueType.BOT.name())
                .build());
    }
    
    @Override
    public void handleMenu(InventoryClickEvent e){
        ItemStack item = e.getCurrentItem();
        if (item == null) return;
        if (item.getType() == Material.AIR) return;
        if (NBTItem.hasTag(item, "command")){
            String command = NBTItem.getTag(item, "command");
            this.getOwner().chat("/" + command);
        }
        if (NBTItem.hasTag(item, "queue-type")){
            PracticeQueueType queueType = PracticeQueueType.valueOf(NBTItem.getTag(item, "queue-type"));
            new PracticeQueueMenu(playerMenuUtility, queueType).open();
        }
    }
}
