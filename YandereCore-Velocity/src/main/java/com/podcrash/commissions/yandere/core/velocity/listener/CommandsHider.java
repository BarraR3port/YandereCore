package com.podcrash.commissions.yandere.core.velocity.listener;


import com.podcrash.commissions.yandere.core.velocity.VMain;
import com.podcrash.commissions.yandere.core.velocity.utils.Utils;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;

import java.util.List;

public class CommandsHider {
    
    @Subscribe
    public void commandHider(CommandExecuteEvent e){
        List<String> commandsHidden = VMain.getInstance().getConfig().getStringList("hiddencommands");
        if (commandsHidden.contains(e.getCommand())){
            if (!e.getCommandSource().hasPermission("yandere.staff.commands")){
                e.getCommandSource().sendMessage(Utils.format(" &8&l▸ &cComando no encontrado, usa &e/ayuda &cpa ver los comandos disponibles."));
                e.setResult(CommandExecuteEvent.CommandResult.denied());
                return;
            }
        }
        for ( String command : commandsHidden ){
            if (e.getCommand().startsWith(command)){
                if (!e.getCommandSource().hasPermission("yandere.staff.commands")){
                    e.getCommandSource().sendMessage(Utils.format(" &8&l▸ &cComando no encontrado, usa &e/ayuda &cpa ver los comandos disponibles."));
                    e.setResult(CommandExecuteEvent.CommandResult.denied());
                    return;
                }
            } else if (command.startsWith(e.getCommand())){
                if (!e.getCommandSource().hasPermission("yandere.staff.commands")){
                    e.getCommandSource().sendMessage(Utils.format(" &8&l▸ &cComando no encontrado, usa &e/ayuda &cpa ver los comandos disponibles."));
                    e.setResult(CommandExecuteEvent.CommandResult.denied());
                    return;
                }
            }
        }
    }
}
