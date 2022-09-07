package com.podcrash.commissions.yandere.core.velocity.commands;

import com.podcrash.commissions.yandere.core.velocity.VMain;
import com.podcrash.commissions.yandere.core.velocity.utils.Utils;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;

public class VAdmin implements SimpleCommand {
    public VAdmin(CommandManager commandManager){
        final CommandMeta meta = commandManager.metaBuilder("vadmin").build();
        commandManager.register(meta, this);
    }
    
    @Override
    public void execute(Invocation invocation){
        if (invocation.arguments().length == 1){
            if (invocation.source().hasPermission("yandere.admin")){
                if (invocation.arguments()[0].equalsIgnoreCase("reload")){
                    invocation.source().sendMessage(Utils.format("&aReloading config..."));
                    VMain.getConfig().forceReload();
                } else if (invocation.arguments()[0].equalsIgnoreCase("debug")){
                    boolean result = !VMain.getConfig().getBoolean("global.debug");
                    invocation.source().sendMessage(Utils.format("&aDebug mode is now " + (result ? "&aenabled" : "&cdisabled")));
                    VMain.getConfig().set("global.debug", result);
                }
            }
        }
    }
    
    @Override
    public boolean hasPermission(Invocation invocation){
        return invocation.source().hasPermission("yandere.admin");
    }
}
