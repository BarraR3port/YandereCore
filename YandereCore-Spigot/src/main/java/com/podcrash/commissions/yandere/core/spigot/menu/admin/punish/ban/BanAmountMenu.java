package com.podcrash.commissions.yandere.core.spigot.menu.admin.punish.ban;

import com.cryptomorin.xseries.XMaterial;
import com.podcrash.commissions.yandere.core.common.data.punish.PunishDateType;
import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.settings.Settings;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.Menu;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BanAmountMenu extends Menu {
    
    private final User target;
    private final String reason;
    private boolean ipBan;
    private boolean permanent;
    private PunishDateType dateType;
    private int time;
    
    public BanAmountMenu(IPlayerMenuUtility playerMenuUtility, User target, String reason){
        super(playerMenuUtility);
        this.target = target;
        this.reason = reason;
        this.dateType = PunishDateType.DAYS;
        this.time = 30;
        this.permanent = false;
        this.ipBan = false;
    }
    
    
    @Override
    public String getMenuName(){
        return "&4• &8Elige un tiempo para Banear:";
    }
    
    @Override
    public int getSlots(){
        return 45;
    }
    
    @Override
    public void handleMenu(InventoryClickEvent event){
        Player p = (Player) event.getWhoClicked();
        
        //TIME
        
        if (event.getSlot() == 11){
            this.time += 1;
            inventory.setItem(20, time());
        }
        
        if (event.getSlot() == 29){
            this.time = Math.max(time - 1, 0);
            inventory.setItem(20, time());
        }
        
        //DATE
        
        if (event.getSlot() == 15){
            if (this.dateType == PunishDateType.DAYS){
                this.dateType = PunishDateType.SECONDS;
            } else if (this.dateType == PunishDateType.HOURS){
                this.dateType = PunishDateType.DAYS;
            } else if (this.dateType == PunishDateType.MINUTES){
                this.dateType = PunishDateType.HOURS;
            } else if (this.dateType == PunishDateType.SECONDS){
                this.dateType = PunishDateType.MINUTES;
            }
            inventory.setItem(24, date());
        }
        
        if (event.getSlot() == 33){
            
            if (this.dateType == PunishDateType.DAYS){
                this.dateType = PunishDateType.HOURS;
            } else if (this.dateType == PunishDateType.HOURS){
                this.dateType = PunishDateType.MINUTES;
            } else if (this.dateType == PunishDateType.MINUTES){
                this.dateType = PunishDateType.SECONDS;
            } else if (this.dateType == PunishDateType.SECONDS){
                this.dateType = PunishDateType.DAYS;
            }
            inventory.setItem(24, date());
        }
        
        //CLOSE
        
        if (event.getSlot() == 31){
            User lp = Main.getInstance().getPlayers().getPlayer(p.getName());
            if (this.permanent){
                Main.getInstance().getPunishments().permanentBan(target, lp, reason, this.ipBan, Settings.PROXY_SERVER_NAME);
            } else {
                Main.getInstance().getPunishments().banPlayer(target, lp, reason, PunishDateType.getDate(this.dateType, this.time), this.ipBan, Settings.PROXY_SERVER_NAME);
            }
            p.closeInventory();
        }
        
        
        if (event.getSlot() == 21){
            if (this.permanent){
                this.permanent = false;
                inventory.setItem(11, add());
                inventory.setItem(20, time());
                inventory.setItem(21, permanent());
                inventory.setItem(29, remove());
                
                inventory.setItem(15, add());
                inventory.setItem(24, date());
                inventory.setItem(33, remove());
            } else {
                this.permanent = true;
                inventory.setItem(11, notAllowed());
                inventory.setItem(20, notAllowed());
                inventory.setItem(21, permanent());
                inventory.setItem(29, notAllowed());
                
                inventory.setItem(15, notAllowed());
                inventory.setItem(24, notAllowed());
                inventory.setItem(33, notAllowed());
            }
        }
        
        if (event.getSlot() == 23){
            this.ipBan = !this.ipBan;
            inventory.setItem(23, ipBan());
            
        }
        
        if (event.getSlot() == 49){
            p.closeInventory();
        }
    }
    
    @Override
    public void setMenuItems(){
        
        inventory.setItem(13, profile());
        inventory.setItem(11, add());
        inventory.setItem(20, time());
        inventory.setItem(21, permanent());
        inventory.setItem(29, remove());
        
        inventory.setItem(15, add());
        inventory.setItem(23, ipBan());
        inventory.setItem(24, date());
        inventory.setItem(33, remove());
        
        inventory.setItem(31, continueTheBan());
        inventory.setItem(40, back());
        
        //DECORACION DE CRISTALES
        ItemStack cristal = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
        ItemMeta cristalMeta = cristal.getItemMeta();
        cristalMeta.setDisplayName(" ");
        cristal.setItemMeta(cristalMeta);
        
        for ( int i = 0; i <= 10; i++ ){
            inventory.setItem(i, cristal);
        }
        inventory.setItem(16, cristal);
        inventory.setItem(17, cristal);
        inventory.setItem(18, cristal);
        inventory.setItem(26, cristal);
        inventory.setItem(27, cristal);
        inventory.setItem(28, cristal);
        for ( int i = 34; i < 45; i++ ){
            if (i != 40){
                inventory.setItem(i, cristal);
            }
        }
    }
    
    private ItemStack profile(){
        return new ItemBuilder(new ItemStack(XMaterial.PLAYER_HEAD.parseItem()))
                .setHeadSkin(target.getSkin())
                .setDisplayName("&dBan &5" + target.getName())
                .addLoreLine("&7Elige una opción para banear")
                .addLoreLine("&7a este jugador de &eYandere&cCraft")
                .build();
    }
    
    private ItemStack time(){
        return new ItemBuilder(new ItemStack(Material.WATCH))
                .setDisplayName("&d" + this.time)
                .build();
    }
    
    private ItemStack date(){
        return new ItemBuilder(new ItemStack(Material.NAME_TAG))
                .setDisplayName("&d" + this.dateType.getName())
                .build();
    }
    
    private ItemStack add(){
        return new ItemBuilder(new ItemStack(Material.STONE_BUTTON))
                .setDisplayName("&7Click para &a+1")
                .build();
    }
    
    private ItemStack remove(){
        return new ItemBuilder(new ItemStack(Material.STONE_BUTTON))
                .setDisplayName("&7Click para &a-1")
                .build();
    }
    
    private ItemStack permanent(){
        return new ItemBuilder(new ItemStack(Material.BLAZE_POWDER))
                .setDisplayName("&7Ban Permanente")
                .addLoreLine("&7Actualmente: " + (this.permanent ? "&aHABILITADO" : "&cDESHABILITADO"))
                .build();
    }
    
    private ItemStack ipBan(){
        return new ItemBuilder(new ItemStack(Material.REDSTONE))
                .setDisplayName("&7Ban IP")
                .addLoreLine("&7Actualmente: " + (this.ipBan ? "&aHABILITADO" : "&cDESHABILITADO"))
                .build();
    }
    
    private ItemStack continueTheBan(){
        return new ItemBuilder(new ItemStack(Material.WATER_LILY))
                .setDisplayName("&dHacking")
                .addLoreLine("&7Banear a " + target.getName() + " por " + this.reason)
                .build();
    }
    
    public ItemStack back(){
        // create a ItemStack with the back icon using the ItemBuilder
        return new ItemBuilder(new ItemStack(Material.BARRIER))
                .setDisplayName("&cVolver o Cerrar")
                .build();
    }
    
    public ItemStack notAllowed(){
        return createCustomSkull("§cBaneo Permanente Activado", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGJlMzViZWNhMWY1NzMxMjBlZTc5MTdhYTk2YTg5ZTE5NGRlZmM0NDQ1ZGY4YzY5ZTQ0OGU3NTVkYTljY2NkYSJ9fX0=");
    }
    
    
}
