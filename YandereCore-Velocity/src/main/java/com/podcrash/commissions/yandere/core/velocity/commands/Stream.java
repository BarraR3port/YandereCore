package com.podcrash.commissions.yandere.core.velocity.commands;

import com.podcrash.commissions.yandere.core.common.data.user.User;
import com.podcrash.commissions.yandere.core.velocity.VMain;
import com.podcrash.commissions.yandere.core.velocity.utils.Utils;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.scheduler.ScheduledTask;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Stream implements SimpleCommand {
    
    public Stream(CommandManager commandManager){
        
        final CommandMeta meta = commandManager.metaBuilder("stream").aliases("twitch").build();
        commandManager.register(meta, this);
    }
    
    @Override
    public void execute(final Invocation invocation){
        if (invocation.source() instanceof final Player player){
            if (invocation.arguments().length != 2){
                if (invocation.arguments().length == 0){
                    player.sendMessage(Utils.format(" &8&l» &7Este comando es exclusivo para &c&lAfiliados &7Autorizados &a✓."));
                    player.sendMessage(Utils.format(" &8&l» &7Intenta este comando con /stream <twitch/youtube> <url>"));
                    return;
                }
                if (invocation.arguments().length == 1 || invocation.arguments().length == 2){
                    if (invocation.arguments()[0].equalsIgnoreCase("cancelar") || invocation.arguments()[0].equalsIgnoreCase("parar") || invocation.arguments()[0].equalsIgnoreCase("stop")){
                        if (invocation.arguments().length == 2 && VMain.getInstance().getProxy().getPlayer(invocation.arguments()[1]).isPresent()){
                            Player target = VMain.getInstance().getProxy().getPlayer(invocation.arguments()[1]).get();
                            VMain.getInstance().getStreams().get(target.getUniqueId()).cancel();
                            VMain.getInstance().getStreams().remove(target.getUniqueId());
                            target.sendMessage(Utils.format(" &8&l» &7Anuncio de Stream parado al streamer &d" + player.getUsername()));
                            player.sendMessage(Utils.format(" &8&l» &7Anuncio de Stream parado por un &dStaff."));
                            return;
                        }
                        
                        if (VMain.getInstance().getStreams().containsKey(player.getUniqueId())){
                            VMain.getInstance().getStreams().get(player.getUniqueId()).cancel();
                            VMain.getInstance().getStreams().remove(player.getUniqueId());
                            player.sendMessage(Utils.format(" &8&l» &7Anuncio de Stream parado."));
                            return;
                        }
                    }
                }
                player.sendMessage(Utils.format(" &8&l» &7Comando incorrecto. Intenta con /stream <twitch/youtube> <url>"));
            } else {
                if (!VMain.getInstance().getStreams().containsKey(player.getUniqueId())){
                    if (invocation.arguments()[0].equalsIgnoreCase("twitch") || invocation.arguments()[0].equalsIgnoreCase("youtube")){
                        if (invocation.arguments()[1].contains("twitch.tv") || invocation.arguments()[1].contains("youtube.com") || invocation.arguments()[1].contains("youtu.be")){
                            String playerName = player.getUsername();
                            String type = invocation.arguments()[0];
                            String url = invocation.arguments()[1];
                            ScheduledTask streamTask = VMain.getInstance().getProxy().getScheduler().buildTask(VMain.getInstance(), () -> {
                                if (VMain.getInstance().getProxy().getPlayer(playerName).isPresent()){
                                    for ( Player p : VMain.getInstance().getProxy().getAllPlayers() ){
                                        User lp = VMain.getInstance().getPlayers().getPlayer(p.getUsername());
                                        if (checkProp(lp)){
                                            p.sendMessage(Utils.format("&m                                                                           &a"));
                                            p.sendMessage(Utils.format(" "));
                                            p.sendMessage(Utils.format("&f                      &cStreamer &7Autorizado &a✓"));
                                            p.sendMessage(Utils.format(" "));
                                            p.sendMessage(Utils.format("&7    Nuestro streamer &c" + playerName + " &7está en directo en:"));
                                            p.sendMessage(Utils.format("&f                            「" + (Objects.equals(type, "twitch") ? "&5&lTwitch" : "&f&lYou&4&lTube") + "&f⏌").clickEvent(ClickEvent.openUrl(url)).hoverEvent(HoverEvent.showText(Utils.format("&7Click aquí para entrar al stream!"))));
                                            p.sendMessage(Utils.format("&7                Se encuentra jugando en " + Utils.getPlayerServerFormatted(player)));
                                            p.sendMessage(Utils.format(" "));
                                            p.sendMessage(Utils.format("&m                                                                           &a"));
                                        }
                                    }
                                }
                            }).repeat(20L, TimeUnit.MINUTES).schedule();
                            VMain.getInstance().setStreams(player.getUniqueId(), streamTask);
                        } else {
                            player.sendMessage(Utils.format(" &8&l» &cURL incorrecta, sólo están permitidas urls de Twitch o Youtube"));
                        }
                    } else {
                        player.sendMessage(Utils.format(" &8&l» &cComando incorrecto. Intenta con /stream <twitch/youtube> <url>"));
                    }
                } else {
                    player.sendMessage(Utils.format(" &8&l» &7Hey tranquil@, tu stream ya se está promocionando!"));
                    player.sendMessage(Utils.format(" &8&l» &7pero puedes paralo con /stream cancelar"));
                }
            }
        }
    }
    
    @Override
    public List<String> suggest(final Invocation invocation){
        List<String> list = new ArrayList<>();
        if (invocation.arguments().length == 0){
            list.add("twitch");
            list.add("youtube");
            list.add("cancelar");
        }
        if (invocation.arguments().length == 2){
            if (invocation.arguments()[0].equalsIgnoreCase("twitch") && invocation.source() instanceof final Player player){
                list.add("https://www.twitch.tv/" + player.getUsername());
                list.add("https://www.twitch.tv/" + invocation.arguments()[1]);
            }
        }
        return list;
    }
    
    @Override
    public boolean hasPermission(final Invocation invocation){
        return invocation.source().hasPermission("yandere.media.stream");
    }
    
    private boolean checkProp(User player){
        return player.getOption("announcements-streams");
    }
}
