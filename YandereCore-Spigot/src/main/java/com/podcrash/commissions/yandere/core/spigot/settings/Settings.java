package com.podcrash.commissions.yandere.core.spigot.settings;

import com.podcrash.commissions.yandere.core.common.data.server.ServerType;
import com.podcrash.commissions.yandere.core.spigot.Main;
import net.lymarket.lyapi.spigot.config.Config;
import org.bukkit.Location;

import java.util.ArrayList;

public class Settings {
    //Disable this in final releases
    public static boolean DEVELOPMENT_MODE = false;
    public static String SERVER_PREFIX = "&eYandere&dCraft";
    public static String SOCKET_IP = "localhost";
    public static int SOCKET_PORT = 5555;
    public static Location SPAWN_LOCATION;
    public static String PROXY_SERVER_NAME;
    public static boolean DEBUG;
    public static ArrayList<String> PERMS_WHEN_CREATING_WORLD = new ArrayList<>();
    public static ArrayList<String> PERMS_WHEN_JOINING_WORLD = new ArrayList<>();
    public static ServerType SERVER_TYPE;
    public static String VERSION;
    public static boolean IS_SERVER_LINKED = false;
    public static String WEB_KEY;
    public static String WEB_URL;
    public static String WEB_UUID;
    
    public Settings(){
    
    }
    
    public static void init(Config config){
        SERVER_PREFIX = config.getString("global.prefix");
        try {
            SPAWN_LOCATION = (Location) config.get("spawn.location", Location.class);
        } catch (NullPointerException | ClassCastException ignored) {
        }
        SOCKET_IP = config.getString("socket.ip");
        SOCKET_PORT = config.getInt("socket.port");
        PROXY_SERVER_NAME = config.getString("global.proxy-server-name");
        WEB_KEY = config.getString("web.key");
        WEB_URL = config.getString("web.url");
        WEB_UUID = config.getString("web.uuid", null);
        DEBUG = config.getBoolean("global.debug");
        PERMS_WHEN_CREATING_WORLD = new ArrayList<>(config.getStringList("perms.when-creating-world"));
        PERMS_WHEN_JOINING_WORLD = new ArrayList<>(config.getStringList("perms.when-joining-world"));
        try {
            SERVER_TYPE = ServerType.valueOf(config.getString("global.server-type"));
        } catch (NullPointerException | IllegalArgumentException e) {
            SERVER_TYPE = ServerType.EMPTY;
        }
        IS_SERVER_LINKED = (PROXY_SERVER_NAME != null || !PROXY_SERVER_NAME.equals("")) && SERVER_TYPE != ServerType.EMPTY;
        switch(Main.getInstance().getNMSVersion()){
            case "v1_8_R3":{
                VERSION = "1.8";
                break;
            }
            case "v1_12_R1":{
                VERSION = "1.12";
                break;
            }
            case "v1_16_R3":{
                VERSION = "1.16";
                break;
            }
            default:{
                VERSION = "1.18";
                break;
            }
        }
    }
    
    
    public static String getServerNameFormatted(){
        return "" + SERVER_TYPE.getName() + "&c#" + (PROXY_SERVER_NAME.charAt(PROXY_SERVER_NAME.length() - 1));
    }
    
}
