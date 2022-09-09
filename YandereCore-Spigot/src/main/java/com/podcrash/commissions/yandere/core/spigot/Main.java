package com.podcrash.commissions.yandere.core.spigot;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.podcrash.commissions.yandere.core.common.YandereApi;
import com.podcrash.commissions.yandere.core.common.data.logs.OfflineLogRepository;
import com.podcrash.commissions.yandere.core.common.data.server.IServerRepository;
import com.podcrash.commissions.yandere.core.common.data.server.ProxyStats;
import com.podcrash.commissions.yandere.core.common.data.server.ServerType;
import com.podcrash.commissions.yandere.core.common.db.IPlayerRepository;
import com.podcrash.commissions.yandere.core.common.db.OfflinePlayerRepository;
import com.podcrash.commissions.yandere.core.common.log.ILogRepository;
import com.podcrash.commissions.yandere.core.common.socket.ISocket;
import com.podcrash.commissions.yandere.core.common.socket.OfflineSocketClient;
import com.podcrash.commissions.yandere.core.spigot.commands.*;
import com.podcrash.commissions.yandere.core.spigot.commands.gamemode.GMACommand;
import com.podcrash.commissions.yandere.core.spigot.commands.gamemode.GMCCommand;
import com.podcrash.commissions.yandere.core.spigot.commands.gamemode.GMSCommand;
import com.podcrash.commissions.yandere.core.spigot.commands.gamemode.GMSPCommand;
import com.podcrash.commissions.yandere.core.spigot.commands.punish.EnderSeeCommand;
import com.podcrash.commissions.yandere.core.spigot.commands.punish.InvSeeCommand;
import com.podcrash.commissions.yandere.core.spigot.commands.spawn.DelSpawn;
import com.podcrash.commissions.yandere.core.spigot.commands.spawn.SetSpawn;
import com.podcrash.commissions.yandere.core.spigot.commands.spawn.Spawn;
import com.podcrash.commissions.yandere.core.spigot.commands.tp.Teleport;
import com.podcrash.commissions.yandere.core.spigot.commands.tp.TpAll;
import com.podcrash.commissions.yandere.core.spigot.commands.tp.TpHere;
import com.podcrash.commissions.yandere.core.spigot.config.YandereConfig;
import com.podcrash.commissions.yandere.core.spigot.cooldowns.CoolDownManager;
import com.podcrash.commissions.yandere.core.spigot.inv.EndInvManager;
import com.podcrash.commissions.yandere.core.spigot.inv.InvManager;
import com.podcrash.commissions.yandere.core.spigot.items.Items;
import com.podcrash.commissions.yandere.core.spigot.lang.ESLang;
import com.podcrash.commissions.yandere.core.spigot.libs.lymarket.lyapi.spigot.utils.Utils;
import com.podcrash.commissions.yandere.core.spigot.listener.DefaultEvents;
import com.podcrash.commissions.yandere.core.spigot.listener.bedwars.BWPlayerEvents;
import com.podcrash.commissions.yandere.core.spigot.listener.bedwars.lobby.LBWPlayerEvents;
import com.podcrash.commissions.yandere.core.spigot.listener.lobby.MainLobbyPlayerEvents;
import com.podcrash.commissions.yandere.core.spigot.listener.plugin.PluginMessage;
import com.podcrash.commissions.yandere.core.spigot.listener.practice.PracticeGameEvents;
import com.podcrash.commissions.yandere.core.spigot.listener.practice.PracticePlayerEvents;
import com.podcrash.commissions.yandere.core.spigot.listener.skywars.SWGameEvents;
import com.podcrash.commissions.yandere.core.spigot.listener.skywars.SWPlayerEvents;
import com.podcrash.commissions.yandere.core.spigot.log.LogRepository;
import com.podcrash.commissions.yandere.core.spigot.papi.Placeholders;
import com.podcrash.commissions.yandere.core.spigot.server.ServerRepository;
import com.podcrash.commissions.yandere.core.spigot.settings.Settings;
import com.podcrash.commissions.yandere.core.spigot.socket.SpigotSocketClient;
import com.podcrash.commissions.yandere.core.spigot.sounds.Sounds;
import com.podcrash.commissions.yandere.core.spigot.users.PlayerRepository;
import com.podcrash.commissions.yandere.core.spigot.vanish.VanishManager;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import net.luckperms.api.LuckPerms;
import net.lymarket.lyapi.common.db.MongoDBClient;
import net.lymarket.lyapi.common.error.LyApiInitializationError;
import net.lymarket.lyapi.common.lang.ILang;
import net.lymarket.lyapi.spigot.LyApi;
import net.lymarket.lyapi.spigot.config.Config;
import net.lymarket.lyapi.spigot.menu.IUpdatableMenu;
import net.lymarket.lyapi.spigot.menu.InventoryMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Level;

@SuppressWarnings({"rawtypes", "unchecked"})
public final class Main extends JavaPlugin implements YandereApi {
    
    public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    private static LyApi api;
    private static Main instance;
    private static LuckPerms lpApi;
    private final CoolDownManager coolDownManager = new CoolDownManager();
    private ProxyStats proxyStats = new ProxyStats();
    private YandereConfig config;
    private YandereConfig items;
    private YandereConfig sounds;
    private String nms_version;
    private IPlayerRepository players;
    private ILogRepository logs;
    private ISocket socket;
    private ViaAPI<Player> viaVersionApi;
    private VanishManager vanishManager;
    private InvManager invManager;
    private EndInvManager endInvManager;
    private boolean hockedIntoBedWars = false;
    private boolean hockedIntoSkyWars = false;
    private IServerRepository serverRepository;
    
    public static LyApi getApi(){
        return api;
    }
    
    public static ILang getLang(){
        return LyApi.getLanguage();
    }
    
    public static Main getInstance(){
        return instance;
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
        config = new YandereConfig(this, "config.yml");
        items = new YandereConfig(this, "items.yml");
        sounds = new YandereConfig(this, "sounds.yml");
        try {
            api = new LyApi(this, "Yandere", "&c[&4ERROR&c] &cNo tienes el siguiente permiso:&e permission", new ESLang(new Config(this, "es.yml"), config.getString("global.prefix"), "&c[&4ERROR&c]"), true);
        } catch (LyApiInitializationError e) {
            e.printStackTrace();
            getServer().shutdown();
        }
        this.nms_version = Bukkit.getServer().getClass().getName().split("\\.")[3];
        Settings.init(config);
        Items.init(items);
        Sounds.init(sounds);
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
        if (config.getBoolean("db.enabled")){
            final MongoDBClient mongo = new MongoDBClient(config.getString("db.urli"), config.getString("db.database"));
            players = new PlayerRepository(mongo, "players");
            logs = new LogRepository(mongo, "logs");
            serverRepository = new ServerRepository(mongo, "servers");
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
        } else {
            players = new OfflinePlayerRepository();
            logs = new OfflineLogRepository();
            socket = new OfflineSocketClient();
        }
        switch(Settings.SERVER_TYPE){
            case LOBBY: {
                getServer().getPluginManager().registerEvents(new MainLobbyPlayerEvents(), this);
                api.getCommandService().registerCommands(new Build());
                getLogger().info("\n------------------------");
                getLogger().info("[YandereCore] Hooked into Lobby");
                getLogger().info("------------------------\n");
                break;
            }
            case LOBBY_BED_WARS: {
                getServer().getPluginManager().registerEvents(new LBWPlayerEvents(), this);
                api.getCommandService().registerCommands(new Build());
                getLogger().info("\n------------------------");
                getLogger().info("[YandereCore] Hooked into Lobby BedWars");
                getLogger().info("------------------------\n");
                break;
            }
            case PRACTICE:{
                if (Bukkit.getPluginManager().getPlugin("StrikePractice") != null){
                    api.getCommandService().registerCommands(new Build());
                    getServer().getPluginManager().registerEvents(new PracticeGameEvents(), this);
                    getServer().getPluginManager().registerEvents(new PracticePlayerEvents(), this);
                    getLogger().info("\n------------------------");
                    getLogger().info("[YandereCore] Hooked into Practice");
                    getLogger().info("------------------------\n");
                } else {
                    getLogger().log(Level.SEVERE, "Disabled due to no StrikePractice dependency found!");
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
        }, 0L, 60L);
        getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            for ( Player p : Bukkit.getOnlinePlayers() ){
                if(p.getOpenInventory().getTopInventory().getHolder() instanceof InventoryMenu){
                    ((InventoryMenu) p.getOpenInventory().getTopInventory().getHolder()).reOpen();
                }
            }
        }, 0L, 10L);
    
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            getLogger().info("\n------------------------");
            getLogger().info("[UPDATE MACHINE] Checking Plugin Updates...");
            this.serverRepository.checkForPluginsUpdates();
            getLogger().info("[UPDATE MACHINE] Done.");
            getLogger().info("------------------------\n");
        }, 0L, 20L * 3600L * 24L);
        //new PacketManager( this );
        overrideSpigotDefaultMessages();
        getServer().getPluginManager().callEvent(new PluginEnableEvent(this));
    
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
    public YandereConfig getConfig(){
        return config;
    }
    
    public YandereConfig getItems(){
        return items;
    }
    
    public YandereConfig getSounds(){
        return sounds;
    }
    
    public IPlayerRepository getPlayers(){
        return players;
    }
    
    public ISocket getSocket(){
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
        return Settings.PROXY_SERVER_NAME;
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
        api.getCommandService().registerCommands(new GamemodeCommand());
        api.getCommandService().registerCommands(new GMSCommand());
        api.getCommandService().registerCommands(new GMCCommand());
        api.getCommandService().registerCommands(new GMACommand());
        api.getCommandService().registerCommands(new GMSPCommand());
        api.getCommandService().registerCommands(new Fly());
        api.getCommandService().registerCommands(new Heal());
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
    
    public CoolDownManager getCoolDownManager(){
        return coolDownManager;
    }
    
    public boolean hookPodBedWars() {
        try {
            if(!hockedIntoBedWars){
                getLogger().info("\n------------------------");
                getLogger().info("[YandereCore] Loading BedWars Events...");
                Bukkit.getPluginManager().registerEvents(new BWPlayerEvents(), Main.getInstance());
                hockedIntoBedWars = true;
                getLogger().info("[YandereCore] Done!");
                getLogger().info("------------------------\n");
                return true;
            } else {
                return false;
            }
        } catch(Exception e) {
            return false;
        }
    }
    
    public boolean hookSkyWars() {
        try {
            if(!hockedIntoSkyWars){
                getLogger().info("\n------------------------");
                getLogger().info("[YandereCore] Loading SkyWars Events...");
                getServer().getPluginManager().registerEvents(new SWGameEvents(), this);
                getServer().getPluginManager().registerEvents(new SWPlayerEvents(), this);
                api.getCommandService().registerCommands(new Build());
                hockedIntoSkyWars = true;
                getLogger().info("[YandereCore] Done!");
                getLogger().info("------------------------\n");
                return true;
            } else {
                return false;
            }
        } catch(Exception e) {
            return false;
        }
    }
    
    public boolean isHookedIntoBedWars() {
        return hockedIntoBedWars;
    }
    
    public boolean isHookedIntoSkyWars() {
        return hockedIntoSkyWars;
    }
    
    public void overrideSpigotDefaultMessages() {
        org.spigotmc.SpigotConfig.unknownCommandMessage = Utils.format("  &8&l▸ &cComando no encontrado, usa &e/ayuda &cpa ver los comandos disponibles.");
    }
    
    public IServerRepository getServerRepository() {
        return serverRepository;
    }
}
