/*
package com.podcrash.commissions.yandere.core.spigot.commands.punish;

import org.bukkit.entity.Player;
import org.lydark.api.common.commands.Command;
import org.lydark.api.common.commands.SubCommand;
import org.lydark.api.common.data.players.LydarkPlayer;
import org.lydark.api.common.data.punish.PunishType;
import org.lydark.api.spigot.commands.ILyCommand;
import org.lydark.api.spigot.commands.CommandContext;
import net.lymarket.lyapi.common.commands.*;
import org.lydark.api.spigot.punish.SPunishManager;
import org.lydark.core.spigot.Core;

import java.util.ArrayList;
import java.util.UUID;

public class UnBanCommand implements ILyCommand {
    
    private final SPunishManager pm = Core.getSMain( ).getPunishManager( );
    
    @Command(name = "unban", permission = "yandere.staff.unban", aliases = "pardon")
    public CommandResponse command( CommandContext context ){
        if ( context.getSender( ) instanceof Player ) {
            Player p = ( Player ) context.getSender( );
            if ( p.hasPermission( "yandere.staff.unban" ) ) {
                if ( context.getArgs( ).length == 0 ) {
                    p.sendMessage( Core.getApi( ).getUtils( ).prefix( ) + Core.getSMain( ).getUtils( ).format( "/unban <player>" ) );
                } else if ( context.getArgs( ).length == 1 ) {
                    LydarkPlayer lpTarget = Core.getSMain( ).getPlayers( ).getUpdatedPlayer( context.getArg( 0 ) );
                    for ( UUID uuid : lpTarget.getPunishments( ).keySet( ) ) {
                        if ( lpTarget.getPunishments( ).get( uuid ).equals( PunishType.BAN ) ) {
                            pm.unBanByUUID( p , uuid , lpTarget );
                        }
                    }
                }
            }
        }
        
        return true;
    }
    
    @Tab
    public ArrayList < String > tabComplete( TabContext TabContext ){
        ArrayList < String > list = new ArrayList <>( );
        return list;
    }
}
*/
