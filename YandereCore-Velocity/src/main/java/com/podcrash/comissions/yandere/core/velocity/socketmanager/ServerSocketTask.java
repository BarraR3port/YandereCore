package com.podcrash.comissions.yandere.core.velocity.socketmanager;

import com.podcrash.comissions.yandere.core.velocity.VMain;
import com.podcrash.comissions.yandere.core.velocity.manager.ServerSocketManager;
import com.velocitypowered.api.scheduler.ScheduledTask;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerSocketTask {
    
    public static boolean compute = true;
    public static List<ScheduledTask> otherTasks = new ArrayList<>();
    private static ServerSocketTask instance = null;
    private final ServerSocket serverSocket;
    
    private ServerSocketTask(int port) throws IOException{
        serverSocket = new ServerSocket(port);
        instance = this;
        compute = true;
        otherTasks.add(VMain.getInstance().getProxy().getScheduler().buildTask(VMain.getInstance(), () -> {
            while (compute) {
                try {
                    Socket s = serverSocket.accept();
                    VMain.getInstance().getProxy().getScheduler().buildTask(VMain.getInstance(), new ProxySocketServer(s)).schedule();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).schedule());
    }
    
    public static boolean init(){
        if (instance == null){
            try {
                new ServerSocketTask(5555);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
    
    public static void stopTasks(){
        compute = false;
        VMain.getInstance().getProxy().getAllServers().forEach(server -> ServerSocketManager.getSocketByServer(server.getServerInfo().getName()).ifPresent(ProxySocketServer::closeConnections));
        for ( ScheduledTask t : otherTasks ){
            t.cancel();
        }
    }
}
