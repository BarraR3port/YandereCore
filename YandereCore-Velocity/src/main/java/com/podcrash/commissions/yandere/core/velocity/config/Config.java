package com.podcrash.commissions.yandere.core.velocity.config;

import com.podcrash.commissions.yandere.core.velocity.VMain;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Path;

public class Config {
    private final Path path;
    private MainConfig config;
    
    public Config(Path originalPath){
        this.path = originalPath.resolve("config.yml");
        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .defaultOptions(opts -> opts
                        .shouldCopyDefaults(true)
                        .header("Yandere | BarraR3port\n------------- | -------------")
                )
                .path(path)
                .build();
        
        try {
            final CommentedConfigurationNode node = loader.load();
            config = node.get(MainConfig.class);
            node.set(MainConfig.class, config);
            loader.save(node);
        } catch (ConfigurateException exception) {
            exception.printStackTrace();
        }
    }
    
    public void reloadConfig(){
        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .defaultOptions(opts -> opts
                        .shouldCopyDefaults(true)
                        .header("Yandere | BarraR3port\n------------- | -------------")
                )
                .path(path)
                .build();
        
        try {
            final CommentedConfigurationNode node = loader.load();
            config = node.get(MainConfig.class);
            node.set(MainConfig.class, config);
            loader.save(node);
        } catch (ConfigurateException exception) {
            VMain.debug("Could not load config.yml file, error: " + exception.getMessage());
        }
    }
    
    public MainConfig getConfig(){
        return config;
    }
    
    @ConfigSerializable
    public static class MainConfig {
        
        @Comment("When true, many logs will be showed up in the console.")
        @Setting(value = "debug")
        private boolean debug = true;
        
        @Comment("Mongodb host")
        @Setting(value = "host")
        private String db_host = "localhost";
        
        @Comment("Mongodb port")
        @Setting(value = "port")
        private int db_port = 27017;
        
        @Comment("Mongodb database")
        @Setting(value = "database")
        private String db_database = "yandere";
        
        @Comment("Mongodb urli")
        @Setting(value = "urli")
        private String db_urli = "mongodb://yandere:JF993KKLALLDJJ2KLALLGJASKJ29I9ASDK@localhost:27017/";
        
        
        @Comment("Mongodb username")
        @Setting(value = "username")
        private String db_username = "yandere";
        
        @Comment("Mongodb password")
        @Setting(value = "password")
        private String db_password = "JF993KKLALLDJJ2KLALLGJASKJ29I9ASDK";
        
        
        public boolean isDebug(){
            return debug;
        }
        
        public void setDebug(boolean debug){
            this.debug = debug;
        }
        
        public String getDb_host(){
            return db_host;
        }
        
        public void setDb_host(String db_host){
            this.db_host = db_host;
        }
        
        public int getDb_port(){
            return db_port;
        }
        
        public void setDb_port(int db_port){
            this.db_port = db_port;
        }
        
        public String getDb_database(){
            return db_database;
        }
        
        public void setDb_database(String db_database){
            this.db_database = db_database;
        }
        
        public String getDb_urli(){
            return db_urli;
        }
        
        public void setDb_urli(String db_urli){
            this.db_urli = db_urli;
        }
        
        public String getDb_username(){
            return db_username;
        }
        
        public void setDb_username(String db_username){
            this.db_username = db_username;
        }
        
        public String getDb_password(){
            return db_password;
        }
        
        public void setDb_password(String db_password){
            this.db_password = db_password;
        }
    }
    
}
