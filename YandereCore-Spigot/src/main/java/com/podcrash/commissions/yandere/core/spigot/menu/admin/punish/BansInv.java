package com.podcrash.commissions.yandere.core.spigot.menu.admin.punish;

import com.cryptomorin.xseries.XMaterial;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.Menu;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class BansInv extends Menu {
    
    private final Player target;
    
    public BansInv(IPlayerMenuUtility playerMenuUtility, Player target){
        super(playerMenuUtility);
        this.target = target;
    }
    
    @Override
    public String getMenuName(){
        return "&4• &8Razones para Banear:";
    }
    
    @Override
    public int getSlots(){
        return 54;
    }
    
    @Override
    public void handleMenu(InventoryClickEvent event){
        
        Player p = (Player) event.getWhoClicked();
        
        if (event.getSlot() == 4){
            p.closeInventory();
            p.performCommand("mod " + target.getName());
        } else if (event.getSlot() == 10){
            
            if (event.getClick().isRightClick()){
                if (p.hasPermission("yandere.smoderator")){
                    p.performCommand("ban " + target.getName() + " Uso de hacks.");
                } else {
                    p.sendMessage("§cDebes ser S.Moderator para banear permanentemente.");
                }
            } else {
                p.performCommand("tempban " + target.getName() + " 30d Uso de hacks.");
            }
            
        } else if (event.getSlot() == 12){
            p.performCommand("ban " + target.getName() + " Acoso.");
        } else if (event.getSlot() == 14){
            
            if (event.getClick().isRightClick()){
                if (p.hasPermission("yandere.smoderator")){
                    p.performCommand("ban " + target.getName() + " Evadir sancion.");
                } else {
                    p.sendMessage("§cDebes ser S.Moderator para banear permanentemente.");
                }
            } else {
                p.performCommand("tempban " + target.getName() + " 10d Evadir sancion.");
            }
            
        } else if (event.getSlot() == 16){
            
            if (event.getClick().isRightClick()){
                if (p.hasPermission("yandere.smoderator")){
                    p.performCommand("ban " + target.getName() + " Multicuentas.");
                } else {
                    p.sendMessage("§cDebes ser S.Moderator para banear permanentemente.");
                }
            } else {
                p.performCommand("tempban " + target.getName() + " 15d Multicuentas.");
            }
            
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
        
        //HACKS
        List<String> baneo1lore = new ArrayList<>();
        baneo1lore.add("§b§m-------------------------------");
        baneo1lore.add("                  §e§lHACKS");
        baneo1lore.add("");
        baneo1lore.add(" §fTiempo: §a30 dias");
        baneo1lore.add(" §fRazon: §aUso de hacks.");
        baneo1lore.add("§b§m-------------------------------");
        ItemStack baneo1 = createItem("§b§lRazon 1", baneo1lore);
        
        //ACOSO
        List<String> baneo2lore = new ArrayList<>();
        baneo2lore.add("§b§m-------------------------------");
        baneo2lore.add("                  §e§lACOSO");
        baneo2lore.add("");
        baneo2lore.add(" §fTiempo: §aPERMANENTE");
        baneo2lore.add(" §fRazon: §aAcoso");
        baneo2lore.add("§b§m-------------------------------");
        ItemStack baneo2 = createItem("§b§lRazon 2", baneo2lore);
        
        //EVADIR SANCION
        List<String> baneo3lore = new ArrayList<>();
        baneo3lore.add("§b§m-------------------------------");
        baneo3lore.add("              §e§lEVADIR SANCION");
        baneo3lore.add("");
        baneo3lore.add(" §fTiempo: §a10 dias");
        baneo3lore.add(" §fRazon: §aEvadir sancion");
        baneo3lore.add("§b§m-------------------------------");
        ItemStack baneo3 = createItem("§b§lRazon 3", baneo3lore);
        
        //MULTICUENTAS
        List<String> baneo4lore = new ArrayList<>();
        baneo4lore.add("§b§m-------------------------------");
        baneo4lore.add("              §e§lMULTICUENTAS");
        baneo4lore.add("");
        baneo4lore.add(" §fTiempo: §a15 dias");
        baneo4lore.add(" §fRazon: §aTener multicuentas");
        baneo4lore.add("§b§m-------------------------------");
        ItemStack baneo4 = createItem("§b§lRazon 4", baneo4lore);
        
        //DECORACION
        ItemStack decoracion = new ItemStack(XMaterial.WHITE_STAINED_GLASS_PANE.parseMaterial(), 1, (short) 15);
        ItemMeta decoracionmeta = decoracion.getItemMeta();
        decoracionmeta.setDisplayName(" ");
        decoracion.setItemMeta(decoracionmeta);
        
        for ( int i = 0; i < 10; i++ ){
            if (!(i == 4)){
                inventory.setItem(i, decoracion);
            }
        }
        for ( int i = 44; i < 54; i++ ){
            inventory.setItem(i, decoracion);
        }
        
        //ITEMS
        inventory.setItem(4, jugadorsancionado);
        inventory.setItem(10, baneo1);
        inventory.setItem(12, baneo2);
        inventory.setItem(14, baneo3);
        inventory.setItem(16, baneo4);
        inventory.setItem(18, decoracion);
        inventory.setItem(27, decoracion);
        inventory.setItem(36, decoracion);
        inventory.setItem(17, decoracion);
        inventory.setItem(26, decoracion);
        inventory.setItem(35, decoracion);
    }
    
    
    private ItemStack createItem(String name, List<String> lore){
        ItemStack item = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
