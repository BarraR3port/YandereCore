package com.podcrash.commissions.yandere.core.velocity.announcements;

import com.podcrash.commissions.yandere.core.common.announcements.Announcements;
import com.podcrash.commissions.yandere.core.common.data.punish.ban.Ban;
import com.podcrash.commissions.yandere.core.common.data.punish.mute.Mute;
import com.podcrash.commissions.yandere.core.common.data.punish.report.Report;
import com.podcrash.commissions.yandere.core.common.data.punish.warn.Warn;
import com.podcrash.commissions.yandere.core.velocity.utils.Utils;
import net.kyori.adventure.text.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class VAnnouncer extends Announcements {
    
    
    public Component banPlayerKickMessage(Ban ban){
        return Utils.format("&4&l⚕ &eYandere&cCraft &c&lPunishments &4&l⚕\n")
                .append(!ban.isPermanent() ? Utils.format("&c»&7Tipo: &cBan\n") : Utils.format("&c»&7Tipo: &cBan &4&lPermanente\n"))
                .append(Utils.format("&c» &7Jugador Baneado: &c%banned%\n".replace("%banned%", ban.getPunished())))
                .append(Utils.format("&c» &7Baneado por: &c%banner%\n".replace("%banner%", ban.getPunisher())))
                .append(Utils.format("&c» &7Razón: &c%reason%\n".replace("%reason%", ban.getReason())))
                .append(Utils.format(!ban.isPermanent() ? "&c» &7Fecha de expiro: &c%expDate%\n".replace("%expDate%", dateToString(ban.getExpDate()) +
                        "\n&c» &7Tiempo Restante: &c%timeLeft%".replace("%timeLeft%", getTimeLeft(new Date(), ban.getExpDate()))) : ""))
                .append(Utils.format("&c» &7Ip: &c%ip%\n".replace("%ip%", String.valueOf(ban.isIpBanned()))));
    }
    
    
    public LinkedList<String> getApiAnnouncement(String version){
        LinkedList<String> lines = new LinkedList<>();
        /*lines.add("&1████████████████████████████████████████████████████████████████████████████");
        lines.add("");
        lines.add("&c██╗     ██╗   ██╗&5██████╗  █████╗ ██████╗ ██╗  ██╗    &a █████╗ ██████╗ ██╗");
        lines.add("&c██║     ╚██╗ ██╔╝&5██╔══██╗██╔══██╗██╔══██╗██║ ██╔╝    &a██╔══██╗██╔══██╗██║");
        lines.add("&c██║      ╚████╔╝ &5██║  ██║███████║██████╔╝█████╔╝     &a███████║██████╔╝██║");
        lines.add("&c██║       ╚██╔╝  &5██║  ██║██╔══██║██╔══██╗██╔═██╗     &a██╔══██║██╔═══╝ ██║");
        lines.add("&c███████╗   ██║   &5██████╔╝██║  ██║██║  ██║██║  ██╗    &a██║  ██║██║     ██║");
        lines.add("&c╚══════╝   ╚═╝   &5╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝    &a╚═╝  ╚═╝╚═╝     ╚═╝  &4" + version);
        lines.add("");
        lines.add("&1████████████████████████████████████████████████████████████████████████████");*/
        return lines;
        
    }
    
    
    public String getTimeLeft(Date d1, Date d2){
        long Time = d2.getTime() - d1.getTime();
        long Seconds = TimeUnit.MILLISECONDS.toSeconds(Time) % 60;
        long Minutes = TimeUnit.MILLISECONDS.toMinutes(Time) % 60;
        long Hours = TimeUnit.MILLISECONDS.toHours(Time) % 24;
        long Days = TimeUnit.MILLISECONDS.toDays(Time) % 365;
        long Years = TimeUnit.MILLISECONDS.toDays(Time) / 365L;
        if (Years >= 1){
            return Years + "a " + Days + "d " + Hours + "h " + Minutes + "m " + Seconds + "s";
        } else if (Days >= 1){
            return Days + "d " + Hours + "h " + Minutes + "m " + Seconds + "s";
        } else if (Hours >= 1){
            return Hours + "h " + Minutes + "m " + Seconds + "s";
        } else if (Minutes >= 1){
            return Minutes + "m " + Seconds + "s";
        } else {
            return Seconds + "s";
        }
    }
    
    public String dateToString(Date date){
        return new SimpleDateFormat("dd-MM-yyyy").format(date);
    }
    
    @Override
    public void announceNewBan(Ban ban){
    
    }
    
    @Override
    public void announceNewWarn(Warn warn){
    
    }
    
    @Override
    public void announceNewMute(Mute mute){
    
    }
    
    @Override
    public void announceNewReport(Report report){
    
    }
    
    @Override
    public void announceStaffChat(String staff, String server, String msg){
    
    }
}
