package com.podcrash.commissions.yandere.core.spigot.menu.lobby;

import com.cryptomorin.xseries.XMaterial;
import com.podcrash.commissions.yandere.core.common.data.server.GlobalServerSettings;
import com.podcrash.commissions.yandere.core.common.data.server.ProxyStats;
import com.podcrash.commissions.yandere.core.common.data.server.ServerType;
import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.items.Items;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.UpdatableMenu;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class LobbyMenu extends UpdatableMenu {
    
    private final UUID targetUserUUID;
    private final GlobalServerSettings globalServerSettings;
    
    public LobbyMenu(IPlayerMenuUtility playerMenuUtility){
        super(playerMenuUtility);
        this.targetUserUUID = getOwner().getUniqueId();
        this.globalServerSettings = Main.getInstance().getGlobalServerSettings().getOrCreate();
    }
    
    public LobbyMenu(IPlayerMenuUtility playerMenuUtility, UUID targetUserUUID){
        super(playerMenuUtility);
        this.targetUserUUID = targetUserUUID;
        this.globalServerSettings = Main.getInstance().getGlobalServerSettings().getOrCreate();
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
            final Player p = (Player) e.getWhoClicked();
            if (!proxy_server_name.equalsIgnoreCase(ServerType.EMPTY.getName())){
                ServerType serverType = ServerType.match(proxy_server_name);
                boolean isInDevelopment = NBTItem.getTag(item, "development").equalsIgnoreCase("true");
                if (isInDevelopment && !p.hasPermission("yandere.development.access." + serverType.getPrefix().replaceAll("-", ""))){
                    super.checkSomething(p, e.getSlot(), item, "&cEsta modalidad está en desarrollo.", "", this.getMenuUUID());
                    return;
                }
                switch(serverType){
                    case TNT_TAG:
                    case SURVIVAL:
                    case PRACTICE:
                        Main.getInstance().getSocket().sendJoinServer(getOwner().getUniqueId(), proxy_server_name);
                        break;
                    case EMPTY:
                        super.checkSomething(p, e.getSlot(), item, "&cNo hay servers disponibles.", "", this.getMenuUUID());
                        break;
                    default:
                        new MultiLobbyMenu(playerMenuUtility, serverType).open();
                }
            } else {
                super.checkSomething(p, e.getSlot(), item, "&cNo hay servers disponibles.", "", this.getMenuUUID());
            }
        } else if (NBTItem.hasTag(item, "ly-menu-close")){
            getOwner().closeInventory();
        }
    }
    
    
    @Override
    public void setMenuItems(){
    
        User user = Main.getInstance().getPlayers().getCachedPlayer(targetUserUUID);
        if (user == null){
            user = Main.getInstance().getPlayers().getCachedPlayer(getOwner().getName());
            user.setUUID(getOwner().getUniqueId());
            Main.getInstance().getPlayers().savePlayer(user);
        }
        final ProxyStats proxyStats = Main.getInstance().getProxyStats();
        inventory.setItem(11, new ItemBuilder(Items.SKY_WARS_BASE.clone())
                .addLoreLine("&7Estado: " + (globalServerSettings.isSkyWarsInDevelopment() ? "&3En Desarrollo" : proxyStats.isSkyWarsOnline() ? "&aACTIVO" : "&cCERRADO"))
                .addLoreLine(globalServerSettings.isSkyWarsInDevelopment() ? null : proxyStats.isSkyWarsOnline() ? "" : null)
                .addLoreLine(globalServerSettings.isSkyWarsInDevelopment() ? null : proxyStats.isSkyWarsOnline() ? "&7Jugadores en linea: &a" + proxyStats.getSkyWarsPlayerSize() : null)
                .addTag("server-name", proxyStats.getRandomSkyWarsServer().getProxyName())
                .addTag("development", globalServerSettings.isSkyWarsInDevelopment() ? "true" : "false")
                .build());
        
        inventory.setItem(13, new ItemBuilder(Items.BED_WARS_BASE.clone())
                .addLoreLine("&7Estado: " + (globalServerSettings.isBedWarsInDevelopment() ? "&3En Desarrollo" : proxyStats.isBedWarsLobbyOnline() ? "&aACTIVO" : "&cCERRADO"))
                .addLoreLine(globalServerSettings.isBedWarsInDevelopment() ? null : proxyStats.isBedWarsLobbyOnline() ? "" : null)
                .addLoreLine(globalServerSettings.isBedWarsInDevelopment() ? null : proxyStats.isBedWarsLobbyOnline() ? "&7Jugadores en linea: &a" + proxyStats.getBedWarsPlayerSize() : null)
                .addTag("server-name", proxyStats.getRandomBedWarsServer().getProxyName())
                .addTag("development", globalServerSettings.isBedWarsInDevelopment() ? "true" : "false")
                .build());
    
        inventory.setItem(15, new ItemBuilder(Items.PRACTICE_BASE.clone())
                .addLoreLine("&7Estado: " + (globalServerSettings.isPracticeInDevelopment() ? "&3En Desarrollo" : proxyStats.isPracticeOnline() ? "&aACTIVO" : "&cCERRADO"))
                .addLoreLine(globalServerSettings.isPracticeInDevelopment() ? null : proxyStats.isPracticeOnline() ? "" : null)
                .addLoreLine(globalServerSettings.isPracticeInDevelopment() ? null : proxyStats.isPracticeOnline() ? "&7Jugadores en linea: &a" + proxyStats.getPracticePlayerSize() : null)
                .addTag("server-name", proxyStats.getRandomPracticeServer().getProxyName())
                .addTag("development", globalServerSettings.isPracticeInDevelopment() ? "true" : "false")
                .build());
    
        inventory.setItem(26, new ItemBuilder(Material.NETHER_STAR)
                .setDisplayName("&a&lLobbies")
                .addLoreLine("&7Estado: " + (proxyStats.isLobbyOnline() ? "&aACTIVO" : "&cCERRADO"))
                .addLoreLine(proxyStats.isLobbyOnline() ? "" : null)
                .addLoreLine(proxyStats.isLobbyOnline() ? "&7Click para ver la lista de Lobbies." : null)
                .addTag("server-name", proxyStats.getRandomLobbyServer().getProxyName())
                .build());
    
        inventory.setItem(30, new ItemBuilder(Items.TNT_TAG.clone())
                .addLoreLine("&7Estado: " + (globalServerSettings.isTntTagInDevelopment() ? "&3En Desarrollo" : proxyStats.isTNTTagOnline() ? "&aACTIVO" : "&cCERRADO"))
                .addLoreLine(globalServerSettings.isTntTagInDevelopment() ? null : proxyStats.isTNTTagOnline() ? "" : null)
                .addLoreLine(globalServerSettings.isTntTagInDevelopment() ? null : proxyStats.isTNTTagOnline() ? "&7Jugadores en linea: &a" + proxyStats.getTNTTagPlayerSize() : null)
                .addTag("server-name", proxyStats.getRandomTNTTagServer().getProxyName())
                .addTag("development", globalServerSettings.isTntTagInDevelopment() ? "true" : "false")
                .build());
    
        inventory.setItem(32, new ItemBuilder(Items.SURVIVAL.clone())
                .addLoreLine("&7Estado: " + (globalServerSettings.isSurvivalGamesInDevelopment() ? "&3En Desarrollo" : proxyStats.isSurvivalOnline() ? "&aACTIVO" : "&cCERRADO"))
                .addLoreLine(globalServerSettings.isSurvivalGamesInDevelopment() ? null : proxyStats.isSurvivalOnline() ? "" : null)
                .addLoreLine(globalServerSettings.isSurvivalGamesInDevelopment() ? null : proxyStats.isSurvivalOnline() ? "&7Jugadores en linea: &a" + proxyStats.getSurvivalPlayerSize() : null)
                .addTag("server-name", proxyStats.getRandomSurvivalGamesServer().getProxyName())
                .addTag("development", globalServerSettings.isSurvivalGamesInDevelopment() ? "true" : "false")
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
        
        inventory.setItem(36, super.CLOSE_ITEM);
        
    }
    
    public void reOpen(){
        this.setMenuItems();
    }
    
}
