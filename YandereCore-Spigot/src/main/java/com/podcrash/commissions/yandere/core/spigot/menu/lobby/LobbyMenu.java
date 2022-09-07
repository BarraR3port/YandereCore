package com.podcrash.commissions.yandere.core.spigot.menu.lobby;

import com.cryptomorin.xseries.XMaterial;
import com.podcrash.commissions.yandere.core.common.data.server.ProxyStats;
import com.podcrash.commissions.yandere.core.common.data.server.ServerType;
import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.items.Items;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.UpdatableMenu;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class LobbyMenu extends UpdatableMenu {
    
    private final UUID targetUserUUID;
    
    public LobbyMenu(IPlayerMenuUtility playerMenuUtility){
        super(playerMenuUtility);
        this.targetUserUUID = getOwner().getUniqueId();
    }
    
    public LobbyMenu(IPlayerMenuUtility playerMenuUtility, UUID targetUserUUID){
        super(playerMenuUtility);
        this.targetUserUUID = targetUserUUID;
    }
    
    @Override
    public String getMenuName(){
        return "&4• &8Menú de Modalidades";
    }
    
    @Override
    public int getSlots(){
        return 45;
    }
    
    @Override
    public void handleMenu(InventoryClickEvent e){
        final ItemStack item = e.getCurrentItem();
        if (NBTItem.hasTag(item, "server-name")){
            final String proxy_server_name = NBTItem.getTag(item, "server-name");
            if (!proxy_server_name.equalsIgnoreCase(ServerType.EMPTY.getName())){
                ServerType serverType = ServerType.match(proxy_server_name);
                switch(serverType){
                    case TNT_TAG:
                    case SURVIVAL:
                    case PRACTICE:
                    case EMPTY:
                        Main.getInstance().getSocket().sendJoinServer(getOwner().getUniqueId(), proxy_server_name);
                        break;
                    default:
                        new MultiLobbyMenu(playerMenuUtility, serverType).open();
                }
            } else {
                final Player p = (Player) e.getWhoClicked();
                super.checkSomething(p, e.getSlot(), item, "&cNo hay servers disponibles.", "", this.getMenuUUID());
            }
        } else if (NBTItem.hasTag(item, "ly-menu-close")){
            getOwner().closeInventory();
        }
    }
    
    
    @Override
    public void setMenuItems(){
    
        User user = Main.getInstance().getPlayers().getLocalStoredPlayer(targetUserUUID);
        if (user == null){
            user = Main.getInstance().getPlayers().getLocalStoredPlayer(getOwner().getName());
            user.setUUID(getOwner().getUniqueId());
            Main.getInstance().getPlayers().savePlayer(user);
        }
        final ProxyStats proxyStats = Main.getInstance().getProxyStats();
        inventory.setItem(11, new ItemBuilder(Items.SKY_WARS_BASE.clone())
                .addLoreLine("")
                .addLoreLine("&7Estado: " + (proxyStats.isSkyWarsOnline() ? "&aACTIVO" : "&cCERRADO"))
                .addLoreLine("")
                .addLoreLine("&7Jugadores en linea: &a" + proxyStats.getSkyWarsPlayerSize())
                .addTag("server-name", proxyStats.getRandomSkyWarsServer().getProxyName())
                .build());
        
        inventory.setItem(13, new ItemBuilder(Items.BED_WARS_BASE.clone())
                .addLoreLine("")
                .addLoreLine("&7Estado: " + (proxyStats.isBedWarsOnline() ? "&aACTIVO" : "&cCERRADO"))
                .addLoreLine("")
                .addLoreLine("&7Jugadores en linea: &a" + proxyStats.getBedWarsPlayerSize())
                .addTag("server-name", proxyStats.getRandomBedWarsServer().getProxyName())
                .build());
        
        inventory.setItem(15, new ItemBuilder(Items.PRACTICE_BASE.clone())
                .addLoreLine("")
                .addLoreLine("&7Estado: " + (proxyStats.isPracticeOnline() ? "&aACTIVO" : "&cCERRADO"))
                .addLoreLine("")
                .addLoreLine("&7Jugadores en linea: &a" + proxyStats.getPracticePlayerSize())
                .addTag("server-name", proxyStats.getRandomPracticeServer().getProxyName())
                .build());
        
        inventory.setItem(30, new ItemBuilder(Items.TNT_TAG.clone())
                .addLoreLine("")
                .addLoreLine("&7Estado: " + (proxyStats.isTNTTagOnline() ? "&aACTIVO" : "&cCERRADO"))
                .addLoreLine("")
                .addLoreLine("&7Jugadores en linea: &a" + proxyStats.getTNTTagPlayerSize())
                .addTag("server-name", proxyStats.getRandomTNTTagServer().getProxyName())
                .build());
        
        inventory.setItem(32, new ItemBuilder(Items.SURVIVAL.clone())
                .addLoreLine("")
                .addLoreLine("&7Estado: " + (proxyStats.isSurvivalOnline() ? "&aACTIVO" : "&cCERRADO"))
                .addLoreLine("")
                .addLoreLine("&7Jugadores en linea: &a" + proxyStats.getSurvivalPlayerSize())
                .addTag("server-name", proxyStats.getRandomSurvivalGamesServer().getProxyName())
                .build());
        
        inventory.setItem(44, new ItemBuilder(XMaterial.PLAYER_HEAD.parseMaterial())
                .setHeadSkin(user.getSkin())
                .setDisplayName("&b&lTus Estadísticas:")
                .addLoreLine("")
                .addLoreLine("&7► Rango: " + user.getRank().getTabPrefix())
                .addLoreLine("&7► Monedas: &c" + user.getCoinsFormatted())
                .addLoreLine("&7► Nivel: &c" + user.getLevel().getLevelName())
                .addLoreLine(user.getLevel().getProgressBar())
                .addTag("stats", "stats")
                .build());
        
        /*inventory.setItem(26, new ItemBuilder(XMaterial.NETHER_STAR.parseItem())
                .setDisplayName("&bLobby")
                .addTag("server-name", proxyStats.getRandomLobbyServer().getProxyName())
                .build());
        */
        inventory.setItem(36, super.CLOSE_ITEM);
        
    }
    
    public void reOpen(){
        this.setMenuItems();
    }
    
}
