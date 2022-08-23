package com.podcrash.commissions.yandere.core.common.data.lobby;

public enum JoinSkyWarsArenaType {
    SOLO,
    TEAM,
    RANKED,
    RANDOM;
    
    public JoinSkyWarsArenaType getNext(){
        switch(this){
            case SOLO:
                return TEAM;
            case TEAM:
                return RANKED;
            case RANKED:
                return RANDOM;
            default:
                return SOLO;
        }
        
    }
    
    
    public JoinSkyWarsArenaType getPrevious(){
        switch(this){
            case SOLO:
                return RANDOM;
            case TEAM:
                return SOLO;
            case RANKED:
                return TEAM;
            default:
                return RANKED;
        }
        
    }
    
    
}
