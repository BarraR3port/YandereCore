package com.podcrash.comissions.yandere.core.spigot.papi;

import com.podcrash.comissions.yandere.core.spigot.Main;
import com.podcrash.comissions.yandere.core.spigot.settings.Settings;
import com.podcrash.comissions.yandere.core.spigot.users.SpigotUser;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class Placeholders extends PlaceholderExpansion {
    
    // We get an instance of the plugin later.
    private final Main plugin;
    
    public Placeholders(Main plugin){
        this.plugin = plugin;
    }
    
    /**
     * Because this is an internal class,
     * you must override this method to let PlaceholderAPI know to not unregister your expansion class when
     * PlaceholderAPI is reloaded
     *
     * @return true to persist through reloads
     */
    @Override
    public boolean persist(){
        return true;
    }
    
    /**
     * Since this expansion requires api access to the plugin "SomePlugin"
     * we must check if said plugin is on the server or not.
     *
     * @return true or false depending on if the required plugin is installed.
     */
    @Override
    public boolean canRegister(){
        return true;
    }
    
    /**
     * The name of the person who created this expansion should go here.
     *
     * @return The name of the author as a String.
     */
    @Override
    public @NotNull String getAuthor(){
        return "BarraR3port";
    }
    
    /**
     * The placeholder identifier should go here.
     * <br>This is what tells PlaceholderAPI to call our onRequest
     * method to obtain a value if a placeholder starts with our
     * identifier.
     * <br>This must be unique and can not contain % or _
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public @NotNull String getIdentifier(){
        return "bbb";
    }
    
    /**
     * This is the version of this expansion.
     * <br>You don't have to use numbers, since it is set as a String.
     *
     * @return The version as a String.
     */
    @Override
    public @NotNull String getVersion(){
        return plugin.getDescription().getVersion();
    }
    
    /**
     * This is the method called when a placeholder with our identifier
     * is found and needs a value.
     * <br>We specify the value identifier in this method.
     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * @param player     A {@link Player}.
     * @param identifier A String containing the identifier/value.
     *
     * @return possib-null String of the requested identifier.
     */
    
    // PLACEHOLDERS
    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier){
        
        if (player == null) return "";
        
        switch(identifier){
            case "server":{
                return Settings.SERVER_NAME;
            }
            case "health":{
                return String.valueOf(player.getHealth());
            }
            case "gamemode":{
                return String.valueOf(player.getGameMode());
            }
            case "version":{
                return plugin.getDescription().getVersion();
            }
            case "server_version":{
                return Settings.VERSION;
            }
            case "server_size":{
                return String.valueOf(Main.getInstance().proxyStats.getAllPlayerSize());
            }
            case "lobby_online":{
                return String.valueOf(Main.getInstance().proxyStats.isLobbyOnline());
            }
            case "lobby_online_formatted":{
                return Main.getInstance().proxyStats.isLobbyOnline() ? "&aABIERTO" : "&cCERRADO";
            }
            
        }
        
        final SpigotUser p = Main.getInstance().getPlayers().getLocalStoredPlayer(player.getUniqueId());
        
        if (p == null) return "Jugador no encontrado";
        //%dark_coins%
        
        switch(identifier){
            case "location_server":{
                return p.getLastLocation().getServer();
            }
            case "location_server_type":{
                return p.getLastLocation().getCurrentServerType().toString();
            }
            case "location_server_formatted":{
                return p.getLastLocation().getCurrentServerTypeFormatted();
            }
            case "stats_time_played":{
                return p.getStats().getFormattedTimePlayed();
            }
            case "address":{
                return p.getAddress();
            }
            case "rank_tab":{
                return p.getRank().getTabPrefix();
            }
            case "rank_score_board":{
                return p.getRank().getScoreBoardName();
            }
            case "rank_prefix":{
                return p.getRank().getPrefix();
            }
            case "coins":{
                return String.valueOf(p.getCoins());
            }
            case "coins_formatted":{
                DecimalFormat df = new DecimalFormat("#.##");
                if (p.getCoins() > 1000000){
                    return df.format(p.getCoins() / 1000000) + "&eM ⛃";
                }
                if (p.getCoins() > 10000){
                    return df.format(p.getCoins() / 1000) + "&eK ⛃";
                }
                return p.getCoins() + "&eK ⛃";
            }
            case "level":{
                return String.valueOf(p.getLevel().getLevel());
            }
            case "level_next_cost":
            case "level_required_xp":{
                return String.valueOf(p.getLevel().getNextLevelXp());
            }
            case "level_name":{
                return p.getLevel().getLevelNameFormatted();
            }
            case "level_current_xp":{
                return String.valueOf(p.getLevel().getCurrentXp());
            }
            case "level_formatted_current_xp":{
                return p.getLevel().getFormattedCurrentXp();
            }
            case "level_progress_bar":{
                return p.getLevel().getProgressBar();
            }
            case "level_progres_xp":{
                return p.getLevel().getProgressXP();
            }
            case "level_formatted_required_xp":{
                return p.getLevel().getFormattedRequiredXp();
            }
        }
        
        return null;
    }
}
