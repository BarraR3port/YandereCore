package com.podcrash.commissions.yandere.core.spigot.party;

import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.PartiesAPI;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PartiesManager {
    
    private static PartiesAPI api;
    
    public PartiesManager(){
        api = Parties.getApi();
    }
    
    public static boolean isInParty(Player p){
        return isInParty(p.getUniqueId());
    }
    
    public static boolean isInParty(UUID uuid){
        return api.getPartyPlayer(uuid).isInParty();
    }
    
    public static boolean isPartyLeader(Player p){
        return isPartyLeader(p.getUniqueId());
    }
    
    public static boolean isPartyLeader(UUID uuid){
        PartyPlayer pp = api.getPartyPlayer(uuid);
        if (pp == null){
            return false;
        }
        Party party = api.getParty(pp.getPartyName());
        if (party == null || party.getLeader() == null){
            return false;
        }
        return party.getLeader().equals(uuid) || pp.getRank() == 1;
    }
    
    public static List<Player> getPlayersParty(Player leader){
        return getPlayersParty(leader.getUniqueId());
    }
    
    public static List<Player> getPlayersParty(UUID uuid){
        PartyPlayer pp = api.getPartyPlayer(uuid);
        if (pp == null)
            return new ArrayList<>();
        Party party = api.getParty(pp.getPartyName());
        List<Player> online = new ArrayList<>();
        party.getOnlineMembers(true).forEach(o -> online.add(Bukkit.getPlayer(o.getPlayerUUID())));
        return online;
    }
    
}