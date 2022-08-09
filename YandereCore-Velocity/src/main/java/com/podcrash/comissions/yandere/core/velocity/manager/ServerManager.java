package com.podcrash.comissions.yandere.core.velocity.manager;

import com.podcrash.comissions.yandere.core.common.data.server.IServerStats;
import com.podcrash.comissions.yandere.core.common.data.server.Server;
import com.podcrash.comissions.yandere.core.velocity.VMain;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public final class ServerManager extends IServerStats {
    
    
    public ServerManager(){
    
    }
    
    public void init(){
        VMain.getInstance().getProxy().getScheduler().buildTask(VMain.getInstance(), () -> {
            final ArrayList<Server> serversToRemove = new ArrayList<>();
            servers.forEach(
                    server ->
                            VMain.getInstance().getProxy().getServer(server.getProxyName()).ifPresent(
                                    registeredServer -> serversToRemove.add(server)
                            ));
            
            serversToRemove.forEach(servers::remove);
            
        }).repeat(5, TimeUnit.SECONDS).schedule();
    }
    
    
}
