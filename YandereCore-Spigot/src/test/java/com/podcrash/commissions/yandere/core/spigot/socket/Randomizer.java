package com.podcrash.commissions.yandere.core.spigot.socket;

import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

public class Randomizer {
    
    private final Integer[][] randomizers = new Integer[30][25];
    private final Integer[][] selectors = new Integer[30][6];
    
    public Randomizer(){
        for ( int i = 0; i < randomizers.length; i++ ){
            HashSet<Integer> selected = new HashSet<>();
            while (selected.size() < randomizers[i].length) {
                selected.add(ThreadLocalRandom.current().nextInt(0, 27));
            }
            HashSet<Integer> unique = new HashSet<>();
            while (unique.size() < 6) {
                unique.add(ThreadLocalRandom.current().nextInt(0, 27));
            }
            selectors[i] = selected.toArray(new Integer[0]);
            randomizers[i] = selected.toArray(new Integer[0]);
        }
    }
    
    public Integer[] getSelectors(){
        return selectors[ThreadLocalRandom.current().nextInt(0, 30)];
    }
    
    public Integer[] getRandomizer(){
        return randomizers[ThreadLocalRandom.current().nextInt(0, 30)];
    }
    
}