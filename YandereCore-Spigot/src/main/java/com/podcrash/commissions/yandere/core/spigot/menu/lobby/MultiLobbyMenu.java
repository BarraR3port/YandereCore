package com.podcrash.commissions.yandere.core.spigot.menu.lobby;

import com.podcrash.commissions.yandere.core.common.data.server.ServerType;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.items.Items;
import com.podcrash.commissions.yandere.core.spigot.settings.Settings;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.UpdatableMenu;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.Color;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MultiLobbyMenu extends UpdatableMenu {
    
    private final ServerType serverType;
    private final int[] serverSlot = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25};
    
    public MultiLobbyMenu(IPlayerMenuUtility playerMenuUtility, ServerType serverType){
        super(playerMenuUtility, true);
        this.serverType = serverType;
    }
    
    public MultiLobbyMenu(IPlayerMenuUtility playerMenuUtility){
        super(playerMenuUtility, true);
        this.serverType = Settings.SERVER_TYPE;
    }
    
    @Override
    public String getMenuName(){
        switch(serverType){
            case SKY_WARS:
                return "&4• &8Lobbies de &eSky Wars";
            case BED_WARS:
            case LOBBY_BED_WARS:
                return "&4• &8Lobbies de &eBed Wars";
            case PRACTICE:
                return "&4• &8Lobbies de &ePractice";
            case TNT_TAG:
                return "&4• &8Lobbies de &eTNT Tag";
            case SURVIVAL:
                return "&4• &8Lobbies de &eSurvival";
            default:
                return "&4• &8Lobbies";
        }
    }
    
    @Override
    public int getSlots(){
        return 36;
    }
    
    @Override
    public void setMenuItems(){
        for ( int i = 0; i < serverSlot.length; i++ ){
            String current = String.valueOf((i + 1));
            String serverName = serverType.getPrefix() + current;
            boolean isSameLobby = Settings.PROXY_SERVER_NAME.equals(serverName);
            int playersOnline = Main.getInstance().getProxyStats().getTargetServerPlayers(serverName);
            boolean isOnline = Main.getInstance().getProxyStats().isTargetServerOnline(serverName);
            Color color = isSameLobby ? Color.ORANGE : (isOnline ? Color.LIME : Color.WHITE);
            this.inventory.setItem(serverSlot[i],
                    new ItemBuilder(Items.MULTI_LOBBY_MENU.getItem().clone())
                            .setDisplayName("&a" + serverType.getName() + " &c#" + current)
                            .clearLore()
                            .addLoreLine("")
                            .addLoreLine("&f&lEstado: " + (isOnline ? "&aAbierto" : "&cCerrado"))
                            .addLoreLine("&f&lJugadores: " + (playersOnline > 0 ? "&a" : "&c") + playersOnline + "&7/&a" + Main.getInstance().getGlobalServerSettings().getOrCreate().getMax(serverType))
                            .addLoreLine(isOnline ? "" : null)
                            .addLoreLine(isSameLobby ? "&cYa estás en este servidor!" : isOnline ? "&7&lClick para entrar!" : null)
                            .addLoreLine("")
                            .setFireWorkColor(color)
                            .addTag("allowed", String.valueOf(!isSameLobby))
                            .addTag("online", String.valueOf(isOnline))
                            .addTag("server", serverName)
                            .build()
            );
        }
        inventory.setItem(31, super.CLOSE_ITEM);
        
    }
    
    @Override
    public void handleMenu(InventoryClickEvent e){
        final ItemStack item = e.getCurrentItem();
        final NBTItem nbtItem = new NBTItem(item);
        
        if (nbtItem.hasTag("allowed")){
            if (nbtItem.getTag("allowed").equalsIgnoreCase("false")){
                getOwner().sendMessage(Utils.format("&cYa estás en este servidor!"));
            } else if (nbtItem.getTag("allowed").equalsIgnoreCase("true")){
                if (nbtItem.getTag("online").equalsIgnoreCase("true")){
                    Main.getInstance().getSocket().sendJoinServer(getOwner().getUniqueId(), nbtItem.getString("server"));
                } else {
                    this.checkSomething(getOwner(), e.getSlot(), item, "&cEste servidor está cerrado!", "", getMenuUUID());
                }
            }
            e.setCancelled(true);
        } else if (nbtItem.hasTag("ly-menu-close")){
            new LobbyMenu(playerMenuUtility).open();
        }
        
    }
}
