package com.podcrash.commissions.yandere.core.velocity.listener;


import com.podcrash.commissions.yandere.core.velocity.VMain;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;

import java.util.List;

public class CommandsHider {
    
    @Subscribe
    public void commandHider(CommandExecuteEvent e){
        List<String> commandsHidden = VMain.getInstance().getConfig().getStringList("hiddencommands");
        if (commandsHidden.contains(e.getCommand())){
            if (!e.getCommandSource().hasPermission("yandere.staff.commands")){
                e.setResult(CommandExecuteEvent.CommandResult.denied());
            }
        }
    }
}
