package com.podcrash.commissions.yandere.core.common.announcements;

import com.podcrash.commissions.yandere.core.common.data.punish.ban.Ban;
import com.podcrash.commissions.yandere.core.common.data.punish.mute.Mute;
import com.podcrash.commissions.yandere.core.common.data.punish.report.Report;
import com.podcrash.commissions.yandere.core.common.data.punish.warn.Warn;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;


public abstract class Announcements {
    
    public LinkedList<String> banPlayer(Ban ban){
        LinkedList<String> lines = new LinkedList<>();
        lines.add("");
        lines.add("         &4&l⚕ &eYandere&cCraft &c&lPunishments &4&l⚕");
        lines.add("");
        if (!ban.isPermanent()){
            lines.add("   &7Tipo: &cBan");
        } else {
            lines.add("   &7Tipo: &cBan &4&lPermanente");
        }
        lines.add("     &b» &7Jugador Baneado: &c%banned% ".replace("%banned%", ban.getPunished()));
        lines.add("     &b» &7Baneado por: &c%banner% ".replace("%banner%", ban.getPunisher()));
        lines.add("     &b» &7Razón: &c%reason% ".replace("%reason%", ban.getReason()));
        if (!ban.isPermanent()){
            lines.add("     &b» &7Fecha de expiro: &c%expDate% ".replace("%expDate%", dateToString(ban.getExpDate())));
        }
        lines.add("     &b» &7Ip: &c%ip% ".replace("%ip%", String.valueOf(ban.isIpBanned())));
        lines.add("     &b» &7Server: &c%server% ".replace("%server%", ban.getServer()));
        lines.add("");
        return lines;
    }
    
    public LinkedList<String> unBanPlayer(String punished, String punisher, String server){
        LinkedList<String> lines = new LinkedList<>();
        lines.add("");
        lines.add("         &4&l⚕ &eYandere&cCraft &c&lPunishments &4&l⚕");
        lines.add("");
        lines.add("   &7Tipo: &cUnban");
        lines.add("     &b» &7Jugador Desbaneado: &c%unbanned% ".replace("%unbanned%", punished));
        lines.add("     &b» &7Desbaneado por: &c%unbanner% ".replace("%unbanner%", punisher));
        lines.add("     &b» &7Server: &c%server% ".replace("%server%", server));
        lines.add("");
        return lines;
    }
    
    public LinkedList<String> warnPlayer(Warn warn){
        LinkedList<String> lines = new LinkedList<>();
        lines.add("");
        lines.add("         &4&l⚕ &eYandere&cCraft &c&lPunishments &4&l⚕");
        lines.add("");
        lines.add("   &7Tipo: &cWarn");
        lines.add(("     &b» &7Jugador Warneado: &c%warned% ").replace("%warned%", warn.getPunished()));
        lines.add(("     &b» &7Warneado por: &c%warner% ").replace("%warner%", warn.getPunisher()));
        lines.add(("     &b» &7Razón: &c%reason% ").replace("%reason%", warn.getReason()));
        lines.add(("     &b» &7Fecha de expiro: &c%expDate% ").replace("%expDate%", dateToString(warn.getExpDate())));
        lines.add(("     &b» &7Tiempo Restante: &c%timeLeft% ").replace("%timeLeft%", getTimeLeft(new Date(), warn.getExpDate())));
        lines.add(("     &b» &7Server: &c%server% ").replace("%server%", warn.getServer()));
        lines.add("");
        return lines;
    }
    
    public LinkedList<String> mutePlayer(Mute mute){
        LinkedList<String> lines = new LinkedList<>();
        lines.add("");
        lines.add("         &4&l⚕ &eYandere&cCraft &c&lPunishments &4&l⚕");
        lines.add("");
        lines.add("   &7Tipo: &cMuteo");
        lines.add(("     &b» &7Jugador Muteado: &c%muted% ").replace("%muted%", mute.getPunished()));
        lines.add(("     &b» &7Muteado por: &c%muter% ").replace("%muter%", mute.getPunisher()));
        lines.add(("     &b» &7Razón: &c%reason% ").replace("%reason%", mute.getReason()));
        lines.add(("     &b» &7Fecha de expiro: &c%expDate% ").replace("%expDate%", dateToString(mute.getExpDate())));
        lines.add(("     &b» &7Tiempo Restante: &c%timeLeft% ").replace("%timeLeft%", getTimeLeft(new Date(), mute.getExpDate())));
        lines.add(("     &b» &7Server: &c%server% ").replace("%server%", mute.getServer()));
        lines.add("");
        return lines;
    }
    
    public LinkedList<String> reportPlayer(Report report){
        LinkedList<String> lines = new LinkedList<>();
        lines.add("");
        lines.add("         &4&l⚕ &eYandere&cCraft &c&lPunishments &4&l⚕");
        lines.add("");
        lines.add("   &7Tipo: &cReporte");
        lines.add(("     &b» &7Jugador Reportado: &c%reported% ").replace("%reported%", report.getPunished()));
        lines.add(("     &b» &7Reportado por: &c%reporter% ").replace("%reporter%", report.getPunisher()));
        lines.add(("     &b» &7Razón: &c%reason% ").replace("%reason%", report.getReason()));
        lines.add(("     &b» &7Server: &c%server% ").replace("%server%", report.getServer()));
        lines.add("");
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
    
    public abstract LinkedList<String> getApiAnnouncement(String version);
    
    public abstract void announceNewBan(Ban ban);
    
    public abstract void announceNewWarn(Warn warn);
    
    public abstract void announceNewMute(Mute mute);
    
    public abstract void announceNewReport(Report report);
    
    public abstract void announceStaffChat(String staff, String server, String msg);
    
}
