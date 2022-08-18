package com.podcrash.commissions.yandere.core.spigot.menu.admin.rank;

import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.common.data.user.props.Rank;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.items.Items;
import com.podcrash.commissions.yandere.core.spigot.menu.admin.AdminMenu;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.UpdatableMenu;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class RankEditor extends UpdatableMenu {
    
    private User user;
    
    public RankEditor(IPlayerMenuUtility playerMenuUtility, User user){
        super(playerMenuUtility);
        this.user = user;
    }
    
    @Override
    public String getMenuName(){
        return "Cambia el rango de: " + user.getName();
    }
    
    @Override
    public int getSlots(){
        return 27;
    }
    
    @Override
    public void setMenuItems(){
        final Rank userRank = user.getRank();
        int slot = 9;
        for ( Rank rank : Rank.values() ){
            inventory.setItem(slot, new ItemBuilder(Items.RANK_BASE.clone()).setDyeColor(slot - 9)
                    .setDisplayName(rank.getScoreBoardName())
                    .setEnchanted(userRank == rank)
                    .addTag("rank", rank.toString())
                    .build());
            slot++;
        }
        inventory.setItem(18, this.CLOSE_ITEM);
        
    }
    
    @Override
    public void handleMenu(InventoryClickEvent e){
        final ItemStack item = e.getCurrentItem();
        
        if (NBTItem.hasTag(item, "rank")){
            final Rank rank = Rank.valueOf(NBTItem.getTag(item, "rank"));
            if (user.getRank() != rank){
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + user.getName() + " group set " + rank.getLpName());
                Main.getInstance().getPlayers().setPlayerRank(user, rank);
                reOpen();
            } else {
                this.checkSomething(getOwner(), e.getSlot(), item, "&cEste usuario ya tiene este rango", "", getMenuUUID());
            }
        } else if (NBTItem.hasTag(item, "ly-menu-close")){
            new AdminMenu(this.playerMenuUtility, user).open();
        }
    }
    
    @Override
    public void onReOpen(){
        this.user = Main.getInstance().getPlayers().getLocalStoredPlayer(user.getUUID());
    }
    
    @Override
    public void reOpen(){
        this.onReOpen();
        this.setMenuItems();
    }
}
