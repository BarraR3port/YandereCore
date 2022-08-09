package com.podcrash.comissions.yandere.core.common.error;

import java.util.UUID;

public class WarpNotFoundError extends RuntimeException {
    
    public WarpNotFoundError(UUID warpUUID){
        super("Warp " + warpUUID.toString() + " not found");
    }
    
    public WarpNotFoundError(String warp){
        super("Warp " + warp + " not found");
    }
    
    public WarpNotFoundError(String warp, String servername){
        super("Warp " + warp + " not found in " + servername);
    }
}
