package com.podcrash.commissions.yandere.core.common.data.lobby;

public enum JoinBedWarsArenaType {
    SOLO("Solo"),
    DUO("Doubles"),
    _3V3V3V3("3v3v3v3"),
    _4V4V4V4("4v4v4v4"),
    RANDOM("Random");
    
    private final String bwName;
    
    JoinBedWarsArenaType(String bwName){
        this.bwName = bwName;
    }
    
    public JoinBedWarsArenaType getNext(){
        switch(this){
            case SOLO:
                return DUO;
            case DUO:
                return _3V3V3V3;
            case _3V3V3V3:
                return _4V4V4V4;
            case _4V4V4V4:
                return RANDOM;
            default:
                return SOLO;
        }
        
    }
    
    public JoinBedWarsArenaType getPrevious(){
        switch(this){
            case SOLO:
                return RANDOM;
            case DUO:
                return SOLO;
            case _3V3V3V3:
                return DUO;
            case _4V4V4V4:
                return _3V3V3V3;
            default:
                return _4V4V4V4;
        }
        
    }
    
    public String getBwName(){
        return bwName;
    }
    
}
