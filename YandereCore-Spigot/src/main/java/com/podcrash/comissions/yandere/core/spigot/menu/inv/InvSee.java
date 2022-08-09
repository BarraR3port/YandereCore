package com.podcrash.comissions.yandere.core.spigot.menu.inv;

import com.podcrash.comissions.yandere.core.spigot.Main;
import com.podcrash.comissions.yandere.core.spigot.items.Items;
import com.podcrash.comissions.yandere.core.spigot.tp.TpManager;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.InventoryMenu;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class InvSee extends InventoryMenu {
    
    private final Player target;
    private final boolean locked;
    
    public InvSee(IPlayerMenuUtility playerMenuUtility, Player target, boolean locked){
        super(playerMenuUtility);
        this.target = target;
        this.locked = locked;
        Utils.playSound(getOwner(), "CHEST_OPEN");
        
    }
    
    @Override
    public String getMenuName(){
        return "&7Inventario de: &d" + target.getName();
    }
    
    @Override
    public int getSlots(){
        return 45;
    }
    
    @Override
    public void handleMenu(InventoryClickEvent e){
        if (locked) e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        ItemStack cursor = e.getCursor();
        int slot = e.getSlot();
        if (e.getClick() == ClickType.DOUBLE_CLICK){
            e.setCancelled(true);
            return;
        }
        
        if (e.getClick() == ClickType.MIDDLE){
            e.setCancelled(true);
            return;
        }
        
        if (e.getClick() == ClickType.DROP || e.getClick() == ClickType.CONTROL_DROP){
            e.setCancelled(true);
            return;
        }
        
        if (e.getClickedInventory() == p.getInventory()){
            return;
        }
        if (e.getSlot() == 27 || e.getSlot() == 28 || e.getSlot() == 29 || e.getSlot() == 31){
            
            
            if (e.getSlot() == 31){
                p.closeInventory();
                TpManager.teleportToPlayer(p, target.getName());
            }
            e.setCancelled(true);
            return;
        } else if (e.getSlot() >= 0 && e.getSlot() < 27){
            slot = slot + 9;
        } else if (e.getSlot() >= 36 && e.getSlot() <= 44){
            slot = slot - 36;
        } else if (e.getSlot() == 30){
            slot = target.getInventory().getHeldItemSlot();
        } else if (e.getSlot() == 32){
            slot = 39;
        } else if (e.getSlot() == 33){
            slot = 38;
        } else if (e.getSlot() == 34){
            slot = 37;
        } else if (e.getSlot() == 35){
            slot = 36;
        }
        
        
        try {
            if (item.getType().equals(Material.AIR)){
                // player put item to inventory
                //Bukkit.getScheduler( ).scheduleSyncDelayedTask( main.plugin , ( ) ->InvManager.updateTargetInventory( p, target ),6L);
                Main.getInstance().getInvManager().setItem(p, target, slot);
            } else if (!item.getType().equals(Material.AIR) && cursor.getType().equals(Material.AIR)){
                // player take item from inventory
                //Bukkit.getScheduler( ).scheduleSyncDelayedTask( main.plugin , ( ) -> invManager.updateTargetInventory( p, target ) , 5L );
                Main.getInstance().getInvManager().removeItem(target, slot);
            } else if (!item.getType().equals(Material.AIR) && !cursor.getType().equals(Material.AIR)){
                // player swap item in inventory
                if (item.getItemMeta().equals(cursor.getItemMeta())){
                    /*
                    TODO ARREGLAR ESTE SISTEMA - El actual duplica o triplica los items, por lo tanto se deshabilita
                    item.setAmount( item.getAmount( ) + cursor.getAmount( ) );
                    int finalSlot = slot;
                    Bukkit.getScheduler( ).scheduleSyncDelayedTask( Core.getInstance( ) , ( ) -> InvManager.setItem( p , target , finalSlot , item ) , 5L );*/
                    e.getWhoClicked().sendMessage("Esta opción está en desarrollo, intenta mover los items de otra manera");
                    e.setCancelled(true);
                } else {
                    Main.getInstance().getInvManager().removeItem(target, slot);
                    Main.getInstance().getInvManager().setItem(p, target, slot);
                }
                //InvManager.removeItem( p , target , slot , item );
                //InvManager.setItem( p , target , slot );
            }
        } catch (NullPointerException ignored) {
            //InvManager.updateTargetInventory( p, target );
            Main.getInstance().getInvManager().setItem(p, target, slot);
        }
        
    }
    
    @Override
    public void handleDragEvent(InventoryDragEvent e){
        Player p = (Player) e.getWhoClicked();
        ArrayList<ItemStack> items = new ArrayList<>(e.getNewItems().values());
        ArrayList<Integer> slots = new ArrayList<>(e.getRawSlots());
        for ( int i = 0; i < e.getRawSlots().size(); i++ ){
            int slot = slots.get(i);
            if (slot == 27 || slot == 28 || slot == 29 || slot == 31){
                e.setCancelled(true);
                return;
            } else if (slot >= 0 && slot < 27){
                slot = slot + 9;
            } else if (slot >= 36 && slot <= 44){
                slot = slot - 36;
            } else if (slot == 30){
                slot = target.getInventory().getHeldItemSlot();
            } else if (slot == 32){
                slot = 39;
            } else if (slot == 33){
                slot = 38;
            } else if (slot == 34){
                slot = 37;
            } else if (slot == 35){
                slot = 36;
            }
            Main.getInstance().getInvManager().setItem(p, target, slot, items.get(i));
        }
        
    }
    
    @Override
    public void subHandleClose(InventoryCloseEvent e){
        Main.getInstance().getInvManager().invSee.remove(getOwner(), target);
        Utils.playSound(getOwner(), "CHEST_CLOSE");
    }
    
    @Override
    public void setMenuItems(){
        HashMap<Integer, ItemStack> hotbar = new HashMap<>();
        for ( int i = 0; i < 9; i++ ){
            try {
                hotbar.put(i, target.getInventory().getItem(i));
            } catch (NullPointerException | ArrayIndexOutOfBoundsException ignored) {
            }
        }
        HashMap<Integer, ItemStack> inv = new HashMap<>();
        for ( int c = 0; c <= 35; c++ ){
            try {
                inv.put(c, target.getInventory().getItem(c + 9));
            } catch (NullPointerException | ArrayIndexOutOfBoundsException ignored) {
            }
        }
        for ( int a = 0; a < hotbar.size(); a++ ){
            inventory.setItem(a + 36, hotbar.get(a));
        }
        for ( int b = 0; b < inv.size(); b++ ){
            inventory.setItem(b, inv.get(b));
        }
        inventory.setItem(27, Items.GREEN_PANEL);
        inventory.setItem(28, Items.potions(target));
        inventory.setItem(29, Items.food(target));
        if (target.getInventory().getItemInHand() != null){
            inventory.setItem(30, target.getInventory().getItemInHand());
        }
        inventory.setItem(31, Items.head(target));
        if (target.getInventory().getHelmet() != null){
            inventory.setItem(32, target.getInventory().getHelmet());
        }
        if (target.getInventory().getChestplate() != null){
            inventory.setItem(33, target.getInventory().getChestplate());
        }
        if (target.getInventory().getLeggings() != null){
            inventory.setItem(34, target.getInventory().getLeggings());
        }
        if (target.getInventory().getBoots() != null){
            inventory.setItem(35, target.getInventory().getBoots());
        }
        Utils.playSound(getOwner(), "CHEST_OPEN");
    }
    
    
}
