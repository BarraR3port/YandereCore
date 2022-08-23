package com.podcrash.commissions.yandere.core.velocity.socketmanager;

import com.podcrash.commissions.yandere.core.velocity.VMain;
import com.podcrash.commissions.yandere.core.velocity.manager.ServerSocketManager;
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
                } catch (java.util.concurrent.RejectedExecutionException ignored) {
                    break;
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
        for ( ProxySocketServer server : ServerSocketManager.getInstance().getSocketByServer().values() ){
            server.closeConnections();
        }
        compute = false;
        for ( ScheduledTask t : otherTasks ){
            t.cancel();
        }
    }
}
