package com.podcrash.commissions.yandere.core.common.log;

public interface PluginLogger {
    
    void info(String s);
    
    void warn(String s);
    
    void warn(String s, Throwable t);
    
    void severe(String s);
    
    void severe(String s, Throwable t);
    
}
