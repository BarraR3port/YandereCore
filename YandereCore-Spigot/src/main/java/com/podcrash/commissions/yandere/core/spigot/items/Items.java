package com.podcrash.commissions.yandere.core.spigot.items;

import com.cryptomorin.xseries.XMaterial;
import com.podcrash.commissions.yandere.core.common.data.lobby.JoinBedWarsArenaType;
import com.podcrash.commissions.yandere.core.common.data.lobby.JoinSkyWarsArenaType;
import com.podcrash.commissions.yandere.core.common.data.lobby.PlayerVisibility;
import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.items.lobby.LobbyItem;
import net.lymarket.lyapi.spigot.config.Config;
import net.lymarket.lyapi.spigot.utils.ItemBuilder;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public final class Items {
    
    public static LobbyItem LOBBY_MENU;
    public static LobbyItem MULTI_LOBBY_MENU;
    public static LobbyItem MULTI_LOBBY_MENU_HOT_BAR;
    public static LobbyItem LOBBY_PLAYER_VISIBILITY_ALL;
    public static LobbyItem LOBBY_PLAYER_VISIBILITY_RANKS;
    public static LobbyItem LOBBY_PLAYER_VISIBILITY_NONE;
    public static LobbyItem LOBBY_JOIN_ARENA_SKYWARS_SOLO;
    public static LobbyItem LOBBY_JOIN_ARENA_SKYWARS_TEAM;
    public static LobbyItem LOBBY_JOIN_ARENA_SKYWARS_RANKED;
    public static LobbyItem LOBBY_JOIN_ARENA_SKYWARS_RANDOM;
    public static LobbyItem LOBBY_JOIN_ARENA_BEDWARS_SOLO;
    public static LobbyItem LOBBY_JOIN_ARENA_BEDWARS_DUO;
    public static LobbyItem LOBBY_JOIN_ARENA_BEDWARS_3V3V3V3;
    public static LobbyItem LOBBY_JOIN_ARENA_BEDWARS_4V4V4V4;
    public static LobbyItem LOBBY_JOIN_ARENA_BEDWARS_RANDOM;
    public static ItemStack SKY_WARS_BASE;
    public static ItemStack BED_WARS_BASE;
    public static ItemStack PRACTICE_BASE;
    public static ItemStack SURVIVAL;
    public static ItemStack TNT_TAG;
    public static ItemStack AVAILABLE_WORLD;
    public static ItemStack UNAVAILABLE_WORLD;
    public static ItemStack CREATED_WORLD_BASE;
    public static ItemStack PLAYERS_IN_WORLD_BASE;
    public static ItemStack PLOT_31_BASE;
    public static ItemStack PLOT_101_BASE;
    public static ItemStack PLOT_501_BASE;
    public static ItemStack PLOT_1001_BASE;
    public static ItemStack WARP_CASAS_BASE;
    public static ItemStack WARP_ARBOLES_BASE;
    public static ItemStack WARP_VARIOS_BASE;
    public static ItemStack EMPTY_ITEM;
    public static ItemStack GREEN_PANEL;
    public static ItemStack VANISH_OFF;
    public static ItemStack VANISH_ON;
    public static ItemStack RANK_BASE;
    private static ItemStack FOOD;
    private static Config config;
    
    public static void init(Config itemsConfig){
        config = itemsConfig;
        LOBBY_MENU = new LobbyItem(itemsConfig.getItem("lobby-item"), itemsConfig.getInt("items.lobby-item.slot"));
        LOBBY_PLAYER_VISIBILITY_ALL = new LobbyItem(itemsConfig.getItem("lobby-player-visibility-all-item"), itemsConfig.getInt("items.lobby-player-visibility-all-item.slot"));
        LOBBY_PLAYER_VISIBILITY_RANKS = new LobbyItem(itemsConfig.getItem("lobby-player-visibility-ranks-item"), itemsConfig.getInt("items.lobby-player-visibility-ranks-item.slot"));
        LOBBY_PLAYER_VISIBILITY_NONE = new LobbyItem(itemsConfig.getItem("lobby-player-visibility-none-item"), itemsConfig.getInt("items.lobby-player-visibility-none-item.slot"));
        LOBBY_JOIN_ARENA_SKYWARS_SOLO = new LobbyItem(itemsConfig.getItem("lobby-player-join-arena-skywars-solo"), itemsConfig.getInt("items.lobby-player-join-arena-skywars-solo.slot"));
        LOBBY_JOIN_ARENA_SKYWARS_TEAM = new LobbyItem(itemsConfig.getItem("lobby-player-join-arena-skywars-team"), itemsConfig.getInt("items.lobby-player-join-arena-skywars-team.slot"));
        LOBBY_JOIN_ARENA_SKYWARS_RANKED = new LobbyItem(itemsConfig.getItem("lobby-player-join-arena-skywars-ranked"), itemsConfig.getInt("items.lobby-player-join-arena-skywars-ranked.slot"));
        LOBBY_JOIN_ARENA_SKYWARS_RANDOM = new LobbyItem(itemsConfig.getItem("lobby-player-join-arena-skywars-random"), itemsConfig.getInt("items.lobby-player-join-arena-skywars-random.slot"));
        
        LOBBY_JOIN_ARENA_BEDWARS_SOLO = new LobbyItem(itemsConfig.getItem("lobby-player-join-arena-bedwars-solo"), itemsConfig.getInt("items.lobby-player-join-arena-bedwars-solo.slot"));
        LOBBY_JOIN_ARENA_BEDWARS_DUO = new LobbyItem(itemsConfig.getItem("lobby-player-join-arena-bedwars-duo"), itemsConfig.getInt("items.lobby-player-join-arena-bedwars-duo.slot"));
        LOBBY_JOIN_ARENA_BEDWARS_3V3V3V3 = new LobbyItem(itemsConfig.getItem("lobby-player-join-arena-bedwars-3v3v3v3"), itemsConfig.getInt("items.lobby-player-join-arena-bedwars-3v3v3v3.slot"));
        LOBBY_JOIN_ARENA_BEDWARS_4V4V4V4 = new LobbyItem(itemsConfig.getItem("lobby-player-join-arena-bedwars-4v4v4v4"), itemsConfig.getInt("items.lobby-player-join-arena-bedwars-4v4v4v4.slot"));
        LOBBY_JOIN_ARENA_BEDWARS_RANDOM = new LobbyItem(itemsConfig.getItem("lobby-player-join-arena-bedwars-random"), itemsConfig.getInt("items.lobby-player-join-arena-bedwars-random.slot"));
        
        MULTI_LOBBY_MENU = new LobbyItem(itemsConfig.getItem("lobby-multi-lobby-item"), itemsConfig.getInt("items.lobby-multi-lobby-item.slot"));
        MULTI_LOBBY_MENU_HOT_BAR = new LobbyItem(itemsConfig.getItem("lobby-multi-lobby-item-hotbar"), itemsConfig.getInt("items.lobby-multi-lobby-item-hotbar.slot"));
        SKY_WARS_BASE = new ItemBuilder(Material.SKULL_ITEM)
                .setHeadSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTNkNTkxNTUzNzhiNGFjNGQyYjE0MmUyZjIzNWQwMzdmNjhhOWI4ZTI0YWU5ZWQ0ODU3MzE2YjI4ZGNlMDU2ZiJ9fX0=")
                .setDisplayName("&b&lSkyWars")
                .addLoreLine("&7Entra a jugar el Clásico modo de juego")
                .addLoreLine("&7sólo o con amigos.")
                .addLoreLine("")
                .build();
        BED_WARS_BASE = new ItemBuilder(Material.SKULL_ITEM)
                .setHeadSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZiMjkwYTEzZGY4ODI2N2VhNWY1ZmNmNzk2YjYxNTdmZjY0Y2NlZTVjZDM5ZDQ2OTcyNDU5MWJhYmVlZDFmNiJ9fX0=")
                .setDisplayName("&b&lBedWars")
                .addLoreLine("&7Entra a jugar el Clásico modo de juego")
                .addLoreLine("&7sólo o con amigos.")
                .addLoreLine("")
                .build();
        PRACTICE_BASE = new ItemBuilder(Material.SKULL_ITEM)
                .setHeadSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWI3NzE3ZDlkMzNkNjlhZjg1NWQ4NGNmY2ExODExMTZhNzI1YzE5MTVmMDBmNTE0NzE2ZDliYmIwZWFlYTZiZiJ9fX0=")
                .setDisplayName("&b&lPractice PVP")
                .addLoreLine("&7Entra a jugar el Clásico modo de juego")
                .addLoreLine("&7sólo o con amigos.")
                .addLoreLine("")
                .build();
        SURVIVAL = new ItemBuilder(Material.SKULL_ITEM)
                .setHeadSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjg3ZjM2ODZjYTJjYzIyMTg1YzQ1NGYzZjk2Zjg2NzNlMDM0ODE0OTVhZDgzNjIzNjEyZGU1MTdhYmNkZWI2ZCJ9fX0=")
                .setDisplayName("&b&lSurvival")
                .addLoreLine("&7Entra a jugar el Clásico modo de juego")
                .addLoreLine("&7sólo o con amigos.")
                .addLoreLine("")
                .build();
        TNT_TAG = new ItemBuilder(Material.SKULL_ITEM)
                .setHeadSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2FmNTk3NzZmMmYwMzQxMmM3YjU5NDdhNjNhMGNmMjgzZDUxZmU2NWFjNmRmN2YyZjg4MmUwODM0NDU2NWU5In19fQ==")
                .setDisplayName("&4&lTNT Tag")
                .addLoreLine("&7Entra a jugar el Clásico modo de juego")
                .addLoreLine("&7sólo o con amigos.")
                .addLoreLine("")
                .build();
        AVAILABLE_WORLD = new ItemBuilder(XMaterial.LIME_STAINED_GLASS_PANE.parseMaterial(), 5)
                .setDisplayName("&7Mundo &aDisponible")
                .addLoreLine("&7Click para configurar un mundo.")
                .addTag("world-available", "world-available")
                .build();
        UNAVAILABLE_WORLD = new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseMaterial(), 14)
                .setDisplayName("&7Mundo &cno disponible")
                .addLoreLine("&7Necesitas ser un rango más alto")
                .addLoreLine("&7para acceder a este slot.")
                .addTag("world-unavailable", "world-unavailable")
                .build();
        CREATED_WORLD_BASE = new ItemBuilder(XMaterial.PLAYER_HEAD.parseMaterial())
                //.setHeadSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjgwZDMyOTVkM2Q5YWJkNjI3NzZhYmNiOGRhNzU2ZjI5OGE1NDVmZWU5NDk4YzRmNjlhMWMyYzc4NTI0YzgyNCJ9fX0=").build();
                .setHeadSkin("ewogICJ0aW1lc3RhbXAiIDogMTU5NDUxNTc5OTE5OCwKICAicHJvZmlsZUlkIiA6ICJhYTZhNDA5NjU4YTk0MDIwYmU3OGQwN2JkMzVlNTg5MyIsCiAgInByb2ZpbGVOYW1lIiA6ICJiejE0IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzYzZjRkNTU1ZmIzYjkzNTdhZjc1ODJmMjczODAwZjFkYTRhYzY5YjA0MjEwYzNhYTM0ZTYzZGIyYTQyMzViYmYiCiAgICB9CiAgfQp9").build();
        PLAYERS_IN_WORLD_BASE = new ItemBuilder(XMaterial.PLAYER_HEAD.parseMaterial())
                //.setHeadSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzBjZjZjZGMxMWRiNzRjMGQ3N2JhMzc1NmM2ZmRlMzQ1ZmU1NDQzZWNmN2VhNGE0MWQxNjI1NGU2NTk1ODRjZiJ9fX0").build();
                .setHeadSkin("ewogICJ0aW1lc3RhbXAiIDogMTU5NDUxNTc5OTE5OCwKICAicHJvZmlsZUlkIiA6ICJhYTZhNDA5NjU4YTk0MDIwYmU3OGQwN2JkMzVlNTg5MyIsCiAgInByb2ZpbGVOYW1lIiA6ICJiejE0IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzYzZjRkNTU1ZmIzYjkzNTdhZjc1ODJmMjczODAwZjFkYTRhYzY5YjA0MjEwYzNhYTM0ZTYzZGIyYTQyMzViYmYiCiAgICB9CiAgfQp9").build();
        PLOT_31_BASE = new ItemBuilder(XMaterial.WHITE_WOOL.parseMaterial(), 0)
                .setDisplayName("&7Plots &a31")
                .addLoreLine("&7Click para entrar a este plot.")
                .addLoreLine("&7Tamaño: &a31x31")
                .addTag("plot-type", "P31")
                .build();
        PLOT_101_BASE = new ItemBuilder(XMaterial.LIGHT_GRAY_WOOL.parseMaterial(), 8)
                .setDisplayName("&7Plots &a101")
                .addLoreLine("&7Click para entrar a este plot.")
                .addLoreLine("&7Tamaño: &a101x101")
                .addTag("plot-type", "P101")
                .build();
        PLOT_501_BASE = new ItemBuilder(XMaterial.GRAY_WOOL.parseMaterial(), 7)
                .setDisplayName("&7Plots &a501x501")
                .addLoreLine("&7Click para entrar a este plot.")
                .addLoreLine("&7Tamaño: &a501x501")
                .addTag("plot-type", "P501")
                .build();
        PLOT_1001_BASE = new ItemBuilder(XMaterial.BLACK_WOOL.parseMaterial(), 15)
                .setDisplayName("&7Plots &a1001x1001")
                .addLoreLine("&7Click para entrar a este plot.")
                .addLoreLine("&7Tamaño: &a1001x1001")
                .addTag("plot-type", "P1001")
                .build();
        RANK_BASE = new ItemBuilder(XMaterial.WHITE_WOOL.parseMaterial())
                .addLoreLine("&7Click poner este rango al jugador.")
                .build();
        WARP_CASAS_BASE = new ItemBuilder(XMaterial.BRICKS.parseItem())
                .setDisplayName("&aCasas")
                .addLoreLine("&7Click para ir a la warp de Casas.")
                .addTag("warp", "CASAS")
                .build();
        WARP_ARBOLES_BASE = new ItemBuilder(XMaterial.JUNGLE_SAPLING.parseItem())
                .setDisplayName("&aArboles")
                .addLoreLine("&7Click para ir a la warp de Arboles")
                .addTag("warp", "ARBOLES")
                .build();
        WARP_VARIOS_BASE = new ItemBuilder(XMaterial.CHEST.parseItem())
                .setDisplayName("&aVarios")
                .addLoreLine("&7Click para ir a las otras warps")
                .addTag("warp", "VARIOS")
                .build();
        FOOD = new ItemBuilder(new ItemStack(XMaterial.COOKED_BEEF.parseMaterial()))
                .build();
        EMPTY_ITEM = new ItemBuilder(new ItemStack(Material.BARRIER))
                .setDisplayName(" ")
                .build();
        GREEN_PANEL = new ItemBuilder(Material.STAINED_GLASS_PANE, 5).setDisplayName(" ").build();
        VANISH_OFF = new ItemBuilder(new ItemStack(XMaterial.GRAY_DYE.parseMaterial()))
                .setDisplayName("&7Vanish &cDesactivado")
                .addLoreLine("&cStaff Utils")
                .addTag("staff-utils", "vanish-off").build();
        VANISH_ON = new ItemBuilder(new ItemStack(XMaterial.LIME_DYE.parseMaterial()))
                .setDisplayName("&7Vanish &aActivado")
                .addLoreLine("&cStaff Utils")
                .addTag("staff-utils", "vanish-on").build();
        
    }
    
    
    public static ItemStack food(Player target){
        return new ItemBuilder(FOOD)
                .setDisplayName("&a" + target.getName() + "'s &7Health Stats:")
                .addLoreLine("&a► &aVida: &c" + (int) target.getHealth() + "&l" + "\u2764")
                .addLoreLine("&a► &aComida: &6" + target.getFoodLevel())
                .build().clone();
    }
    
    public static ItemStack potions(Player target){
        ArrayList<PotionEffect> potions = new ArrayList<>(target.getActivePotionEffects());
        ItemStack item = new ItemStack(Material.BREWING_STAND);
        if (!potions.isEmpty()){
            ArrayList<String> lore = new ArrayList<>();
            for ( PotionEffect a : potions ){
                lore.add(Utils.format("&a► &7" + a.getType().getName() + " - " + getTime((long) a.getDuration())));
            }
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(Utils.format("&aPociones:"));
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);
        } else {
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(Utils.format("&aPociones:"));
            itemMeta.setLore(Collections.singletonList(Utils.format("&a► &cSin Pociones")));
            item.setItemMeta(itemMeta);
        }
        return item.clone();
    }
    
    public static ItemStack head(Player owner){
        final User user = Main.getInstance().getPlayers().getCachedPlayer(owner.getUniqueId());
        return new ItemBuilder(XMaterial.PLAYER_HEAD.parseMaterial())
                .setHeadSkin(user.getSkin())
                .setDisplayName("&a" + owner.getName() + "'s &7Stats:")
                .addLoreLine("&a► &7Modo De Juego: &b" + owner.getGameMode())
                .addLoreLine("&a► &7Locación: &c" + (int) owner.getLocation().getX() + " &3" + (int) owner.getLocation().getY() + " &c" + (int) owner.getLocation().getZ())
                .addLoreLine("&a► Rango: " + user.getRank().getTabPrefix())
                .addLoreLine("&a► Monedas: &c" + user.getCoinsFormatted())
                .addLoreLine("&a► Nivel: &c" + user.getLevel().getLevelName())
                .addLoreLine(user.getLevel().getProgressBar())
                .addLoreLine("")
                .addLoreLine("&5STAFF MEMBER")
                .addLoreLine("&a► &7Vanished " + (Main.getInstance().getVanishManager().isVanished(owner) ? "&aOn" : "&cOff"))
                .addTag("head", "head")
                .addTag("name", owner.getName())
                .build().clone();
    }
    
    public static void setLobbyItems(Player p){
        p.getInventory().clear();
        p.getInventory().setItem(LOBBY_MENU.getSlot(), LOBBY_MENU.getItem());
        p.getInventory().setItem(MULTI_LOBBY_MENU_HOT_BAR.getSlot(), MULTI_LOBBY_MENU_HOT_BAR.getItem());
    
        User user = Main.getInstance().getPlayers().getCachedPlayer(p.getUniqueId());
        PlayerVisibility playerVisibility = user.getPlayerVisibility();
        switch(playerVisibility){
            case ALL:
                p.getInventory().setItem(LOBBY_PLAYER_VISIBILITY_ALL.getSlot(), LOBBY_PLAYER_VISIBILITY_ALL.getItem());
                break;
            case RANKS:
                p.getInventory().setItem(LOBBY_PLAYER_VISIBILITY_RANKS.getSlot(), LOBBY_PLAYER_VISIBILITY_RANKS.getItem());
                break;
            case NONE:
                p.getInventory().setItem(LOBBY_PLAYER_VISIBILITY_NONE.getSlot(), LOBBY_PLAYER_VISIBILITY_NONE.getItem());
                break;
        }
        p.setHealth(p.getMaxHealth());
        p.setGameMode(GameMode.ADVENTURE);
        p.setFoodLevel(20);
        p.setFireTicks(0);
        p.setExp(0);
        p.setLevel(0);
        p.setSaturation(20F);
        p.getInventory().setHeldItemSlot(0);
        p.updateInventory();
    
    }
    
    public static void setSkyWarsLobbyItems(Player p){
        p.getInventory().clear();
        p.getInventory().setItem(LOBBY_MENU.getSlot(), LOBBY_MENU.getItem());
        p.getInventory().setItem(MULTI_LOBBY_MENU_HOT_BAR.getSlot(), MULTI_LOBBY_MENU_HOT_BAR.getItem());
        for ( String name : config.getConfigurationSection("items").getKeys(false) ){
            if (!name.startsWith("lobby-skywars-")) continue;
            p.getInventory().setItem(config.getInt("items." + name + ".slot"), config.getItem(name));
        }
    
        User user = Main.getInstance().getPlayers().getCachedPlayer(p.getUniqueId());
        PlayerVisibility playerVisibility = user.getPlayerVisibility();
        switch(playerVisibility){
            case ALL:
                p.getInventory().setItem(LOBBY_PLAYER_VISIBILITY_ALL.getSlot(), LOBBY_PLAYER_VISIBILITY_ALL.getItem());
                break;
            case RANKS:
                p.getInventory().setItem(LOBBY_PLAYER_VISIBILITY_RANKS.getSlot(), LOBBY_PLAYER_VISIBILITY_RANKS.getItem());
                break;
            case NONE:
                p.getInventory().setItem(LOBBY_PLAYER_VISIBILITY_NONE.getSlot(), LOBBY_PLAYER_VISIBILITY_NONE.getItem());
                break;
        }
        JoinSkyWarsArenaType joinArenaType = user.getJoinSkyWarsArenaType();
        switch(joinArenaType){
            case SOLO:
                p.getInventory().setItem(LOBBY_JOIN_ARENA_SKYWARS_SOLO.getSlot(), LOBBY_JOIN_ARENA_SKYWARS_SOLO.getItem());
                break;
            case TEAM:
                p.getInventory().setItem(LOBBY_JOIN_ARENA_SKYWARS_TEAM.getSlot(), LOBBY_JOIN_ARENA_SKYWARS_TEAM.getItem());
                break;
            case RANKED:
                p.getInventory().setItem(LOBBY_JOIN_ARENA_SKYWARS_RANKED.getSlot(), LOBBY_JOIN_ARENA_SKYWARS_RANKED.getItem());
                break;
            case RANDOM:
                p.getInventory().setItem(LOBBY_JOIN_ARENA_SKYWARS_RANDOM.getSlot(), LOBBY_JOIN_ARENA_SKYWARS_RANDOM.getItem());
                break;
        }
        p.setHealth(p.getMaxHealth());
        p.setGameMode(GameMode.ADVENTURE);
        p.setFoodLevel(20);
        p.setFireTicks(0);
        p.setExp(0);
        p.setLevel(0);
        p.setSaturation(20F);
        p.getInventory().setHeldItemSlot(0);
        p.updateInventory();
    
    }
    
    public static void setBedWarsLobbyItems(Player p){
        p.getInventory().clear();
        p.getInventory().setItem(LOBBY_MENU.getSlot(), LOBBY_MENU.getItem());
        p.getInventory().setItem(MULTI_LOBBY_MENU_HOT_BAR.getSlot(), MULTI_LOBBY_MENU_HOT_BAR.getItem());
        for ( String name : config.getConfigurationSection("items").getKeys(false) ){
            if (!name.startsWith("lobby-bedwars-")) continue;
            p.getInventory().setItem(config.getInt("items." + name + ".slot"), config.getItem(name));
        }
    
        User user = Main.getInstance().getPlayers().getCachedPlayer(p.getUniqueId());
        PlayerVisibility playerVisibility = user.getPlayerVisibility();
        switch(playerVisibility){
            case ALL:
                p.getInventory().setItem(LOBBY_PLAYER_VISIBILITY_ALL.getSlot(), LOBBY_PLAYER_VISIBILITY_ALL.getItem());
                break;
            case RANKS:
                p.getInventory().setItem(LOBBY_PLAYER_VISIBILITY_RANKS.getSlot(), LOBBY_PLAYER_VISIBILITY_RANKS.getItem());
                break;
            case NONE:
                p.getInventory().setItem(LOBBY_PLAYER_VISIBILITY_NONE.getSlot(), LOBBY_PLAYER_VISIBILITY_NONE.getItem());
                break;
        }
        JoinBedWarsArenaType joinArenaType = user.getJoinBedWarsArenaType();
        switch(joinArenaType){
            case SOLO:
                p.getInventory().setItem(LOBBY_JOIN_ARENA_BEDWARS_SOLO.getSlot(), LOBBY_JOIN_ARENA_BEDWARS_SOLO.getItem());
                break;
            case DUO:
                p.getInventory().setItem(LOBBY_JOIN_ARENA_BEDWARS_DUO.getSlot(), LOBBY_JOIN_ARENA_BEDWARS_DUO.getItem());
                break;
            case _3V3V3V3:
                p.getInventory().setItem(LOBBY_JOIN_ARENA_BEDWARS_3V3V3V3.getSlot(), LOBBY_JOIN_ARENA_BEDWARS_3V3V3V3.getItem());
                break;
            case _4V4V4V4:
                p.getInventory().setItem(LOBBY_JOIN_ARENA_BEDWARS_4V4V4V4.getSlot(), LOBBY_JOIN_ARENA_BEDWARS_4V4V4V4.getItem());
                break;
            case RANDOM:
                p.getInventory().setItem(LOBBY_JOIN_ARENA_BEDWARS_RANDOM.getSlot(), LOBBY_JOIN_ARENA_BEDWARS_RANDOM.getItem());
                break;
        }
        
        
        p.updateInventory();
        
    }
    
    private static String getTime(Long timeLeft){
        long left = (timeLeft / 20) * 1000;
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        Date date = new Date(left);
        return format.format(date);
    }
    
}
