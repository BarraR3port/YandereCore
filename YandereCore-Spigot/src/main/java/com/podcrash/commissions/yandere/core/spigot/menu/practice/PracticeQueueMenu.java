package com.podcrash.commissions.yandere.core.spigot.menu.practice;


import com.podcrash.commissions.yandere.core.common.data.practice.PracticeQueueType;
import ga.strikepractice.StrikePractice;
import ga.strikepractice.api.StrikePracticeAPI;
import ga.strikepractice.battlekit.BattleKit;
import ga.strikepractice.battlekit.BattleKitType;
import ga.strikepractice.fights.AbstractFight;
import ga.strikepractice.fights.Fight;
import ga.strikepractice.fights.botduel.BotDuel;
import ga.strikepractice.fights.duel.Duel;
import net.lymarket.lyapi.spigot.menu.IPlayerMenuUtility;
import net.lymarket.lyapi.spigot.menu.UpdatableMenu;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PracticeQueueMenu extends UpdatableMenu {
    
    private final PracticeQueueType type;
    private final StrikePracticeAPI api;
    private final int[] slots = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43};
    
    public PracticeQueueMenu(IPlayerMenuUtility playerMenuUtility, PracticeQueueType type){
        super(playerMenuUtility, true);
        this.type = type;
        api = StrikePractice.getAPI();
    }
    
    @Override
    public String getMenuName(){
        return "&4• " + (type == PracticeQueueType.EDIT_KIT ? "" : "&8Cola ") + type.getDisplayName();
    }
    
    @Override
    public int getSlots(){
        return 54;
    }
    
    @Override
    public void handleMenu(InventoryClickEvent e){
        final ItemStack item = e.getCurrentItem();
        Player p = (Player) e.getWhoClicked();
        if (NBTItem.hasTag(item, "kit-name")){
            BattleKit kit = api.getKit(NBTItem.getTag(item, "kit-name"));
            if (kit == null) return;
            switch(type){
                case UNRANKED:
                case RANKED:{
                    api.joinQueue(p, kit);
                    break;
                }
                case PREMIUM:{
                    api.joinPremiumQueue(p, kit);
                    break;
                }
                case BOT:{
                    BotDuel botDuel = new BotDuel(StrikePractice.getInstance(), p.getName(), kit);
                    new ArenaSelector(playerMenuUtility, api, kit, type, botDuel).open();
                    break;
                }
                case EDIT_KIT:{
                    p.chat("/kiteditor " + kit.getName());
                    break;
                }
            }
        } else if (NBTItem.hasTag(item, "ly-menu-close")){
            new PracticeMenu(playerMenuUtility).open();
        }
    }
    
    @Override
    public void setMenuItems(){
        List<BattleKit> kits = new ArrayList<>();
        switch(type){
            case BOT:
            case UNRANKED:{
                kits = new ArrayList<>(api.getKits()).stream().filter(kit -> !kit.isElo() && kit.getKitTypes().contains(BattleKitType.ANY)).collect(Collectors.toList());
                break;
            }
            case EDIT_KIT:{
                kits = new ArrayList<>();
                List<String> arenasAdded = new ArrayList<>();
                List<String> arenasAddedDisplay = new ArrayList<>();
                for ( BattleKit kit : api.getKits() ){
                    if (arenasAdded.contains(kit.getName())) continue;
                    if (arenasAddedDisplay.contains(kit.getFancyName())) continue;
                    if (kit.isEditable()){
                        kits.add(kit);
                        arenasAdded.add(kit.getName());
                        arenasAddedDisplay.add(kit.getFancyName());
                    }
                }
                break;
            }
            case RANKED:{
                kits = new ArrayList<>(api.getKits()).stream().filter(kit -> kit.isElo() && kit.getKitTypes().contains(BattleKitType.ANY)).collect(Collectors.toList());
                break;
            }
            case PREMIUM:
                kits = new ArrayList<>(api.getKits()).stream().filter(kit -> kit.getKitTypes().contains(BattleKitType.PREMIUM_QUEUE)).collect(Collectors.toList());
                break;
        }
        
        
        for ( int i = 0; i < kits.size() && i < slots.length; i++ ){
            BattleKit kit = kits.get(i);
            int playing = 0;
            int queue = 0;
            switch(type){
                case UNRANKED:{
                    queue = Math.toIntExact(api.getUnrankedQueue().keySet().stream().filter(kit2 -> kit2.getName().equals(kit.getName())).count());
                    for ( Player player : Bukkit.getOnlinePlayers() ){
                        Fight currentFight = AbstractFight.getCurrentFight(player);
                        if (currentFight == null) continue;
                        if (!currentFight.getKit().isElo() && currentFight.getKit().getName().equals(kit.getName())){
                            playing++;
                        }
                    }
                    break;
                }
                case RANKED:{
                    queue = Math.toIntExact(api.getRankedQueue().keySet().stream().filter(kit2 -> kit2.getName().equals(kit.getName())).count());
                    for ( Player player : Bukkit.getOnlinePlayers() ){
                        Fight currentFight = AbstractFight.getCurrentFight(player);
                        if (currentFight == null) continue;
                        if (currentFight.getKit().isElo() && currentFight.getKit().getName().equals(kit.getName())){
                            playing++;
                        }
                    }
                    break;
                }
                case PREMIUM:{
                    queue = Math.toIntExact(api.getRankedQueue().keySet().stream().filter(kit2 -> kit2.getName().equals(kit.getName())).count());
                    for ( Player player : Bukkit.getOnlinePlayers() ){
                        Fight currentFight = AbstractFight.getCurrentFight(player);
                        if (currentFight == null) continue;
                        if (currentFight.getKit().isElo() &&
                                currentFight.getKit().getName().equals(kit.getName()) &&
                                currentFight instanceof Duel &&
                                ((Duel) currentFight).isPremiumQueue()){
                            playing++;
                        }
                    }
                    break;
                }
                case BOT:
                    inventory.setItem(slots[i], new ItemBuilder(kit.getIcon().clone())
                            .setDisplayName(kit.getFancyName())
                            .addLoreLine("")
                            .addLoreLine("&7Click para jugar contra un Bot")
                            .addTag("kit-name", kit.getName())
                            .build());
                    continue;
                case EDIT_KIT:{
                    inventory.setItem(slots[i], new ItemBuilder(kit.getIcon().clone())
                            .setDisplayName(kit.getFancyName())
                            .addLoreLine("")
                            .addLoreLine("&7Click para editar este kit.")
                            .addTag("kit-name", kit.getName())
                            .build());
                    continue;
                }
            }
            
            inventory.setItem(slots[i], new ItemBuilder(kit.getIcon().clone())
                    .setAmount(queue)
                    .setDisplayName(kit.getFancyName())
                    .addLoreLine("")
                    .addLoreLine(" &a⋆ &fEn cola: &c" + queue)
                    .addLoreLine(" &a⋆ &fJugando: &c" + playing)
                    .addLoreLine("")
                    .addLoreLine("&7Click para unirte a la cola!")
                    .addTag("kit-name", kit.getName())
                    .build());
            
        }
        
        inventory.setItem(49, CLOSE_ITEM);
        
        
    }
}
