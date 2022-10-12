package com.podcrash.commissions.yandere.core.spigot.papi;

import com.podcrash.commissions.yandere.core.common.data.server.ServerType;
import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.spigot.Main;
import com.podcrash.commissions.yandere.core.spigot.settings.Settings;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.lymarket.lyapi.spigot.utils.Utils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
        return "yandere";
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
        
        if (identifier.startsWith("server_size_")){
            return String.valueOf(Main.getInstance().getProxyStats().getTargetServerSize(identifier.replace("server_size_", "")));
        }
        if (identifier.startsWith("server_online_")){
            return String.valueOf(Main.getInstance().getProxyStats().isTargetServerOnline(identifier.replace("server_online_", "")));
        }
        if (identifier.startsWith("server_online_formatted_")){
            return Main.getInstance().getProxyStats().isTargetServerOnline(identifier.replace("server_online_formatted_", "")) ? "&aABIERTO" : "&cCERRADO";
        }
        if (identifier.startsWith("server_capacity_formatted_")){
            String server = identifier.replace("server_capacity_formatted_", "");
            return Main.getInstance().getProxyStats().getTargetServerCapacity(ServerType.valueOf(server), Main.getInstance().getGlobalServerSettings().getOrCreate());
        }
        if (identifier.startsWith("server_capacity_current_")){
            String server = identifier.replace("server_capacity_current_", "");
            return String.valueOf(Main.getInstance().getProxyStats().getCurrentTargetServerCapacity(ServerType.valueOf(server)));
        }
        if (identifier.startsWith("server_capacity_current_formatted_")){
            String server = identifier.replace("server_capacity_current_formatted_", "");
            return Main.getInstance().getProxyStats().getCurrentTargetServerCapacityFormatted(ServerType.valueOf(server), Main.getInstance().getGlobalServerSettings().getOrCreate());
        }
    
        switch(identifier){
            case "server_name":
            case "server":{
                return Settings.PROXY_SERVER_NAME;
            }
            case "version":{
                return plugin.getDescription().getVersion();
            }
            case "server_version":{
                return Settings.VERSION;
            }
            case "proxy_server_size":{
                return String.valueOf(Main.getInstance().getProxyStats().getAllPlayerSize());
            }
            case "server_size":{
                return String.valueOf(Main.getInstance().getProxyStats().getTargetServerSize(Settings.PROXY_SERVER_NAME));
            }
            case "server_type_name_formatted":{
                return Settings.SERVER_TYPE.getName();
            }
            case "server_name_number":{
                return String.valueOf(Settings.PROXY_SERVER_NAME.charAt(Settings.PROXY_SERVER_NAME.length() - 1));
            }
            
        }
        
        if (player == null) return "Jugador no encontrado";
        if (identifier.equals("health")) return String.valueOf(player.getHealth());
        if (identifier.equals("gamemode")) return String.valueOf(player.getGameMode());
        
        final User p = Main.getInstance().getPlayers().getCachedPlayer(player.getUniqueId());
        if (p == null) return "Jugador no encontrado";
        
        switch(identifier){
            case "location_server":{
                return p.getLastLocation().getServer();
            }
            case "location_server_type":
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
            case "rank_score_board_practice":{
                return Utils.format(p.getRank().getScoreBoardName().replace("§l", ""));
            }
            case "rank_prefix":{
                return p.getRank().getTagNamePrefix();
            }
            case "suffix":{
                return p.getSuffix();
            }
            case "coins":{
                return p.getCoinsSemiFormatted();
            }
            case "coins_formatted":{
                return p.getCoinsFormatted();
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
            case "level_progress_bar_formatted":{
                return p.getLevel().getProgressBarFormatted();
            }
            case "level_progres_xp":{
                return p.getLevel().getProgressXP();
            }
            case "level_formatted_required_xp":{
                return p.getLevel().getFormattedRequiredXp();
            }
            case "bw_join_type":{
                return p.getJoinBedWarsArenaType().getFormattedName();
            }
            case "sw_join_type":{
                return p.getJoinSkyWarsArenaType().getFormattedName();
            }
        }
        
        return "&cJugador oo encontrado";
    }
}
