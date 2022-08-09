package com.podcrash.comissions.yandere.core.common.data.user.props;

public class Stats {
    private long TIME_PLAYED;
    
    public Stats(){
        TIME_PLAYED = 0;
    }
    
    
    public long getTIME_PLAYED(){
        return TIME_PLAYED;
    }
    
    public void addTIME_PLAYED(long TIME_PLAYED){
        this.TIME_PLAYED = this.TIME_PLAYED + TIME_PLAYED;
    }
    
    
    public String getFormattedTimePlayed(){
        long difference_In_Seconds = (TIME_PLAYED / 1000) % 60;
        long difference_In_Minutes = (TIME_PLAYED / (1000 * 60)) % 60;
        long difference_In_Hours = (TIME_PLAYED / (1000 * 60 * 60)) % 24;
        long difference_In_Years = (TIME_PLAYED / (1000L * 60 * 60 * 24 * 365));
        long difference_In_Days = (TIME_PLAYED / (1000 * 60 * 60 * 24)) % 365;
        
        return (difference_In_Years < 10 ? "0" : "") + difference_In_Years + ":" +
                (difference_In_Days < 10 ? "0" : "") + difference_In_Days + ":" +
                (difference_In_Hours < 10 ? "0" : "") + difference_In_Hours + ":" +
                (difference_In_Minutes < 10 ? "0" : "") + difference_In_Minutes + ":" +
                (difference_In_Seconds < 10 ? "0" : "") + difference_In_Seconds;
        
    }
}
