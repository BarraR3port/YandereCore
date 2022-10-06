package com.podcrash.commissions.yandere.core.spigot.menu.settings.player;

import com.cryptomorin.xseries.XMaterial;
import com.podcrash.commissions.yandere.core.common.data.level.Level;
import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.menu.lobby.LobbyMenu;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.Menu;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerSettingsMainMenu extends Menu {
    
    
    private final int[] decorateSlots = {
            0, 1, 7, 8,
            9, 17,
            27, 35,
            36, 37, 43, 44};
    
    private final UUID targetUUID;
    
    public PlayerSettingsMainMenu(IPlayerMenuUtility playerMenuUtility){
        super(playerMenuUtility, true);
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
        User user = Main.getInstance().getPlayers().getCachedPlayer(targetUUID);
        ItemBuilder options = new ItemBuilder(XMaterial.MAP.parseItem())
                .setDisplayName("&9&lOpciones:")
                .addLoreLine("&7Haz click para ver tu opciones")
                .addLoreLine("&7y así poder cambiarlas.")
                .addLoreLine("")
                .addLoreLine("&f&lOpciones:")
                .addTag("settings", "options")
                .addTag("development", "true");
        for ( String option : user.getOptions().keySet() ){
            options.addLoreLine(" &4• &7" + option + ": " + (user.getOption(option) ? "&aSi" : "&cNo"));
        }
        inventory.setItem(20, options.build());
    
        ItemBuilder props = new ItemBuilder(XMaterial.NAME_TAG.parseItem())
                .setDisplayName("&9&lPropiedades:")
                .addLoreLine("&7Haz click para ver las propiedades")
                .addLoreLine("&7atribuidas a tu cuenta.")
                .addLoreLine("")
                .addLoreLine("&f&lPropiedades:")
                .addTag("settings", "props")
                .addTag("development", "true");
        for ( String property : user.getProperties().keySet() ){
            props.addLoreLine(" &4• &7" + property + ": &f" + user.getProperty(property));
        }
        inventory.setItem(21, props.build());
    
        inventory.setItem(22, new ItemBuilder(XMaterial.SUNFLOWER.parseItem())
                .setDisplayName("&e&lDinero:")
                .addLoreLine("&7Actualmente tienes &f" + user.getCoinsFormatted())
                .addLoreLine("&7monedas en tu cuenta.")
                .addLoreLine("&7Haz click para ver el registro de gastos.")
                .addLoreLine("")
                .addTag("settings", "money")
                .addTag("development", "true")
                .build());
        Level level = user.getLevel();
        inventory.setItem(23, new ItemBuilder(XMaterial.EXPERIENCE_BOTTLE.parseItem())
                .setDisplayName("&a&lNivel:")
                .addLoreLine("&7Actualmente eres Nº " + level.getLevel())
                .addLoreLine("&7y tienes &f" + level.getCurrentXp() + " &7de &f" + level.getTotalNextLevelXp() + " &7XP.")
                .addLoreLine("&7Haz click para ver tu progreso.")
                .addLoreLine(level.getProgressBarFormatted())
                .addLoreLine("")
                .addTag("settings", "level")
                .addTag("development", "true")
                .build());
    
        inventory.setItem(24, new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
                .setHeadSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc1NGVlZDhkMDVhMTk2YzNmYzJkMjUxMTQxN2ViNTYyNjI2MjE0MTRjZTNiM2RmYjM1NzFhZWE0ZGRkYzQ3MCJ9fX0=")
                .setDisplayName("&3&lRecompensas:")
                .addLoreLine("&7Actualmente tienes &f" + user.getRewards().size() + " &7recompensas.")
                .addLoreLine("&7Haz click para ver tus recompensas")
                .addLoreLine("")
                .addTag("settings", "reward")
                .addTag("development", "true")
                .build());
    
        inventory.setItem(31, new ItemBuilder(XMaterial.BARRIER.parseItem())
                .setDisplayName("&4&lProhibiciones:")
                .addLoreLine("&7Actualmente tienes &f" + user.getPunishments().size() + " &7prohibiciones.")
                .addLoreLine("&7Haz click para ver tus prohibiciones.")
                .addLoreLine("")
                .addTag("settings", "punish")
                .addTag("development", "true")
                .build());
    
        inventory.setItem(13, new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
                .setHeadSkin(user.getSkin())
                .setDisplayName("&4&lRango:")
                .addLoreLine("&7Actualmente tienes el rango &f" + user.getRank().getTabPrefix())
                .addLoreLine("")
                .addTag("settings", "punish")
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
