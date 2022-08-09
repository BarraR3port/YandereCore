package com.podcrash.comissions.yandere.core.common.error;

import java.util.UUID;

public class WorldNotFoundError extends RuntimeException {
    
    public WorldNotFoundError(UUID worldUUID){
        super("World " + worldUUID.toString() + " not found");
    }
}
