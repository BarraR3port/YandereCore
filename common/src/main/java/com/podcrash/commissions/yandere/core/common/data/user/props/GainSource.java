package com.podcrash.commissions.yandere.core.common.data.user.props;

public enum GainSource {
    PER_MINUTE("Por Minuto"),
    PER_TEAMMATE("Por Aliado"),
    GAME_WIN("Partida Ganada"),
    GAME_LOSE("Partida Perdido"),
    GAME_END("Partida Terminada"),
    BED_DESTROYED("Cama Destruída"),
    PER_KILL("Por Asesinato"),
    FINAL_KILL("Por Ultimo Asesinato"),
    PER_DEATH("Por Muerte"),
    PER_ASSISTANT("Por Asistencia"),
    OTHER("Otro"),
    BUY("Comprar"),
    SELL("Vender"),
    COMMAND("Comando");
    
    private final String name;
    
    GainSource(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
}