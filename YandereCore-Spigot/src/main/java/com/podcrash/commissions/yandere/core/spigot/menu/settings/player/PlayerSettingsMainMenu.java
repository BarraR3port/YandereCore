package com.podcrash.commissions.yandere.core.spigot.menu.settings.player;

import com.cryptomorin.xseries.XMaterial;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.menu.lobby.LobbyMenu;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.UpdatableMenu;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerSettingsMainMenu extends UpdatableMenu {
    
    
    private final int[] decorateSlots = {
            0, 1, 7, 8,
            9, 17,
            27, 35,
            36, 37, 43, 44};
    
    private final UUID targetUUID;
    
    public PlayerSettingsMainMenu(IPlayerMenuUtility playerMenuUtility){
        super(playerMenuUtility);
        this.targetUUID = getOwner().getUniqueId();
        this.setDebug(true);
        
    }
    
    public PlayerSettingsMainMenu(IPlayerMenuUtility playerMenuUtility, UUID target){
        super(playerMenuUtility);
        this.targetUUID = target;
        this.setDebug(true);
    }
    
    @Override
    public String getMenuName(){
        return "&4• &8" + (targetUUID.equals(getOwner().getUniqueId()) ? "Ajustes" : "Ajustes de " + Main.getInstance().getPlayers().getCachedPlayer(targetUUID).getName());
    }
    
    @Override
    public int getSlots(){
        return 45;
    }
    
    @Override
    public void setMenuItems(){
        ItemStack fillerMidItem = new ItemBuilder(FILLER_GLASS.clone()).setDyeColor(14).build();
        for ( int i : decorateSlots ){
            inventory.setItem(i, fillerMidItem);
        }
    
        inventory.setItem(20, new ItemBuilder(XMaterial.MAP.parseItem())
                .setDisplayName("&c&lOpciones:")
                .addLoreLine("&7Haz click para ver tu opciones")
                .addLoreLine("&7y así poder cambiarlas.")
                .addLoreLine("")
                .addTag("settings", "options")
                .addTag("development", "true")
                .build());
    
    
        inventory.setItem(21, new ItemBuilder(XMaterial.NAME_TAG.parseItem())
                .setDisplayName("&c&lPropiedades:")
                .addLoreLine("&7Haz click para ver las propiedades")
                .addLoreLine("&7atribuidas a tu cuenta.")
                .addLoreLine("")
                .addTag("settings", "options")
                .addTag("development", "true")
                .build());
    
        inventory.setItem(21, new ItemBuilder(XMaterial.SUNFLOWER.parseItem())
                .setDisplayName("&c&lDinero:")
                .addLoreLine("&7Haz click para ver tu dinero")
                .addLoreLine("&7atribuidas a tu cuenta.")
                .addLoreLine("")
                .addTag("settings", "options")
                .addTag("development", "true")
                .build());
    
    
        inventory.setItem(40, CLOSE_ITEM);
    }
    
    @Override
    public void handleMenu(InventoryClickEvent e){
        ItemStack item = e.getCurrentItem();
        if (NBTItem.hasTag(item, "ly-menu-close")){
            new LobbyMenu(playerMenuUtility).open();
        }
    }
}
