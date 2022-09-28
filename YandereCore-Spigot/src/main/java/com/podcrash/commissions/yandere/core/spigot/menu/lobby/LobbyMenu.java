package com.podcrash.commissions.yandere.core.spigot.menu.lobby;

import com.cryptomorin.xseries.XMaterial;
import com.podcrash.commissions.yandere.core.common.data.server.GlobalServerSettings;
import com.podcrash.commissions.yandere.core.common.data.server.ProxyStats;
import com.podcrash.commissions.yandere.core.common.data.server.ServerType;
import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.items.Items;
import com.podcrash.commissions.yandere.core.spigot.settings.Settings;
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
                        Main.getInstance().getSocket().sendJoinServer(getOwner().getUniqueId(), proxy_server_name);
                        break;
                    case EMPTY:
                        super.checkSomething(p, e.getSlot(), item, "&cNo hay servers disponibles.", "", this.getMenuUUID());
                        break;
                    default:{
                        if (e.getClick().isRightClick()){
                            new MultiLobbyMenu(playerMenuUtility, serverType).open();
                        } else {
                            if (proxy_server_name.equals(Settings.PROXY_SERVER_NAME)){
                                super.checkSomething(p, e.getSlot(), item, "&cYa estás conectado en este server.", "", this.getMenuUUID());
                                break;
                            }
                            Main.getInstance().getSocket().sendJoinServer(getOwner().getUniqueId(), proxy_server_name);
                        }
                    }
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
        final boolean isSkyWarsOnline = proxyStats.isServerByTypeOnline(ServerType.SKY_WARS);
        final boolean isBedWarsOnline = proxyStats.isServerByTypeOnline(ServerType.LOBBY_BED_WARS);
        final boolean isPracticeOnline = proxyStats.isServerByTypeOnline(ServerType.PRACTICE);
        final boolean isLobbyOnline = proxyStats.isServerByTypeOnline(ServerType.LOBBY);
        final boolean isTNTTagOnline = proxyStats.isServerByTypeOnline(ServerType.TNT_TAG);
        final boolean isSurvivalOnline = proxyStats.isServerByTypeOnline(ServerType.SURVIVAL);
        inventory.setItem(11, new ItemBuilder(Items.SKY_WARS_BASE.clone())
                .addLoreLine("&7Estado: " + (globalServerSettings.isSkyWarsInDevelopment() ? "&3En Desarrollo" : isSkyWarsOnline ? "&aACTIVO" : "&cCERRADO"))
                .addLoreLine(globalServerSettings.isSkyWarsInDevelopment() ? null : isSkyWarsOnline ? "" : null)
                .addLoreLine(globalServerSettings.isSkyWarsInDevelopment() ? null : isSkyWarsOnline ? "&7Jugadores en linea: &a" + proxyStats.getServerSizeByType(ServerType.SKY_WARS) : null)
                .addLoreLine(globalServerSettings.isSkyWarsInDevelopment() ? null : isSkyWarsOnline ? "" : null)
                .addLoreLine(globalServerSettings.isSkyWarsInDevelopment() ? null : isSkyWarsOnline ? "&eClick Izq. para entrar a un server." : null)
                .addLoreLine(globalServerSettings.isSkyWarsInDevelopment() ? null : isSkyWarsOnline ? "&eClick Der. para ver los lobbies." : null)
                .addTag("server-name", proxyStats.getRandomServerByType(ServerType.SKY_WARS).getProxyName())
                .addTag("development", globalServerSettings.isSkyWarsInDevelopment() ? "true" : "false")
                .build());
        
        inventory.setItem(13, new ItemBuilder(Items.BED_WARS_BASE.clone())
                .addLoreLine("&7Estado: " + (globalServerSettings.isBedWarsInDevelopment() ? "&3En Desarrollo" : isBedWarsOnline ? "&aACTIVO" : "&cCERRADO"))
                .addLoreLine(globalServerSettings.isBedWarsInDevelopment() ? null : isBedWarsOnline ? "" : null)
                .addLoreLine(globalServerSettings.isBedWarsInDevelopment() ? null : isBedWarsOnline ? "&7Jugadores en linea: &a" + (proxyStats.getServerSizeByType(ServerType.BED_WARS) + proxyStats.getServerSizeByType(ServerType.LOBBY_BED_WARS)) : null)
                .addLoreLine(globalServerSettings.isBedWarsInDevelopment() ? null : isBedWarsOnline ? "" : null)
                .addLoreLine(globalServerSettings.isBedWarsInDevelopment() ? null : isBedWarsOnline ? "&eClick Izq. para entrar a un server." : null)
                .addLoreLine(globalServerSettings.isBedWarsInDevelopment() ? null : isBedWarsOnline ? "&eClick Der. para ver los lobbies." : null)
                .addTag("server-name", proxyStats.getRandomServerByType(ServerType.LOBBY_BED_WARS).getProxyName())
                .addTag("development", globalServerSettings.isBedWarsInDevelopment() ? "true" : "false")
                .build());
    
        inventory.setItem(15, new ItemBuilder(Items.PRACTICE_BASE.clone())
                .addLoreLine("&7Estado: " + (globalServerSettings.isPracticeInDevelopment() ? "&3En Desarrollo" : isPracticeOnline ? "&aACTIVO" : "&cCERRADO"))
                .addLoreLine(globalServerSettings.isPracticeInDevelopment() ? null : isPracticeOnline ? "" : null)
                .addLoreLine(globalServerSettings.isPracticeInDevelopment() ? null : isPracticeOnline ? "&7Jugadores en linea: &a" + proxyStats.getServerSizeByType(ServerType.PRACTICE) : null)
                .addLoreLine(globalServerSettings.isPracticeInDevelopment() ? null : isPracticeOnline ? "" : null)
                .addLoreLine(globalServerSettings.isPracticeInDevelopment() ? null : isPracticeOnline ? "&eClick Izq. para entrar a un server." : null)
                .addLoreLine(globalServerSettings.isPracticeInDevelopment() ? null : isPracticeOnline ? "&eClick Der. para ver los lobbies." : null)
                .addTag("server-name", proxyStats.getRandomServerByType(ServerType.PRACTICE).getProxyName())
                .addTag("development", globalServerSettings.isPracticeInDevelopment() ? "true" : "false")
                .build());
    
        inventory.setItem(26, new ItemBuilder(Material.NETHER_STAR)
                .setDisplayName("&a&lLobbies")
                .addLoreLine("&7Estado: " + (isLobbyOnline ? "&aACTIVO" : "&cCERRADO"))
                .addLoreLine(isLobbyOnline ? "" : null)
                .addLoreLine(isLobbyOnline ? "&7Click para ver la lista de Lobbies." : null)
                .addTag("server-name", proxyStats.getRandomServerByType(ServerType.LOBBY).getProxyName())
                .addTag("development", "false")
                .build());
    
        inventory.setItem(30, new ItemBuilder(Items.TNT_TAG.clone())
                .addLoreLine("&7Estado: " + (globalServerSettings.isTntTagInDevelopment() ? "&3En Desarrollo" : isTNTTagOnline ? "&aACTIVO" : "&cCERRADO"))
                .addLoreLine(globalServerSettings.isTntTagInDevelopment() ? null : isTNTTagOnline ? "" : null)
                .addLoreLine(globalServerSettings.isTntTagInDevelopment() ? null : isTNTTagOnline ? "&7Jugadores en linea: &a" + proxyStats.getServerSizeByType(ServerType.TNT_TAG) : null)
                .addTag("server-name", proxyStats.getRandomServerByType(ServerType.TNT_TAG).getProxyName())
                .addTag("development", globalServerSettings.isTntTagInDevelopment() ? "true" : "false")
                .build());
    
        inventory.setItem(32, new ItemBuilder(Items.SURVIVAL.clone())
                .addLoreLine("&7Estado: " + (globalServerSettings.isSurvivalGamesInDevelopment() ? "&3En Desarrollo" : isSurvivalOnline ? "&aACTIVO" : "&cCERRADO"))
                .addLoreLine(globalServerSettings.isSurvivalGamesInDevelopment() ? null : isSurvivalOnline ? "" : null)
                .addLoreLine(globalServerSettings.isSurvivalGamesInDevelopment() ? null : isSurvivalOnline ? "&7Jugadores en linea: &a" + proxyStats.getServerSizeByType(ServerType.SURVIVAL) : null)
                .addTag("server-name", proxyStats.getRandomServerByType(ServerType.SURVIVAL).getProxyName())
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
                .addTag("development", "false")
                .build());
        
        inventory.setItem(36, super.CLOSE_ITEM);
        
    }
    
    public void reOpen(){
        this.setMenuItems();
    }
    
}
