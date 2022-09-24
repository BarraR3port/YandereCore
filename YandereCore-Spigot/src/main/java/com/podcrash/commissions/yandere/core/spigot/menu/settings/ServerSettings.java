package com.podcrash.commissions.yandere.core.spigot.menu.settings;

import com.podcrash.commissions.yandere.core.common.data.server.GlobalServerSettings;
import com.podcrash.commissions.yandere.core.spigot.Main;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.UpdatableMenu;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class ServerSettings extends UpdatableMenu {
    
    private GlobalServerSettings globalServerSettings;
    
    public ServerSettings(IPlayerMenuUtility playerMenuUtility){
        super(playerMenuUtility);
        this.globalServerSettings = Main.getInstance().getGlobalServerSettings().getOrCreate();
    }
    
    @Override
    public String getMenuName(){
        return "&4• &8Ajustes Del Servidor";
    }
    
    @Override
    public int getSlots(){
        return 45;
    }
    
    @Override
    public void handleMenu(InventoryClickEvent e){
        /*switch(e.getSlot()){
            case 20:
                new GenMenu(playerMenuUtility, arena).open();
                break;
            case 21:
                new GeneralConfigMenu(playerMenuUtility, arena).open();
                break;
            case 22:
                new TNTMenu(playerMenuUtility, arena).open();
                break;
            case 23:
                new FireballMenu(playerMenuUtility, arena).open();
                break;
            case 24:
                new PlayerSettingsMenu(playerMenuUtility, arena).open();
                break;
            case 31:
                boolean value = arena.getSettings().getGeneralConfig().switchBoolean("CUSTOM_ARENA");
                String msg = Utils.format("&c&lMODO PARTNER &8» &7Se han " + (value ? "&aHabilitado" : "&cDeshabilitado") + " &7la &e&lArena Custom&7.");
                arena.getPlayers().forEach(p -> p.sendMessage(msg));
                this.reOpen();
                break;
        }*/
        if (e.getCurrentItem().equals(CLOSE_ITEM)){
            getOwner().closeInventory();
        }
    }
    
    @Override
    public void setMenuItems(){
        
        inventory.setItem(13, currentServerSettings());
        /*inventory.setItem(20, oresConfig());
        inventory.setItem(21, generalConfig());
        inventory.setItem(22, tntMenu());
        inventory.setItem(23, fbMenu());
        inventory.setItem(24, playersConfig());
        inventory.setItem(31, CustomArena());*/
        inventory.setItem(40, this.CLOSE_ITEM);
        
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
    
    public void onReOpen(){
        this.globalServerSettings = Main.getInstance().getGlobalServerSettings().getOrCreate();
    }
    
    private ItemStack currentServerSettings(){
        return new ItemBuilder(new ItemStack(Material.NETHER_STAR))
                .setDisplayName("&cAjustes Globales")
                .addLoreLine("&7Estos son los ajustes globales")
                .addLoreLine("&7actuales de YandereCraft.")
                .addLoreLine("&7")
                .addLoreLine("&4• &f&lBEDWARS")
                .addLoreLine(" &a⋆ &fEn Desarrollo: " + (globalServerSettings.isBedWarsInDevelopment() ? "&aSi" : "&cNo"))
                .addLoreLine(" &a⋆ &fMax Jugadores: &a" + globalServerSettings.getMaxBedWarsPlayers())
                .addLoreLine("&7")
                .addLoreLine("&4• &f&lSKYWARS")
                .addLoreLine(" &a⋆ &fEn Desarrollo: " + (globalServerSettings.isSkyWarsInDevelopment() ? "&aSi" : "&cNo"))
                .addLoreLine(" &a⋆ &fMax Jugadores: &a" + globalServerSettings.getMaxSkyWarsPlayers())
                .addLoreLine("&7")
                .addLoreLine("&4• &f&lPRACTICE")
                .addLoreLine(" &a⋆ &fEn Desarrollo: " + (globalServerSettings.isPracticeInDevelopment() ? "&aSi" : "&cNo"))
                .addLoreLine(" &a⋆ &fMax Jugadores: &a" + globalServerSettings.getMaxPracticePlayers())
                .addLoreLine("&7")
                .addLoreLine("&4• &f&lSURVIVAL GAMES")
                .addLoreLine(" &a⋆ &fEn Desarrollo: " + (globalServerSettings.isSurvivalGamesInDevelopment() ? "&aSi" : "&cNo"))
                .addLoreLine(" &a⋆ &fMax Jugadores: &a" + globalServerSettings.getMaxSurvivalGamesPlayers())
                .addLoreLine("&7")
                .addLoreLine("&4• &f&lTNT TAG")
                .addLoreLine(" &a⋆ &fEn Desarrollo: " + (globalServerSettings.isTntTagInDevelopment() ? "&aSi" : "&cNo"))
                .addLoreLine(" &a⋆ &fMax Jugadores: &a" + globalServerSettings.getMaxTNTTagPlayers())
                .addLoreLine("&7")
                
                .addLoreLine("")
                .build();
    }
    
    /*private ItemStack tntMenu(){
        return new ItemBuilder(new ItemStack(Material.TNT))
                .setDisplayName("&c&lTNT")
                .addLoreLine("&7Click para abrir un menu")
                .addLoreLine("&7donde podrás configurar")
                .addLoreLine("&7las &cTNT's &7a tu estilo.")
                .addLoreLine("")
                .addLoreLine("&7Modos disponibles:")
                .addLoreLine(" &a⋆ &cNormal")
                .addLoreLine(" &a⋆ &cInsta")
                .addLoreLine(" &a⋆ &cExtremo")
                .addLoreLine(" &a⋆ &cInsta Extremo")
                .addLoreLine(" &a⋆ &cCustom")
                .addLoreLine("")
                .addLoreLine("&aValores Actuales:")
                .addLoreLine(" &c⮩ &7Segundos: &c" + arena.getSettings().getTNTConfig().getTNT_SECONDS() + (isDifferentTNTS() ? "&7(" + (differenceTNTS() >= 0 ? "&a+" : "&c") + differenceTNTS() + "&7)" : ""))
                .addLoreLine(" &c⮩ &7Radio de explosión: &c" + arena.getSettings().getTNTConfig().getTNT_NEARBY_RADIUS() + (isDifferentTNTNR() ? "&7(" + (differenceTNTNR() >= 0 ? "&a+" : "&c") + differenceTNTNR() + "&7)" : ""))
                .addLoreLine(" &c⮩ &7Fuerza de empuje: &c" + arena.getSettings().getTNTConfig().getTNT_PUSH_FORCE() + (isDifferentTNTPF() ? "&7(" + (differenceTNTPF() >= 0 ? "&a+" : "&c") + differenceTNTPF() + "&7)" : ""))
                .addLoreLine(" &c⮩ &7Fuerza de empuje de altura: &c" + arena.getSettings().getTNTConfig().getTNT_HEIGHT_FORCE() + (isDifferentTNTHF() ? "&7(" + (differenceTNTHF() >= 0 ? "&a+" : "&c") + differenceTNTHF() + "&7)" : ""))
                .build();
        
        
    }
    
    private ItemStack fbMenu(){
        return new ItemBuilder(new ItemStack(Material.FIREBALL))
                .setDisplayName("&c&lFireball")
                .addLoreLine("&7Click para abrir un menu")
                .addLoreLine("&7donde podrás configurar")
                .addLoreLine("&7las &cFIREBALLS &7a tu estilo.")
                .addLoreLine("")
                .addLoreLine("&7Modos disponibles:")
                .addLoreLine(" &a⋆ &cNormal")
                .addLoreLine(" &a⋆ &cMedio")
                .addLoreLine(" &a⋆ &cExtremo")
                .addLoreLine(" &a⋆ &cCustom")
                .addLoreLine("")
                .addLoreLine("&aValores Actuales:")
                .addLoreLine(" &c⮩ &7Radio de explosión: &c" + arena.getSettings().getFBConfig().getFB_NEARBY_RADIUS() + (isDifferentFBNR() ? "&7(" + (differenceFBNR() >= 0 ? "&a+" : "&c") + differenceFBNR() + "&7)" : ""))
                .addLoreLine(" &c⮩ &7Fuerza de empuje: &c" + arena.getSettings().getFBConfig().getFB_PUSH_FORCE() + (isDifferentFBPF() ? "&7(" + (differenceFBPF() >= 0 ? "&a+" : "&c") + differenceFBPF() + "&7)" : ""))
                .addLoreLine(" &c⮩ &7Fuerza de empuje de altura: &c" + arena.getSettings().getFBConfig().getFB_HEIGHT_FORCE() + (isDifferentFBHF() ? "&7(" + (differenceFBHF() >= 0 ? "&a+" : "&c") + differenceFBHF() + "&7)" : ""))
                .addLoreLine(" &c⮩ &7Velocidad: &c" + arena.getSettings().getFBConfig().getFB_SPEED() + (isDifferentFBS() ? "&7(" + (differenceFBS() >= 0 ? "&a+" : "&c") + differenceFBS() + "&7)" : ""))
                .build();
        
    }
    
    private ItemStack generalConfig(){
        return new ItemBuilder(new ItemStack(Material.MAP))
                .setDisplayName("&c&lConfiguración General")
                .addLoreLine("&7Click para abrir un menu")
                .addLoreLine("&7donde podrás configurar")
                .addLoreLine("&7algunos aspectos generales")
                .addLoreLine("&7de la partida.")
                .addLoreLine("")
                .addLoreLine("&7Opciones disponibles:")
                .addLoreLine(" &a⋆ &cCama Frágil &8(" + (arena.getSettings().getGeneralConfig().getFeature("INSTA_BED_BREAK").getBoolean() ? "&a✓" : "&c☓") + "&8)")
                .addLoreLine(" &a⋆ &cPermitir Espectadores &8(" + (arena.getSettings().getGeneralConfig().getFeature("ALLOW_SPECTATORS").getBoolean() ? "&a✓" : "&c☓") + "&8)")
                .addLoreLine(" &a⋆ &cAltura mínima para Morir: " + arena.getSettings().getGeneralConfig().getFeature("KILL_HEIGHT").getInt() + (isDifferentGCKH() ? "&7(" + (differenceGCKH() >= 0 ? "&a+" : "&c") + differenceGCKH() + "&7)" : ""))
                .addLoreLine("")
                .build();
        
    }
    
    private ItemStack oresConfig(){
        return new ItemBuilder(new ItemStack(Material.DIAMOND))
                .setDisplayName("&c&lConfiguración de Generadores")
                .addLoreLine("&7Click para abrir un menu")
                .addLoreLine("&7donde podrás configurar")
                .addLoreLine("&7detalladamente cada generador")
                .addLoreLine("&7de la arena.")
                .addLoreLine("")
                .addLoreLine("&7Opciones disponibles:")
                .addLoreLine(" &a⋆ &cGenerador de &f&lHierro &8(&a✓&8)")
                .addLoreLine(" &a⋆ &cGenerador de &6&lOro &8(&a✓&8)")
                .addLoreLine(" &a⋆ &cGenerador de &b&lDiamantes &8(&a✓&8)")
                .addLoreLine(" &a⋆ &cGenerador de &2&lEsmeraldas &8(&a✓&8)")
                .addLoreLine("")
                .build();
        
    }
    
    private ItemStack playersConfig(){
        return new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
                .setSkullOwner(getOwner().getName())
                .setDisplayName("&c&lConfiguración de Jugabilidad")
                .addLoreLine("&7Click para abrir un menu")
                .addLoreLine("&7donde podrás definir")
                .addLoreLine("&7detalladamente ciertas")
                .addLoreLine("&7configuraciones que afectan la")
                .addLoreLine("&7jugabilidad general.")
                .addLoreLine("")
                .addLoreLine("&7Opciones disponibles:")
                .addLoreLine(" &a⋆ &cInsta Kill &8(&a✓&8)")
                .addLoreLine(" &a⋆ &cVida extra &8(&a✓&8)")
                .addLoreLine(" &a⋆ &cVelocidad &8(&a✓&8)")
                .addLoreLine(" &a⋆ &cFuerza &8(&a✓&8)")
                .addLoreLine(" &a⋆ &cSalto Alto &8(&a✓&8)")
                .addLoreLine(" &a⋆ &cHora del día &8(&a✓&8)")
                .addLoreLine("")
                .build();
        
    }
    
    private ItemStack CustomArena(){
        boolean isCustom = arena.getSettings().getGeneralConfig().getFeature("CUSTOM_ARENA").getBoolean();
        return new ItemBuilder(XMaterial.FIREWORK_STAR.parseItem())
                .setFireWorkColor(isCustom ? Color.LIME : Color.WHITE)
                .setDisplayName("&cArena Custom")
                .addLoreLine("&7Determina si esta arena es")
                .addLoreLine("&7customizable o no.")
                .addLoreLine("")
                .addLoreLine("&aValor actual:")
                .addLoreLine(" &c⋆ &c" + (isCustom ? "&cActivado" : "&5Desactivado"))
                .addLoreLine("")
                .addLoreLine("&7Click para " + (isCustom ? "&cDesactivar" : "&aActivar"))
                .build();
    }*/
}
