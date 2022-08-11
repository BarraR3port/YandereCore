package com.podcrash.comissions.yandere.core.spigot.menu.lobby;

import com.cryptomorin.xseries.XMaterial;
import com.podcrash.comissions.yandere.core.common.data.server.ProxyStats;
import com.podcrash.comissions.yandere.core.common.data.server.ServerType;
import com.podcrash.comissions.yandere.core.common.data.user.props.Stats;
import com.podcrash.comissions.yandere.core.spigot.Main;
import com.podcrash.comissions.yandere.core.spigot.items.Items;
import com.podcrash.comissions.yandere.core.spigot.users.SpigotUser;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.Menu;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class LobbyMenu extends Menu {
    
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
        return "YandereCraft";
    }
    
    @Override
    public int getSlots(){
        return 54;
    }
    
    @Override
    public void handleMenu(InventoryClickEvent e){
        final ItemStack item = e.getCurrentItem();
        if (NBTItem.hasTag(item, "server-name")){
            final String proxy_server_name = NBTItem.getTag(item, "server-name");
            if (!proxy_server_name.equals(ServerType.EMPTY.getName())){
                Main.getInstance().getSocket().sendJoinServer(getOwner().getUniqueId(), proxy_server_name);
            } else {
                final Player p = (Player) e.getWhoClicked();
                super.checkSomething(p, e.getSlot(), item, "&cNo Servers Disponibles", "", this.getMenuUUID());
                
            }
            
        } else if (NBTItem.hasTag(item, "type")){
            Main.getInstance().getSocket().sendJoinServer(getOwner().getUniqueId(), "lobby");
        } else if (NBTItem.hasTag(item, "ly-menu-close")){
            getOwner().closeInventory();
        }
    }
    
    
    @Override
    public void setMenuItems(){
        
        SpigotUser user = Main.getInstance().getPlayers().getUpdatedPlayer(targetUserUUID);
        if (user == null){
            user = Main.getInstance().getPlayers().getPlayer(getOwner().getName());
            user.setUUID(getOwner().getUniqueId());
            Main.getInstance().getPlayers().savePlayer(user);
        }
        final Stats userStats = user.getStats();
        inventory.setItem(20, new ItemBuilder(Items.SKY_WARS_BASE.clone())
                .addLoreLine("")
                .addLoreLine("&7Estado: " + (Main.getInstance().getProxyStats().isSkyWarsOnline() ? "&aACTIVO" : "&cCERRADO"))
                .addLoreLine("")
                .addLoreLine("&7Jugadores en linea: &a" + Main.getInstance().getProxyStats().getSkyWarsPlayerSize())
                .addTag("server-name", Main.getInstance().getProxyStats().getRandomSkyWarsServer().getProxyName())
                .build());
        
        inventory.setItem(22, new ItemBuilder(Items.BED_WARS_BASE.clone())
                .addLoreLine("")
                .addLoreLine("&7Estado: " + (Main.getInstance().getProxyStats().isBedWarsOnline() ? "&aACTIVO" : "&cCERRADO"))
                .addLoreLine("")
                .addLoreLine("&7Jugadores en linea: &a" + Main.getInstance().getProxyStats().getBedWarsPlayerSize())
                .addTag("server-name", Main.getInstance().getProxyStats().getRandomBedWarsServer().getProxyName())
                .build());
        
        inventory.setItem(24, new ItemBuilder(Items.PRACTICE_BASE.clone())
                .addLoreLine("")
                .addLoreLine("&7Estado: " + (Main.getInstance().getProxyStats().isPracticeOnline() ? "&aACTIVO" : "&cCERRADO"))
                .addLoreLine("")
                .addLoreLine("&7Jugadores en linea: &a" + Main.getInstance().getProxyStats().getPracticePlayerSize())
                .addTag("server-name", Main.getInstance().getProxyStats().getRandomPracticeServer().getProxyName())
                .build());
        
        inventory.setItem(39, new ItemBuilder(Items.TNT_TAG.clone())
                .addLoreLine("")
                .addLoreLine("&7Estado: " + (Main.getInstance().getProxyStats().isTNTTagOnline() ? "&aACTIVO" : "&cCERRADO"))
                .addLoreLine("")
                .addLoreLine("&7Jugadores en linea: &a" + Main.getInstance().getProxyStats().getTNTTagPlayerSize())
                .addTag("server-name", Main.getInstance().getProxyStats().getRandomTNTTagServer().getProxyName())
                .build());
        
        inventory.setItem(41, new ItemBuilder(Items.SURVIVAL.clone())
                .addLoreLine("")
                .addLoreLine("&7Estado: " + (Main.getInstance().getProxyStats().isSurvivalOnline() ? "&aACTIVO" : "&cCERRADO"))
                .addLoreLine("")
                .addLoreLine("&7Jugadores en linea: &a" + Main.getInstance().getProxyStats().getSurvivalPlayerSize())
                .addTag("server-name", Main.getInstance().getProxyStats().getRandomSurvivalGamesServer().getProxyName())
                .build());
        
        /*inventory.setItem(40, new ItemBuilder(Items.WORLDS.clone())
                //.addLoreLine("&7Mundos: &a" + Main.getInstance().getWorlds().getWorldsByUser(targetUserUUID).size())
                .addLoreLine("")
                .addLoreLine("&7Estado: " + (Main.getInstance().getProxyStats().world_1_12_online || Main.getInstance().getProxyStats().world_1_16_online || Main.getInstance().getProxyStats().world_1_18_online ? "&aACTIVO" : "&cCERRADO"))
                .addLoreLine("")
                .addLoreLine("&7Jugadores en linea: &a" + (Main.getInstance().getProxyStats().world_1_12_player_size + Main.getInstance().getProxyStats().world_1_16_player_size + Main.getInstance().getProxyStats().world_1_18_player_size))
                .build());*/
        
        inventory.setItem(53, new ItemBuilder(XMaterial.PLAYER_HEAD.parseMaterial())
                .setHeadSkin(user.getSkin())
                .setDisplayName("&b&lStats")
                .addLoreLine("")
                .addLoreLine("&aTiempo Jugado: " + userStats.getFormattedTimePlayed())
                .addLoreLine("&eLocation: " + (user.getLastLocation().getServer()))
                .addLoreLine("&e" + user.getLastLocation().getCurrentServerType() + ":" + (user.getLastLocation().getCurrentServerTypeFormatted()))
                .addTag("stats", "stats")
                .build());
        
        /*inventory.setItem(26, new ItemBuilder(XMaterial.NETHER_STAR.parseItem())
                .setDisplayName("&bLobby")
                .addTag("server-name", Main.getInstance().getProxyStats().getRandomLobbyServer().getProxyName())
                .build());*/
        inventory.setItem(45, super.CLOSE_ITEM);
        
    }
}
