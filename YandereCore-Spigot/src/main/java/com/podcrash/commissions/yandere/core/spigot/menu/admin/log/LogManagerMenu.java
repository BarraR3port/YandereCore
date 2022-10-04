package com.podcrash.commissions.yandere.core.spigot.menu.admin.log;

import com.cryptomorin.xseries.XMaterial;
import com.podcrash.commissions.yandere.core.common.data.logs.Log;
import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.spigot.Main;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.PaginatedMenu;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class LogManagerMenu extends PaginatedMenu<Log> {
    
    private final User user;
    private final int pageMultiplier = 10;
    private final LogMenuDisplayType type;
    private final int[] slots = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43};
    
    public LogManagerMenu(IPlayerMenuUtility playerMenuUtility, UUID uuid){
        super(playerMenuUtility);
        this.user = Main.getInstance().getPlayers().getCachedPlayer(uuid);
        this.list = Main.getInstance().getLogs().getLogsByPage(index * pageMultiplier);
        setSize();
        super.maxItemsPerPage = slots.length;
        type = LogMenuDisplayType.PLAYER;
    }
    
    public LogManagerMenu(IPlayerMenuUtility playerMenuUtility){
        super(playerMenuUtility);
        this.user = Main.getInstance().getPlayers().getCachedPlayer(getOwner().getUniqueId());
        super.maxItemsPerPage = slots.length;
        type = LogMenuDisplayType.PLAYER;
        setSize();
    }
    
    @Override
    public String getMenuName(){
        return "&4• &8Logs de: " + user.getName();
    }
    
    @Override
    public int getSlots(){
        return 54;
    }
    
    @Override
    public void setSize(){
        list.clear();
        setLogs();
        super.size = list.size();
    }
    
    private void setLogs(){
        switch(type){
            case ALL:{
                list = Main.getInstance().getLogs().getLogsByPage(index);
                break;
            }
            case PLAYER:{
                list = Main.getInstance().getLogs().getLogsByPageAndName(index, user.getName());
                break;
            }
        }
    }
    
    @Override
    public void setMenuItems(){
        super.NEXT_ITEM = new ItemBuilder(super.NEXT_ITEM.clone()).setDisplayName("&7Próxima página: &c" + (page + 2)).build();
        super.PREV_ITEM = new ItemBuilder(super.PREV_ITEM.clone()).setDisplayName("&7Página anterior: &c" + (page - 2)).build();
        addMenuBorder();
        if (!list.isEmpty()){
            for ( int i = 0; i < super.maxItemsPerPage; i++ ){
                if (i >= list.size()) break;
                if (i >= slots.length) break;
                Log log = list.get(i);
                if (log != null){
                    index = super.maxItemsPerPage * (Math.max(page - 1, 0)) + i;
                    XMaterial material;
                    switch(log.getType()){
                        case PUNISHMENT:
                            material = XMaterial.BARRIER;
                            break;
                        case COMMAND:
                            material = XMaterial.COMMAND_BLOCK;
                            break;
                        case OTHER:
                            material = XMaterial.COMPASS;
                            break;
                        case ERROR:
                            material = XMaterial.REDSTONE_BLOCK;
                            break;
                        case CHAT:
                            material = XMaterial.PAPER;
                            break;
                        case TP:
                            material = XMaterial.ENDER_PEARL;
                            break;
                        case EVENT:
                            material = XMaterial.CLOCK;
                            break;
                        default:
                            material = XMaterial.PAINTING;
                            break;
                    }
                    
                    inventory.setItem(slots[i], new ItemBuilder(material.parseItem())
                            .setDisplayName("&fLog de: " + log.getOwner())
                            .addLoreLine("&4• &7Tipo: &7" + log.getType().name())
                            .addLoreLine("&4• &7Mensaje: &f" + log.getMsg())
                            .addLoreLine("&4• &7Server: &f" + log.getServer())
                            .addLoreLine("&4• &7Creado: el &f" + log.getCreateDate())
                            .addLoreLine("&4• &7ID: &7&o" + log.getUuid().toString().split("-")[0])
                            .addTag("uuid", log.getUuid().toString())
                            .build());
                }
            }
        }
    }
    
    @Override
    public void handleMenu(InventoryClickEvent e){
        final ItemStack item = e.getCurrentItem();
        NBTItem nbtItem = new NBTItem(item);
        if (nbtItem.hasTag("ly-menu-close")){
            //new AdminMenu(this.playerMenuUtility, user).open();
        } else if (nbtItem.hasTag("ly-menu-next")){
            nextPage();
        } else if (nbtItem.hasTag("ly-menu-previous")){
            prevPage();
        }
    }
    
    private enum LogMenuDisplayType {
        PLAYER,
        ALL,
    }
    
}

