package com.podcrash.commissions.yandere.core.velocity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.podcrash.commissions.yandere.core.common.data.server.ProxyStats;
import com.podcrash.commissions.yandere.core.common.log.Slf4jPluginLogger;
import com.podcrash.commissions.yandere.core.velocity.commands.Lobby;
import com.podcrash.commissions.yandere.core.velocity.commands.Ping;
import com.podcrash.commissions.yandere.core.velocity.commands.Stream;
import com.podcrash.commissions.yandere.core.velocity.commands.VAdmin;
import com.podcrash.commissions.yandere.core.velocity.config.Config;
import com.podcrash.commissions.yandere.core.velocity.listener.PlayerEvents;
import com.podcrash.commissions.yandere.core.velocity.listener.ServerEvents;
import com.podcrash.commissions.yandere.core.velocity.manager.PlayerRepository;
import com.podcrash.commissions.yandere.core.velocity.manager.ServerSocketManager;
import com.podcrash.commissions.yandere.core.velocity.socketmanager.ProxySocketServer;
import com.podcrash.commissions.yandere.core.velocity.socketmanager.ServerSocketTask;
import com.podcrash.commissions.yandere.core.velocity.utils.ChatColor;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import com.velocitypowered.api.scheduler.ScheduledTask;
import net.lymarket.lyapi.common.db.MongoDBClient;
import net.lymarket.lyapi.velocity.LyApiVelocity;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Plugin(id = "yandere",
        name = "Yandere",
        version = "1.0-ALPHA",
        authors = {"BarraR3port"},
        url = "https://podcrash.com/",
        dependencies = {@Dependency(id = "luckperms")})
public final class VMain extends LyApiVelocity {
    
    public final static Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    private static VMain instance;
    private static Config config;
    private final ServerSocketManager serverSocketManager = new ServerSocketManager();
    private final ProxyStats serverManager;
    private final ProxyServer proxy;
    private final Slf4jPluginLogger logger;
    private final Path path;
    private final HashMap<UUID, ScheduledTask> streams = new HashMap<>();
    private PlayerRepository playersRepository;
    
    /**
     * Constructor for ChatRegulator Plugin
     *
     * @param server the proxy server
     * @param logger logger
     * @param path   the plugin path
     */
    @Inject
    @Internal
    public VMain(final ProxyServer server, final Logger logger, final @DataDirectory Path path){
        super(server, "&cSin permisos");
        this.proxy = server;
        this.path = path;
        this.logger = new Slf4jPluginLogger(logger);
        serverManager = new ProxyStats();
    }
    
    @Internal
    public static VMain getInstance(){
        return instance;
    }
    
    @Internal
    public static void debug(String msg){
        if (config.getConfig().isDebug()){
            instance.getLogger().info(ChatColor.RED + "[DEBUG] " + ChatColor.LIGHT_PURPLE + msg);
        }
    }
    
    @Internal
    public static Config getConfig(){
        return config;
    }
    
    @Subscribe
    @Internal
    public void onProxyInitialize(ProxyInitializeEvent event){
        // Plugin startup logic
        instance = this;
        config = new Config(path);
        proxy.getChannelRegistrar().register(new LegacyChannelIdentifier("podcrash:yandere"));
        proxy.getEventManager().register(this, new PlayerEvents());
        proxy.getEventManager().register(this, new ServerEvents());
        if (ServerSocketTask.init()){
            debug("ServerSocketTask started");
        }
        String url = config.getConfig().getDb_urli(); /*config.getDb_urli( ).equals( "" ) ? "mongodb://" + config.getDb_username( ) + ":" + config.getDb_password( ) + "@" + config.getDb_host( ) + ":" + config.getDb_port( ) :*/
        final MongoDBClient mongo = new MongoDBClient(url, config.getConfig().getDb_database());
        playersRepository = new PlayerRepository(mongo, "players");
    
        VMain.getInstance().getProxy().getScheduler().buildTask(VMain.getInstance(), this::sendInfo).repeat(5, TimeUnit.SECONDS).schedule();
    
        new Lobby(proxy.getCommandManager());
        new VAdmin(proxy.getCommandManager());
        new Stream(proxy.getCommandManager());
        new Ping(proxy.getCommandManager());
    
    }
    
    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event){
        // Plugin shutdown logic
        ServerSocketTask.stopTasks();
        logger.info(ChatColor.BLUE + "YandereCore Velocity plugin shutdown");
    }
    
    public void sendInfo(){
        for ( ProxySocketServer socket : VMain.getInstance().getServerSocketManager().getSocketByServer().values() ){
            try {
                socket.sendProxyServerStats();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    @Internal
    public ServerSocketManager getServerSocketManager(){
        return serverSocketManager;
    }
    
    @Internal
    public ProxyStats getServerManager(){
        return serverManager;
    }
    
    @Internal
    public ProxyServer getProxy(){
        return proxy;
    }
    
    @Internal
    public Slf4jPluginLogger getLogger(){
        return logger;
    }
    
    public PlayerRepository getPlayers(){
        return playersRepository;
    }
    
    public HashMap<UUID, ScheduledTask> getStreams(){
        return streams;
    }
    
    public void setStreams(UUID uuid, ScheduledTask task){
        streams.put(uuid, task);
    }
}
