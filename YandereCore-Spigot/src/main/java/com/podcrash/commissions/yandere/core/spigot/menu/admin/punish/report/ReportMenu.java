package com.podcrash.commissions.yandere.core.spigot.menu.admin.punish.report;

import com.cryptomorin.xseries.XMaterial;
import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.settings.Settings;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.Menu;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ReportMenu extends Menu {
    
    private final User target;
    
    public ReportMenu(IPlayerMenuUtility playerMenuUtility, User target){
        super(playerMenuUtility);
        this.target = target;
    }
    
    @Override
    public String getMenuName(){
        return "&4• &8Reportar a &c" + target.getName();
    }
    
    @Override
    public int getSlots(){
        return 54;
    }
    
    @Override
    public void handleMenu(InventoryClickEvent event){
        Player p = (Player) event.getWhoClicked();
        NBTItem item = new NBTItem(event.getCurrentItem());
        if (item.hasTag("reason")){
            String reason = item.getString("reason");
            User lp = Main.getInstance().getPlayers().getPlayer(p.getName());
            Main.getInstance().getPunishments().createReport(target, lp, reason, Settings.PROXY_SERVER_NAME, event.getCurrentItem().getType().toString());
            p.closeInventory();
        }
        
        if (event.getSlot() == 49){
            p.closeInventory();
        }
    }
    
    @Override
    public void setMenuItems(){
        
        inventory.setItem(13, profile());
        inventory.setItem(19, hacking());
        inventory.setItem(21, killaura());
        inventory.setItem(23, flying());
        inventory.setItem(25, speed());
        
        inventory.setItem(27, spamming());
        inventory.setItem(29, griefing());
        inventory.setItem(31, bhop());
        inventory.setItem(33, other());
        inventory.setItem(49, back());
        
        //DECORACION DE CRISTALES
        ItemStack cristal = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
        ItemMeta cristalMeta = cristal.getItemMeta();
        cristalMeta.setDisplayName(" ");
        cristal.setItemMeta(cristalMeta);
        
        for ( int i = 0; i < 10; i++ ){
            inventory.setItem(i, cristal);
        }
        inventory.setItem(10, cristal);
        inventory.setItem(16, cristal);
        inventory.setItem(17, cristal);
        inventory.setItem(18, cristal);
        inventory.setItem(26, cristal);
        inventory.setItem(27, cristal);
        inventory.setItem(35, cristal);
        inventory.setItem(36, cristal);
        inventory.setItem(37, cristal);
        for ( int i = 43; i < 54; i++ ){
            if (i != 49){
                inventory.setItem(i, cristal);
            }
        }
    }
    
    private ItemStack profile(){
        return new ItemBuilder(new ItemStack(XMaterial.PLAYER_HEAD.parseItem()))
                .setHeadSkin(target.getSkin())
                .setDisplayName("&dBan &5" + target.getName())
                .addLoreLine("&7Elige una opción para reportar")
                .addLoreLine("&7a este jugador de &eYandere&cCraft")
                .build();
    }
    
    private ItemStack hacking(){
        return new ItemBuilder(new ItemStack(Material.RED_ROSE))
                .setDisplayName("&dHacking")
                .addLoreLine("&7Reportar a " + target.getName() + " por Hacking.")
                .addTag("reason", "Hacking")
                .build();
    }
    
    private ItemStack killaura(){
        return new ItemBuilder(new ItemStack(Material.DIAMOND_SWORD))
                .setDisplayName("&4KillAura")
                .addLoreLine("&7Reportar a " + target.getName() + " por KillAura.")
                .addTag("reason", "KillAura")
                .build();
    }
    
    private ItemStack flying(){
        return new ItemBuilder(new ItemStack(Material.FEATHER))
                .setDisplayName("&4Flying")
                .addLoreLine("&7Reportar a " + target.getName() + " por Flying.")
                .addTag("reason", "Flying")
                .build();
    }
    
    private ItemStack speed(){
        return new ItemBuilder(new ItemStack(Material.SUGAR))
                .setDisplayName("&4Speed")
                .addLoreLine("&7Reportar a " + target.getName() + " por Speed.")
                .addTag("reason", "Speed")
                .build();
    }
    
    private ItemStack spamming(){
        return new ItemBuilder(new ItemStack(Material.PAPER))
                .setDisplayName("&4Spamming")
                .addLoreLine("&7Reportar a " + target.getName() + " por Flying.")
                .addTag("reason", "Spamming")
                .build();
    }
    
    private ItemStack griefing(){
        return new ItemBuilder(new ItemStack(Material.TNT))
                .setDisplayName("&4Griefing")
                .addLoreLine("&7Reportar a " + target.getName() + " por Griefing.")
                .addTag("reason", "Griefing")
                .build();
    }
    
    private ItemStack bhop(){
        return new ItemBuilder(new ItemStack(Material.RABBIT_HIDE))
                .setDisplayName("&4BunnyHop")
                .addLoreLine("&7Reportar a " + target.getName() + " por BunnyHop.")
                .addTag("reason", "BunnyHop")
                .build();
    }
    
    private ItemStack other(){
        return new ItemBuilder(new ItemStack(Material.BOWL))
                .setDisplayName("&4Other reason")
                .addLoreLine("&7Reportar a " + target.getName() + " por otra razón.")
                .addTag("reason", "other")
                .build();
    }
    
    public ItemStack back(){
        // create a ItemStack with the back icon using the ItemBuilder
        return new ItemBuilder(new ItemStack(Material.BARRIER))
                .setDisplayName("&cVolver o Cerrar")
                .build();
    }
    
}
