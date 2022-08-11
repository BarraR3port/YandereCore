package com.podcrash.comissions.yandere.core.spigot;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.podcrash.comissions.yandere.core.common.YandereApi;
import com.podcrash.comissions.yandere.core.common.data.logs.ILogRepository;
import com.podcrash.comissions.yandere.core.common.data.server.ProxyStats;
import com.podcrash.comissions.yandere.core.common.data.server.ServerType;
import com.podcrash.comissions.yandere.core.spigot.commands.*;
import com.podcrash.comissions.yandere.core.spigot.commands.punish.EnderSeeCommand;
import com.podcrash.comissions.yandere.core.spigot.commands.punish.InvSeeCommand;
import com.podcrash.comissions.yandere.core.spigot.commands.spawn.DelSpawn;
import com.podcrash.comissions.yandere.core.spigot.commands.spawn.SetSpawn;
import com.podcrash.comissions.yandere.core.spigot.commands.spawn.Spawn;
import com.podcrash.comissions.yandere.core.spigot.commands.tp.Teleport;
import com.podcrash.comissions.yandere.core.spigot.commands.tp.TpAll;
import com.podcrash.comissions.yandere.core.spigot.commands.tp.TpHere;
import com.podcrash.comissions.yandere.core.spigot.items.Items;
import com.podcrash.comissions.yandere.core.spigot.lang.ESLang;
import com.podcrash.comissions.yandere.core.spigot.listener.DefaultEvents;
import com.podcrash.comissions.yandere.core.spigot.listener.bedwars.BWGameEvents;
import com.podcrash.comissions.yandere.core.spigot.listener.bedwars.BWPlayerEvents;
import com.podcrash.comissions.yandere.core.spigot.listener.lobby.LobbyPlayerEvents;
import com.podcrash.comissions.yandere.core.spigot.listener.plugin.PluginMessage;
import com.podcrash.comissions.yandere.core.spigot.log.LogRepository;
import com.podcrash.comissions.yandere.core.spigot.menu.inv.EndInvManager;
import com.podcrash.comissions.yandere.core.spigot.menu.inv.InvManager;
import com.podcrash.comissions.yandere.core.spigot.papi.Placeholders;
import com.podcrash.comissions.yandere.core.spigot.settings.Settings;
import com.podcrash.comissions.yandere.core.spigot.socket.SpigotSocketClient;
import com.podcrash.comissions.yandere.core.spigot.sounds.Sounds;
import com.podcrash.comissions.yandere.core.spigot.users.PlayersRepository;
import com.podcrash.comissions.yandere.core.spigot.users.SpigotUser;
import com.podcrash.comissions.yandere.core.spigot.vanish.VanishManager;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import net.luckperms.api.LuckPerms;
import net.lymarket.common.config.ConfigGenerator;
import net.lymarket.common.db.MongoDBClient;
import net.lymarket.common.error.LyApiInitializationError;
import net.lymarket.common.lang.ILang;
import net.lymarket.lyapi.spigot.LyApi;
import net.lymarket.lyapi.spigot.config.Config;
import net.lymarket.lyapi.spigot.menu.IUpdatableMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Level;

@SuppressWarnings({"rawtypes", "unchecked"})
public final class Main extends JavaPlugin implements YandereApi<SpigotUser> {
    
    public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    private ProxyStats proxyStats = new ProxyStats();
    private static LyApi api;
    private static Main instance;
    private static LuckPerms lpApi;
    private Config config;
    private Config items;
    private Config sounds;
    private String nms_version;
    private PlayersRepository players;
    private LogRepository logs;
    private SpigotSocketClient socket;
    private ViaAPI<Player> viaVersionApi;
    private VanishManager vanishManager;
    private InvManager invManager;
    private EndInvManager endInvManager;
    
    public static LyApi getApi(){
        return api;
    }
    
    public static ILang getLang(){
        return LyApi.getLanguage();
    }
    
    public static Main getInstance(){
        return instance;
    }
    
    public static LuckPerms getLuckPerms(){
        return lpApi;
    }
    
    public static void debug(String message){
        if (Settings.DEBUG){
            instance.getLogger().info(ChatColor.RED + "[DEBUG] " + ChatColor.LIGHT_PURPLE + message);
        }
    }
    
    @Override
    public void onEnable(){
        instance = this;
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "podcrash:yandere", new PluginMessage());
        getServer().getMessenger().registerOutgoingPluginChannel(this, "podcrash:yandere");
        config = new Config(this, "config.yml");
        items = new Config(this, "items.yml");
        sounds = new Config(this, "sounds.yml");
        try {
            api = new LyApi(this, "Yandere", "&c[&4ERROR&c] &cNo tienes el siguiente permiso:&e permission", new ESLang(new ConfigGenerator(this, "es.yml"), config.getString("global.prefix"), "&c[&4ERROR&c]"));
        } catch (LyApiInitializationError e) {
            e.printStackTrace();
            getServer().shutdown();
        }
        this.nms_version = Bukkit.getServer().getClass().getName().split("\\.")[3];
        Settings.init(config);
        Items.init(items);
        Sounds.init(sounds);
        
        /*try {
            Class<?> supp = Class.forName("net.lymarket.lyapi.support.version." + nms_version + "." + nms_version);
            this.nms = (VersionSupport) supp.getConstructor(Class.forName("org.bukkit.plugin.java.JavaPlugin")).newInstance(this);
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                 ClassNotFoundException e) {
            e.printStackTrace();
            getServer().shutdown();
        }*/
        if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null){
            RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
            if (provider != null){
                lpApi = provider.getProvider();
            } else {
                getLogger().log(Level.SEVERE, String.format("[%s] - Disabled due to no LuckPerms dependency found!", getDescription().getName()));
                getServer().getPluginManager().disablePlugin(this);
                getServer().shutdown();
            }
        }
        
        
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new Placeholders(this).register();
        }
        
        if (Bukkit.getPluginManager().getPlugin("ViaVersion") != null){
            viaVersionApi = Via.getAPI();
        }
        
        registerCommands();
        
        //final MongoDBClient mongo = new MongoDBClient( "mongodb://" + config.getString( "db.host" ) + ":" + config.getString( "db.port" ) , config.getString( "db.database" ) );
        final MongoDBClient mongo = new MongoDBClient(config.getString("db.urli"), config.getString("db.database"));
        players = new PlayersRepository(mongo, "players");
        logs = new LogRepository(mongo, "logs");
        try {
            socket = new SpigotSocketClient(players);
            if (Settings.IS_SERVER_LINKED){
                socket.init();
                socket.sendUpdate();
            } else {
                getServer().getPluginManager().registerEvents(new DefaultEvents(), this);
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("GetServer");
                Bukkit.getServer().sendPluginMessage(Main.getInstance(), "podcrash:yandere", out.toByteArray());
            }
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            getServer().shutdown();
        }
        switch(Settings.SERVER_TYPE){
            case LOBBY:
            case LOBBY_BED_WARS:{
                getServer().getPluginManager().registerEvents(new LobbyPlayerEvents(), this);
                break;
            }
            case BED_WARS:{
                if (Bukkit.getPluginManager().getPlugin("PodBedWars") != null){
                    getServer().getPluginManager().registerEvents(new BWGameEvents(), this);
                    getServer().getPluginManager().registerEvents(new BWPlayerEvents(), this);
                } else {
                    getLogger().log(Level.SEVERE, String.format("[%s] - Disabled due to no PodBedWars dependency found!", getDescription().getName()));
                    getServer().getPluginManager().disablePlugin(this);
                    getServer().shutdown();
                }
                break;
            }
            /*case PLOT:{
                getServer().getPluginManager().registerEvents(new PlotsPlayerEvent(), this);
                break;
            }*/
        }
        vanishManager = new VanishManager();
        invManager = new InvManager();
        endInvManager = new EndInvManager();
        getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            for ( Player p : Bukkit.getOnlinePlayers() ){
                if (p.getOpenInventory().getTopInventory().getHolder() instanceof IUpdatableMenu){
                    ((IUpdatableMenu) p.getOpenInventory().getTopInventory().getHolder()).reOpen();
                }
            }
        }, 0L, 20L);
        //new PacketManager( this );
        
    }
    
    @Override
    public void error(String message){
        if (Settings.DEBUG){
            instance.getLogger().severe(ChatColor.RED + "[ERROR] " + ChatColor.GRAY + message);
        }
    }
    
    @Override
    public void onDisable(){
        socket.sendDisconnectInfoToProxy();
        socket.disable();
        getServer().getScheduler().cancelTasks(this);
    }
    
    @Override
    public Config getConfig(){
        return config;
    }
    
    public Config getItems(){
        return items;
    }
    
    public Config getSounds(){
        return sounds;
    }
    
    public PlayersRepository getPlayers(){
        return players;
    }
    
    public SpigotSocketClient getSocket(){
        return socket;
    }
    
    public String getVersion(){
        return Settings.VERSION;
    }
    
    @Override
    public String getNMSVersion(){
        return nms_version;
    }
    
    public ViaAPI<Player> getViaVersion(){
        return viaVersionApi;
    }
    
    @Override
    public String getProxyServerName(){
        return Settings.SERVER_NAME;
    }
    
    private void registerCommands(){
        api.getCommandService().registerCommands(new SetSpawn());
        api.getCommandService().registerCommands(new DelSpawn());
        api.getCommandService().registerCommands(new Spawn());
        api.getCommandService().registerCommands(new Menu());
        api.getCommandService().registerCommands(new Admin());
        api.getCommandService().registerCommands(new RankMenu());
        api.getCommandService().registerCommands(new EnderSeeCommand());
        api.getCommandService().registerCommands(new InvSeeCommand());
        api.getCommandService().registerCommands(new SetSpawn());
        api.getCommandService().registerCommands(new Spawn());
        api.getCommandService().registerCommands(new DelSpawn());
        api.getCommandService().registerCommands(new Teleport());
        api.getCommandService().registerCommands(new TpAll());
        api.getCommandService().registerCommands(new TpHere());
        api.getCommandService().registerCommands(new Back());
        api.getCommandService().registerCommands(new ChatClear());
        api.getCommandService().registerCommands(new CoinsCommand());
        api.getCommandService().registerCommands(new EconomyCommand());
        api.getCommandService().registerCommands(new LevelCommand());
        api.getCommandService().registerCommands(new OPCommand());
        api.getCommandService().registerCommands(new DeOpCommand());
        api.getCommandService().registerCommands(new Speed());
        api.getCommandService().registerCommands(new VanishCommand());
        api.getCommandService().registerCommands(new XPCommand());
    }
    
    public void reconnectToProxy(){
        socket.disable();
        try {
            socket.init();
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        socket.sendUpdate();
        getServer().getScheduler().runTaskTimer(this, () -> {
            
            for ( Player p : Bukkit.getOnlinePlayers() ){
                if (p.getOpenInventory().getTopInventory().getHolder() instanceof IUpdatableMenu){
                    ((IUpdatableMenu) p.getOpenInventory().getTopInventory().getHolder()).reOpen();
                }
            }
            
        }, 20L, 20L);
    }
    
    public ProxyStats getProxyStats(){
        return proxyStats;
    }
    
    public void setProxyStats(ProxyStats proxyStats){
        this.proxyStats = proxyStats;
    }
    
    @Override
    public ServerType getServerType(){
        return Settings.SERVER_TYPE;
    }
    
    @Override
    public ILogRepository getLogs(){
        return logs;
    }
    
    public VanishManager getVanishManager(){
        return vanishManager;
    }
    
    public InvManager getInvManager(){
        return invManager;
    }
    
    public EndInvManager getEndInvManager(){
        return endInvManager;
    }
}
