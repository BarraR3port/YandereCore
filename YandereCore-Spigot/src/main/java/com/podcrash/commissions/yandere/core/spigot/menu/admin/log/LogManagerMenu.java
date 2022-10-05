package com.podcrash.commissions.yandere.core.spigot.menu.admin.log;

import com.cryptomorin.xseries.XMaterial;
import com.podcrash.commissions.yandere.core.common.data.logs.Log;
import com.podcrash.commissions.yandere.core.common.data.logs.LogType;
import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.spigot.Main;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.PaginatedMenu;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class LogManagerMenu extends PaginatedMenu<Log> {
    
    private final User user;
    private final int pageMultiplier = 10;
    private final LogMenuDisplayType type;
    private final int[] slots = {
            /*10, 11, 12, 13, 14, 15, 16,*/
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43};
    private LogType filter;
    
    public LogManagerMenu(IPlayerMenuUtility playerMenuUtility, User targetUser){
        super(playerMenuUtility, true);
        filter = LogType.UNKNOWN;
        this.user = targetUser;
        super.maxItemsPerPage = slots.length;
        type = LogMenuDisplayType.PLAYER;
        int[] borderSlots = {
                9, 10, 11, 12, 13, 14, 15, 16, 17,
                18, 26,
                27, 35,
                36, 44,
                45, 46, 47, 48, 49, 50, 51, 52, 53
        };
        setBorderSlots(borderSlots);
        setSize();
    }
    
    public LogManagerMenu(IPlayerMenuUtility playerMenuUtility){
        super(playerMenuUtility, true);
        filter = LogType.UNKNOWN;
        this.user = Main.getInstance().getPlayers().getCachedPlayer(getOwner().getUniqueId());
        super.maxItemsPerPage = slots.length;
        type = LogMenuDisplayType.PLAYER;
        int[] borderSlots = {
                9, 10, 11, 12, 13, 14, 15, 16, 17,
                18, 26,
                27, 35,
                36, 44,
                45, 46, 47, 48, 49, 50, 51, 52, 53
        };
        setBorderSlots(borderSlots);
        setSize();
    }
    
    @Override
    public String getMenuName(){
        return "  &4• &8Logs de: " + user.getName();
    }
    
    @Override
    public int getSlots(){
        return 54;
    }
    
    @Override
    public void setSize(){
        super.size = Main.getInstance().getLogs().getLogsByPageAndNameSize(index, maxItemsPerPage, user.getName());
    }
    
    @Override
    public void updateList(){
        switch(type){
            case ALL:{
                list = Main.getInstance().getLogs().getLogsByPage(index, maxItemsPerPage, filter);
                break;
            }
            case PLAYER:{
                list = Main.getInstance().getLogs().getLogsByPageAndName(index, user.getName(), maxItemsPerPage, filter);
                break;
            }
        }
    }
    
    @Override
    public void setMenuItemsAsync(){
        int currentPageIndex = 0;
        if (!list.isEmpty()){
            for ( int i = 0; i < super.maxItemsPerPage; i++ ){
                if (i >= list.size()) break;
                if (i >= slots.length) break;
                Log log = list.get(i);
                if (log != null){
                    currentPageIndex = (super.maxItemsPerPage * (Math.max(page - 1, 0)) + i);
                    ItemBuilder builder = new ItemBuilder(XMaterial.valueOf(log.getType().getMaterial()).parseItem());
                    inventory.setItem(slots[i], builder
                            .setDisplayName("&c" + log.getType().getName())
                            .addLoreLine("&f&lINFORMACIÓN:")
                            .addLoreLine("  &4• &8Mensaje: &f" + log.getMsg())
                            .addLoreLine("  &4• &8Tipo: &f" + log.getType().getColor() + log.getType().name())
                            .addLoreLine("  &4• &8Jugador: &f" + log.getOwner())
                            .addLoreLine("  &4• &8Server: &f" + log.getServer())
                            .addLoreLine("  &4• &8Creado: el &f" + log.getCreateDate())
                            .addLoreLine("  &4• &8ID: &8&o" + log.getUuid().toString().split("-")[0])
                            .addTag("uuid", log.getUuid().toString())
                            .build());
                }
            }
            getOwner().updateInventory();
        }
        index = currentPageIndex + index;
    }
    
    @Override
    public void setMenuItems(){
        int currentPage = page + 1;
        inventory.setItem(0, new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
                .setHeadSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGM0ZTQ0MWVhYzg4NGRlMzM0N2E4Nzc1YTA3YTY2YmJjNGM4MmEyNGVkMmQwY2ZlYjFhY2FmNmNlOTlkNTNiNiJ9fX0=")
                .setDisplayName("&f&lFILTROS ⇝ &a")
                .addLoreLine("")
                .addLoreLine("  &4• &8Mostrando logs: &f" + (type == LogMenuDisplayType.PLAYER ? "&f" + user.getName() : "&fTodos"))
                .addLoreLine((filter != LogType.UNKNOWN) ? "  &4• &8Filtro actual: &f" + filter.getColor() + filter.getName() : null)
                .addLoreLine("  &4• &8Página actual: &f" + currentPage)
                .addLoreLine("")
                .addTag("current-page", currentPage)
                .build());
        for ( LogType logType : LogType.values() ){
            inventory.addItem(new ItemBuilder(XMaterial.valueOf(logType.getMaterial()).parseItem())
                    .setDisplayName("&f&lFILTRO ⇝ " + logType.getColor() + logType.getName())
                    .addLoreLine("")
                    .addLoreLine((logType == filter || logType == LogType.UNKNOWN) ? "&aFiltro Activado" : null)
                    .addLoreLine((logType == filter || logType == LogType.UNKNOWN) ? "" : null)
                    .addLoreLine("&7Haz click para filtrar por este tipo de log.")
                    .addLoreLine("")
                    .addTag("filter", logType.name())
                    .addTag("current-page", currentPage)
                    .build());
        }
    }
    
    @Override
    public void setCustomMenuItems(){
        int currentPage = page + 1;
        super.NEXT_ITEM = new ItemBuilder(super.NEXT_ITEM.clone()).setDisplayName("&7Próxima página: &c" + (currentPage + 1)).addTag("current-page", currentPage).build();
        super.PREV_ITEM = new ItemBuilder(super.PREV_ITEM.clone()).setDisplayName("&7Página anterior: &c" + Math.max(currentPage - 1, 1)).addTag("current-page", currentPage).build();
        inventory.setItem(getSlots() - 7, new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
                .setHeadSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTE4YTJkZDViZWYwYjA3M2IxMzI3MWE3ZWViOWNmZWE3YWZlODU5M2M1N2E5MzgyMWU0MzE3NTU3MjQ2MTgxMiJ9fX0=")
                .setDisplayName("&7Retroceder 10 Páginas")
                .addTag("current-page", currentPage)
                .addTag("go", "back")
                .build());
        inventory.setItem(getSlots() - 3, new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
                .setHeadSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDk5ZjI4MzMyYmNjMzQ5ZjQyMDIzYzI5ZTZlNjQxZjRiMTBhNmIxZTQ4NzE4Y2FlNTU3NDY2ZDUxZWI5MjIifX19")
                .setDisplayName("&7Avanzar 10 Páginas")
                .addTag("current-page", currentPage)
                .addTag("go", "front")
                .build());
        super.addMenuBorder();
    }
    
    @Override
    public void addInteractiveItems(){
        inventory.setItem(getSlots() - 6, page == 0 ? FILLER_GLASS.clone() : PREV_ITEM.clone());
        inventory.setItem(getSlots() - 5, CLOSE_ITEM.clone());
        inventory.setItem(getSlots() - 4, list.size() > maxItemsPerPage ? NEXT_ITEM.clone() : FILLER_GLASS.clone());
    }
    
    @Override
    public void handleMenu(InventoryClickEvent e){
        final ItemStack item = e.getCurrentItem();
        NBTItem nbtItem = new NBTItem(item);
        if (nbtItem.hasTag("ly-menu-close")){
            //new AdminMenu(this.playerMenuUtility, user).open();
        } else if (nbtItem.hasTag("ly-menu-next")){
            nextPage();
            getOwner().updateInventory();
        } else if (nbtItem.hasTag("ly-menu-previous")){
            prevPage();
            getOwner().updateInventory();
        } else if (nbtItem.hasTag("filter")){
            LogType postFiler = LogType.valueOf(nbtItem.getString("filter"));
            if (postFiler == filter)
                return;
            filter = postFiler;
            page = 0;
            index = 0;
            reloadPage();
        }
    }
    
    private enum LogMenuDisplayType {
        PLAYER,
        ALL,
    }
    
}

