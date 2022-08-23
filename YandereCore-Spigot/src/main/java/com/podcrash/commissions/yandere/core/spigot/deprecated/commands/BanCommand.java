/*
package com.podcrash.commissions.yandere.core.spigot.commands.punish;

import org.bukkit.entity.Player;
import org.lydark.api.common.commands.Command;
import org.lydark.api.common.commands.SubCommand;
import org.lydark.api.common.data.players.LydarkPlayer;
import org.lydark.api.spigot.SMain;
import org.lydark.api.spigot.commands.ILyCommand;
import org.lydark.api.spigot.commands.CommandContext;
import net.lymarket.lyapi.common.commands.*;
import org.lydark.api.spigot.punish.SPunishManager;
import org.lydark.core.spigot.Core;
import org.lydark.core.spigot.staff.PunishDateType;
import org.lydark.core.spigot.staff.ban.BanMenu;

import java.util.ArrayList;

public class BanCommand implements ILyCommand {
    
    private final SPunishManager pm = Core.getSMain( ).getPunishManager( );
    
    @Command(name = "ban", permission = "yandere.staff.ban")
    public CommandResponse command( CommandContext context ){
        if ( context.getSender( ) instanceof Player ) {
            Player p = ( Player ) context.getSender( );
            if ( p.hasPermission( "yandere.staff.ban" ) ) {
                if ( context.getArgs( ).length == 0 ) {
                    p.sendMessage( Core.getApi( ).getUtils( ).prefix( ) + Core.getSMain( ).getUtils( ).format( "/ban <player> <tiempo> <-ip> <razón>" ) );
                } else if ( context.getArgs( ).length == 1 ) {
                    new BanMenu( SMain.getPlayerMenuUtility( p ) , context.getArg( 0 ) ).open( );
                } else {
                    StringBuilder reason = new StringBuilder( );
                    LydarkPlayer lp = Core.getSMain( ).getPlayers( ).getPlayer( p.getName( ) );
                    LydarkPlayer lpTarget = Core.getSMain( ).getPlayers( ).getUpdatedPlayer( context.getArg( 0 ) );
                    if ( isNormal( context.getArg( 1 ) ) ) {
                        String lastWord = context.getArg( 1 ).substring( context.getArg( 1 ).length( ) - 1 );
                        int amount = Integer.parseInt( context.getArg( 1 ).substring( 0 , context.getArg( 1 ).length( ) - 1 ) );
                        boolean ip = false;
                        
                        for ( int i = 2; i < context.getArgs( ).length; i++ ) {
                            if ( context.getArg( i ).equalsIgnoreCase( "-ip" ) ) {
                                ip = true;
                            } else {
                                reason.append( context.getArg( i ) ).append( " " );
                            }
                        }
                        String finalReason = reason.toString( );
                        pm.banPlayer( p , lpTarget , lp , finalReason , PunishDateType.ConvertDate( amount , lastWord ) , ip , Core.getSMain( ).getUtils( ).getServer( ) );
                        
                    } else {
                        boolean ip = false;
                        String finalReason = reason.toString( );
                        
                        for ( int i = 1; i < context.getArgs( ).length; i++ ) {
                            if ( context.getArg( i ).equalsIgnoreCase( "-ip" ) ) {
                                ip = true;
                            } else {
                                reason.append( context.getArg( i ) ).append( " " );
                            }
                        }
                        pm.permanentBan( p , lpTarget , lp , finalReason , ip , Core.getSMain( ).getUtils( ).getServer( ) );
                        
                    }
                }
            }
        }
        
        return true;
    }
    
    private Boolean isNormal( String s ){
        String lastWord = s.substring( s.length( ) - 1 );
        
        return lastWord.equalsIgnoreCase( "s" ) ||
                lastWord.equalsIgnoreCase( "m" ) ||
                lastWord.equalsIgnoreCase( "h" ) ||
                lastWord.equalsIgnoreCase( "d" );
        
    }
    
    @Tab
    public ArrayList < String > tabComplete( TabContext TabContext ){
        ArrayList < String > list = new ArrayList <>( );
        return list;
    }
}
*/
