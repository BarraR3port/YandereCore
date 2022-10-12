package com.podcrash.commissions.yandere.core.spigot.menu.admin.log;

import com.cryptomorin.xseries.XMaterial;
import com.podcrash.commissions.yandere.core.common.YandereApi;
import com.podcrash.commissions.yandere.core.common.data.DBOrderType;
import com.podcrash.commissions.yandere.core.common.data.logs.Log;
import com.podcrash.commissions.yandere.core.common.data.logs.LogType;
import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.spigot.Main;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.Menu;
import net.lymarket.lyapi.spigot.menu.PaginatedMenu;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class LogManagerMenu extends PaginatedMenu<Log> {
    
    private final User user;
    private final LogMenuDisplayType type;
    private final int[] slots = {
            /*10, 11, 12, 13, 14, 15, 16,*/
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43};
    private DBOrderType orderType;
    private LogType filter;
    private int maxItems = 0;
    private boolean loading = true;
    private final Menu prevMenu;
    private boolean locked;
    
    
    public LogManagerMenu(IPlayerMenuUtility playerMenuUtility, User targetUser, Menu prevMenu){
        this(playerMenuUtility, targetUser, LogMenuDisplayType.PLAYER, DBOrderType.DATE_ASCENDING, LogType.UNKNOWN, false, prevMenu);
    }
    
    public LogManagerMenu(IPlayerMenuUtility playerMenuUtility, User targetUser){
        this(playerMenuUtility, targetUser, LogMenuDisplayType.PLAYER, DBOrderType.DATE_ASCENDING, LogType.UNKNOWN, false, null);
    }
    
    public LogManagerMenu(IPlayerMenuUtility playerMenuUtility, User targetUser, LogMenuDisplayType type, DBOrderType orderType, LogType filer, boolean locked, Menu prevMenu){
        super(playerMenuUtility, prevMenu != null);
        this.prevMenu = prevMenu;
        this.filter = filer;
        this.orderType = orderType;
        this.user = targetUser;
        super.maxItemsPerPage = slots.length;
        this.type = type;
        this.locked = locked;
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
        super(playerMenuUtility, false);
        filter = LogType.UNKNOWN;
        orderType = DBOrderType.DATE_ASCENDING;
        this.user = Main.getInstance().getPlayers().getCachedPlayer(getOwner().getUniqueId());
        super.maxItemsPerPage = slots.length;
        type = LogMenuDisplayType.ALL;
        this.prevMenu = null;
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
        return (type == LogMenuDisplayType.ALL ? " &4• &8Logs Generales:" : " &4• &8Logs de: " + user.getName());
    }
    
    @Override
    public int getSlots(){
        return 54;
    }
    
    @Override
    public void setSize(){
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            maxItems = type == LogMenuDisplayType.ALL ? Main.getInstance().getLogs().getLogsByType(filter).size() : Main.getInstance().getLogs().getLogsByType(user.getName(), filter).size();
            int currentPage = page + 1;
            int maxPages = (int) Math.ceil(((double) maxItems) / (double) maxItemsPerPage);
            int pagesLeft = maxPages - currentPage;
            int pagesRight = maxPages - pagesLeft;
            inventory.setItem(0, new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
                    .setHeadSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGM0ZTQ0MWVhYzg4NGRlMzM0N2E4Nzc1YTA3YTY2YmJjNGM4MmEyNGVkMmQwY2ZlYjFhY2FmNmNlOTlkNTNiNiJ9fX0=")
                    .setDisplayName("&f&lFILTROS ⇝ &a")
                    .addLoreLine("")
                    .addLoreLine(" &4• &7Mostrando logs: &f" + (type == LogMenuDisplayType.PLAYER ? "&f" + user.getName() : "&fTodos"))
                    .addLoreLine((filter != LogType.UNKNOWN) ? " &4• &7Filtro actual: &f" + filter.getColor() + filter.getName() : null)
                    .addLoreLine(" &4• &7Página actual: &f" + currentPage + "/" + maxPages)
                    .addLoreLine(" &4• &7Orden actual: &f" + orderType.getOrderName())
                    .addLoreLine("")
                    .addTag("current-page", currentPage)
                    .build());
            if (page != 0){
                inventory.setItem(getSlots() - 7, new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
                        .setHeadSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTE4YTJkZDViZWYwYjA3M2IxMzI3MWE3ZWViOWNmZWE3YWZlODU5M2M1N2E5MzgyMWU0MzE3NTU3MjQ2MTgxMiJ9fX0=")
                        .setDisplayName("&7Retroceder " + Math.min(pagesRight, 10) + " Páginas")
                        .addTag("current-page", currentPage)
                        .addTag("move-amount", Math.min(pagesRight, 10))
                        .addTag("go", "back")
                        .build());
            }
            if (list.size() > maxItemsPerPage){
                inventory.setItem(getSlots() - 3, new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
                        .setHeadSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDk5ZjI4MzMyYmNjMzQ5ZjQyMDIzYzI5ZTZlNjQxZjRiMTBhNmIxZTQ4NzE4Y2FlNTU3NDY2ZDUxZWI5MjIifX19=")
                        .setDisplayName("&7Avanzar " + Math.min(pagesLeft, 10) + " Páginas")
                        .addTag("current-page", currentPage)
                        .addTag("go", "forward")
                        .addTag("move-amount", Math.min(pagesLeft, 10))
                        .build());
            }
            super.size = type == LogMenuDisplayType.ALL ? Main.getInstance().getLogs().getLogsByPageSize(index, filter, maxItemsPerPage) : Main.getInstance().getLogs().getLogsByPageAndNameSize(index, filter, maxItemsPerPage, user.getName());
            int currentPageIndex = 0;
            if (!list.isEmpty()){
                for ( int i = 0; i < super.maxItemsPerPage; i++ ){
                    if (i >= list.size()) break;
                    if (i >= slots.length) break;
                    Log log = list.get(i);
                    if (log != null){
                        currentPageIndex = (super.maxItemsPerPage * (Math.max(page, 0)) + i);
                        XMaterial material = XMaterial.valueOf(log.getType().getMaterial());
                        if (log.getProperty("material") != null && filter == LogType.PUNISHMENT){
                            try {
                                material = XMaterial.valueOf(log.getProperty("material"));
                            } catch (IllegalArgumentException e) {
                                material = XMaterial.matchXMaterial(Material.valueOf(log.getProperty("material")));
                            }
                        }
                        log.removeProperty("material");
                        ItemBuilder builder = new ItemBuilder(material.parseItem());
                        builder
                                .setDisplayName("&c" + log.getType().getName())
                                .addLoreLine("&f&lINFORMACIÓN:")
                                .addLoreLine(" &4• &7Mensaje: &f" + log.getMsg())
                                .addLoreLine(" &4• &7Tipo: &f" + log.getType().getColor() + log.getType().name())
                                .addLoreLine(" &4• &7Jugador: &f" + log.getOwner())
                                .addLoreLine(" &4• &7Server: &f" + log.getServer())
                                .addLoreLine(" &4• &7Creado: el &f" + YandereApi.DATE_FORMAT.format(log.getCreateDate()))
                                .addLoreLine(" &4• &7ID: &7&o" + log.getUuid().toString().split("-")[0])
                                .addLoreLine(log.getProperties().size() > 0 ? "" : null)
                                .addLoreLine(log.getProperties().size() > 0 ? "&f&lPROPIEDADES:" : null)
                                .addTag("uuid", log.getUuid().toString());
    
                        for ( String s : log.getProperties().keySet() ){
                            builder.addLoreLine(s + log.getProperty(s));
                        }
                        //FOR DEBUG
                        /*builder
                                .addLoreLine("-------------------------------")
                                .addLoreLine(" &4• &7Page: &f" + (page))
                                .addLoreLine(" &4• &7Index: &f" + index)
                                .addLoreLine(" &4• &7CurrentIndex: &f" + (currentPageIndex))
                                .addLoreLine("-------------------------------");*/
                        inventory.setItem(slots[i], builder.build());
                    }
                }
                getOwner().updateInventory();
            }
            index = currentPageIndex;
            loading = false;
        });
        
        
    }
    
    @Override
    public void updateList(){
        list = type == LogMenuDisplayType.ALL ? Main.getInstance().getLogs().getLogsByPage(index, maxItemsPerPage, filter, orderType) : Main.getInstance().getLogs().getLogsByPageAndName(index, user.getName(), maxItemsPerPage, filter, orderType);
    }
    
    @Override
    public void setMenuItemsAsync(){
    }
    
    @Override
    public void setMenuItems(){
        int currentPage = page + 1;
        inventory.setItem(0, new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
                .setHeadSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGM0ZTQ0MWVhYzg4NGRlMzM0N2E4Nzc1YTA3YTY2YmJjNGM4MmEyNGVkMmQwY2ZlYjFhY2FmNmNlOTlkNTNiNiJ9fX0=")
                .setDisplayName("&f&lFILTROS ⇝ &a")
                .addLoreLine("")
                .addLoreLine(" &4• &7Mostrando logs: &f" + (type == LogMenuDisplayType.PLAYER ? "&f" + user.getName() : "&fTodos"))
                .addLoreLine((filter != LogType.UNKNOWN) ? " &4• &7Filtro actual: &f" + filter.getColor() + filter.getName() : null)
                .addLoreLine(" &4• &7Página actual: &f" + currentPage + "/...")
                .addLoreLine(" &4• &7Orden actual: &f" + orderType.getOrderName())
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
                    .setEnchanted(logType == filter)
                    .build());
        }
    }
    
    @Override
    public void setCustomMenuItems(){
        int currentPage = page + 1;
        super.NEXT_ITEM = new ItemBuilder(super.NEXT_ITEM.clone()).setDisplayName("&7Próxima página: &c" + (currentPage + 1)).addTag("current-page", currentPage).build();
        super.PREV_ITEM = new ItemBuilder(super.PREV_ITEM.clone()).setDisplayName("&7Página anterior: &c" + Math.max(currentPage - 1, 1)).addTag("current-page", currentPage).build();
        super.addMenuBorder();
        if (page != 0){
            inventory.setItem(getSlots() - 7, new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
                    .setHeadSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTE4YTJkZDViZWYwYjA3M2IxMzI3MWE3ZWViOWNmZWE3YWZlODU5M2M1N2E5MzgyMWU0MzE3NTU3MjQ2MTgxMiJ9fX0=")
                    .setDisplayName("&7Retroceder 10 Páginas")
                    .addTag("current-page", currentPage)
                    .addTag("go", "back")
                    .addTag("move-amount", 10)
                    .build());
            
        }
        if (list.size() > maxItemsPerPage){
            inventory.setItem(getSlots() - 3, new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
                    .setHeadSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDk5ZjI4MzMyYmNjMzQ5ZjQyMDIzYzI5ZTZlNjQxZjRiMTBhNmIxZTQ4NzE4Y2FlNTU3NDY2ZDUxZWI5MjIifX19=")
                    .setDisplayName("&7Avanzar 10 Páginas")
                    .addTag("current-page", currentPage)
                    .addTag("go", "forward")
                    .addTag("move-amount", 10)
                    .build());
        }
    
        if (locked){
            inventory.setItem(27, new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
                    .setHeadSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2E0ZmFiM2ZkOTdlYjdlY2Y0OGFiNGZkMzI3ZTA5M2U4ODZmNGUyMTdhYWI2OTU4NTMxM2MyN2E1MDM1ODMxYSJ9fX0=")
                    .setDisplayName("&eRe Cargar Página ⇝ &a")
                    .addLoreLine(" &4• &eClick para recargar la página.")
                    .addTag("re-charge", "re-charge")
                    .build());
        } else {
            inventory.setItem(18, new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
                    .setHeadSkin(orderType == DBOrderType.DATE_ASCENDING ? "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWRhMDI3NDc3MTk3YzZmZDdhZDMzMDE0NTQ2ZGUzOTJiNGE1MWM2MzRlYTY4YzhiN2JjYzAxMzFjODNlM2YifX19" : "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNjYmY5ODgzZGQzNTlmZGYyMzg1YzkwYTQ1OWQ3Mzc3NjUzODJlYzQxMTdiMDQ4OTVhYzRkYzRiNjBmYyJ9fX0=")
                    .setDisplayName("&eOrden por Fecha ⇝ &a")
                    .addLoreLine(" &4• &7Orden actual: &f" + orderType.getOrderName())
                    .addLoreLine(" &4• &eClick para cambiar a: " + DBOrderType.DATE_ASCENDING.getOrderName())
                    .addTag("order", DBOrderType.DATE_ASCENDING.name())
                    .setEnchanted(orderType == DBOrderType.DATE_ASCENDING)
                    .build());
        
            if (type.equals(LogMenuDisplayType.ALL)){
                inventory.setItem(27, new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
                        .setHeadSkin(
                                (orderType == DBOrderType.ALPHABETICAL_ASCENDING || orderType == DBOrderType.ALPHABETICAL_DESCENDING
                                        ? (orderType == DBOrderType.ALPHABETICAL_ASCENDING ?
                                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjBhZmQ3NzdkNTU3YTIwN2JhYzdhYWQ4NDIxZmRmNzg4ZDY2ODU4NzNjNDk1MTVkNTUyOTFlOTMwNjk5ZiJ9fX0=" :
                                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzYxNTdiOWMxMDEzMTI4MzhiMTI4ZTcxZDU1YmZjOWFkNGUyNmI1OTE5YTczODA3YjEzNDY2YTc2ZDVlMCJ9fX0=")
                                        : "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTdkZDM0OTI0ZDJiNmEyMTNhNWVkNDZhZTU3ODNmOTUzNzNhOWVmNWNlNWM4OGY5ZDczNjcwNTk4M2I5NyJ9fX0="))
                    
                    
                        .setDisplayName("&eOrden Alfabético ⇝ &a")
                        .addLoreLine(" &4• &7Orden actual: &f" + orderType.getOrderName())
                        .addLoreLine(" &4• &eSiguiente Orden: " + (orderType == DBOrderType.ALPHABETICAL_DESCENDING ? DBOrderType.DATE_ASCENDING.getOrderName() : DBOrderType.ALPHABETICAL_DESCENDING.getOrderName()))
                        .addLoreLine(" &4• &eClick para cambiar el orden.")
                        .addTag("order", orderType == DBOrderType.ALPHABETICAL_DESCENDING ? DBOrderType.ALPHABETICAL_ASCENDING.name() : DBOrderType.ALPHABETICAL_DESCENDING.name())
                        .setEnchanted(orderType == DBOrderType.ALPHABETICAL_DESCENDING || orderType == DBOrderType.ALPHABETICAL_ASCENDING)
                        .build());
            }
        
            inventory.setItem(36, new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
                    .setHeadSkin(orderType == DBOrderType.DATE_DESCENDING ? "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmY3NDE2Y2U5ZTgyNmU0ODk5YjI4NGJiMGFiOTQ4NDNhOGY3NTg2ZTUyYjcxZmMzMTI1ZTAyODZmOTI2YSJ9fX0=" : "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzI0MzE5MTFmNDE3OGI0ZDJiNDEzYWE3ZjVjNzhhZTQ0NDdmZTkyNDY5NDNjMzFkZjMxMTYzYzBlMDQzZTBkNiJ9fX0=")
                    .setDisplayName("&eOrden por Fecha ⇝ &a")
                    .addLoreLine(" &4• &7Orden actual: &f" + orderType.getOrderName())
                    .addLoreLine(" &4• &eClick para cambiar a: " + DBOrderType.DATE_DESCENDING.getOrderName())
                    .addTag("order", DBOrderType.DATE_DESCENDING.name())
                    .setEnchanted(orderType == DBOrderType.DATE_DESCENDING)
                    .build());
        }
    
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
            if (this.isLinked()){
                if (prevMenu != null){
                    prevMenu.open();
                }
            } else {
                getOwner().closeInventory();
            }
            //new AdminMenu(this.playerMenuUtility, user).open();
        }
        if (loading){
            checkSomething(getOwner(), e.getSlot(), item, "&cEspera un poco, se está cargando la página.", "", getMenuUUID());
            e.setCancelled(true);
            return;
        }
        if (nbtItem.hasTag("go")){
            String type = nbtItem.getString("go");
            if (type.equals("back")){
                int amount = nbtItem.getInteger("move-amount");
                loading = true;
                page = Math.max((page) - amount, 0);
                index = Math.max(super.maxItemsPerPage * page, 0);
                reloadPage();
            } else if (type.equals("forward")){
                int amount = nbtItem.getInteger("move-amount");
                int maxPages = maxItems / maxItemsPerPage;
                loading = true;
                page = Math.min((page) + amount, maxPages);
                index = Math.min(super.maxItemsPerPage * page, maxPages * maxItemsPerPage);
                reloadPage();
            }
        }
        if (locked){
            if (nbtItem.hasTag("re-charge")){
                loading = true;
                reloadPage();
            }
        } else {
            if (nbtItem.hasTag("ly-menu-next")){
                loading = true;
                nextPage();
                getOwner().updateInventory();
            } else if (nbtItem.hasTag("ly-menu-previous")){
                loading = true;
                prevPage();
                getOwner().updateInventory();
            } else if (nbtItem.hasTag("filter")){
                LogType postFiler = LogType.valueOf(nbtItem.getString("filter"));
                if (postFiler == filter)
                    return;
                loading = true;
                filter = postFiler;
                page = 0;
                index = 0;
                reloadPage();
            } else if (nbtItem.hasTag("order")){
                DBOrderType order = DBOrderType.valueOf(nbtItem.getString("order"));
                if (order == orderType)
                    return;
                loading = true;
                orderType = order;
                page = 0;
                index = 0;
                reloadPage();
            }
        
        }
    }
    
    public enum LogMenuDisplayType {
        PLAYER,
        ALL,
    }
    
}

