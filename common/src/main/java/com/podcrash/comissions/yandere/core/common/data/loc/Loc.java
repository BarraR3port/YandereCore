package com.podcrash.comissions.yandere.core.common.data.loc;

import com.podcrash.comissions.yandere.core.common.data.server.ServerType;
import net.lymarket.common.Api;

import java.util.UUID;

public class Loc {
    
    private final String Server;
    private final String World;
    private final String Plot;
    private final UUID BWorld;
    private final double X;
    private final double Y;
    private final double Z;
    private final float Yaw;
    private final float Pitch;
    
    public Loc(String server, String world, double x, double y, double z){
        this.Server = server;
        this.World = world;
        this.X = x;
        this.Y = y;
        this.Z = z;
        this.Yaw = 0.5F;
        this.Pitch = 0.5F;
        this.Plot = null;
        this.BWorld = null;
    }
    
    public Loc(String server, String world, double x, double y, double z, UUID bWorld){
        this.Server = server;
        this.World = world;
        this.X = x;
        this.Y = y;
        this.Z = z;
        this.Yaw = 0.5F;
        this.Pitch = 0.5F;
        this.BWorld = bWorld;
        this.Plot = null;
    }
    
    public Loc(String server, String world, double x, double y, double z, String Plot){
        this.Server = server;
        this.World = world;
        this.X = x;
        this.Y = y;
        this.Z = z;
        this.Yaw = 0.5F;
        this.Pitch = 0.5F;
        this.BWorld = null;
        this.Plot = Plot;
    }
    
    public Loc(String server, String world, double x, double y, double z, float yaw, float pitch){
        this.Server = server;
        this.World = world;
        this.X = x;
        this.Y = y;
        this.Z = z;
        this.Yaw = yaw;
        this.Pitch = pitch;
        this.Plot = null;
        this.BWorld = null;
    }
    
    public String serialize(){
        return Api.getGson().toJson(this);
    }
    
    public double getX(){
        return X;
    }
    
    public double getY(){
        return Y;
    }
    
    public double getZ(){
        return Z;
    }
    
    public float getYaw(){
        return Yaw;
    }
    
    public float getPitch(){
        return Pitch;
    }
    
    public String getServer(){
        return Server;
    }
    
    public String getWorld(){
        return World;
    }
    
    public boolean isInPlot(){
        return Server.startsWith("PP-") || Plot != null;
    }
    
    public String getPlot(){
        return Plot;
    }
    
    public boolean isInBWorld(){
        return Server.startsWith("PW-") || BWorld != null;
    }
    
    public ServerType getCurrentServerType(){
        /*if (Server.startsWith("PP-")){
            return ServerType.PLOT;
        } else if (Server.startsWith("PW-")){
            return ServerType.WORLDS;
        } else {
            return ServerType.LOBBY;
        }*/
        return ServerType.LOBBY;
    }
    
    public String getCurrentServerTypeFormatted(){
        return ServerType.match(Server).getName();
    }
    
    public UUID getBWorld(){
        return BWorld;
    }
    
}
