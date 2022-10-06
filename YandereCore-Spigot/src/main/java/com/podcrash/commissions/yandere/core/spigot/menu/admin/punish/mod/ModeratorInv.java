package com.podcrash.commissions.yandere.core.spigot.menu.admin.punish.mod;

import com.cryptomorin.xseries.XMaterial;
import com.podcrash.commissions.yandere.core.spigot.menu.admin.punish.BansInv;
import net.lymarket.lyapi.spigot.LyApi;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.Menu;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class ModeratorInv extends Menu {
    
    private final Player target;
    
    public ModeratorInv(IPlayerMenuUtility playerMenuUtility, Player target){
        super(playerMenuUtility);
        this.target = target;
    }
    
    @Override
    public String getMenuName(){
        return "&4• &8Moderation Inv";
    }
    
    @Override
    public int getSlots(){
        return 54;
    }
    
    @Override
    public void handleMenu(InventoryClickEvent event){
        
        Player p = (Player) event.getWhoClicked();
        
        if (event.getSlot() == 20){
            new BansInv(LyApi.getPlayerMenuUtility(p), target).open();
        } else if (event.getSlot() == 22){
            //kickeos
        } else if (event.getSlot() == 24){
            //warn
        } else if (event.getSlot() == 30){
            //mute
        } else if (event.getSlot() == 32){
            p.closeInventory();
            p.performCommand("history " + target.getName());
        }
        
    }
    
    @Override
    public void setMenuItems(){
        
        //JUGADOR SANCIONADO
        ItemStack jugadorsancionado = new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial(), 1, (byte) SkullType.PLAYER.ordinal());
        SkullMeta jugadorsancionadometa = (SkullMeta) jugadorsancionado.getItemMeta();
        
        jugadorsancionadometa.setOwner(target.getName());
        jugadorsancionadometa.setDisplayName("§aModerando a §3" + target.getName());
        
        jugadorsancionado.setItemMeta(jugadorsancionadometa);
        
        //BANEOS
        List<String> baneoslore = new ArrayList<>();
        baneoslore.add("§b§m-------------------------------");
        baneoslore.add("§fLista de pre-sets para baneos.");
        baneoslore.add("§b§m-------------------------------");
        
        ItemStack baneos = createCustomSkull("§4§lBaneos", baneoslore, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzFmZTdhYzJlNWEzNzYyMDE0YzY4NDA3ZTdiZGM3OGY0N2I1ZjM5OTU5ZWM3MzkwNzA0ZTQ2ZGI4MGRjYzNhNCJ9fX0");
        
        //KICKEOS
        List<String> kickeoslore = new ArrayList<>();
        kickeoslore.add("§b§m-------------------------------");
        kickeoslore.add("§fLista de pre-sets para kickeos.");
        kickeoslore.add("§b§m-------------------------------");
        
        ItemStack kickeos = createCustomSkull("§c§lKickeos", kickeoslore, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDg4M2Y5NDYyMDJiYmRmMjVmOGExMDYyZThjMzMxNzE5NDMzMzEyODIwN2M4OWE0NzRiZGUzMDBjNTMzZGMzYyJ9fX0");
        
        //WARN
        List<String> warnlore = new ArrayList<>();
        warnlore.add("§b§m-------------------------------");
        warnlore.add("§fLista de pre-sets para warneos.");
        warnlore.add("§b§m-------------------------------");
        
        ItemStack warn = createCustomSkull("§7§lWarn", warnlore, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWY1YzFhNjc4YmZhMTViOTBiNmE4YjgzZGEzMzlmZmZmNTY3YWMyYWI0MTljMjhmMDQyMjc1OWIxY2Q1NDIwOCJ9fX0=");
        
        //MUTE
        List<String> mutelore = new ArrayList<>();
        mutelore.add("§b§m-------------------------------");
        mutelore.add("§fLista de pre-sets para mute.");
        mutelore.add("§b§m-------------------------------");
        
        ItemStack mute = createCustomSkull("§8§lMuteos", mutelore, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmQxMGFkODhjOTZjZmFmM2Q3Y2I4ZmViODlkZTQ2ZTIxMzg4MmEyZjFlYjRiMjIyODg0YzZlNWRkMjY0ZjE3NyJ9fX0=");
        
        //HISTORY
        List<String> historylore = new ArrayList<>();
        historylore.add("§b§m-------------------------------");
        historylore.add("§fHistorial del jugador.");
        historylore.add("§b§m-------------------------------");
        
        ItemStack history = createCustomSkull("§f§lHistory", historylore, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjkzYmUxODE1YjZjMmExODQ2NDRiYTgzYmQ1ODdjNzc4YTUwMDU3Y2ZjODdkNjdkZTIyZjY2NDQxNTY4ZTA4YyJ9fX0=");
        
        //DECORACION
        ItemStack decoracion = new ItemStack(XMaterial.WHITE_STAINED_GLASS_PANE.parseMaterial(), 1, (short) 15);
        ItemMeta decoracionmeta = decoracion.getItemMeta();
        decoracionmeta.setDisplayName(" ");
        decoracion.setItemMeta(decoracionmeta);
        
        inventory.setItem(4, jugadorsancionado);
        inventory.setItem(20, baneos);
        inventory.setItem(22, kickeos);
        inventory.setItem(24, warn);
        inventory.setItem(30, mute);
        inventory.setItem(32, history);
        
        for ( int i = 0; i < 10; i++ ){
            if (!(i == 4)){
                inventory.setItem(i, decoracion);
            }
        }
        for ( int i = 44; i < 54; i++ ){
            inventory.setItem(i, decoracion);
        }
        inventory.setItem(18, decoracion);
        inventory.setItem(27, decoracion);
        inventory.setItem(36, decoracion);
        inventory.setItem(17, decoracion);
        inventory.setItem(26, decoracion);
        inventory.setItem(35, decoracion);
    }
    
}
